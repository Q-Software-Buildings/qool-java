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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.stream.Collectors;

public final class InjectorFactory {
  public static InjectorFactory create(
    ComponentScanner scanner,
    Collection<Module> manuelModules
  ) {
    Preconditions.checkNotNull(scanner);
    Preconditions.checkNotNull(manuelModules);
    return new InjectorFactory(manuelModules, scanner);
  }

  private final Collection<Module> manuelModules;
  private final ComponentScanner componentScanner;

  private InjectorFactory(
    Collection<Module> manuelModules,
    ComponentScanner scanner) {
    this.manuelModules = manuelModules;
    this.componentScanner = scanner;
  }

  private Collection<Module> findModules() {
    System.out.println("Loaded the following guice modules: ");
    return this.componentScanner.classes()
      .findSubTypes(Module.class)
      .peek(module -> System.out.printf("  - %s\n", module.getSimpleName()))
      .map(this::instantiateModule)
      .collect(Collectors.toList());
  }

  @Nullable
  private Module instantiateModule(Class<? extends Module> modules) {
    try {
      return modules.getDeclaredConstructor().newInstance();
    } catch (
      InstantiationException
        | IllegalAccessException
        | NoSuchMethodException
        | InvocationTargetException instantiateFailure
    ) {
      System.err.println("Could not instantiate injector module");
      System.err.println("The application will still load");
      System.err.println(instantiateFailure.getMessage());
      return null;
    }
  }

  public Injector createInjector() {
    Collection<Module> modules = this.findModules();
    modules.addAll(this.manuelModules);
    return Guice.createInjector(modules);
  }
}
