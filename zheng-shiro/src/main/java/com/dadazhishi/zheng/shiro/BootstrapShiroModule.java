package com.github.zhengframework.shiro;

import com.google.inject.Provides;
import com.google.inject.name.Names;
import javax.servlet.ServletContext;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.config.Ini;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.guice.aop.ShiroAopModule;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.realm.text.IniRealm;

/**
 * https://shiro.apache.org/guice.html
 */
public class BootstrapShiroModule extends ShiroWebModule {

  public BootstrapShiroModule(ServletContext servletContext) {
    super(servletContext);
  }

  @Override
  protected void configureShiroWeb() {
    install(new ShiroAopModule());
    try {
      bindRealm().toConstructor(IniRealm.class.getConstructor(Ini.class));
    } catch (NoSuchMethodException e) {
      addError(e);
    }

    bind(CredentialsMatcher.class).to(HashedCredentialsMatcher.class);
    bind(HashedCredentialsMatcher.class);
    bindConstant().annotatedWith(Names.named("shiro.hashAlgorithmName")).to(Md5Hash.ALGORITHM_NAME);

    addFilterChain("/public/**", ANON);
    addFilterChain("/stuff/allowed/**", AUTHC_BASIC, config(PERMS, "yes"));
    addFilterChain("/stuff/forbidden/**", AUTHC_BASIC, config(PERMS, "no"));
    addFilterChain("/**", AUTHC_BASIC);


  }

  @Provides
  Ini loadShiroIni() {
    return Ini.fromResourcePath("classpath:shiro.ini");
  }
}
