/**
 * A road which connects two intersections.
 *
 * @see Intersection
 */
public class Road {
  /** The ID of this road. */
  private String id;
  /** An intersection that is an endpoint of this road. */
  private Intersection intersection1;
  /** An intersection that is the other endpoint of this road. */
  private Intersection intersection2;
  /** The distance, or length, of this road, in miles. */
  private double distance;

  /** The radius of the Earth, in kilometers. */
  private static double RADIUS = 6371;

  // for Dijkstra
  private boolean isShortestRoad = false;

  /**
   * Constructs a new {@code Road} with the given ID and {@code Intersection} endpoints.
   *
   * @param id the ID that identifies this road
   * @param intersection1 one of the endpoints of this road
   * @param intersection2 the other endpoint of this road
   */
  public Road(String id, Intersection intersection1, Intersection intersection2) {
    this.id = id;
    this.intersection1 = intersection1;
    this.intersection2 = intersection2;

    // Compute the distance using the haversine formula
    this.distance =
        haversine(
            intersection1.getLatitude(),
            intersection1.getLongtitude(),
            intersection2.getLatitude(),
            intersection2.getLongtitude());
  }

  /**
   * The haversine formula calculates the distance between two geographical points given their
   * latitudes and longtitudes.
   *
   * @param lat1 the geodetic latitude, in degrees, of the first point
   * @param long1 the geodetic longtitude, in degrees, of the first point
   * @param lat2 the geodetic latitude, in degrees, of the second point
   * @param long2 the geodetic longtitude, in degrees, of the second point
   * @return the distance between the two geographical points, in miles, with the assumption that
   *     the Earth is a perfect sphere with a radius of {@code RADIUS} kilometers.
   */
  private static double haversine(double lat1, double long1, double lat2, double long2) {
    double dLat = Math.toRadians(lat2 - lat1);
    double dLong = Math.toRadians(long2 - long1);

    lat1 = Math.toRadians(lat1);
    lat2 = Math.toRadians(lat2);

    double a =
        Math.pow(Math.sin(dLat / 2), 2)
            + Math.pow(Math.sin(dLong / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
    double c = 2 * Math.asin(Math.sqrt(a));
    double d = RADIUS * c; // in kilometers
    return d * 0.62; // convert to miles
  }

  /**
   * Checks whether two roads are equal.
   *
   * <p>Returns {code true} if the argument {@code obj} is a {@code Road} object that has the same
   * ID and connects the same intersections as this road.
   *
   * @return {@code true} if the objects are equal, {@code false} otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Road road)
        && ((this.intersection1.equals(road.intersection1))
                && (this.intersection2.equals(road.intersection2))
            || (this.intersection1.equals(road.intersection2))
                && (this.intersection2.equals(road.intersection1)));
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  /**
   * Returns the ID of this road.
   *
   * @return the ID of this road.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Returns the first intersection of this road.
   *
   * @return the first intersection of this road.
   */
  public Intersection getIntersection1() {
    return this.intersection1;
  }

  /**
   * Returns the second intersection of this road.
   *
   * @return the second intersection of this road.
   */
  public Intersection getIntersection2() {
    return this.intersection2;
  }

  /**
   * Returns the distance of this road.
   *
   * @return the distance, in miles, of this road.
   */
  public double getDistance() {
    return this.distance;
  }

  /**
   * Returns whether this road is a shortest road in a shortest path.
   *
   * <p>This is intended for use in the Djikstra's algorithm.
   *
   * @return {@code true} if this road is a shortest road in a shortest path, {@code false}
   *     otherwise.
   */
  public boolean getIsShortestRoad() {
    return this.isShortestRoad;
  }

  /**
   * Marks or unmarks this road as the shortest road in a shortest path.
   *
   * @param isShortestRoad {@code true} if this road is a shortest road in a shortest path, {@code
   *     false} if not.
   */
  public void setIsShortestRoad(boolean isShortestRoad) {
    this.isShortestRoad = isShortestRoad;
  }

  /**
   * Given an endpoint of this road, returns the other endpoint.
   *
   * @param intersection an endpoint of this road
   * @return the other endpoint, or {@code null} if the given {@code Intersection} is not an
   *     endpoint of this road.
   */
  public Intersection getTheOtherEnd(Intersection intersection) {
    if (intersection.equals(this.intersection1)) {
      return this.intersection2;
    } else if (intersection.equals(this.intersection2)) {
      return this.intersection1;
    } else {
      return null;
    }
  }
}
