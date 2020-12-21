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

package io.qsoftware.qooljava.logging;

import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public final class LoggerTest {
  public static final class TestLogAppender implements LogAppender {
    public static TestLogAppender create() {
      return new TestLogAppender();
    }

    private TestLogAppender() {}

    @Override
    public void append(String message) {
      System.out.println("APPEND: " + message);
    }
  }

  @Test
  @BeforeAll
  public static void testLoggerCreation() {
    Logger.enableDebugMode();
    Logger.updateColorCodes(Logger.ColorCodes.ALL);
    Logger.setDateFormat("dd.MM.yyyy hh:mm:ss");
    Logger.addLogAppender(TestLogAppender.create());
  }

  private static final String TEST_MESSAGE = "Hey, my name is: %s";
  private static final String NAME = "Qetz";

  @Test
  @Order(0)
  public void testDebug() {
    Logger.debug(TEST_MESSAGE, NAME);
  }

  @Test
  @Order(1)
  public void testInfo() {
    Logger.info(TEST_MESSAGE, NAME);
  }

  @Test
  @Order(2)
  public void testWarning() {
    Logger.warn(TEST_MESSAGE, NAME);
  }

  @Test
  @Order(3)
  public void testError() {
    Logger.error(TEST_MESSAGE, NAME);
  }

  @Test
  @Order(4)
  public void testFatal() {
    Logger.fatal(TEST_MESSAGE, NAME);
  }
}
