package io.github.cszach.Trailblazer.tiles;

import java.util.Optional;

public interface TileProvider {
  public static Optional<String> getURLTemplate(int provider);

  private static boolean requiresSubdomain(int provider);
}
