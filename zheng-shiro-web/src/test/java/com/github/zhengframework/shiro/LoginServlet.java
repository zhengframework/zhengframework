package com.github.zhengframework.shiro;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

@Slf4j
public class LoginServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    System.out.println("getContextPath=" + req.getContextPath());
    System.out.println("getRequestURI=" + req.getRequestURI());
    resp.getWriter().print("Hello, World");
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    String username = req.getParameter("username");
    String password = req.getParameter("password");
    String rememberMe = req.getParameter("rememberMe");
    System.out.println("username=" + username);
    System.out.println("password=" + password);
    System.out.println("rememberMe=" + rememberMe);
    log.info("username={}", username);
    log.info("password={}", password);
    log.info("rememberMe={}", rememberMe);
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

    resp.getWriter().println("currentUser=" + currentUser.getPrincipal());
    //all done - log out!
    currentUser.logout();

  }
}
