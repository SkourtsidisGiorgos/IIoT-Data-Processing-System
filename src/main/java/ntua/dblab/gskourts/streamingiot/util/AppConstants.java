package ntua.dblab.gskourts.streamingiot.util;

import java.time.format.DateTimeFormatter;

public class AppConstants {
      public static final DateTimeFormatter timeDateZoneFormatter = DateTimeFormatter
                  .ofPattern("yyyyMMdd HH:mm:ss.SSS Z");

      public static final String TEMPERATURE_DEVICE_PREFIX = "temperature-";
      public static final String PRESSURE_DEVICE_PREFIX = "pressure-";
        public static final String POWER_DEVICE_PREFIX = "power-";
      public static final String KAFKA_BROKERS = "localhost:9092";
      public static final Integer MESSAGE_COUNT = 1000;
      public static final String CLIENT_ID = "client1";

      public static final String TOPIC_TEMPERATURE_INPUT = "streaming.input.temperatureMeasurements"; // Celsius
      public static final String TOPIC_PRESSURE_INPUT = "streaming.input.pressureMeasurements"; // PSI
      public static final String TOPIC_POWER_INPUT = "streaming.input.powerMeasurements"; // Watts

      public static final String TOPIC_AGGREGATED_TEMPERATURE_OUTPUT = "streaming.output.temperatureMeasurements"; // Celsius
      public static final String TOPIC_AGGREGATED_PRESSURE_OUTPUT = "streaming.output.pressureMeasurements"; // PSI
      public static final String TOPIC_AGGREGATED_POWER_OUTPUT = "streaming.output.powerMeasurements"; // Watts
      public static final String TEMPERATURE_AGGREGATE_STORE = "temperatureAggregateStore";
      public static final String CONSUMER_TEMPERATURE_AGGREGATOR = "temperature_aggregator";
      public static final String GROUP_ID_CONFIG = "consumerGroup1";
      public static final Integer MAX_NO_MESSAGE_FOUND_COUNT = 100;
      public static final String OFFSET_RESET_LATEST = "latest";
      public static final String OFFSET_RESET_EARLIER = "earliest";
      public static final Integer MAX_POLL_RECORDS = 1;
      public static final Integer SUCCESS_INTEGER = 0;
      public static final Integer FAILURE_INTEGER = -1;
      public static final String SUCCESS_STRING = "success";
      public static final String FAILURE_STRING = "failure";
      public static final String KAFKA_SERVER_URL = "localhost";
      public static final Integer KAFKA_SERVER_PORT = 9092;
      public static final Integer KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;
      public static final Integer CONNECTION_TIMEOUT = 100000;
      public static final String CLIENT_ID_STRING = "SimpleConsumerDemoClient";
      public static final String UNKNOWN = "UNKNOWN";
      public static final String DATETIME_FORMAT = "yyyyMMdd.HHmmss";

      private AppConstants() {
      }
}
