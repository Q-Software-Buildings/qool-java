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

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.stream.Stream;

public final class ClassScanner {
  private final ClassInfoList result;

  private ClassScanner(ClassInfoList result) {
    this.result = result;
  }

  public Stream<Class<?>> findNonAnnotated(Annotation annotation) {
    return result
      .filter(classInfo -> !classInfo
        .hasAnnotation(annotation.getClass().getName()))
      .loadClasses().stream();
  }

  public Stream<Class<?>> findAnnotated(Annotation annotation) {
    return result
      .filter(classInfo -> classInfo
        .hasAnnotation(annotation.getClass().getName()))
      .loadClasses().stream();
  }

  public Stream<Class<?>> findAnnotated(
    Class<? extends Annotation> annotationType
  ) {
    return result
      .filter(classInfo -> classInfo.hasAnnotation(annotationType.getName()))
      .loadClasses().stream();
  }

  public Stream<Class<?>> findNonAnnotated(
    Class<? extends Annotation> annotationType
  ) {
    return result
      .filter(classInfo -> !classInfo.hasAnnotation(annotationType.getName()))
      .loadClasses().stream();
  }

  public <E> Stream<Class<E>> findSubTypes(Class<E> superType) {
    return result
      .filter(classInfo -> classInfo.extendsSuperclass(superType.getName()))
      .stream()
      .map(this::unsafeTypeCast);
  }

  @SuppressWarnings("unchecked")
  private <E> Class<E> unsafeTypeCast(ClassInfo classInfo) {
    return (Class<E>) classInfo.loadClass();
  }

  public Stream<Class<?>> classes() {
    return result.loadClasses().stream();
  }

  public static ClassScanner create() {
    return new ClassScanner(new ClassGraph()
      .enableClassInfo()
      .enableAnnotationInfo()
      .scan()
      .getAllClasses());
  }

  public static ClassScanner createInPackage(String packageName) {
    return new ClassScanner(new ClassGraph()
      .enableClassInfo()
      .enableAnnotationInfo()
      .whitelistPackagesNonRecursive(packageName)
      .scan()
      .getAllClasses());
  }

  public static ClassScanner createInPackageRecursive(String packageName) {
    return new ClassScanner(new ClassGraph()
      .enableClassInfo()
      .enableAnnotationInfo()
      .whitelistPackages(packageName)
      .scan()
      .getAllClasses());
  }

  public static ClassScanner of(Collection<Class<?>> classes) {
    Preconditions.checkNotNull(classes);
    return new ClassScanner(new ClassGraph()
      .enableClassInfo()
      .enableAnnotationInfo()
      .whitelistClasses(classes.stream()
        .map(Class::getName)
        .toArray(String[]::new))
      .scan()
      .getAllClasses());
  }
}