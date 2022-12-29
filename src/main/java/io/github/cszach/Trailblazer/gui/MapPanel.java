<<<<<<<< HEAD:src/main/java/io/github/cszach/Trailblazer/gui/MapPanel.java
package io.github.cszach.Trailblazer.gui;

========
import java.awt.BasicStroke;
>>>>>>>> main:MapPanel.java
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

<<<<<<<< HEAD:src/main/java/io/github/cszach/Trailblazer/gui/MapPanel.java
import io.github.cszach.Trailblazer.geo.Intersection;
import io.github.cszach.Trailblazer.geo.Road;
import io.github.cszach.Trailblazer.geo.Geography;
import io.github.cszach.Trailblazer.projection.Projection;

========
>>>>>>>> main:MapPanel.java
/**
 * A {@code JPanel} that displays a map of a {@code Geography}.
 *
 * @see Geography
 */
public class MapPanel extends TransformPanel {
<<<<<<<< HEAD:src/main/java/io/github/cszach/Trailblazer/gui/MapPanel.java
  /**
   * The geographical data of the map that is displayed by this panel.
   */
  private Geography geo;
  /**
   * The projection used to draw the map.
   */
  private Projection projection;

  /**
   * The debugging flag.
   */
  private boolean debugging = false;
  /**
   * The bounding box of the drawn map without any {@code AffineTransform}.
   */
========
  /** The geographical data of the map that is displayed by this panel. */
  private Geography geo;
  /** The projection used to draw the map. */
  private Projection projection;

  /** The debugging flag. */
  private boolean debugging = false;
  /** The bounding box of the drawn map without any {@code AffineTransform}. */
>>>>>>>> main:MapPanel.java
  private Rectangle boundingBox;

  private final BasicStroke roadStroke = new BasicStroke(1.0f);
  private final BasicStroke pathStroke = new BasicStroke(2.0f);
  private final double LOG_2 = Math.log(2);

  /**
<<<<<<<< HEAD:src/main/java/io/github/cszach/Trailblazer/gui/MapPanel.java
   * Constructs a new {@code MapPanel} to draw the given {@code Geography} using the given
   * {@code Projection}.
========
   * Constructs a new {@code MapPanel} to draw the given {@code Geography} using the given {@code
   * Projection}.
>>>>>>>> main:MapPanel.java
   *
   * @param geo the {@code Geography} for display
   * @param projection the {@code Projection} used to display the map
   */
  public MapPanel(Geography geo, Projection projection) {
    super();

    this.geo = geo;
    this.projection = projection;
  }

  /**
   * Turns on or off debugging mode.
   *
   * @param debugging {@code true} to turn on debugging mode, {@code false} to turn off debugging
<<<<<<<< HEAD:src/main/java/io/github/cszach/Trailblazer/gui/MapPanel.java
   *        mode
========
   *     mode
>>>>>>>> main:MapPanel.java
   */
  public void setDebugging(boolean debugging) {
    this.debugging = debugging;
  }

  /**
   * Assigns x and y values for all the intersections in the current {@code Geography} using the
   * current {@code Projection}.
   */
  public void project() {
    for (Intersection intersection : this.geo.getIntersections()) {
      intersection.setLocation(
          this.projection.project(intersection.getLatitude(), intersection.getLongtitude()));
    }
  }

  /**
   * Computes the bounding box of the projected map.
   *
<<<<<<<< HEAD:src/main/java/io/github/cszach/Trailblazer/gui/MapPanel.java
   * <p>
   * Works only when the {@code project} method has been invoked.
========
   * <p>Works only when the {@code project} method has been invoked.
>>>>>>>> main:MapPanel.java
   *
   * @see project
   */
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

  /**
   * Reprojects the map such that the map's bounding box fits the panel and repaints.
   *
<<<<<<<< HEAD:src/main/java/io/github/cszach/Trailblazer/gui/MapPanel.java
   * <p>
   * Rotation and scale are preserved.
========
   * <p>Rotation and scale are preserved.
>>>>>>>> main:MapPanel.java
   */
  public void resetView() {
    computeBoundingBox();

    while (this.boundingBox.getWidth() < 12.0) {
      this.projection.setZoomLevel(this.projection.getZoomLevel() + 1);
      this.project();
      computeBoundingBox();
    }

<<<<<<<< HEAD:src/main/java/io/github/cszach/Trailblazer/gui/MapPanel.java
    this.projection.setZoomLevel((Math
        .log(this.projection.getWidth()
            / (this.boundingBox.getWidth() / Math.pow(2.0, this.projection.getZoomLevel())))
        / LOG_2));
========
    this.projection.setZoomLevel(
        (Math.log(
                this.projection.getWidth()
                    / (this.boundingBox.getWidth() / Math.pow(2.0, this.projection.getZoomLevel())))
            / LOG_2));
>>>>>>>> main:MapPanel.java
    this.project();

    computeBoundingBox();
    this.setFocus(this.boundingBox);

    this.center();

    this.repaint();
  }

  /**
   * Draws the map by drawing the roads.
   *
<<<<<<<< HEAD:src/main/java/io/github/cszach/Trailblazer/gui/MapPanel.java
   * <p>
   * This method only works when the map has been projected. See the {@code project} method.
========
   * <p>This method only works when the map has been projected. See the {@code project} method.
>>>>>>>> main:MapPanel.java
   *
   * @see project
   */
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

      g2d.drawLine((int) intersection1.getX(), (int) intersection1.getY(),
          (int) intersection2.getX(), (int) intersection2.getY());
    }

    if (this.debugging && this.boundingBox != null) {
      Point boundingBoxCorner = this.boundingBox.getLocation();

<<<<<<<< HEAD:src/main/java/io/github/cszach/Trailblazer/gui/MapPanel.java
      g.setColor(Color.BLUE);
      g.drawRect((int) boundingBoxCorner.getX(), (int) boundingBoxCorner.getY(),
          (int) (this.boundingBox.getWidth()), (int) (this.boundingBox.getHeight()));
========
      g2d.setColor(Color.BLUE);
      g2d.drawRect(
          (int) boundingBoxCorner.getX(),
          (int) boundingBoxCorner.getY(),
          (int) (this.boundingBox.getWidth()),
          (int) (this.boundingBox.getHeight()));
>>>>>>>> main:MapPanel.java
    }
  }
}
