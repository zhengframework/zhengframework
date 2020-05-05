package com.github.zhengframework.shiro.jaxrs;

/*-
 * #%L
 * zheng-shiro-jaxrs
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

@Slf4j
@Path(TestResource.PATH)
public class TestResource {

  public static final String PATH = "test";

  @Path("login")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @POST
  public String login(
      @FormParam("username") String username
      , @FormParam("password") String password
      , @Context Response response) {
    log.info("username={}", username);
    log.info("password={}", password);

    UsernamePasswordToken token = new UsernamePasswordToken(username, password);
    token.setRememberMe(true);
    Subject currentUser = SecurityUtils.getSubject();
    Session session = currentUser.getSession();
    session.setAttribute("someKey", "aValue");
    String value = (String) session.getAttribute("someKey");
    if (value.equals("aValue")) {
      log.info("Retrieved the correct value! [" + value + "]");
    }

    if (!currentUser.isAuthenticated()) {
      try {
        currentUser.login(token);
      } catch (UnknownAccountException uae) {
        log.info("There is no user with username of " + token.getPrincipal());
      } catch (IncorrectCredentialsException ice) {
        log.info("Password for account " + token.getPrincipal() + " was incorrect!");
      } catch (LockedAccountException lae) {
        log.info("The account for username " + token.getPrincipal() + " is locked.  " +
            "Please contact your administrator to unlock it.");
      }
      // ... catch more exceptions here (maybe custom ones specific to your application?
      catch (AuthenticationException ae) {
        //unexpected condition?  error?
      }
    }

    //say who they are:
    //print their identifying principal (in this case, a username):
    log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
    //test a role:
    if (currentUser.hasRole("schwartz")) {
      log.info("May the Schwartz be with you!");
    } else {
      log.info("Hello, mere mortal.");
    }

    //test a typed permission (not instance-level)
    if (currentUser.isPermitted("lightsaber:wield")) {
      log.info("You may use a lightsaber ring.  Use it wisely.");
    } else {
      log.info("Sorry, lightsaber rings are for schwartz masters only.");
    }

    //a (very powerful) Instance Level permission:
    if (currentUser.isPermitted("winnebago:drive:eagle5")) {
      log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
          "Here are the keys - have fun!");
    } else {
      log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
    }
    log.info("currentUser=" + currentUser.getPrincipal());
    return "currentUser=" + currentUser.getPrincipal() + "\n";
  }

  @GET
  @Path("requiresRoles")
  @RequiresRoles("goodguy")
  public String RequiresRoles() {
    return "OK";
  }

}
