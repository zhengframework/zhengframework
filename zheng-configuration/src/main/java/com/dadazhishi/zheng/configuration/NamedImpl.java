package com.dadazhishi.zheng.configuration;

import com.google.common.base.Preconditions;
import com.google.inject.internal.Annotations;
import com.google.inject.name.Named;
import java.io.Serializable;
import java.lang.annotation.Annotation;

@SuppressWarnings("ClassExplicitlyAnnotation")
class NamedImpl implements Named, Serializable {

  private static final long serialVersionUID = 0L;
  private final String value;

  public NamedImpl(String value) {
    this.value = Preconditions.checkNotNull(value, "name");
  }

  public String value() {
    return this.value;
  }

  public int hashCode() {
    return 127 * "value".hashCode() ^ this.value.hashCode();
  }

  public boolean equals(Object o) {
    if (!(o instanceof Named)) {
      return false;
    } else {
      Named other = (Named) o;
      return this.value.equals(other.value());
    }
  }

  public String toString() {
    return "@" + Named.class.getName() + "(value=" + Annotations.memberValueString(this.value)
        + ")";
  }

  public Class<? extends Annotation> annotationType() {
    return Named.class;
  }
}