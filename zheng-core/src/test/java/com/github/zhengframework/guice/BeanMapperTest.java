// package com.github.zhengframework.guice;
//
// import static org.junit.Assert.assertArrayEquals;
// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertNull;
//
// import com.fasterxml.jackson.annotation.JsonAutoDetect;
// import com.fasterxml.jackson.databind.DeserializationFeature;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.SerializationFeature;
// import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
// import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
// import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
// import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
// import com.github.zhengframework.configuration.Configuration;
// import com.github.zhengframework.configuration.ConfigurationDefine;
// import com.github.zhengframework.configuration.MapConfiguration;
// import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
// import com.github.zhengframework.configuration.annotation.ConfigurationTypeParser;
// import com.github.zhengframework.configuration.parser.BeanMapper;
// import com.github.zhengframework.configuration.parser.Parser;
// import com.google.common.collect.Lists;
// import com.google.common.collect.Sets;
// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Map;
// import java.util.Set;
// import java.util.stream.Collectors;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.EqualsAndHashCode;
// import lombok.NoArgsConstructor;
// import lombok.ToString;
// import org.apache.commons.beanutils.BeanUtils;
// import org.apache.commons.beanutils.BeanUtilsBean;
// import org.junit.Test;
// import org.yaml.snakeyaml.Yaml;
//
// public class BeanMapperTest {
//
//  @Test
//  public void example() throws Exception {
//    HashMap<String, String> map = new HashMap<>();
//    map.put("zheng.hibernate.driverClassName", "org.h2.Driver");
//    map.put("zheng.hibernate.url", "jdbc:h2:mem:test");
//    map.put("zheng.hibernate.username", "sa");
//    map.put("zheng.hibernate.entityPackages", "com.github.zhengframework.guice");
//    map.put("zheng.hibernate.fee", "2222");
//    map.put("zheng.hibernate.money", "222.2");
//    map.put("zheng.hibernate.bool", "true");
//
//    map.put("zheng.hibernate.user", "zheng:1");
//
//    map.put("zheng.hibernate.user.name", "zheng");
//    map.put("zheng.hibernate.user.id", "1");
//
//    map.put("zheng.hibernate.userArray.1", "zheng:1");
//    map.put("zheng.hibernate.userArray.2", "zheng:2");
//    map.put("zheng.hibernate.userArray.3", "zheng:3");
//
//    map.put("zheng.hibernate.userArrayList.1.1", "zheng:1");
//    map.put("zheng.hibernate.userArrayList.1.2", "zheng:1");
//    map.put("zheng.hibernate.userArrayList.2.1", "zheng:2");
//    map.put("zheng.hibernate.userArrayList.2.2", "zheng:2");
//
//    map.put("zheng.hibernate.server.host", "192.168.1.102");
//    map.put("zheng.hibernate.server.port", "2222");
//    map.put("zheng.hibernate.server.stringSet.1", "9999");
//    map.put("zheng.hibernate.server.stringSet.2", "5555");
//    map.put("zheng.hibernate.server.stringSet.3", "4444");
//
//    map.put("zheng.hibernate.integerList.1", "1");
//    map.put("zheng.hibernate.integerList.2", "2");
//    map.put("zheng.hibernate.integerList.3", "3");
//    map.put("zheng.hibernate.integerList.4", "4");
//
//    map.put("zheng.hibernate.serverList.1.host", "192.168.1.101");
//    map.put("zheng.hibernate.serverList.1.port", "1111");
//    map.put("zheng.hibernate.serverList.2.host", "192.168.1.102");
//    map.put("zheng.hibernate.serverList.2.port", "2222");
//    map.put("zheng.hibernate.serverList.2.stringSet.1", "9999");
//    map.put("zheng.hibernate.serverList.2.stringSet.2", "5555");
//    map.put("zheng.hibernate.serverList.2.stringSet.3", "4444");
//
//    map.put("zheng.hibernate.serverSet.1.host", "192.168.1.101");
//    map.put("zheng.hibernate.serverSet.1.port", "1111");
//    map.put("zheng.hibernate.serverSet.2.host", "192.168.1.102");
//    map.put("zheng.hibernate.serverSet.2.port", "2222");
//    map.put("zheng.hibernate.serverSet.2.stringSet.1", "9999");
//    map.put("zheng.hibernate.serverSet.2.stringSet.2", "5555");
//    map.put("zheng.hibernate.serverSet.2.stringSet.3", "4444");
//
//    map.put("zheng.hibernate.stringSet.1", "1111");
//    map.put("zheng.hibernate.stringSet.2", "2111");
//    map.put("zheng.hibernate.stringSet.3", "3111");
//    map.put("zheng.hibernate.stringSet.4", "4111");
//
//    map.put("zheng.hibernate.stringServerMap.server1.host", "192.168.1.101");
//    map.put("zheng.hibernate.stringServerMap.server1.port", "1111");
//    map.put("zheng.hibernate.stringServerMap.server2.host", "192.168.1.102");
//    map.put("zheng.hibernate.stringServerMap.server2.port", "2222");
//    map.put("zheng.hibernate.stringServerMap.server2.stringSet.1", "9999");
//    map.put("zheng.hibernate.stringServerMap.server2.stringSet.2", "5555");
//    map.put("zheng.hibernate.stringServerMap.server2.stringSet.3", "4444");
//
//    map.put("zheng.hibernate.stringStringMap.hibernate.dialect",
// "org.hibernate.dialect.H2Dialect");
//    map.put("zheng.hibernate.stringStringMap.hibernate.hbm2ddl.auto", "create");
//
//    map.put("zheng.hibernate.integerServerMap.1.host", "192.168.1.101");
//    map.put("zheng.hibernate.integerServerMap.1.port", "1111");
//    map.put("zheng.hibernate.integerServerMap.2.host", "192.168.1.102");
//    map.put("zheng.hibernate.integerServerMap.2.port", "2222");
//    map.put("zheng.hibernate.integerServerMap.2.stringSet.1", "9999");
//    map.put("zheng.hibernate.integerServerMap.2.stringSet.2", "5555");
//    map.put("zheng.hibernate.integerServerMap.2.stringSet.3", "4444");
//
//    Configuration configuration = new MapConfiguration(map);
//    Configuration prefix = configuration.prefix("zheng.hibernate");
//
//    BeanMapper beanMapper = new BeanMapper();
//
//    beanMapper.addParser(Server.class, new Parser<Server>() {
//      @Override
//      public Server parse(String input) {
//        // 192.168.1.101:1111
//        String[] strings = input.split(":");
//        Server server = new Server();
//        server.setHost(strings[0]);
//        server.setPort(Integer.parseInt(strings[1]));
//        return server;
//      }
//    });
//
//    beanMapper.addBasicParser(User.class, new Parser<User>() {
//
//      @Override
//      public User parse(String input) {
//        // zheng:1
//        String[] strings = input.split(":");
//        User user = new User();
//        user.setName(strings[0]);
//        user.setId(Integer.parseInt(strings[1]));
//        return user;
//      }
//    });
//
//    HibernateConfig hibernateConfig = beanMapper.read(HibernateConfig.class, prefix);
////    HibernateConfig hibernateConfig = beanMapper.read(HibernateConfig.class, configuration);
//    System.out.println(hibernateConfig);
//
//    assertEquals("org.h2.Driver", hibernateConfig.getDriverClassName());
//    assertEquals("jdbc:h2:mem:test", hibernateConfig.getUrl());
//    assertEquals("sa", hibernateConfig.getUsername());
//    assertNull(hibernateConfig.getPassword());
//    assertEquals("com.github.zhengframework.guice", hibernateConfig.getEntityPackages());
//    assertEquals(BigDecimal.valueOf(2222), hibernateConfig.getFee());
//    assertEquals(222.2d, hibernateConfig.getMoney(), 1);
//    assertEquals(true, hibernateConfig.getBool());
//
//    Server server = new Server();
//    server.setHost("192.168.1.101");
//    server.setPort(1111);
//
//    Server server2 = new Server();
//    server2.setHost("192.168.1.102");
//    server2.setPort(2222);
//    server2.setStringSet(Sets.newHashSet("9999", "5555", "4444"));
//
//    assertEquals(server2, hibernateConfig.getServer());
//
//    assertEquals(Lists.newArrayList(1, 2, 3, 4), hibernateConfig.getIntegerList());
//
//    assertEquals(Sets.newHashSet(
//        Arrays.stream(new Integer[]{1111, 2111, 3111, 4111}).map(String::valueOf).collect(
//            Collectors.toSet())), hibernateConfig.getStringSet());
//
//    assertEquals(Arrays.asList(server, server2), hibernateConfig.getServerList());
//    assertEquals(Sets.newHashSet(server, server2), hibernateConfig.getServerSet());
//    HashMap<String, String> stringStringHashMap = new HashMap<>();
//    stringStringHashMap.put("hibernate.hbm2ddl.auto", "create");
//    stringStringHashMap.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//    assertEquals(stringStringHashMap, hibernateConfig.getStringStringMap());
//
//    Map<String, Server> stringServerMap = new HashMap<>();
//    stringServerMap.put("server2", server2);
//    stringServerMap.put("server1", server);
//    assertEquals(stringServerMap, hibernateConfig.getStringServerMap());
//    Map<Integer, Server> integerServerMap = new HashMap<>();
//    integerServerMap.put(2, server2);
//    integerServerMap.put(1, server);
//    assertEquals(integerServerMap, hibernateConfig.getIntegerServerMap());
//
//    User user = new User();
//    user.setName("zheng");
//    user.setId(1);
//    assertEquals(user, hibernateConfig.getUser());
//
//    assertArrayEquals(new User[]{new User("zheng", 1), new User("zheng", 2), new User("zheng",
// 3)},
//        hibernateConfig.getUserArray());
//
//    ObjectMapper mapper=new ObjectMapper(new YAMLFactory()
//        .configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false)
//    )
//        .enable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
//    mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
//        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
//        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
//        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
//        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
//        .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));
//
//    String string = mapper.writeValueAsString(hibernateConfig);
//    System.out.println(string);
//
//    HibernateConfig hibernateConfig1 = mapper.readValue(string, HibernateConfig.class);
//    assertEquals(hibernateConfig,hibernateConfig1);
//
//    Yaml yaml = new Yaml();
//    String dumpAsMap = yaml.dumpAsMap(hibernateConfig);
//    System.out.println(dumpAsMap);
//    HibernateConfig config = yaml.loadAs(string, HibernateConfig.class);
//    assertEquals(hibernateConfig,config);
//
//    Map load = yaml.load(string);
//    System.out.println(load.keySet());
//
//    JavaPropsMapper javaPropsMapper = new JavaPropsMapper();
//    javaPropsMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//    HibernateConfig2 hibernateConfig2 = javaPropsMapper
//        .readMapAs(prefix.asMap(), HibernateConfig2.class);
//    System.out.println(hibernateConfig2);
//  }
//
//  public static class ServerParser implements Parser<Server> {
//
//    @Override
//    public Server parse(String input) {
//      // 192.168.1.101:1111
//      String[] strings = input.split(":");
//      Server server = new Server();
//      server.setHost(strings[0]);
//      server.setPort(Integer.parseInt(strings[1]));
//      return server;
//    }
//  }
//
//  public static class UserParser implements Parser<User> {
//
//    @Override
//    public User parse(String input) {
//      // zheng:1
//      String[] strings = input.split(":");
//      User user = new User();
//      user.setName(strings[0]);
//      user.setId(Integer.parseInt(strings[1]));
//      return user;
//    }
//  }
//
//  @EqualsAndHashCode
//  @Data
//  @NoArgsConstructor
//  public static class Server {
//
//    private String host;
//    private int port;
//
//    private Set<String> stringSet = new HashSet<>();
//
//  }
//
//  @AllArgsConstructor
//  @EqualsAndHashCode
//  @Data
//  @NoArgsConstructor
//  public static class User {
//
//    private String name;
//    private int id;
//
//  }
//
//  @EqualsAndHashCode
//  @ToString
//  @Data
//  @NoArgsConstructor
//  @ConfigurationInfo(prefix = "zheng.hibernate",
//      typeParser = {
//          @ConfigurationTypeParser(type = Server.class, parser = ServerParser.class)
//      },
//      basicTypeParser = {
//          @ConfigurationTypeParser(type = {User.class}, parser = UserParser.class)
//      })
//  public static class HibernateConfig implements ConfigurationDefine {
//
//    public static final String PREFIX = "zheng.hibernate";
//    private String driverClassName;
//    private String url;
//    private String username;
//    private String password;
//    private String entityPackages;
//    private BigDecimal fee;
//    private double money;
//    private Boolean bool;
//    private User user;
//    private User[] userArray;
//    private Server server;
//
////    private List<User[]> userArrayList = new ArrayList<>();// not recommend
//
//    private List<Integer> integerList = new ArrayList<>();
//
//    private List<Server> serverList = new ArrayList<>();
//
//    private Set<String> stringSet = new HashSet<>();
//
//    private Set<Server> serverSet = new HashSet<>();
//
//    private Map<String, String> stringStringMap = new HashMap<>();
//
//    private Map<String, Server> stringServerMap = new HashMap<>();
//
//    private Map<Integer, Server> integerServerMap = new HashMap<>();
//
////    private Map<Server, Integer> serverIntegerMaps = new HashMap<>();//not support
//
////    private Map<Server, Server> serverServerMaps = new HashMap<>();// not support
//
//  }
//
//  @EqualsAndHashCode
//  @ToString
//  @Data
//  @NoArgsConstructor
//  @ConfigurationInfo(prefix = "zheng.hibernate",
//      typeParser = {
//          @ConfigurationTypeParser(type = Server.class, parser = ServerParser.class)
//      },
//      basicTypeParser = {
//          @ConfigurationTypeParser(type = {User.class}, parser = UserParser.class)
//      })
//  public static class HibernateConfig2 implements ConfigurationDefine {
//
//    public static final String PREFIX = "zheng.hibernate";
//    private String driverClassName;
//    private String url;
//    private String username;
//    private String password;
//    private String entityPackages;
//    private BigDecimal fee;
//    private double money;
//    private Boolean bool;
//    private User user;
////    private User[] userArray;
//    private Server server;
//
////    private List<User[]> userArrayList = new ArrayList<>();// not recommend
//
//    private List<Integer> integerList = new ArrayList<>();
//
//    private List<Server> serverList = new ArrayList<>();
//
//    private Set<String> stringSet = new HashSet<>();
//
//    private Set<Server> serverSet = new HashSet<>();
//
////    private Map<String, String> stringStringMap = new HashMap<>();
//
//    private Map<String, Server> stringServerMap = new HashMap<>();
//
////    private Map<Integer, Server> integerServerMap = new HashMap<>();
//
////    private Map<Server, Integer> serverIntegerMaps = new HashMap<>();//not support
//
////    private Map<Server, Server> serverServerMaps = new HashMap<>();// not support
//  }
// }
