package io.github.cszach.Trailblazer.tiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RasterTileBuilder {
  private Optional<TileURLTemplate> urlTemplate;
  private Optional<Integer> tileX;
  private Optional<Integer> tileY;
  private Optional<Integer> zoomLevel;
  private Optional<String> subdomain;
  private Optional<String> apiKey;

  public RasterTileBuilder() {
    this.urlTemplate = Optional.empty();
    this.tileX = Optional.empty();
    this.tileY = Optional.empty();
    this.zoomLevel = Optional.empty();
    this.subdomain = Optional.empty();
    this.apiKey = Optional.empty();
  }

  public Optional<TileURLTemplate> getURLTemplate() {
    return this.urlTemplate;
  }

  public Optional<Integer> getTileX() {
    return this.tileX;
  }

  public Optional<Integer> getTileY() {
    return this.tileY;
  }

  public Optional<Integer> getZoomLevel() {
    return this.zoomLevel;
  }

  public Optional<String> getSubdomain() {
    return this.subdomain;
  }

  public Optional<String> getApiKey() {
    return this.apiKey;
  }

  public void setURLTemplate(TileURLTemplate urlTemplate) {
    if (urlTemplate == null) {
      throw new IllegalArgumentException("Tile URL template cannot be null");
    }

    this.urlTemplate = Optional.of(urlTemplate);
  }

  public void setTileX(int x) {
    this.tileX = Optional.of(x);
  }

  public void setTileY(int y) {
    this.tileY = Optional.of(y);
  }

  public void setZoomLevel(int zoomLevel) {
    this.zoomLevel = Optional.of(zoomLevel);
  }

  public void setSubdomain(String subdomain) {
    this.subdomain = Optional.of(subdomain);
  }

  public void setApiKey(String apiKey) {
    this.apiKey = Optional.of(apiKey);
  }

  public boolean isReadyToBuild() {
    if (!(this.urlTemplate.isPresent() && this.tileX.isPresent() && this.tileY.isPresent()))
      return false;
    if (this.urlTemplate.get().requiresSubdomain() && !this.subdomain.isPresent())
      return false;
    if (this.urlTemplate.get().requiresApiKey() && !this.apiKey.isPresent())
      return false;

    return true;
  }

  /**
   * Returns a tile URL given the builder's current configuration.
   *
   * <p>
   * This method directly invokes
   * {@link TileURLTemplate#getTileURL(int, int, int, Optional, Optional)}.
   *
   * @return the {@code URL} built by this builder with its current ingredients.
   *
   * @throws MalformedURLException if the URL created is invalid, which is likely because the URL
   *         template does not have a protocol or has an invalid protocol;
   * @throws BuilderException if the builder does not have enough ingredients to build a URL.
   *
   * @see TileURLTemplate#getTileURL(int, int, int, Optional, Optional)
   */
  public URL getTileURL() throws MalformedURLException, BuilderException {
    if (this.isReadyToBuild()) {
      return this.urlTemplate.get().getTileURL(this.tileX.get(), this.tileY.get(),
          this.zoomLevel.get(), this.subdomain, this.apiKey);
    } else {
      throw new BuilderException("Builder is not ready to build");
    }
  }
}
