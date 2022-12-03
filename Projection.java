import java.awt.Point;

public interface Projection {
  public int getWidth();
  public int getHeight();
  public double getZoomLevel();
  public void setWidth(int width);
  public void setHeight(int height);
  public void setZoomLevel(double zoomLevel);
  public Point project(double latitude, double longtitude);
}
