code base on [guice-persist-hibernate](https://github.com/jcampos8782/guice-persist-hibernate)


```java

    public void configure() {
        install(new HibernatePersistModule());
        
        bind(HibernateEntityClassProvider.class)
            .toInstance(new PackageScanEntityClassProvider("package1","package2"));

        bind(HibernateEntityClassProvider.class).toInstance(new HibernateEntityClassProvider() {
            @Override
            public List<Class<?>> get() {
                return Arrays.asList(Account.class);
            }
        });

        bind(HibernatePropertyProvider.class).toInstance(new HibernatePropertyProvider() {
            @Override
            public Map<String, String> get() {
                final Map<String, String> properties = new HashMap<>();
                properties.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
                properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
                properties.put("hibernate.connection.url", "jdbc:mysql://127.0.0.1/p_user?useSSL=false");
                properties.put("hibernate.connection.username", "root");
                properties.put("hibernate.connection.password", "password");
                return properties;
            }
        });
    }
```
