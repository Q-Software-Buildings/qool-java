package io.qsoftware.qooljava;

import org.junit.jupiter.api.Test;

public final class ComponentScannerTest {
  @Component
  public static final class TestAnnotatedClass {}

  @Test
  public void findAnnotatedClass() {
   ClassScanner classScanner = ClassScanner
     .createInPackage(getClass().getPackageName());
   ComponentScanner scanner = ComponentScanner.createOfScanner(classScanner);
   scanner.loadComponents();

   scanner.classes().classes()
     .forEach(foundClass -> System.out.println(foundClass.getSimpleName()));
  }
}
