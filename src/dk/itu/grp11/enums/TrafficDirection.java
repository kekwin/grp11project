package dk.itu.grp11.enums;

import java.util.HashMap;

public enum TrafficDirection {
  TO_FROM("tf"), FROM_TO("ft"), BOTH_WAYS(""), DRIVING_NOT_ALLOWED("n");
  
  public static void main(String[] args) {
    System.out.println(TrafficDirection.getDirectionById("tf"));
    System.out.println(TrafficDirection.getDirectionById("ft"));
    System.out.println(TrafficDirection.getDirectionById(""));
    System.out.println(TrafficDirection.getDirectionById("n"));
  }
  
  private String id;
  private static java.util.Map<String, TrafficDirection> options = new HashMap<>();
  
  static {
    for(TrafficDirection rt : TrafficDirection.values()) {
      options.put(rt.getId(), rt);
    }
  }
  
  private TrafficDirection(String id) {
    this.id = id;
  }
  
  private String getId() {
    return id;
  }
  
  public static TrafficDirection getDirectionById(String id) {
    return options.get(id);
  }
}
