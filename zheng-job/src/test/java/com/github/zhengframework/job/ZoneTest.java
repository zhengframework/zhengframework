package com.github.zhengframework.job;

import com.google.common.collect.Sets;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ZoneTest {

  @Test
  public void zone() {
    TimeZone timeZone = TimeZone.getDefault();
    ZoneId zoneId = ZoneOffset.systemDefault();
    log.info("{}", timeZone.getID());
    log.info("{}", zoneId);
    Set<String> availableZoneIds = ZoneOffset.getAvailableZoneIds();
    Set<String> availableIDs = Sets.newHashSet(TimeZone.getAvailableIDs());
    log.info("{}", Sets.difference(availableIDs, availableZoneIds));
  }
}
