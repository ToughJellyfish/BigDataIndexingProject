package edu.neu.coe.info7255.service;

import edu.neu.coe.info7255.exception.ApiResouceAlreadyExistException;
import edu.neu.coe.info7255.exception.ApiResouceNotFoundException;
import edu.neu.coe.info7255.exception.JsonBuildException;
import edu.neu.coe.info7255.util.Utils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Service
public class PlansServiceImpl implements PlansService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RestHighLevelClient ecClient;

    @Override
    public String save(String plan) {
        JSONObject oPlan = new JSONObject(new JSONTokener(plan));
        String planId = oPlan.getString("objectId");
        Set<String> ks = stringRedisTemplate.keys("*" + planId + "*");
        if (!ks.isEmpty()) throw new ApiResouceAlreadyExistException(planId + " exist");
        saveJSON(oPlan, "");

        //Enqueue to redis
        RedisConnection rc = stringRedisTemplate.getConnectionFactory().getConnection();
        rc.rPush(Utils.IndexAllQueue.getBytes(),oPlan.toString().getBytes(StandardCharsets.UTF_8));
        rc.close();

        logger.info(planId + " add");
        return planId + " add!";
    }

    private void saveJSON(JSONObject jo, String from) {
        String objId = jo.getString("objectId");
        String objType = jo.getString("objectType");
        String storeKey;
        if (from.equals("")) {
            storeKey = objType + Utils.redisSeparator + objId;
        } else {
            storeKey = from + Utils.redisSeparator + objType + Utils.redisSeparator + objId;
        }
        for (String k : jo.keySet()) {
            if (jo.get(k) instanceof JSONObject) {
                saveJSON((JSONObject) jo.get(k), storeKey + Utils.redisSeparator + k);
            } else if (jo.get(k) instanceof JSONArray) {
                for (Object o : (JSONArray) jo.get(k)) {
                    //May not work if ijo is not a JSONObject, eg. an int or String
                    JSONObject ijo = (JSONObject) o;
                    String iObjId = ijo.getString("objectId");
                    String iObjType = ijo.getString("objectType");
                    stringRedisTemplate.opsForSet().add(storeKey + Utils.redisSeparator + k, iObjType + Utils.redisSeparator + iObjId);
                    saveJSON(ijo, "");
                }
            } else {
                //if (from.equals("")) {
                    stringRedisTemplate.opsForHash().put(storeKey, k, jo.get(k).toString());
                //} else {
                //    stringRedisTemplate.opsForHash().put(storeKey + Utils.redisSeparator + k, k, jo.get(k).toString());
                //}
            }
        }

    }

    @Override
    public String update(String plan) {
        JSONObject oPlan = new JSONObject(new JSONTokener(plan));
        String planId = oPlan.getString("objectId");
        saveJSON(oPlan, "");

        RedisConnection rc = stringRedisTemplate.getConnectionFactory().getConnection();
        rc.rPush(Utils.IndexAllQueue.getBytes(),oPlan.toString().getBytes(StandardCharsets.UTF_8));
        rc.close();

        logger.info(planId + " update");
        return planId + " update!";
    }

    @Override
    public String delete(String planId) {
        Set<String> ks = stringRedisTemplate.keys("*" + planId + "*");
        if (!ks.isEmpty()) {
            for (String k : ks) {
                logger.info("REDIS TYPE: " + stringRedisTemplate.type(k).code());
                if (stringRedisTemplate.type(k).code().equals("set") && !stringRedisTemplate.opsForSet().members(k).isEmpty()) {
                    for (String ik : stringRedisTemplate.opsForSet().members(k)) {
                        logger.info("SET MEMBERS: " + ik);
                        delete(ik);
                    }
                }
                stringRedisTemplate.delete(k);
                String ecId = k.split(Utils.redisSeparator)[k.split(Utils.redisSeparator).length-1];
                String routingId = null;
                try {
                    routingId = k.split(Utils.redisSeparator)[1];
                } catch (IndexOutOfBoundsException e) {
                    logger.info("Not a child.");
                }
                try {
                    ecClient.delete(new DeleteRequest("index_all","plan",ecId));

            } catch (IOException e) {
                logger.error("ElasticSearch delete index error:"+e);
            }
            logger.info("ElasticSearch index id=" + ecId + " deleted.");

        }
            logger.info(planId + " deleted");
            return planId + " deleted";
        } else {
            logger.info(planId + " doesn't exist");
            throw new ApiResouceNotFoundException(planId + " doesn't exist");
        }
    }

    @Override
    public String query(String planId, String type) {
        Set<String> ks = stringRedisTemplate.keys("*" + planId + "*");
        if (ks.isEmpty()) throw new ApiResouceNotFoundException("type: " + type + ", id: " + planId + " doesn't exist");
        return buildJSON(type + Utils.redisSeparator + planId).toString(3);
    }

    private JSONObject buildJSON(String redisId) throws JsonBuildException {
        JSONObject result = new JSONObject();
        Set<String> ks = stringRedisTemplate.keys("*" + redisId + "*");
        logger.info("PATTERN=" + "*" + redisId + "*" + " ,KEY SET SIZE=" + ks.size());
        ks.removeAll(stringRedisTemplate.keys("*" + redisId + Utils.redisSeparator + "*" + Utils.redisSeparator + "*" + Utils.redisSeparator + "*" + Utils.redisSeparator + "*"));
        logger.info("PATTERN=" + "*" + redisId + Utils.redisSeparator + "*" + Utils.redisSeparator + "*" + " ,KEY SET SIZE AFTER REMOVE=" + ks.size());
        if (!ks.isEmpty()) {
            for (String k : ks) {
                logger.info("REDIS TYPE: " + stringRedisTemplate.type(k).code());
                //key is type + separator + planId => hashmap with all top level properties
                logger.info("k=" + k + ", redisId=" + redisId);
                if (k.endsWith(redisId)) {
                    JSONObject rawSchema = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/public/json/object/definitions.json")));
                    stringRedisTemplate.opsForHash().entries(k).forEach((hkey, hvalue) -> {
                        try {
                            String schemaType = ((JSONObject) rawSchema.get((String) hkey)).get("type").toString();
                            switch (schemaType) {
                                case "integer":
                                    result.put((String) hkey, Integer.parseInt((String) hvalue));
                                    break;
                                case "number":
                                    result.put((String) hkey, Double.parseDouble((String) hvalue));
                                    break;
                                case "boolean":
                                    result.put((String) hkey, Boolean.parseBoolean((String) hvalue));
                                    break;
                                default:
                                    logger.info("Schema Type:=" + schemaType);
                                    result.put((String) hkey, (String) hvalue);
                                    break;
                            }
                        } catch (JSONException e) {
                            logger.info(e.getMessage());
                            result.put((String) hkey, (String) hvalue);
                        }
                    });
                } else {
                    //key is type + separator + planId + separator + another name =>
                    String aName;
                    //1 if it is a hashmap => an insider json object named [another name]
                    if (stringRedisTemplate.type(k).code().equals("hash")) {
                        JSONObject ijo = buildJSON(k);
                        logger.info("INNER JSON=" + ijo.toString());
                        aName = k.split(Utils.redisSeparator)[k.split(Utils.redisSeparator).length-3];
                        result.put(aName, ijo);
                    } else
                        //2 if it is a set => an insider json array named [another name], contains [type + separator + planId] recursion
                        if (stringRedisTemplate.type(k).code().equals("set")) {
                            JSONArray ja = new JSONArray();
                            for (String ik : stringRedisTemplate.opsForSet().members(k)) {
                                logger.info("SET MEMBERS: " + ik);
                                ja.put(buildJSON(ik));
                            }
                            aName = k.split(Utils.redisSeparator)[k.split(Utils.redisSeparator).length-1];
                            result.put(aName, ja);
                        }
                }
            }
        } else {
            logger.error("Can't find redis id=" + redisId);
            throw new JsonBuildException("Can't find redis id=" + redisId);
        }
        logger.info("FINAL JSON=" + result.toString());
        return result;
    }
}
