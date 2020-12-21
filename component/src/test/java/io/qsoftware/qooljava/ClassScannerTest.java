package io.qsoftware.qooljava;

import io.github.classgraph.ClassGraph;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class ClassScannerTest {
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface TestAnnotation {}

  @TestAnnotation
  public final static class TestAnnotatedClass {}

  @Test
  public void testFindAnnotatedClasses() {
    ClassScanner scanner = ClassScanner
      .createInPackage(this.getClass().getPackageName());

    scanner.findAnnotated(TestAnnotation.class)
      .forEach(foundClass -> System.out.println(foundClass.getSimpleName()));
  }

  @Test
  public void testProjectClasses() {
    ClassScanner scanner = ClassScanner
      .createInPackage(getClass().getPackageName());

    scanner.classes()
      .forEach(foundClass -> System.out.println(foundClass.getSimpleName()));
    System.out.println("--");
    new ClassGraph()
      .whitelistPackages(getClass().getPackageName())
      .enableClassInfo()
      .enableAnnotationInfo()
      .scan()
      .getAllClasses()
      .forEach(standardClasses ->
        System.out.println(standardClasses.getSimpleName()));
  }
}
