package io.github.cszach.Trailblazer.tiles;

import java.awt.Point;

/**
 * A tile interface.
 */
public interface Tile {
  /**
   * Returns the X coordinate of this tile.
   *
   * @return the X coordinate of the upper-left corner of this tile.
   */
  public int getX();

  /**
   * Returns the Y coordinate of this tile.
   *
   * @return the Y coordinate of the upper-left corner of this tile.
   */
  public int getY();

  /**
   * Returns the width of this tile.
   *
   * @return the width of this tile.
   */
  public int getWidth();

  /**
   * Returns the height of this tile.
   *
   * @return the height of this tile.
   */
  public int getHeight();

  /**
   * Returns the location of this tile.
   *
   * @return the {@code Point} that is the upper-left corner of this tile.
   */
  public Point getLocation();

  /**
   * Returns the zoom level of this tile.
   *
   * @return the zoom level of this tile.
   */
  public int getZoomLevel();

  /**
   * Sets the X coordinate of this tile.
   *
   * @param x the new X coordinate of the upper-left corner of this tile
   */
  public void setX(int x);

  /**
   * Sets the Y coordinate of this tile.
   *
   * @param y the new Y coordinate of the upper-left corner of this tile
   */
  public void setY(int y);

  /**
   * Sets the location of this tile.
   *
   * @param location the new {@code Point} that is the upper-left corner of this tile
   */
  public void setLocation(Point location);
}
