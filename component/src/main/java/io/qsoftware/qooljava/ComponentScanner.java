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

import io.qsoftware.qooljava.collection.Lists;

import java.util.Collection;
import java.util.stream.Collectors;

public final class ComponentScanner {
  public static ComponentScanner createOfScanner(ClassScanner classScanner) {
    Preconditions.checkNotNull(classScanner);
    return new ComponentScanner(classScanner, Lists.newArrayList());
  }

  private final ClassScanner scanner;
  private Collection<Class<?>> classes;

  private ComponentScanner(
    ClassScanner scanner,
    Collection<Class<?>> classes
  ) {
    this.scanner = scanner;
    this.classes = classes;
  }

  public void loadComponents() {
    this.classes = this.scanner
      .findAnnotated(Component.class)
      .collect(Collectors.toList());
  }

  public ClassScanner classes() {
    return ClassScanner.of(this.classes);
  }
}
