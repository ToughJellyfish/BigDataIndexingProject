package edu.neu.coe.info7255.service;

public interface PlansService {

    String save(String plan);

    String update(String plan);

    String delete(String planId);

    String query(String planId, String type);
}
