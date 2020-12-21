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

package io.qsoftware.qooljava.io;

import io.qsoftware.qooljava.Preconditions;

import java.io.*;

public final class Files {
  public enum StartPoint {
    BEGINNING(false),
    ENDING(true);

    private boolean asBoolean;

    StartPoint(boolean asBoolean) {
      this.asBoolean = asBoolean;
    }

    public boolean asBoolean() {
      return asBoolean;
    }
  }

  private Files() {}

  public static boolean existsFile(File file) {
    Preconditions.checkNotNull(file);
    return file.exists();
  }

  public static boolean existsFileThrowException(File file)
    throws FileNotFoundException
  {
    Preconditions.checkNotNull(file);
    if (file.exists()) {
      return true;
    }
    throw new FileNotFoundException();
  }

  public static boolean createFileOrDirectory(File file) throws IOException {
    Preconditions.checkNotNull(file);
    return file.isDirectory() ? file.mkdir() : file.createNewFile();
  }

  public static boolean createFileOrParentDirectory(File file)
    throws IOException
  {
    Preconditions.checkNotNull(file);
    return file.isDirectory() ? file.mkdirs() : file.createNewFile();
  }

  public static boolean createFileAndParentDirectory(File file)
    throws IOException
  {
    Preconditions.checkNotNull(file);
    return file.isDirectory() ? file.mkdirs()
      : createFileWithParentDirectories(file);
  }

  private static boolean createFileWithParentDirectories(File file)
    throws IOException {
    boolean success = true;
    File directory = file.getParentFile();
    if (!directory.exists()) {
      success = directory.mkdirs();
    }
    return file.createNewFile() && success;
  }

  public static void writeAndFlush(
    File file,
    String message,
    StartPoint startPoint
  ) throws FileNotFoundException
  {
    Files.existsFileThrowException(file);
    try (FileWriter fileWriter = new FileWriter(file, startPoint.asBoolean)) {
      try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
        writer.write(message);
        writer.flush();
      }
    } catch (IOException fileWritingFailure) {
      fileWritingFailure.printStackTrace();
    }
  }

  // Creates and closes PrintWriter instant so the complete files gets cleared
  @SuppressWarnings("EmptyTryBlock")
  public static void clearWriteAndFlush(
    File file,
    String message,
    StartPoint startPoint
  ) throws FileNotFoundException
  {
    Files.existsFileThrowException(file);
    try (PrintWriter writer = new PrintWriter(file)) {}
    writeAndFlush(file, message, startPoint);
  }
}