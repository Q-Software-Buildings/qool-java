/*
MIT License

Copyright (c) 2020 Q-Software (Contact: Qetz#5363)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package io.qsoftware.qooljava.database;

import io.qsoftware.qooljava.Preconditions;

import java.util.Objects;

public final class CredentialsBuilder implements Credentials {
  private final String host;
  private final int port;
  private final String database;
  private final String username;
  private final String password;

  private CredentialsBuilder(
    String host,
    int port,
    String database,
    String username,
    String password) {
    this.host = host;
    this.port = port;
    this.database = database;
    this.username = username;
    this.password = password;
  }

  @Override
  public String host() {
    return host;
  }

  @Override
  public int port() {
    return port;
  }

  @Override
  public String database() {
    return database;
  }

  @Override
  public String username() {
    return username;
  }

  @Override
  public String password() {
    return password;
  }

  private static final String JDBC_CONNECTION_URL_FORMAT
    = "jdbc:%s://%s:%s/%s%s";
  public static final String DEFAULT_OPTIONS
    = "?connectTimeout=0&autoReconnect=true";

  @Override
  public String createJdbcConnectionUrl(String databaseType, String options) {
    return String.format(JDBC_CONNECTION_URL_FORMAT,
      databaseType,
      host,
      port,
      database,
      options);
  }

  private static final String TO_STRING_FORMAT = "CredentialsBuilder{" +
    "host='%s', " +
    "port=%s, " +
    "database='%s', " +
    "username='%s', " +
    "password='%s'}";

  @Override
  public String toString() {
    return String.format(TO_STRING_FORMAT,
      host,
      port,
      database,
      username,
      password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(host, port, database, username, password);
  }

  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof CredentialsBuilder)) {
      return false;
    }
    CredentialsBuilder credentials = (CredentialsBuilder) object;
    return credentials.host.equals(host)
      && credentials.port == port
      && credentials.database.equals(database)
      && credentials.username.equals(username)
      && credentials.password.equals(password);
  }

  public CredentialsBuilder clone() {
    return new CredentialsBuilder(host, port, database, username, password);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static CredentialsBuilder createFromPreset(Credentials credentials) {
    return new CredentialsBuilder(credentials.host(),
      credentials.port(),
      credentials.database(),
      credentials.username(),
      credentials.password());
  }

  public static final class Builder {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    private Builder() {}

    public Builder withHost(String host) {
      this.host = host;
      return this;
    }

    public Builder withPort(int port) {
      this.port = port;
      return this;
    }

    public Builder withDatabase(String database) {
      this.database = database;
      return this;
    }

    public Builder withUsername(String username) {
      this.username = username;
      return this;
    }

    public Builder withPassword(String password) {
      this.password = password;
      return this;
    }

    public CredentialsBuilder createCredentials() {
      Preconditions.checkNotNull(host);
      Preconditions.checkNotNull(database);
      Preconditions.checkNotNull(username);
      Preconditions.checkNotNull(password);
      return new CredentialsBuilder(host, port, database, username, password);
    }
  }
}