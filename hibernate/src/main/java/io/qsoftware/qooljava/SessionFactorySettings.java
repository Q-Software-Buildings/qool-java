package io.qsoftware.qooljava;

import io.qsoftware.qooljava.map.Maps;

import java.util.Map;

public final class SessionFactorySettings {
  public static SessionFactorySettings createOf(
    String driver,
    Map<String, String> additionalSettings
  ) {
    Preconditions.checkNotNull(driver);
    Preconditions.checkNotNull(additionalSettings);
    return new SessionFactorySettings(driver,
      Maps.newHashMap(additionalSettings));
  }

  private final String driver;
  private final Map<String, String> additionalSettings;

  private SessionFactorySettings(
    String driver,
    Map<String, String> additionalSettings
  ) {
    this.driver = driver;
    this.additionalSettings = additionalSettings;
  }

  public String driver() {
    return driver;
  }

  public Map<String, String> additionalSettings() {
    return Maps.newHashMap(additionalSettings);
  }
}
