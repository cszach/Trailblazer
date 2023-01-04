package io.github.cszach.Trailblazer.tiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * A tile URL template of a raster tile provider/server.
 *
 * <p>
 * A raster tile provider (or raster tile server) provides tiles given their URLs which must contain
 * the x and y coordinates of the tile as well as the zoom level of the tile. A URL template is a
 * template of a URL at which a tile provider provides tiles. A URL template contains the following
 * placeholders:
 * <ul>
 * <li>{@code ${x}}: the x coordinate of the tile;</li>
 * <li>{@code ${y}}: the y coordinate of the tile;</li>
 * <li>{@code ${z}}: the zoom level of the tile;</li>
 * <li>{@code ${s}}: the subdomain where the tile may be accessed at;</li>
 * <li>{@code ${k}}: the API key used to access the provider's API.</li>
 * </ul>
 * The last two placeholders are optional; if a subdomain is required, the tile URL template's
 * {@code requiresSubdomain} attribute must be {@code true}. Likewise, if an API key is required,
 * the tile URL template template's {@code requiresApiKey} attribute must be {@code true}.
 *
 * <p>
 * Several tile providers such as OpenStreetMap are known and recognized; the class provides their
 * identifiers through integer constants.
 *
 * <p>
 * An API for getting tiles given x/y coordinates, zoom level, and (optionally) subdomain and API
 * key is provided. However, clients should use the {@code RasterTileBuilder} instead to avoid
 * passing the same arguments repeatedly.
 *
 * <p>
 * For more placeholders such as tile ID, this class may be extended from.
 *
 * @see <a href="https://wiki.openstreetmap.org/wiki/Raster_tile_providers">Raster tile
 *      providers</a>
 * @see RasterTileBuilder
 * @see #of(int)
 */
public class TileURLTemplate {
  /**
   * OpenStreetMap's Standard tile layer
   */
  public static final int OPENSTREETMAP = 0;

  /**
   * The string of this tile URL template.
   */
  private String string;

  /**
   * Whether or not this tile URL template requires a subdomain.
   */
  private boolean requiresSubdomain;

  /**
   * Whether or not this tile URL template requires an API key.
   */
  private boolean requiresApiKey;

  /**
   * Returns the tile URL template of a recognized tile provider, given its identifier.
   *
   * @param provider the integer identifier of the provider
   * @return the {@code TileURLTemplate} of the specified provider.
   *
   * @throws IllegalArgumentException if the identifier is invalid.
   *
   * @see #OPENSTREETMAP
   */
  public static TileURLTemplate of(int provider) {
    String string;
    boolean requiresSubdomain;
    boolean requiresApiKey;

    switch (provider) {
      case OPENSTREETMAP:
        string = "https://tile.openstreetmap.org/${z}/${x}/${y}.png";
        requiresSubdomain = false;
        requiresApiKey = false;
        break;
      default:
        throw new IllegalArgumentException("Invalid tile provider identifier");
    }

    return new TileURLTemplate(string, requiresSubdomain, requiresApiKey);
  }

  /**
   * Constructs a new {@code TileURLTemplate} given its string and requirements.
   *
   * <p>
   * A tile URL template must have a protocol and must contain these placeholders:
   * <ul>
   * <li>{@code ${x}}: the x coordinate;</li>
   * <li>{@code ${y}}: the y coordinate;</li>
   * <li>{@code ${z}}: the zoom level;</li>
   * </ul>
   *
   * <p>
   * A tile URL template may contain these placeholders, depending on the tile provider.
   * <ul>
   * <li>{@code ${s}}: the subdomain where tiles may be accessed at;</li>
   * <li>{@code ${k}}: the API key needed to access the provider's API.</li>
   * </ul>
   *
   * @param string the string of the tile URL template
   * @param requiresSubdomain {@code true} if the new tile URL template requires a subdomain,
   *        {@code false} otherwise
   * @param requiresApiKey {@code true} if the new tile URL template requires an API key,
   *        {@code false} otherwise
   *
   * @throws NullPointerException if {@code string} is {@code null}.
   */
  public TileURLTemplate(String string, boolean requiresSubdomain, boolean requiresApiKey) {
    if (string == null) {
      throw new NullPointerException("The string of a URL template cannot be null");
    }

    this.string = string;
    this.requiresSubdomain = requiresSubdomain;
    this.requiresApiKey = requiresApiKey;
  }

