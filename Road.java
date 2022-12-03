public class Road {
  private String id;
  private Intersection intersection1;
  private Intersection intersection2;
  private double distance;

  // for haversine
  private static double RADIUS = 6371;

  // for Dijkstra
  private boolean isShortestRoad = false;

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

  private static double haversine(double lat1, double long1, double lat2, double long2) {
    double dLat = Math.toRadians(lat2 - lat1);
    double dLong = Math.toRadians(long2 - long1);

    lat1 = Math.toRadians(lat1);
    lat2 = Math.toRadians(lat2);

    double a =
        Math.pow(Math.sin(dLat / 2), 2)
            + Math.pow(Math.sin(dLong / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
    double c = 2 * Math.asin(Math.sqrt(a));
    return RADIUS * c;
  }

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

  public String getId() {
    return this.id;
  }

  public Intersection getIntersection1() {
    return this.intersection1;
  }

  public Intersection getIntersection2() {
    return this.intersection2;
  }

  public double getDistance() {
    return this.distance;
  }

  public boolean getIsShortestRoad() {
    return this.isShortestRoad;
  }

  public void setIsShortestRoad(boolean isShortestRoad) {
    this.isShortestRoad = isShortestRoad;
  }

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
