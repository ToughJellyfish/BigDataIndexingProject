package edu.neu.coe.info7255.service;

public interface JsonValidateService {

    String validatePlan(String in);

    Boolean patchPlan(String in, String type, String name);

}
