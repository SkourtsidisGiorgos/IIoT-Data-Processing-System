package ntua.dblab.gskourts.streamingiot.web.endpoint;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ntua.dblab.gskourts.streamingiot.util.AppConstants;
import ntua.dblab.gskourts.streamingiot.model.dto.GenericResponse;

@RestController
@RequestMapping(value = "/app/info", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppInfoEndpoint {
   private static final Logger LOG = LoggerFactory.getLogger(AppInfoEndpoint.class);

   @Autowired
   private BuildProperties buildProperties;

   /*@Autowired
   @Qualifier("compositeMetricsRegistry")
   private MeterRegistry compositeMeterRegistry;
   private Counter counter = null;*/

   @GetMapping(value = { "", "/status" }, produces = MediaType.APPLICATION_JSON_VALUE)
   public GenericResponse<String, String> status() {
      return GenericResponse.factoryInfo(GenericResponse.CODE_OK_DFLT,
            "RUNNING. Current time:" + AppConstants.timeDateZoneFormatter.format(ZonedDateTime.now()));
   }

   // @Timed("sample.controller.appinfo.check")
   @RequestMapping(value = { "/health", "/check", "/healthcheck" }, method = {
         RequestMethod.HEAD, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
   public GenericResponse<String, String> healthcheck() {
      /*long start = System.currentTimeMillis();
      Timer timer = compositeMeterRegistry.timer("appinfo.check.timer");
      timer.record(start, TimeUnit.MILLISECONDS);
      counter.increment();*/

      return status();
   }

   @GetMapping(value = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
   public String version() {
      return buildProperties.getVersion();
   }

   @GetMapping(value = "/buildinfo", produces = MediaType.APPLICATION_JSON_VALUE)
   public BuildProperties buildInfo() {
      return buildProperties;
   }
}
