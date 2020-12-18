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
  private final ClassInfoList classList;

  private ClassScanner(ClassInfoList classList) {
    this.classList = classList;
  }

  public Stream<Class<?>> findNonAnnotated(Annotation annotation) {
    return classList
      .filter(classInfo -> !isAnnotated(classInfo, annotation))
      .loadClasses().stream();
  }

  public Stream<Class<?>> findAnnotated(Annotation annotation) {
    return classList
      .filter(type -> isAnnotated(type, annotation))
      .loadClasses().stream();
  }

  private boolean isAnnotated(ClassInfo classInfo, Annotation annotation) {
    var annotationName = annotation.getClass().getName();
    return classInfo.hasAnnotation(annotationName);
  }

  public Stream<Class<?>> findNonAnnotated(
    Class<? extends Annotation> annotationType
  ) {
    return classList
      .filter(type -> !isAnnotated(type, annotationType))
      .loadClasses().stream();
  }

  public Stream<Class<?>> findAnnotated(
    Class<? extends Annotation> annotationType
  ) {
    return classList
      .filter(type -> isAnnotated(type, annotationType))
      .loadClasses().stream();
  }

  private boolean isAnnotated(
    ClassInfo classInfo,
    Class<? extends Annotation> annotationType
  ) {
    var annotationName = annotationType.getName();
    return classInfo.hasAnnotation(annotationName);
  }

  public <E> Stream<Class<E>> findSubTypes(Class<E> superType) {
    return classList
      .filter(classInfo -> isSubType(classInfo, superType))
      .filter(this::isNoInterface)
      .stream()
      .map(this::unsafeTypeCast);
  }

  private boolean isSubType(ClassInfo classInfo, Class<?> target) {
    return target.isAssignableFrom(classInfo.loadClass());
  }

  private boolean isNoInterface(ClassInfo classInfo) {
    return !classInfo.isInterface();
  }

  @SuppressWarnings("unchecked")
  private <E> Class<E> unsafeTypeCast(ClassInfo classInfo) {
    return (Class<E>) classInfo.loadClass();
  }

  public Stream<Class<?>> classes() {
    return classList.loadClasses().stream();
  }

  private static final ClassGraph DEFAULT_CLASS_GRAPH = new ClassGraph()
    .enableClassInfo()
    .enableAnnotationInfo()
    .enableExternalClasses();

  public static ClassScanner create() {
    return new ClassScanner(DEFAULT_CLASS_GRAPH.scan().getAllClasses());
  }

  public static ClassScanner createInPackage(String packageName) {
    return new ClassScanner(DEFAULT_CLASS_GRAPH
      .whitelistPackages(packageName)
      .scan()
      .getAllClasses()
      .filter(classInfo -> classInfo.getPackageName().equals(packageName)));
  }

  public static ClassScanner createInPackageRecursive(String packageName) {
    return new ClassScanner(DEFAULT_CLASS_GRAPH
    .whitelistPackages(packageName)
    .scan()
    .getAllClasses());
  }

  public static ClassScanner of(Collection<Class<?>> classes) {
    Preconditions.checkNotNull(classes);
    return new ClassScanner(DEFAULT_CLASS_GRAPH
      .whitelistClasses(classes.stream()
        .map(Class::getName).toArray(String[]::new))
      .scan()
      .getAllClasses());
  }
}