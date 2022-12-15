import java.awt.Point;

/**
 * The Web Mercator Projection.
 *
 * <p>The zoom level is an exponent for the base 2. This means that {@code zoomLevel} = 0 means x1
 * zoom, 1 means x2 zoom, 2 means x4 zoom, 4 means x8 zoom, and so on.
 *
 * <p>The input latitude and longtitude must be geodetic coordinates measured in degrees.
 */
public class WebMercatorProjection implements Projection {
  private int width;
  private int height;
  private double zoomLevel;

  private final double QUARTER_PI = Math.PI / 4;
  private final double TWO_PI = Math.PI * 2;
  private final double LOG_2 = Math.log(2);
  private double numTiles;

  public WebMercatorProjection(int width, int height, double zoomLevel) {
    this.setWidth(width);
    this.setHeight(height);
    this.setZoomLevel(zoomLevel);
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public double getZoomLevel() {
    return this.zoomLevel;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setZoomLevel(double zoomLevel) {
    this.zoomLevel = zoomLevel;
    this.numTiles = Math.pow(2.0, this.zoomLevel);
  }

  public Point project(double latitude, double longtitude) {
    latitude = Math.toRadians(latitude);
    longtitude = Math.toRadians(longtitude);

    double x = (this.width / TWO_PI) * this.numTiles * (longtitude + Math.PI);
    double y =
        (this.height / TWO_PI)
            * this.numTiles
            * (Math.PI - Math.log(Math.tan(QUARTER_PI + latitude / 2)));

    // if (zoomLevel != 0) {
    //   double roundedZoomLevel = Math.ceil(this.zoomLevel * log2(this.getWidth() / 256.0));
    //   double n = Math.pow(2, roundedZoomLevel);
    //   double x256 = n * ((1.0 + (longtitude / Math.PI)) / 2.0);
    //   double y256 = n * ((1.0 - (Math.log(Math.tan(latitude) + 1.0 / Math.cos(latitude)) /
    // Math.PI)) / 2.0);

    //   System.out.println(Math.floor(x256) + " " + Math.floor(y256) + " " + roundedZoomLevel);
    // }

    return new Point((int) x, (int) y);
  }
}
