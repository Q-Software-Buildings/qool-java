package io.qsoftware.qooljava.logging;

import io.qsoftware.qooljava.database.CredentialsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class CredentialsBuilderCloneTest {

  private static final CredentialsBuilder DEFAULT_CREDENTIALS =
    CredentialsBuilder.newBuilder()
      .withHost("localhost")
      .withPort(3306)
      .withDatabase("database")
      .withUsername("root")
      .withPassword("")
      .withUrlDatabase("mysql")
      .withUrlOptions(CredentialsBuilder.DEFAULT_OPTIONS + "&serverTimezone=UTC")
      .createCredentials();

  @Test
  public void testClone() {
    CredentialsBuilder clonedCredentials = DEFAULT_CREDENTIALS.clone();
    validate(DEFAULT_CREDENTIALS, clonedCredentials);
  }

  void validate(
    CredentialsBuilder defaultCredentials,
    CredentialsBuilder clonedCredentials
  ) {
    Assertions.assertEquals(defaultCredentials, clonedCredentials);
    Assertions.assertNotSame(defaultCredentials, clonedCredentials);
    Assertions.assertSame(defaultCredentials.getClass(),
      clonedCredentials.getClass());
  }
}
