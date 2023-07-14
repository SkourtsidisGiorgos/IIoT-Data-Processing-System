# Differences between KeyValueStore and Kafka Topic 

In Apache Kafka, a topic is a logical separation of data within a Kafka cluster, while a KeyValueStore is a data structure used within a Kafka stream processing application to store and retrieve data in a key-value format. There are several key differences between these two concepts:

A Kafka topic is a logical container for messages, whereas a KeyValueStore is a physical data structure used to store and retrieve data within a Kafka stream processing application.
A Kafka topic can have multiple partitions, each of which can contain multiple messages, whereas a KeyValueStore is a single, logical store of data.
A Kafka topic is used to stream data in real-time, while a KeyValueStore is used to store and retrieve data within a Kafka stream processing application.
A Kafka topic can be thought of as a queue or a log of messages, while a KeyValueStore is more like a database table or a map data structure.
In summary, while a Kafka topic and a KeyValueStore both deal with data in a key-value format, they serve very different purposes within the context of Apache Kafka.


# KeyValueStore In Kafka Streams

In Apache Kafka, a KeyValueStore is a data structure used within a Kafka stream processing application to store and retrieve data in a key-value format. A Kafka topic is a logical container for messages, and each topic can have multiple partitions, each of which can contain multiple messages.

In Kafka Streams, a KeyValueStore is associated with a specific instance of a Kafka stream processing application, rather than a specific partition of a Kafka topic. This means that all the data that is processed by a Kafka Streams application will be stored in a single KeyValueStore, regardless of which Kafka topics and partitions the data came from.

However, it is important to note that Kafka Streams applications can be horizontally scaled by running multiple instances of the same application. In this case, each instance of the application will have its own KeyValueStore, and data will be distributed across these stores based on the specific configuration of the application.

# A Kafka Streams application has exactly once KeyValueStore
In Apache Kafka, a KeyValueStore is a data structure used within a Kafka stream processing application to store and retrieve data in a key-value format. In Kafka Streams, a KeyValueStore is associated with a specific instance of a Kafka stream processing application.

A Kafka Streams application can have exactly one KeyValueStore per instance of the application. However, it is possible to horizontally scale a Kafka Streams application by running multiple instances of the same application. In this case, each instance of the application will have its own KeyValueStore, and data will be distributed across these stores based on the specific configuration of the application.

It is also worth noting that a Kafka Streams application can use multiple KeyValueStores by creating multiple instances of the KeyValueStore and using them within the application. However, each instance of a KeyValueStore will only be accessible within the specific instance of the Kafka Streams application in which it was created.


# Not all messages of a Kafka Streams application are stored in a KeyValueStore
It is important to note that not all messages that are processed by a Kafka Streams application will necessarily be stored in the associated KeyValueStore. This is because a KeyValueStore is typically used to store the results of transformations and aggregations that are performed on the data within the Kafka Streams application, rather than the raw input data.

Additionally, a KeyValueStore in Kafka Streams is not designed to store failed messages or late events. Kafka Streams applications can be configured to handle failed messages and late events in a variety of ways, but these messages are typically not stored in the KeyValueStore. Instead, they may be logged, sent to a dead letter queue, or simply dropped, depending on the specific configuration of the application.
