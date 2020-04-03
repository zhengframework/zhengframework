package com.dadazhishi.zheng.swagger;


import com.google.inject.servlet.ServletModule;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = false, of = {})
public class SwaggerModule extends ServletModule {

  @Override
  protected void configureServlets() {
//		Multibinder.newSetBinder(binder(), ServletContextListener.class).addBinding().to(SwaggerServletContextListener.class);
    bind(OpenApiResource.class);
    bind(AcceptHeaderOpenApiResource.class);
    filter("/*").through(ApiOriginFilter.class);
  }

}
