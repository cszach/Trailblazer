package io.github.cszach.Trailblazer.tiles;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * A raster tile provider provides raster tiles to be drawn on the map panel.
 *
 * <p>
 * A raster tile provider provides tiles given their URLs which must contain the x and y coordinates
 * of the tile as well as the zoom level of the tile. A URL template is associated with each raster
 * tile provider. The URL template contains the following placeholders:
 * <ul>
 * <li>{@code ${x}}: the x coordinate of the tile;</li>
 * <li>{@code ${y}}: the y coordinate of the tile;</li>
 * <li>{@code ${z}}: the zoom level of the tile;</li>
 * <li>{@code ${s}}: the subdomain where the tile may be accessed at;</li>
 * <li>{@code ${k}}: the API key used to access the provider's API.</li>
 * </ul>
 * The last two placeholders are optional; if a subdomain is required, the provider's
 * {@code requiresSubdomain} attribute must be {@code true}. Likewise, if an API key is required,
 * the provider's {@code requiresApiKey} attribute must be {@code true}.
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
 * @see RasterTileBuilder
 * @see #get(int)
 */
public class RasterTileProvider {
  /**
   * OpenStreetMap's Standard tile layer
   */
  public static final int OPENSTREETMAP = 0;

  /**
   * The URL template of this provider; the URL at which tiles can be accessed.
   */
  private String urlTemplate;
  /**
   * Whether this provider requires a subdomain to access tiles.
   */
  private boolean requiresSubdomain;
  /**
   * Whether this provider requires an API key to access tiles.
   */
  private boolean requiresApiKey;

  /**
   * Constructs a new {@code RasterTileProvider} given its URL template.
   *
   * <p>
   * The URL template may contain these placeholders:
   * <ul>
   * <li>{@code ${x}}: the x coordinate of the tile;</li>
   * <li>{@code ${y}}: the y coordinate of the tile;</li>
   * <li>{@code ${z}}: the zoom level of the tile;</li>
   * <li>{@code ${s}}: the subdomain where the tile may be accessed at;</li>
   * <li>{@code ${k}}: the API key used to access the provider's API.</li>
   * </ul>
   *
   * @param urlTemplate the template of the URL at which tiles are provided by the provider
   * @param requiresSubdomain {@code true} if the provider requires access at a subdomain,
   *        {@code false} otherwise
   * @param requiresApiKey {@code true} if the provider requires an API key, {@code false} otherwise
   */
  public RasterTileProvider(String urlTemplate, boolean requiresSubdomain, boolean requiresApiKey) {
    this.urlTemplate = urlTemplate;
    this.requiresSubdomain = requiresSubdomain;
    this.requiresApiKey = requiresApiKey;
  }

  /**
   * Constructs a new {@code RasterTileProvider} given its URL template; the provider's subdomain
   * and API key requirements are determined from the URL.
   *
   * <p>
   * The URL template may contain these placeholders:
   * <ul>
   * <li>{@code ${x}}: the x coordinate of the tile;</li>
   * <li>{@code ${y}}: the y coordinate of the tile;</li>
   * <li>{@code ${z}}: the zoom level of the tile;</li>
   * <li>{@code ${s}}: the subdomain where the tile may be accessed at;</li>
   * <li>{@code ${k}}: the API key used to access the provider's API.</li>
   * </ul>
   *
   * <p>
   * The provider's {@code requiresSubdomain} attribute is {@code true} if the template contains the
   * string "{@code ${s}}", {@code false} otherwise. The provider's {@code requiresApiKey} attribute
   * is {@code true} if the template contains the string "{@code ${k}}", {@code false} otherwise.
   *
   * @param urlTemplate the template of the URL at which tiles are provided by the provider
   */
  public RasterTileProvider(String urlTemplate) {
    this(urlTemplate, urlTemplate.indexOf("${s}") != -1, urlTemplate.indexOf("${k}") != -1);
  }

  /**
   * Returns a public, known raster tile provider, given its identifer.
   *
   * @param provider the identifier of the provider e.g. {@code OPENSTREETMAP}
   * @return the provider through a {@code RasterTileProvider} object.
   */
  public static RasterTileProvider get(int provider) {
    switch (provider) {
      case OPENSTREETMAP:
        return new RasterTileProvider("https://tile.openstreetmap.org/${z}/${x}/${y}.png", false,
            false);
      default:
        throw new IllegalArgumentException("Invalid raster tile provider identifier");
    }
  }

  /**
   * Returns the URL template of this provider.
   *
   * @return the URL template of this provider.
   */
  public String getUrlTemplate() {
    return this.urlTemplate;
  }

  /**
   * Returns whether this provider requires a subdomain to access tiles.
   *
   * @return {@code true} if this provider requires a subdomain to access tiles, {@code false}
   *         otherwise.
   */
  public boolean requiresSubdomain() {
    return this.requiresSubdomain;
  }

  /**
   * Returns whether this provider requires an API key to access tiles.
   *
   * @return {@code true} if this provider requires an API key to access tiles, {@code false}
   *         otherwise.
   */
  public boolean requiresApiKey() {
    return this.requiresApiKey;
  }

  /**
   * Returns a tile provided by this provider given the tile's x and y coordinates, the tile's zoom
   * level, a subdomain, and an API key (the last two may not be required).
   *
   * @param tileX the x coordinate of the upper left coordinate of the tile
   * @param tileY the y coordinate of the upper left coordinate of the tile
   * @param zoomLevel the zoom level of the tile
   * @param subdomain the subdomain where the tile may be accessed, or {@code null} if the URL
   *        template does not have a placeholder for a subdomain
   * @param apiKey the API key required by the provider to access the tile, or {@code null} if the
   *        URL template does not have a placeholder for an API key
   * @return the raster image provided by the provider when the given arguments fill in the
   *         placeholders of the URL template.
   *
   * @throws IllegalArgumentException if the provider requires a subdomain or an API key but
   *         received {@code null} in the arguments;
   * @throws MalformedURLException when the URL string contains an invalid protocol;
   * @throws IOException when there is an issue reading the image from the URL.
   */
  public Image getTile(int tileX, int tileY, int zoomLevel, String subdomain, String apiKey)
      throws MalformedURLException, IOException {

    if (this.requiresSubdomain && subdomain == null) {
      throw new IllegalArgumentException(
          "Raster tile provider requires a subdomain, received null");
    }

    if (this.requiresApiKey && apiKey == null) {
      throw new IllegalArgumentException("Raster tile provider requires an API key, received null");
    }

    String tileXStr = Integer.toString(tileX);
    String tileYStr = Integer.toString(tileY);
    String zoomLevelStr = Integer.toString(zoomLevel);

    String urlStr = this.urlTemplate.replace("${x}", tileXStr).replace("${y}", tileYStr)
        .replace("${z}", zoomLevelStr);

    if (this.requiresSubdomain) {
      urlStr = urlStr.replace("${s}", subdomain);
    }

    if (this.requiresApiKey) {
      urlStr = urlStr.replace("${k}", apiKey);
    }

    URL url = new URL(urlStr);
    BufferedImage image = ImageIO.read(url);

    if (image == null)
      throw new IOException("Could not read image at URL " + urlStr);

    return image;
  }
}
