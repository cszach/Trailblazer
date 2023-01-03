package io.github.cszach.Trailblazer.tiles;

import java.awt.Point;

public abstract class Tile {
  /**
   * The coordinate of the upper-left corner of this tile on the drawing context e.g. a
   * {@code JPanel}.
   */
  private Point location;
  /**
   * The zoom level of this tile.
   */
  private int zoomLevel;

  /**
   * Constructs a new tile with the given location and zoom level
   *
   * @param x the x coordinate of the upper corner of the tile
   * @param y the y coordinate of the upper corner of the tile
   * @param zoomLevel the zoom level of this tile
   */
  public Tile(int x, int y, int zoomLevel) {
    this.location = new Point(x, y);
    this.zoomLevel = zoomLevel;
  }

  /**
   * Returns the coordinate of the upper-left corner of this tile.
   *
   * @return the coordinate of the upper-left corner of this tile.
   */
  public Point getLocation() {
    return this.location;
  }

  /**
   * Returns the zoom level of this tile.
   *
   * @return the zoom level of this tile.
   */
  public int getZoomLevel() {
    return this.zoomLevel;
  }
}
