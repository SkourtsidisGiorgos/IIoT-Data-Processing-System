# In Kafka Streams, how to store latest values of aggregation in Redis?

To store the latest values of an aggregation in Redis with Kafka Streams, you can use the RedisStore class to write the results of your aggregation to a Redis key-value store. Here is an example of how to do this:

Add the Redis dependency to your project. For example, if you're using Maven, you can add the following dependency to your pom.xml file:

```xml
<dependency>
  <groupId>org.springframework.data</groupId>
  <artifactId>spring-data-redis</artifactId>
  <version>2.3.0.RELEASE</version>
</dependency>
```

Configure a RedisStore object with the settings for your Redis instance. You can do this using the RedisStoreBuilder class, which allows you to specify the host, port, and other connection settings for your Redis instance.
```java
RedisStoreBuilder redisStoreBuilder = new RedisStoreBuilder(streamsBuilder, applicationId, inputTopic);
redisStoreBuilder
  .withHost("localhost")
  .withPort(6379);
```

Use the aggregate method on your KStream to perform the aggregation, and specify the RedisStore as the output store for the aggregation results. This will automatically write the latest values of the aggregation to your Redis instance.

```java
KTable<String, Long> aggregatedTable = inputStream
  .groupByKey()
  .aggregate(
    () -> 0L, // initial value
    (aggKey, newValue, aggValue) -> aggValue + newValue, // add new values
    Materialized.as(redisStoreBuilder) // specify RedisStore for aggregation results
      .withKeySerde(...)
      .to(...)
  );
```
Start your Kafka Streams application and write some data to the input topic. The aggregation results will be automatically written to your Redis instance.


