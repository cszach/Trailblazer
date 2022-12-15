import java.awt.Point;
import java.util.HashMap;

/**
 * A geographical intersection that is the endpoint of a road.
 *
 * <p>
 * An intersection is identified by its ID and has a latitude and longtitude.
 *
 * <p>
 * An intersection is also a {@code Point} whose x and y values can be used to
 * e.g. determine its location on a drawing panel (such as {@code JPanel}).
 */
public class Intersection extends Point {
  /**
   * The ID of this intersection.
   */
  private String id;
  /**
   * The geodetic latitude, in degrees, of this intersection.
   */
  private double latitude;
  /**
   * The geodetic longtitude, in degrees, of this intersection.
   */
  private double longtitude;
  /**
   * A {@code Map} that maps this intersection's neighbors to the roads that
   * connect between this intersection and the respective neighbor.
   */
  private HashMap<Intersection, Road> neighbors;

  // For Dijkstra's algorithm
  private double distance;
  private Road prev;

  /**
   * Constructs a new {@code Intersection} with the given ID, latitude, and
   * longtitude.
   * 
   * @param id the ID that identifies the intersection
   * @param latitude the geodetic latitude, in degrees, of this intersection
   * @param longtitude the geodetic longtitude, in degrees, of this intersection
   */
  public Intersection(String id, double latitude, double longtitude) {
    this.id = id;
    this.latitude = latitude;
    this.longtitude = longtitude;

    this.neighbors = new HashMap<>();
  }

  /**
   * Checks whether two intersections are equal.
   *
   * <p>
   * Returns {@code true} if the argument {@code obj} is an {@code Intersection}
   * object and has the same ID, latitude, and longtitude as this intersection.
   *
   * @return {@code true} if the objects are equal, {@code false} otherwise.
   */
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

  /**
   * Returns the ID of this intersection.
   * 
   * @return the ID of this intersection.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Returns the latitude of this intersection.
   * 
   * @return the geodetic latitude, in degrees, of this intersection.
   */
  public double getLatitude() {
    return this.latitude;
  }

  /**
   * Returns the longtitude of this intersection.
   * 
   * @return the geodetic longtitude, in degrees, of this intersection.
   */
  public double getLongtitude() {
    return this.longtitude;
  }

  /**
   * Returns the {@code HashMap} that maps from the neighbors of this
   * intersection to the roads that connect this intersection and the
   * corresponding neighbor.
   * 
   * @return the {@code HashMap} that maps from the neighbors of this
   * intersection to the roads that connect this intersection and the
   * corresponding neighbor.
   */
  public HashMap<Intersection, Road> getNeighbors() {
    return this.neighbors;
  }

  /**
   * Returns the distance of the shortest path that connects between a start
   * intersection and this intersection.
   *
   * <p>
   * This is intended for use in the Djikstra's algorithm.
   * 
   * @return the distance, in miles, of the shortest path that connects between
   * a start intersection and this intersection.
   */
  protected double getDistance() {
    return this.distance;
  }

  /**
   * Returns the road that connects this intersection and the previous
   * intersection in the shortest path between a start intersection and this
   * intersection.
   * 
   * <p>
   * This is intended for use in the Djikstra's algorithm.
   *
   * @return the road that connects this intersection and the previous
   * intersection in the shortest path between a start intersection and this
   * intersection.
   */
  protected Road getPrev() {
    return this.prev;
  }

  /**
   * Updates the distance of the shortest path that connects between a start
   * intersection and this intersection.
   *
   * <p>
   * This is intended for use in the Djikstra's algorithm.
   * 
   * @param distance the new distance of the shortest path that connects between
   * a start intersection and this intersection
   */
  protected void setDistance(double distance) {
    this.distance = distance;
  }

  /**
   * Updates the road that connects this intersection and the previous
   * intersection in the shortest path between a start intersection and this
   * intersection.
   * 
   * <p>
   * This is intended for use in the Djikstra's algorithm.
   *
   * @param prev the new road that connects this intersection and the previous
   * intersection in the shortest path between a start intersection and this
   * intersection.
   */
  protected void setPrev(Road prev) {
    this.prev = prev;
  }
}
