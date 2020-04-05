package com.dadazhishi.zheng.swagger;


import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationAware;
import com.dadazhishi.zheng.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.servlet.ServletModule;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import javax.inject.Singleton;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = false, of = {})
public class SwaggerModule extends ServletModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  protected void configureServlets() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    bind(OpenApiResource.class);
    bind(AcceptHeaderOpenApiResource.class);

    SwaggerConfig swaggerConfig = ConfigurationBeanMapper
        .resolve(configuration, SwaggerConfig.PREFIX, SwaggerConfig.class);
    bind(SwaggerConfig.class).toInstance(swaggerConfig);
    bind(SwaggerUIServlet.class).in(Singleton.class);

    serve(swaggerConfig.getBasePath() + "/*").with(SwaggerUIServlet.class);
  }


  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
