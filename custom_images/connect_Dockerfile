FROM confluentinc/cp-kafka-connect:7.3.0

ENV CONNECT_PLUGIN_PATH: "/usr/share/java,/usr/share/confluent-hub-components"

RUN confluent-hub install --no-prompt confluentinc/kafka-connect-influxdb:1.2.6