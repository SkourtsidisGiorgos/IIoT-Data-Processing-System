package ntua.dblab.gskourts.streamingiot.controller;

import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@ConditionalOnProperty(name="web-server-only", havingValue="false")
@RequestMapping("/redis")
public class RedisController {

    private final RedisCommands<String,String> redisCommands;

    // create a "GET" Mapping that returns the latest measurement for a given sensor. Sensor should be given as a query parameter.
    // e.g. http://localhost:8080/redis/temp-1
    @GetMapping("/latest-measurement/{sensor}")
    public String getLatestMeasurement(@PathVariable String sensor) {
        // check if exists
        Long existingKeys = redisCommands.exists(sensor);
        if (existingKeys == 0) {
            return "No such sensor";
        }
        return redisCommands.get(sensor);
    }

    // get all keys-value pairs
    @GetMapping("/all")
    public String getAll() {
        return redisCommands.keys("*").toString();
    }
}
