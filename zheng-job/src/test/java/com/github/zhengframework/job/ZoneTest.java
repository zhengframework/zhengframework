package com.github.zhengframework.job;

import com.google.common.collect.Sets;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.TimeZone;
import org.junit.Test;

public class ZoneTest {

  @Test
  public void zone() {
    TimeZone timeZone = TimeZone.getDefault();
    ZoneId zoneId = ZoneOffset.systemDefault();
    System.out.println(timeZone.getID());
    System.out.println(zoneId);
    Set<String> availableZoneIds = ZoneOffset.getAvailableZoneIds();
    Set<String> availableIDs = Sets.newHashSet(TimeZone.getAvailableIDs());
    System.out.println(Sets.difference(availableIDs, availableZoneIds));
  }
}
