```java
Configuration config=...;

ZhengApplication app=ZhengApplication.create(config,new Module1(),new Module2());
app.start();
app.stop();
```