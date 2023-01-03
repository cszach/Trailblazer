package io.github.cszach.Trailblazer.tiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RasterTileBuilder {
  private RasterTileProvider provider = RasterTileProvider.get(RasterTileProvider.OPENSTREETMAP);
  private Optional<Integer> tileX;
  private Optional<Integer> tileY;
  private Optional<Integer> zoomLevel;
  private Optional<String> subdomain;
  private Optional<String> apiKey;

  public RasterTileBuilder() {
    this.tileX = Optional.empty();
    this.tileY = Optional.empty();
    this.zoomLevel = Optional.empty();
    this.subdomain = Optional.empty();
    this.apiKey = Optional.empty();
  }

  public RasterTileProvider getProvider() {
    return this.provider;
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

  public void setProvider(RasterTileProvider provider) {
    if (provider == null) {
      throw new IllegalArgumentException("Raster tile provider cannot be null");
    }

    this.provider = provider;
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

  public Image getTile() {
    if (!(this.tileX.isPresent() && this.tileY.isPresent() && this.zoomLevel.isPresent())) {
      throw new Exception("");
    }
  }
}
