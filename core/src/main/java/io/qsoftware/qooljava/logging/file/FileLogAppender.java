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

package io.qsoftware.qooljava.logging.file;

import io.qsoftware.qooljava.Preconditions;
import io.qsoftware.qooljava.io.Files;
import io.qsoftware.qooljava.logging.LogAppender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class FileLogAppender implements LogAppender {
  public static FileLogAppender createWithFile(File file) {
    Preconditions.checkNotNull(file);
    secureFileExistence(file);
    return new FileLogAppender(file);
  }

  private static void secureFileExistence(File file) {
    try {
      Files.createFileOrDirectory(file);
    } catch (IOException fileNotCreatable) {
      fileNotCreatable.printStackTrace();
    }
  }

  private final File file;

  private FileLogAppender(File file) {
    this.file = file;
  }

  @Override
  public void append(String message) {
    var logMessage = String.format("%s\n", message);
    try {
      Files.writeAndFlush(file, logMessage, Files.StartPoint.ENDING);
    } catch (FileNotFoundException fileNotFoundFailure) {
      System.err.printf("Could not append log to file for message: %s\n",
        logMessage);
      System.err.println(fileNotFoundFailure.getMessage());
    }
  }
}