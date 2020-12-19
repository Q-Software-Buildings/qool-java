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

package io.qsoftware.qooljava;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.qsoftware.qooljava.database.Credentials;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import java.util.Collection;
import java.util.Map;

public final class SessionFactoryFactory {
  public static SessionFactoryFactory create(
    SessionFactorySettings settings,
    Credentials credentials
  ) {
    Preconditions.checkNotNull(settings);
    Preconditions.checkNotNull(credentials);
    return new SessionFactoryFactory(settings, credentials);
  }

  private final SessionFactorySettings settings;
  private final Credentials credentials;

  private SessionFactoryFactory(
    SessionFactorySettings settings,
    Credentials credentials
  ) {
    this.settings = settings;
    this.credentials = credentials;
  }

  private SessionFactory sessionFactory;

  public void initializeSessionFactory() {
    var serviceRegistry = createServiceRegistry();
    var sessionMetadata = createSessionMetadata(serviceRegistry);
    sessionFactory = sessionMetadata.getSessionFactoryBuilder().build();
  }

  private StandardServiceRegistry createServiceRegistry() {
    StandardServiceRegistryBuilder registryBuilder = new
      StandardServiceRegistryBuilder();
    Map<String, String> settings = createSettings();

    registryBuilder.applySettings(settings);
    return registryBuilder.build();
  }

  private Map<String, String> createSettings() {
    Map<String, String> settings = Maps.newHashMap();
    settings.put(Environment.DRIVER, this.settings.driver());
    settings.put(Environment.URL, credentials.createJdbcConnectionUrl());
    settings.put(Environment.USER, credentials.username());
    settings.put(Environment.PASS, credentials.password());
    settings.putAll(this.settings.additionalSettings());
    return settings;
  }

  private Metadata createSessionMetadata(StandardServiceRegistry serviceRegistry) {
    MetadataSources sources = new MetadataSources(serviceRegistry);
    entities().forEach(sources::addAnnotatedClass);
    return sources.buildMetadata();
  }

  private Collection<Class<?>> entities() {
    return Lists.newArrayList();
  }

  public SessionFactory receiveSessionFactory() {
    if (sessionFactory == null) {
      initializeSessionFactory();
    }
    return sessionFactory;
  }
}
