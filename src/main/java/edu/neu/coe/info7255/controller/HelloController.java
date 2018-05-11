package edu.neu.coe.info7255.controller;

import edu.neu.coe.info7255.exception.JsonValidationException;
import edu.neu.coe.info7255.service.JsonValidateService;
import edu.neu.coe.info7255.service.PlansService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

import static edu.neu.coe.info7255.util.Utils.addLineSeparator;

@RestController
public class HelloController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JsonValidateService jsonValidateService;

    @Autowired
    PlansService plansService;

    @RequestMapping(method = RequestMethod.POST, value = "/file")
    public String insertPlan(@RequestBody String plan) {
        logger.info("INPUT PLAN= " + plan);
        String validationResult = jsonValidateService.validatePlan(plan);
        if (validationResult.equals("Success")) {
            return addLineSeparator(plansService.save(plan));
        } else {
            throw new JsonValidationException(validationResult);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/file")
    public String updatePlan(@RequestBody String plan) {
        logger.info("UPDATE PLAN= " + plan);
        String validationResult = jsonValidateService.validatePlan(plan);
        if (validationResult.equals("Success")) {
            return addLineSeparator(plansService.update(plan));
        } else {
            throw new JsonValidationException(validationResult);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/1/{type}/{planId}")
    public String queryPlan(@PathVariable("planId") String planId, @PathVariable("type") String type, HttpServletResponse response) {
        logger.info("queryPlan, PLAN TYPE=" + type + ", PLAN ID=" + planId);
        String result = plansService.query(planId,type);
        response.setHeader("Cache-Control", "max-age: 3600");
        return addLineSeparator(result);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/file/{planId}")
    public String deletePlan(@PathVariable("planId") String planId) {
        logger.info("deletePlan, PLAN ID=" + planId);
        return addLineSeparator(plansService.delete(planId));
    }
    @RequestMapping(method = RequestMethod.GET, value = "/{type}/{name}")
    public String plan(@PathVariable("type") String type, @PathVariable("name") String name, HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control", "max-age: 3600");
        return addLineSeparator(IOUtils.toString(this.getClass().getResourceAsStream("/public/json/"+type+"/"+name+".json"), StandardCharsets.UTF_8));
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/2/{type}/{name}")
    public String patchPlan(@RequestBody String plan, @PathVariable("type") String type, @PathVariable("name") String name) throws Exception {
        jsonValidateService.patchPlan(plan,type,name);
        return addLineSeparator(IOUtils.toString(this.getClass().getResourceAsStream("/public/json/"+type+"/"+name+".json"), StandardCharsets.UTF_8));
    }

}