import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;

public class Map extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
  private Geography geo;
  private Projection projection;

  private int offsetX = 0;
  private int offsetY = 0;
  private double zoom = 1;

  private int prevMouseX;
  private int prevMouseY;

  private boolean debugging = false;
  private Rectangle boundingBox;

  private final double LOG_2 = Math.log(2);

  public Map(Geography geo, Projection projection) {
    this.geo = geo;
    this.projection = projection;

    this.addMouseListener(this);
    this.addMouseMotionListener(this);
    this.addMouseWheelListener(this);
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

  public void adjust() {
    computeBoundingBox();

    while (this.boundingBox.getWidth() == 0) {
      this.projection.setZoomLevel(this.projection.getZoomLevel() + 1);
      this.project();
      computeBoundingBox();
    }

    this.projection.setZoomLevel(
        Math.log(this.projection.getWidth() / this.boundingBox.getWidth()) / LOG_2);
    this.project();

    computeBoundingBox();

    // double boundingBoxRatio = this.boundingBox.getWidth() / this.boundingBox.getHeight();
    // double canvasRatio = this.getWidth() / this.getHeight();

    // if (canvasRatio / boundingBoxRatio > 1) {
    //   this.zoom = this.getHeight() / this.boundingBox.getHeight();
    // } else {
    //   this.zoom = this.getWidth() / this.boundingBox.getWidth();
    // }

    this.offsetX = (int) -(this.zoom * Math.round(boundingBox.getX()));
    this.offsetY = (int) -(this.zoom * Math.round(boundingBox.getY()));

    this.repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    for (Road road : this.geo.getRoads()) {
      Intersection intersection1 = road.getIntersection1();
      Intersection intersection2 = road.getIntersection2();

      if (road.getIsShortestRoad()) {
        g.setColor(Color.RED);
      } else {
        g.setColor(Color.BLACK);
      }

      g.drawLine(
          (int) (this.offsetX + (this.zoom * intersection1.getX())),
          (int) (this.offsetY + (this.zoom * intersection1.getY())),
          (int) (this.offsetX + (this.zoom * intersection2.getX())),
          (int) (this.offsetY + (this.zoom * intersection2.getY())));
    }

    if (this.debugging && this.boundingBox != null) {
      g.setColor(Color.BLUE);
      g.drawRect(
          (int) (this.offsetX + (this.zoom * this.boundingBox.getX())),
          (int) (this.offsetY + (this.zoom * this.boundingBox.getY())),
          (int) (this.zoom * (this.boundingBox.getWidth())),
          (int) (this.zoom * (this.boundingBox.getHeight())));
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {
    this.prevMouseX = e.getX();
    this.prevMouseY = e.getY();
  }

  @Override
  public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

    this.offsetX += x - this.prevMouseX;
    this.offsetY += y - this.prevMouseY;

    this.prevMouseX = x;
    this.prevMouseY = y;

    this.repaint();
  }

  @Override
  public void mouseMoved(MouseEvent e) {}

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    // Update zoom
    double delta = e.getPreciseWheelRotation() / -1000;
    this.zoom += delta;
    // if (zoom + delta > 0.5 || delta > 0) zoom += delta;
    this.repaint();
  }
}
