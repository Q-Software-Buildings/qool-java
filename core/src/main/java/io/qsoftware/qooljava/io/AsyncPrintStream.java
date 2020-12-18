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

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class AsyncPrintStream extends PrintStream {
  public static AsyncPrintStream createByOutputStream(
    OutputStream outputStream
  ) {
    Preconditions.checkNotNull(outputStream);
    BlockingQueue<Runnable> executionQueue = new LinkedBlockingQueue<>();
    return new AsyncPrintStream(executionQueue, outputStream,
      PrintThread.createOfQueue(executionQueue));
  }

  private final BlockingQueue<Runnable> executionQueue;
  private final PrintThread printThread;

  private static final boolean AUTO_FLUSH = true;
  private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  private AsyncPrintStream(
    BlockingQueue<Runnable> executionQueue,
    OutputStream outputStream,
    PrintThread printThread
  ) {
    super(outputStream, AUTO_FLUSH, DEFAULT_CHARSET);
    this.executionQueue = executionQueue;
    this.printThread = printThread;
  }

  @Override
  public void print(int integer) {
    insertQueue(() -> super.print(integer));
  }

  @Override
  public void print(char charSequence) {
    insertQueue(() -> super.println(charSequence));
  }

  @Override
  public void print(long longNumber) {
    insertQueue(() -> super.print(longNumber));
  }

  @Override
  public void print(float floatNumber) {
    insertQueue(() -> super.print(floatNumber));
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public void print(char[] charArray) {
    insertQueue(() -> super.print(charArray));
  }

  @Override
  public void print(double doubleNumber) {
    insertQueue(() -> super.print(doubleNumber));
  }

  @Override
  public void print(String string) {
    insertQueue(() -> super.print(string));
  }

  @Override
  public void print(boolean booleanState) {
    insertQueue(() -> super.print(booleanState));
  }

  @Override
  public void print(Object object) {
    insertQueue(() -> super.print(object));
  }

  @Override
  public void println(int integer) {
    insertQueue(() -> super.println(integer));
  }

  @Override
  public void println(char charSequence) {
    insertQueue(() -> super.println(charSequence));
  }

  @Override
  public void println(long longNumber) {
    insertQueue(() -> super.println(longNumber));
  }

  @Override
  public void println(float floatNumber) {
    insertQueue(() -> super.println(floatNumber));
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public void println(char[] charArray) {
    insertQueue(() -> super.println(charArray));
  }

  @Override
  public void println(double doubleNumber) {
    insertQueue(() -> super.println(doubleNumber));
  }

  @Override
  public void println(Object object) {
    insertQueue(() -> super.println(object));
  }

  @Override
  public void println(String string) {
    insertQueue(() -> super.println(string));
  }

  @Override
  public void println(boolean booleanState) {
    insertQueue(() -> super.println(booleanState));
  }

  private void insertQueue(Runnable runnable) {
    if (Thread.currentThread() != printThread) {
      executionQueue.offer(runnable);
    } else {
      runnable.run();
    }
  }

  private static final class PrintThread extends Thread {
    public static PrintThread createOfQueue(
      BlockingQueue<Runnable> executionQueue
    ) {
      Preconditions.checkNotNull(executionQueue);
      PrintThread printThread = new PrintThread(executionQueue);
      printThread.setupThreadSettings();
      return printThread;
    }

    private void setupThreadSettings() {
      setPriority(MIN_PRIORITY);
      setDaemon(true);
      start();
    }

    private final BlockingQueue<Runnable> executionQueue;

    private PrintThread(BlockingQueue<Runnable> executionQueue) {
      this.executionQueue = executionQueue;
    }

    @Override
    public void run() {
      while (!isInterrupted()) {
        try {
          executionQueue.take().run();
        } catch (InterruptedException takingFailure) {
          takingFailure.printStackTrace();
        }
      }
    }
  }
}