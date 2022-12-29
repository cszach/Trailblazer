package io.github.cszach.Trailblazer.geo;

import java.awt.Point;
import java.util.HashMap;

public class Intersection extends Point {
  private String id;
  private double latitude;
  private double longtitude;
  private HashMap<Intersection, Road> neighbors;

  // For Dijkstra's algorithm
  private double distance;
  private Road prev;

  public Intersection(String id, double latitude, double longtitude) {
    this.id = id;
    this.latitude = latitude;
    this.longtitude = longtitude;

    this.neighbors = new HashMap<>();
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Intersection intersection)
        && (this.id.equals(intersection.id))
        && (this.latitude == intersection.latitude)
        && this.longtitude == intersection.longtitude;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  public String getId() {
    return this.id;
  }

  public double getLatitude() {
    return this.latitude;
  }

  public double getLongtitude() {
    return this.longtitude;
  }

  public HashMap<Intersection, Road> getNeighbors() {
    return this.neighbors;
  }

  protected double getDistance() {
    return this.distance;
  }

  protected Road getPrev() {
    return this.prev;
  }

  protected void setDistance(double distance) {
    this.distance = distance;
  }

  protected void setPrev(Road prev) {
    this.prev = prev;
  }
}
