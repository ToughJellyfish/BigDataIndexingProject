package edu.neu.coe.info7255.service;

import edu.neu.coe.info7255.util.Utils;
import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static edu.neu.coe.info7255.util.Utils.addLineSeparator;

@Service
public class JsonValidateServiceImpl implements JsonValidateService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String validatePlan(String in) {
        JSONObject inJson = new JSONObject(in);
        String type = inJson.getString("objectType");
        try (InputStream inputStream = getClass().getResourceAsStream("/public/json/object/"+type+".json")) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(inJson); // throws a ValidationException if this object is invalid
        } catch (ValidationException e2) {
            logger.error(e2.getMessage());
            e2.getCausingExceptions().stream()
                    .map(ValidationException::getMessage)
                    .forEach(logger::error);

            StringBuilder sb = new StringBuilder().append(e2.getMessage() + System.lineSeparator());
            if (e2.getViolationCount() > 1) {
                e2.getCausingExceptions().stream()
                        .map(ValidationException::getMessage)
                        .map(Utils::addLineSeparator)
                        .forEach(sb::append);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unknown Exception: " + e);
            return e.getMessage();
        }
        logger.info("Validation Success!");
        return "Success";
    }

    @Override
    public Boolean patchPlan(String in, String type, String name) {
        JSONObject inJson = new JSONObject(in);
        InputStream inputStream = getClass().getResourceAsStream("/public/json/"+type+"/"+name+".json");
        JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));

        InputStream inputStreamDef = getClass().getResourceAsStream("/public/json/object/definitions.json");
        JSONObject rawSchemaDef = new JSONObject(new JSONTokener(inputStreamDef));

        //!!!modify json schema may break the exist data in redis
        for (String k:inJson.keySet()) {
            //for delete properties
            if (((JSONObject)rawSchema.get("properties")).keySet().contains(k) && inJson.get(k).equals("null")) {
                logger.info(k+ " is found and need to be deleted");
                rawSchemaDef.remove(k);
                ((JSONObject)rawSchema.get("properties")).remove(k);
                ((JSONArray) rawSchema.get("required")).remove(((JSONArray) rawSchema.get("required")).toList().indexOf(k));
                continue;
            }

            //for add/update properties
            if (inJson.get(k) instanceof JSONObject) {
                String newType = ((JSONObject) inJson.get(k)).get("type").toString();
                if (newType.equals("integer") || newType.equals("number") || newType.equals("boolean") || newType.equals("string")) {
                    rawSchemaDef.put(k,inJson.get(k));
                    if (((JSONObject)rawSchema.get("properties")).keySet().contains(k)) {
                        ((JSONObject) rawSchema.get("properties")).put(k,new JSONObject().put("$ref","definitions.json#/"+ k));
                        logger.info(k+" is found!");
                    } else {
                        ((JSONObject) rawSchema.get("properties")).put(k,new JSONObject().put("$ref","definitions.json#/"+ k));
                        ((JSONArray) rawSchema.get("required")).put(k);
                    }
                } else {
                    logger.info(k+" is NOT a supported data type");
                }
            } else {
                logger.info(k+" does not exist or is NOT a valid patch");
            }
        }

        logger.info("In object rawSchemaDef: "+rawSchemaDef.toString(3));
        logger.info("In object rawSchema: "+rawSchema.toString(3));
        try {
            FileWriter defFile = new FileWriter(getClass().getResource("/public/json/object/definitions.json").getPath(),false);
            rawSchemaDef.write(defFile,3,0);
            defFile.close();

            FileWriter file = new FileWriter(getClass().getResource("/public/json/"+type+"/"+name+".json").getPath(),false);
            rawSchema.write(file,3,0);
            file.close();
            //logger.info(defFile.getPath());
            logger.info("In file definitions.json: "+addLineSeparator(IOUtils.toString(this.getClass().getResourceAsStream("/public/json/object/definitions.json"), StandardCharsets.UTF_8)));
            logger.info("In file "+name+".json: "+addLineSeparator(IOUtils.toString(this.getClass().getResourceAsStream("/public/json/"+type+"/"+name+".json"), StandardCharsets.UTF_8)));
        } catch (IOException e) {
            logger.error("error:"+e);
        }
        return true;
    }

}
