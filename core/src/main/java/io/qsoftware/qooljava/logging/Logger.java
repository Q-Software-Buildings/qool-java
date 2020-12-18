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

import io.qsoftware.qooljava.Preconditions;
import io.qsoftware.qooljava.io.AsyncPrintStream;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public final class Logger {
  public enum DebugMode {
    ACTIVATE,
    DEACTIVATE
  }
  public enum ColorCodes {
    ALL,
    WARN,
    ERROR,
    NO
  }

  private static final Collection<LogAppender> appenders;
  private static SimpleDateFormat dateFormat;
  private static PrintStream printStream;
  private static DebugMode debugMode = DebugMode.DEACTIVATE;
  private static ColorCodes colorCodes = ColorCodes.ERROR;

  static {
    appenders = Collections.emptyList();
  }

  private Logger() {}

  public static void initialize() {
    printStream = AsyncPrintStream.createByOutputStream(System.out);
    System.setOut(printStream);
  }

  public static void enableDebugMode() {
    debugMode = DebugMode.ACTIVATE;
  }

  public static void updateColorCodes(ColorCodes colorCodes) {
    Preconditions.checkNotNull(colorCodes);
    Logger.colorCodes = colorCodes;
  }

  public static void setDateFormat(String format) {
    Preconditions.checkNotNull(format);
    dateFormat = new SimpleDateFormat(format);
  }

  public static void addLogAppender(LogAppender appender) {
    Preconditions.checkNotNull(appender);
    appenders.add(appender);
  }

  private static final String ERROR_MESSAGE = "Unexpected log level: %s." +
    " Message: %s";

  public static void log(LogLevel level, String message, Object... options) {
    switch (level) {
      case INFO:
      case WARNING:
      case ERROR:
      case FATAL:
        printLog(level, message, options);
        break;
      case DEBUG:
        if (debugMode == DebugMode.ACTIVATE) {
          printLog(level, message, options);
        }
        break;
      default:
        printLog(LogLevel.FATAL, ERROR_MESSAGE, level, message);
        break;
    }
  }

  public static void debug(String message, Object... options) {
    log(LogLevel.DEBUG, message, options);
  }

  public static void info(String message, Object... options) {
    log(LogLevel.INFO, message, options);
  }

  public static void warn(String message, Object... options) {
    log(LogLevel.WARNING, message, options);
  }

  public static void error(String message, Object... options) {
    log(LogLevel.ERROR, message, options);
  }

  public static void fatal(String message, Object... options) {
    log(LogLevel.FATAL, message, options);
  }

  private static void printLog(
    LogLevel level,
    String message,
    Object... options
  ) {
    checkStreamState(message, options);

    message = String.format(message, options);
    message = appendLogLevel(message, level);
    message = tryAppendDate(message);
    var finalMessage = appendColorCode(message, level);

    printStream.println(finalMessage);
    appenders.forEach(appender -> appender.append(finalMessage));
  }

  private static void checkStreamState(String message, Object... options) {
    if (printStream == null) {
      System.err.printf("Logger wasn't correct initialized. Message: %s",
        String.format(message, options));
      throw new IllegalStateException();
    }
  }

  // Message-Format: dd.MM.yyyy HH:mm:ss [LEVEL]: %message%

  private static String appendLogLevel(String message, LogLevel level) {
    return String.format("[%s]: %s", level, message);
  }

  private static String tryAppendDate(String message) {
    if (dateFormat != null) {
      String date = dateFormat.format(new Date());
      return String.format("%s %s", date, message);
    }
    return message;
  }

  private static final String DEBUG_COLOR_CODE = "\033[93m";
  private static final String INFO_COLOR_CODE = "\033[32m";
  private static final String WARNING_COLOR_CODE = "\033[33m";
  private static final String ERROR_COLOR_CODE = "\033[91m";
  private static final String FATAL_COLOR_CODE = "\033[31m\033[1m";

  private static String appendColorCode(String message, LogLevel level) {
    if (colorCodes != ColorCodes.NO) {
      switch (colorCodes) {
        case ALL -> message = replaceAllColors(message, level);
        case WARN ->  message = replaceWarnColors(message, level);
        case ERROR ->  message = replaceErrorColors(message, level);
      }
    }
    return message;
  }

  private static String replaceAllColors(String message, LogLevel level) {
    switch (level) {
      case DEBUG -> message = addColorCodes(message, DEBUG_COLOR_CODE);
      case INFO -> message = addColorCodes(message, INFO_COLOR_CODE);
      case WARNING -> message = addColorCodes(message, WARNING_COLOR_CODE);
      case ERROR -> message = addColorCodes(message, ERROR_COLOR_CODE);
      case FATAL -> message = addColorCodes(message, FATAL_COLOR_CODE);
    }
    return message;
  }

  private static String replaceWarnColors(String message, LogLevel level) {
    switch (level) {
      case WARNING -> message = addColorCodes(message, WARNING_COLOR_CODE);
      case ERROR -> message = addColorCodes(message, ERROR_COLOR_CODE);
      case FATAL -> message = addColorCodes(message, FATAL_COLOR_CODE);
    }
    return message;
  }

  private static String replaceErrorColors(String message, LogLevel level) {
    switch (level) {
      case ERROR -> message = addColorCodes(message, ERROR_COLOR_CODE);
      case FATAL -> message = addColorCodes(message, FATAL_COLOR_CODE);
    }
    return message;
  }

  private static final String END_CODE = "\033[0m";

  private static String addColorCodes(String message, String colorCode) {
    return String.format("%s%s%s", colorCode, message, END_CODE);
  }
}
