package com.github.zhengframework.validator.aop;

import com.google.inject.matcher.AbstractMatcher;
import java.lang.reflect.Method;

public class DeclaredMethodMatcher extends AbstractMatcher<Method> {

    @Override
    public boolean matches(final Method method) {
        return !method.isSynthetic() || !method.isBridge();
    }
}
