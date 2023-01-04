# Use Connect API to write to database

The Apache Kafka Connect API is a framework for building connectors that move data in and out of Apache Kafka. It provides a simple way to move large amounts of data between Apache Kafka and other systems.

To use the Connect API to write data to a database, you will need to create a connector that reads data from one or more Kafka topics, transforms the data as needed, and then writes the data to the database.

Here is a high-level overview of the process:

First, you will need to set up and configure a Kafka Connect cluster. This will be a separate cluster from your Kafka broker cluster, and it will be used to run the connectors that you create.

Next, you will need to create a connector configuration that specifies the details of the connector, such as which Kafka topics it should read from, how to transform the data, and where to write the data.

You can then use the Kafka Connect REST API to submit the connector configuration to the Kafka Connect cluster. The connector will then be started and will begin reading data from Kafka, transforming it as specified, and writing it to the database.

As the connector is running, it will continually read data from Kafka, transform it, and write it to the database. If you need to make any changes to the connector configuration, you can use the Kafka Connect REST API to update the configuration and the connector will pick up the changes and apply them.


# Why not write to DB from a stream

https://stackoverflow.com/questions/46524930/how-to-process-a-kafka-kstream-and-write-to-database-directly-instead-of-sending