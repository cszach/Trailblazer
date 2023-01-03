package io.github.cszach.Trailblazer.tiles;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.awt.Point;

public class TileManager {
  private List<Map<Point, RasterTile>> tiles;

  public TileManager(int numZoomLevels) {
    this.tiles = new ArrayList<Map<Point, RasterTile>>(numZoomLevels);

    for (Map setOfTiles : this.tiles) {
      setOfTiles = new HashMap<>();
    }
  }

  public TileManager() {
    this(18);
  }

  void addTile(RasterTile tile) {
    this.tiles[tile.getZoomLevel()].put(tile.getLocation(), tile);
  }
}
