package edu.cs4224;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Utils {

  public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  public static Date parseDateFromString(String str) {
    try {
      if (str.equals("null")) {
        return null;
      }
      return formatter.parse(str);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static class Triple<T, U, V> {
    public T _0;
    public U _1;
    public V _2;

    public Triple(T t, U u, V v) {
      _0 = t;
      _1 = u;
      _2 = v;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
      return Objects.equals(_0, triple._0) &&
          Objects.equals(_1, triple._1) &&
          Objects.equals(_2, triple._2);
    }

    @Override
    public int hashCode() {
      return Objects.hash(_0, _1, _2);
    }
  }

}
