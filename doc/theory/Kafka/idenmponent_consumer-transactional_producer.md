# Idempotent consumer
In Apache Kafka, an idempotent consumer is a type of consumer that can process messages from a Kafka topic without changing the end state if the same message is processed multiple times. This is useful for ensuring that messages are processed exactly once, even if the consumer fails or is restarted.

Here is an example of using idempotent consumers with Spring Kafka:

```java
@Bean
public ConsumerFactory<String, String> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(
        consumerConfigs(),
        new StringDeserializer(),
        new StringDeserializer()
    );
}

@Bean
public Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>();
    // set properties for enabling idempotent consumers
    props.put(ConsumerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
    // set other consumer configuration properties as needed
    return props;
}

@Bean
public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    // set other factory configuration properties as needed
    return factory;
}

// use the KafkaListenerContainerFactory in your Kafka listener
@KafkaListener(topics = "my-topic")
public void processMessage(String message) {
    // process message
}
```

In this example, the consumerConfigs() method sets the enable.idempotence property to true to enable idempotent consumers. The kafkaListenerContainerFactory() method



# Transactional producer

In Apache Kafka, a transactional producer is a producer that is able to guarantee that messages are published to the Kafka cluster in a way that is atomic, consistent, isolated, and durable (ACID). This means that all messages are either published successfully or not published at all, and that the state of the producer is always consistent with the state of the Kafka cluster.

One way to create a transactional producer using Spring Kafka is to use the KafkaTransactionManager and the @Transactional annotation. Here is an example:

```java
@Bean
public KafkaTransactionManager<String, String> transactionManager(
        ProducerFactory<String, String> producerFactory) {
    return new KafkaTransactionManager<>(producerFactory);
}

@Transactional
public void sendTransactionalMessage(String message) {
    kafkaTemplate.send("my-topic", message);
}
```

In this example, the KafkaTransactionManager is used to manage the transactions for the producer, and the sendTransactionalMessage method is annotated with @Transactional to indicate that it should participate in a transaction. This means that when the sendTransactionalMessage method is called, the message will be published to the Kafka cluster in a transactional way, ensuring that it is ACID-compliant.
