package com.dadazhishi.zheng.kafka;

import net.mguenther.kafka.junit.EmbeddedKafkaCluster;
import net.mguenther.kafka.junit.EmbeddedKafkaClusterConfig;
import net.mguenther.kafka.junit.ObserveKeyValues;
import net.mguenther.kafka.junit.SendValues;
import org.junit.Rule;
import org.junit.Test;

public class KafkaModuleTest {

  @Rule
  public EmbeddedKafkaCluster cluster = EmbeddedKafkaCluster.provisionWith(
      EmbeddedKafkaClusterConfig.useDefaults());

  @Test
  public void shouldWaitForRecordsToBePublished() throws Exception {
    cluster.send(SendValues.to("test-topic", "a", "b", "c", "d").useDefaults());
    cluster.observe(ObserveKeyValues.on("test-topic", 4).useDefaults());
  }
}