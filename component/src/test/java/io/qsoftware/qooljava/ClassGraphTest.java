package io.qsoftware.qooljava;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.Test;

import java.util.Collections;

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

  @Test
  public void testWhitelistedClasses() {
    String className = Component.class.getName();

    new ClassGraph().enableClassInfo()
      .whitelistClasses(className)
      .scan()
      .getAllClasses()
      .forEach(foundClass -> System.out.println(foundClass.getSimpleName()));
  }

  @Test
  public void testWhitelistedClassesOfArray() {
    var classes = Collections
      .singleton(ComponentScannerTest.TestAnnotatedClass.class);
    new ClassGraph()
      .enableClassInfo()
      .enableAnnotationInfo()
      .whitelistClasses(classes.stream()
        .map(Class::getName)
        .toArray(String[]::new))
      .scan()
      .getAllClasses()
      .forEach(foundClass -> System.out.println(foundClass.getSimpleName()));
  }
}
