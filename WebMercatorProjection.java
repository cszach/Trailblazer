import java.awt.Point;

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

    return new Point((int) x, (int) y);
  }
}