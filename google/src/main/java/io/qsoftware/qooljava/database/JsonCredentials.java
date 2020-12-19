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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qsoftware.qooljava.io.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public final class JsonCredentials implements Credentials {
  private final String host;
  private final int port;
  private final String database;
  private final String username;
  private final String password;

  private JsonCredentials(
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

  private static final Gson JSON_SERIALIZER = new GsonBuilder()
    .serializeNulls()
    .setPrettyPrinting()
    .create();

  @Override
  public String toString() {
    return JSON_SERIALIZER.toJson(this);
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
    if (!(object instanceof JsonCredentials)) {
      return false;
    }
    JsonCredentials credentials = (JsonCredentials) object;
    return credentials.host.equals(host)
      && credentials.port == port
      && credentials.database.equals(database)
      && credentials.username.equals(username)
      && credentials.password.equals(password);
  }

  public JsonCredentials clone() {
    return new JsonCredentials(host, port, database, username, password);
  }

  public void saveToFile(File file) throws IOException {
    Files.existsFileThrowException(file);
    String serialized = JSON_SERIALIZER.toJson(this);
    Files.clearWriteAndFlush(file, serialized, Files.StartPoint.BEGINNING);
  }

  public void saveAndCreateToFile(File file) throws IOException {
    Files.createFileOrDirectory(file);
    saveToFile(file);
  }

  public static JsonCredentials loadFromFile(File file)
    throws IOException
  {
    Files.existsFileThrowException(file);
    try (FileReader reader = new FileReader(file)) {
      return JSON_SERIALIZER.fromJson(reader, JsonCredentials.class);
    }
  }

  public static JsonCredentials createFromPreset(Credentials credentials) {
    return new JsonCredentials(credentials.host(),
      credentials.port(),
      credentials.database(),
      credentials.username(),
      credentials.password());
  }
}
