import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Map extends TransformPanel {
  private Geography geo;
  private Projection projection;

  private boolean debugging = false;
  private Rectangle boundingBox;

  private final BasicStroke roadStroke = new BasicStroke(1.0f);
  private final BasicStroke pathStroke = new BasicStroke(2.0f);
  private final double LOG_2 = Math.log(2);

  public Map(Geography geo, Projection projection) {
    super();

    this.geo = geo;
    this.projection = projection;
  }

  public void setDebugging(boolean debugging) {
    this.debugging = debugging;
  }

  public void project() {
    for (Intersection intersection : this.geo.getIntersections()) {
      intersection.setLocation(
          this.projection.project(intersection.getLatitude(), intersection.getLongtitude()));
    }
  }

  private void computeBoundingBox() {
    int boxX = 0, boxY = 0, boxWidth = 0, boxHeight = 0;
    boolean isFirst = true;

    for (Intersection intersection : this.geo.getIntersections()) {
      if (isFirst) {
        boxX = (int) Math.round(intersection.getX());
        boxY = (int) Math.round(intersection.getY());
        boxWidth = 0;
        boxHeight = 0;

        isFirst = false;
      } else {
        int x = (int) Math.round(intersection.getX());
        int y = (int) Math.round(intersection.getY());

        if (x < boxX) {
          boxX = x;
        } else if (x > boxX + boxWidth) {
          boxWidth = x - boxX;
        }

        if (y < boxY) {
          boxY = y;
        } else if (y > boxY + boxHeight) {
          boxHeight = y - boxY;
        }
      }
    }

    this.boundingBox = new Rectangle(boxX, boxY, boxWidth, boxHeight);
  }

  public void resetView() {
    computeBoundingBox();

    while (this.boundingBox.getWidth() < 12.0) {
      this.projection.setZoomLevel(this.projection.getZoomLevel() + 1);
      this.project();
      computeBoundingBox();
    }

    this.projection.setZoomLevel(
        (Math.log(this.projection.getWidth() / (this.boundingBox.getWidth() / Math.pow(2.0, this.projection.getZoomLevel()))) / LOG_2));
    this.project();

    computeBoundingBox();
    this.setFocus(this.boundingBox);

    this.center();

    this.repaint();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    for (Road road : this.geo.getRoads()) {
      Intersection intersection1 = road.getIntersection1();
      Intersection intersection2 = road.getIntersection2();

      if (road.getIsShortestRoad()) {
        g2d.setColor(Color.RED);
        g2d.setStroke(pathStroke);
      } else {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(roadStroke);
      }

      g2d.drawLine(
          (int) intersection1.getX(),
          (int) intersection1.getY(),
          (int) intersection2.getX(),
          (int) intersection2.getY());
    }

    if (this.debugging && this.boundingBox != null) {
      Point boundingBoxCorner = this.boundingBox.getLocation();

      g2d.setColor(Color.BLUE);
      g2d.drawRect(
          (int) boundingBoxCorner.getX(),
          (int) boundingBoxCorner.getY(),
          (int) (this.boundingBox.getWidth()),
          (int) (this.boundingBox.getHeight()));
    }
  }
}
