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

package io.qsoftware.qooljava.map;

import io.qsoftware.qooljava.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public final class SearchHashMap<K, V> extends HashMap<K, V> {
  public SearchHashMap(Map<? extends K, ? extends V> map) {
    super(map);
  }

  public SearchHashMap() {}

  public Optional<V> findByKey(K key) {
    Preconditions.checkNotNull(key);
    return Optional.ofNullable(get(key));
  }

  public Optional<K> findByValue(V value) {
    Preconditions.checkNotNull(value);
    for (K key : keySet()) {
      if (get(key) == value) {
        return Optional.of(key);
      }
    }
    return Optional.empty();
  }

  public Optional<V> findByConditions(Predicate<V> predicate) {
    for (V value : values()) {
      if (predicate.test(value)) {
        return Optional.of(value);
      }
    }
    return Optional.empty();
  }
}
