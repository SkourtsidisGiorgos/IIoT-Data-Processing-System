package ntua.dblab.gskourts.streamingiot.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.influxdb")
public class InfluxDbConfiguration {
    private final Logger log = LoggerFactory.getLogger(InfluxDbConfiguration.class);

    private String host;
    private Integer port;
    private String database;
    private String username;
    private String password;
    private String token;
    private String org;
    private String bucket;
    private String retention;

    @PostConstruct
    public void init() {
        log.info("InfluxDB configuration: " + this);
    }
}
