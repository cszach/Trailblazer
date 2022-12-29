package io.github.cszach.Trailblazer.tiles;

import java.net.MalformedURLException;
import java.net.URL;

public class TileProviders {
  public static final int OPENSTREETMAP = 0;

  public static URL getTile(int provider, int tileX, int tileY, int zoomLevel) {
    switch (provider) {
      case 0:
        try {
          return new URL(
              String.format("https://tile.openstreetmap.org/%d/%d/%d.png", zoomLevel, tileX, tileY));
        }
        catch (MalformedURLException exception) {
          return null;
        }
    }

    return null; // TODO: raise exception instead
  }
}
