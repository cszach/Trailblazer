package io.github.cszach.Trailblazer.tiles;

import java.awt.Image;

/**
 * A raster tile, which is rendered and has a raster image associated with it.
 *
 * @see Tile
 * @see VectorTile
 */
public class RasterTile extends Tile {
  /**
   * The raster image of this tile.
   */
  private Image image;

  /**
   * Constructs a tile given its x, y, zoom level, and raster image.
   *
   * @param x the x coordinate of the upper corner of the tile
   * @param y the y coordinate of the upper corner of the tile
   * @param image the raster image of the tile
   */
  public RasterTile(int x, int y, int zoomLevel, Image image) {
    super(x, y, zoomLevel);
    this.image = image;
  }

  public Image getImage() {
    return this.image;
  }
}
