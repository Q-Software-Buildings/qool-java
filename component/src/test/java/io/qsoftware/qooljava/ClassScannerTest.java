package io.qsoftware.qooljava;

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
}
