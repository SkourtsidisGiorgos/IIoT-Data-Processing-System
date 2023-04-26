package ntua.dblab.gskourts.streamingiot.components;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfluxDBWriter {

    private static final Logger log = LoggerFactory.getLogger(InfluxDBWriter.class);
    private final InfluxDBClient influxDBClient;
    private final WriteApiBlocking writeApi;

    public InfluxDBWriter(String host, Integer port, String token, String org, String bucket) {
        String url = "http://" + host + ":" + port;
        influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
        writeApi = influxDBClient.getWriteApiBlocking();
    }

    public void writeAggregatedData(Integer key, String measurementType, Integer value,
                                    Long timestamp, String sensorId) {
        Point point = Point
                .measurement("aggregated_measurement")
                .time(timestamp, WritePrecision.MS)
                .addTag("sensor_id", sensorId)
                .addTag("measurement_type", measurementType)
                .addField("value", value);

        writeApi.writePoint(point);
        log.info("Data written to InfluxDB: " + point);
    }

    public void close() {
        influxDBClient.close();
    }
}
