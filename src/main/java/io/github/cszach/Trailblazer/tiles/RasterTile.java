package io.github.cszach.Trailblazer.tiles;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * A raster tile is a {@code BufferedImage} that is a tile on a map and has a pair of coordinates to
 * determine the tile's location on the drawing (map) panel and a zoom level associated with the
 * tile.
 *
 * <p>
 * The tile's location is <i>before</i> any transform is applied to the panel's drawing context
 * (e.g. because of mouse drags). For more information on the X and Y coordinates, see the external
 * link in the <i>See also</i> section. Note that the information there corresponds to tiles that
 * are 256 pixels in both width and height.
 *
 * @see <a href="https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#X_and_Y">X and Y</a>
 * @see Tile
 * @see RasterTile
 * @see VectorTile
 */
public class RasterTile extends BufferedImage implements Tile {
  /**
   * The X coordinate of the upper-left corner of this tile on the map panel.
   */
  private int x;
  /**
   * The Y coordinate of the upper-left corner of this tile on the map panel.
   */
  private int y;
  /**
   * The zoom level of this tile.
   */
  private int zoomLevel;

  /**
   * Constructs a new {@code RasterTile} with the specified location and zoom level from a specified
   * {@code BufferedImage}.
   *
   * @param x the X coordinate of the upper-left corner of this tile on the map panel
   * @param y the Y coordinate of the upper-left corner of this tile on the map panel
   * @param zoomLevel the zoom level of the new tile
   * @param image the raster image of the new tile
   */
  public RasterTile(int x, int y, int zoomLevel, BufferedImage image) {
    super(image.getColorModel(), image.getRaster(), image.isAlphaPremultiplied(), null);

    this.x = x;
    this.y = y;
    this.zoomLevel = zoomLevel;
  }

  /**
   * Constructs a new {@code RasterTile} with the specified zoom level and an initial location of
   * (0, 0) from the specified {@code BufferedImage}.
   *
   * <p>
   * The location can be set using {@code setX} and {@code setY} or {@code setLocation}.
   *
   * @param zoomLevel the zoom level of the new tile
   * @param image the raster image of the new tile
   *
   * @see #setX(int)
   * @see #setY(int)
   * @see #setLocation(Point)
   */
  public RasterTile(int zoomLevel, BufferedImage image) {
    this(0, 0, zoomLevel, image);
  }

  @Override
  public int getX() {
    return this.x;
  }

  @Override
  public int getY() {
    return this.y;
  }

  @Override
  public Point getLocation() {
    return new Point(x, y);
  }

  @Override
  public int getZoomLevel() {
    return this.zoomLevel;
  }

  @Override
  public void setX(int x) {
    this.x = x;
  }

  @Override
  public void setY(int y) {
    this.y = y;
  }

  @Override
  public void setLocation(Point location) {
    this.setX((int) location.getX());
    this.setY((int) location.getY());
  }
}