  /**
   * Constructs a new {@code TileURLTemplate} given its string; the subdomain and API key
   * requirements are determined from the string.
   *
   * <p>
   * The tile URL template may contain these placeholders:
   * <ul>
   * <li>{@code ${x}}: the x coordinate;</li>
   * <li>{@code ${y}}: the y coordinate;</li>
   * <li>{@code ${z}}: the zoom level;</li>
   * <li>{@code ${s}}: the subdomain where tiles may be accessed at;</li>
   * <li>{@code ${k}}: the API key needed to access the provider's API.</li>
   * </ul>
   *
   * <p>
   * The tile URL template's {@code requiresSubdomain} attribute is {@code true} if the template
   * contains the string "{@code ${s}}", {@code false} otherwise. The provider's
   * {@code requiresApiKey} attribute is {@code true} if the template contains the string
   * "{@code ${k}}", {@code false} otherwise.
   *
   * @param string the string of the tile URL template
   */
  public TileURLTemplate(String string) {
    this(string, string.indexOf("${s}") != -1, string.indexOf("${k}") != -1);
  }

  /**
   * Returns the string of this tile URL template.
   *
   * @return the string of this tile URL template.
   */
  public String getString() {
    return this.string;
  }

  /**
   * Returns whether or not this tile URL template requires a subdomain to access tiles.
   *
   * @return {@code true} if this URL template requires a subdomain to access tiles, {@code false}
   *         otherwise.
   */
  public boolean requiresSubdomain() {
    return this.requiresSubdomain;
  }

  /**
   * Returns whether or not this URL template requires an API key to access tiles.
   *
   * @return {@code true} if this URL template requires an API key to access tiles, {@code false}
   *         otherwise.
   */
  public boolean requiresApiKey() {
    return this.requiresApiKey;
  }

  /**
   * Returns a tile URL provided the values for this tile URL template's placeholders.
   *
   * @param tileX the X coordinate of the upper left coordinate of the tile
   * @param tileY the Y coordinate of the upper left coordinate of the tile
   * @param zoomLevel the zoom level of the tile
   * @param subdomain the subdomain where the tile may be accessed, or an empty {@code Optional}
   *        instance if the URL template does not have a placeholder for a subdomain
   * @param apiKey the API key required by the provider to access the tile, or an empty
   *        {@code Optional} instance if the URL template does not have a placeholder for an API key
   * @return the URL received when the given arguments fill in the placeholders of this URL
   *         template.
   *
   * @throws IllegalArgumentException if the provider requires a subdomain or an API key but
   *         received an empty {@code Optional} instance in the arguments;
   * @throws MalformedURLException when the result URL string contains an invalid protocol.
   */
  public URL getTile(int tileX, int tileY, int zoomLevel, Optional<String> subdomain,
      Optional<String> apiKey) throws IllegalArgumentException, MalformedURLException {

    if (this.requiresSubdomain && !subdomain.isPresent()) {
      throw new IllegalArgumentException(
          "Raster tile provider requires a subdomain, received empty");
    }

    if (this.requiresApiKey && !apiKey.isPresent()) {
      throw new IllegalArgumentException(
          "Raster tile provider requires an API key, received empty");
    }

    String tileXStr = Integer.toString(tileX);
    String tileYStr = Integer.toString(tileY);
    String zoomLevelStr = Integer.toString(zoomLevel);

    String urlStr = this.string.replace("${x}", tileXStr).replace("${y}", tileYStr).replace("${z}",
        zoomLevelStr);

    if (this.requiresSubdomain) {
      urlStr = urlStr.replace("${s}", subdomain.get());
    }

    if (this.requiresApiKey) {
      urlStr = urlStr.replace("${k}", apiKey.get());
    }

    return new URL(urlStr);
  }
}
