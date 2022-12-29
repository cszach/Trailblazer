package io.github.cszach.Trailblazer.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import io.github.cszach.Trailblazer.geo.Intersection;
import io.github.cszach.Trailblazer.geo.Road;
import io.github.cszach.Trailblazer.geo.Geography;
import io.github.cszach.Trailblazer.projection.Projection;

/**
 * A {@code JPanel} that displays a map of a {@code Geography}.
 *
 * @see Geography
 */
public class MapPanel extends TransformPanel {
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
  private Rectangle boundingBox;

  private final double LOG_2 = Math.log(2);

  /**
   * Constructs a new {@code MapPanel} to draw the given {@code Geography} using the given
   * {@code Projection}.
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
   *        mode
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
   * <p>
   * Works only when the {@code project} method has been invoked.
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
   * <p>
   * Rotation and scale are preserved.
   */
  public void resetView() {
    computeBoundingBox();

    while (this.boundingBox.getWidth() < 12.0) {
      this.projection.setZoomLevel(this.projection.getZoomLevel() + 1);
      this.project();
      computeBoundingBox();
    }

    this.projection.setZoomLevel((Math
        .log(this.projection.getWidth()
            / (this.boundingBox.getWidth() / Math.pow(2.0, this.projection.getZoomLevel())))
        / LOG_2));
    this.project();

    computeBoundingBox();
    this.setFocus(this.boundingBox);

    this.center();

    this.repaint();
  }

  /**
   * Draws the map by drawing the roads.
   *
   * <p>
   * This method only works when the map has been projected. See the {@code project} method.
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
      } else {
        g2d.setColor(Color.BLACK);
      }

      g2d.drawLine((int) intersection1.getX(), (int) intersection1.getY(),
          (int) intersection2.getX(), (int) intersection2.getY());
    }

    if (this.debugging && this.boundingBox != null) {
      Point boundingBoxCorner = this.boundingBox.getLocation();

      g.setColor(Color.BLUE);
      g.drawRect((int) boundingBoxCorner.getX(), (int) boundingBoxCorner.getY(),
          (int) (this.boundingBox.getWidth()), (int) (this.boundingBox.getHeight()));
    }
  }
}
