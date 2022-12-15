import java.awt.Point;

/**
 * An interface for a map projection method.
 *
 * <p>A projection method can know the width and height of the projection, as well as zoom level.
 * How zoom level is used is up to the implementation.
 */
public interface Projection {
  /**
   * Returns the width of the projection.
   *
   * @return the width of the projection.
   */
  public int getWidth();
  /**
   * Returns the height of the projection.
   *
   * @return the height of the projection.
   */
  public int getHeight();
  /**
   * Returns the zoom level of the projection.
   *
   * @return the zoom level of the projection.
   */
  public double getZoomLevel();
  /**
   * Sets the width of the projection.
   *
   * @param width the new width of the projection
   */
  public void setWidth(int width);
  /**
   * Sets the height of the projection.
   *
   * @param height the new height of the projection
   */
  public void setHeight(int height);
  /**
   * Sets the zoom level of the projection.
   *
   * @param zoomLevel the new zoom level of the projection
   */
  public void setZoomLevel(double zoomLevel);
  /**
   * Projects a point given its latitude and longtitude.
   *
   * <p>Assumptions about the latitude and longtitude, such as their units or whether they are
   * geodetic or geocentric are left to the implementation.
   *
   * @param latitude the latitude of a point
   * @param longtitude the longtitude of the point
   * @return a {@code Point} that is the projection of the given point.
   */
  public Point project(double latitude, double longtitude);
}
