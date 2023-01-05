package io.github.cszach.Trailblazer.tiles;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.awt.Point;

/**
 * A tile manager manages a collection of {@link Tile} instances that have different locations and
 * zoom levels, but must have the same width and height. It does this by using an internal builder,
 * whose URL template configuration is specified by the user, and keeps the URL template (along with
 * subdomain and API key, if any) constant.
 *
 * A tile manager provides API to load tiles as well as clear tiles residing in memory by X & Y
 * coordinates as well as zoom level.
 */
public class TileManager {
  /**
   * The internal {@link RasterTileBuilder} this manager uses to load tiles from.
   */
  private RasterTileBuilder builder;
  /**
   * The number of zoom levels that this tile manager is managing.
   */
  private int numZoomLevels;
  /**
   * The maximum number of bytes of all tile images that this tile manager can manage at a time.
   */
  private int maxBytes;

  public TileManager(TileURLTemplate urlTemplate, Optional<String> subdomain,
      Optional<String> apiKey, int numZoomLevels, int maxBytes) {
    this.numZoomLevels = numZoomLevels;
    this.maxBytes = maxBytes;

    this.builder = new RasterTileBuilder();
    this.builder.setURLTemplate(urlTemplate);
    subdomain.ifPresent(s -> this.builder.setSubdomain(s));
    apiKey.ifPresent(a -> this.builder.setApiKey(a));
  }
}
