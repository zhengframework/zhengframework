package com.github.zhengframework.shiro;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class ShiroMethodInterceptor implements MethodInterceptor {

  private org.apache.shiro.aop.MethodInterceptor methodInterceptor;

  public ShiroMethodInterceptor(org.apache.shiro.aop.MethodInterceptor methodInterceptor) {
    this.methodInterceptor = methodInterceptor;
  }


  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    return methodInterceptor.invoke(new ShiroMethodInvocation(methodInvocation));
  }

  private static class ShiroMethodInvocation implements org.apache.shiro.aop.MethodInvocation {

    private final MethodInvocation methodInvocation;

    public ShiroMethodInvocation(MethodInvocation methodInvocation) {
      this.methodInvocation = methodInvocation;
    }

    @Override
    public Object proceed() throws Throwable {
      return methodInvocation.proceed();
    }

    @Override
    public Method getMethod() {
      return methodInvocation.getMethod();
    }

    @Override
    public Object[] getArguments() {
      return methodInvocation.getArguments();
    }

    @Override
    public Object getThis() {
      return methodInvocation.getThis();
    }
  }

}
