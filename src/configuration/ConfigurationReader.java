package configuration;

import java.io.File;

public abstract class ConfigurationReader {

  protected File file;

  public ConfigurationReader(File file) {
    this.file = file;
  }
  public abstract void load() throws Exception;
}
