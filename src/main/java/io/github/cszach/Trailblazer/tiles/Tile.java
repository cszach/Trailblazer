package io.github.cszach.Trailblazer.tiles;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Tile extends Point {
  int zoomLevel;
  BufferedImage image;

  public Tile(int x, int y, int zoomLevel, BufferedImage image) {
    super(x, y);
    this.zoomLevel = zoomLevel;
    this.image = image;
  }
}
