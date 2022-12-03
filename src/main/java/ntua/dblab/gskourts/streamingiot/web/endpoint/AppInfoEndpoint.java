//package ntua.dblab.gskourts.streamingiot.web.endpoint;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping(value = "/app/info", produces = MediaType.APPLICATION_JSON_VALUE)
//public class AppInfoEndpoint {
//      private static final Logger LOG = LoggerFactory.getLogger(AppInfoEndpoint.class);

//      @Autowired
//      private BuildProperties buildProperties;
//      @Autowired
//      private AppStatus appStatus;

//      /*@Autowired
//      @Qualifier("compositeMetricsRegistry")
//      private MeterRegistry compositeMeterRegistry;
//      private Counter counter = null;*/

//      @GetMapping(value = { "", "/status" }, produces = MediaType.APPLICATION_JSON_VALUE)
//      public GenericResponse<String, String> status() {
//         if (appStatus.isAppPaused()) {
//            LOG.warn("status: App disabled throw error. appIsPaused={}", appStatus.isAppPaused());
//            throw new AppUnavailableException("Application not enabled");
//         }

//         return GenericResponse.factoryInfo(GenericResponse.CODE_OK_DFLT,
//               "RUNNING. Current time:" + AppConstants.timeDateZoneFormatter.format(ZonedDateTime.now()));
//      }

//      // @Timed("sample.controller.appinfo.check")
//      @RequestMapping(value = { "/health", "/check", "/healthcheck" }, method = {
//            RequestMethod.HEAD, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
//      public GenericResponse<String, String> healthcheck() {
//         /*long start = System.currentTimeMillis();
//         Timer timer = compositeMeterRegistry.timer("appinfo.check.timer");
//         timer.record(start, TimeUnit.MILLISECONDS);
//         counter.increment();*/

//         return status();
//      }

//      @GetMapping(value = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
//      public String version() {
//         return buildProperties.getVersion();
//      }

//      @GetMapping(value = "/buildinfo", produces = MediaType.APPLICATION_JSON_VALUE)
//      public BuildProperties buildInfo() {
//         return buildProperties;
//      }
//   }
//}
