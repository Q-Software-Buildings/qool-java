package io.qsoftware.qooljava;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.Test;

public final class ClassGraphTest {
  @Test
  public void testResolvingAllClasses() {
    try (ScanResult result = new ClassGraph()
      .enableClassInfo()
      .scan()) {
      result.getAllClasses()
        .forEach(classResult -> System.out.println(classResult.getSimpleName()));
    }
  }

  @Test
  public void testResolvingClassesInPackageWithoutSubPackage() {
    try (ScanResult result = new ClassGraph()
      .enableClassInfo()
      .whitelistPackages("io.qsoftware.qooljava.logging")
      .scan()) {
      result
        .getAllClasses()
        .filter(info -> info.getPackageName()
          .equals("io.qsoftware.qooljava.logging"))
        .forEach(classResult -> System.out.println(classResult.getSimpleName()));
    }
  }

  @Test
  public void testAnnotationFinding() {
    String testAnnotationName
      = ClassScannerTest.TestAnnotation.class.getName();
    try (ScanResult result = new ClassGraph()
      .enableAllInfo()
      .scan()) {
      result.getClassesWithAnnotation(testAnnotationName)
        .forEach(foundClasses -> System.out.println(foundClasses.getSimpleName()));
    }
  }
}
