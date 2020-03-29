package com.dadazhishi.zheng.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
public class ServiceRegistryModuleTests {

  protected Injector injector;
  protected ServiceManager mgr;
  protected Service testService1;
  protected Service testService2;
  @Mock
  protected Service.Listener mockListener;
  @Mock
  protected ServiceManager.Listener mockMgrListener;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    testService1 = new NoOpService();
    testService2 = new NoOpService();

    injector = Guice.createInjector(new ServicesModule());

    injector.getInstance(ServiceRegistry.class).add(testService1);
    injector.getInstance(ServiceRegistry.class).add(testService2);
    injector.getInstance(ServiceRegistry.class).add(mockListener);
    injector.getInstance(ServiceRegistry.class).add(mockMgrListener);

    mgr = injector.getInstance(ServiceManager.class);
  }

  @After
  public void teardown() {
    mgr.stopAsync().awaitStopped();
  }

  @Test
  public void serviceMgrAndServicesRunning() {
    mgr.startAsync().awaitHealthy();
    assertThat(mgr.isHealthy(), equalTo(true));
    assertThat(testService1.state(), equalTo(Service.State.RUNNING));
    assertThat(testService2.state(), equalTo(Service.State.RUNNING));
  }

  @Test
  public void serviceManagerListenerWasCalledOnce() {
    mgr.startAsync().awaitHealthy();
    verify(mockMgrListener, times(1)).healthy();
  }

  @Test
  public void serviceListenerCalledForEachService() {
    mgr.startAsync().awaitHealthy();
    verify(mockListener, times(2)).starting();
  }

  public static class NoOpService extends AbstractIdleService {

    @Override
    protected void startUp() throws Exception {
    }

    @Override
    protected void shutDown() throws Exception {
    }
  }


}
