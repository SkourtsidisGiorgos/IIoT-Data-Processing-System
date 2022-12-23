package ntua.dblab.gskourts.streamingiot.util;

import java.time.Duration;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.NamingConvention;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;
import io.micrometer.graphite.GraphiteConfig;
import io.micrometer.graphite.GraphiteMeterRegistry;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import lombok.ToString;

@Configuration
@ToString
public class MetricsConfiguration {

   private static Logger LOG = LoggerFactory.getLogger(MetricsConfiguration.class);

   @Value("${management.metrics.registries}")
   private String registriesCsv;

   @Value("${management.metrics.export.graphite.host}")
   private String graphiteHost;

   @Value("${management.metrics.export.graphite.port}")
   private int graphitePort;

   @Value("${management.metrics.export.graphite.enabled}")
   private Boolean isMetricsEnabled;

   @Value("${management.metrics.export.graphite.step}")
   private Duration graphiteStep;

   @Value("${management.metrics.export.graphite.instance-id}")
   private String instanceIdModes;

   @Value("${management.metrics.allowed}")
   private String allowedMetricsCsv;

   @Autowired
   private AppConf appConf;

   //@Bean
   public GraphiteConfig graphiteConfig() {
      GraphiteConfig graphiteConfig = new GraphiteConfig() {
         @Override
         public String host() {
            return graphiteHost;
         }

         @Override
         public int port() {
            return graphitePort;
         }

         @Override
         public boolean enabled() {
            return isMetricsEnabled;
         }

         @Override
         public Duration step() {
            return graphiteStep;
         }

         @Override
         public String get(String k) {
            if ("graphite.graphiteTagsEnabled".equalsIgnoreCase(k)) {
               return "FALSE";
            }
            return null; // accept the rest of the defaults
         }

         @Override
         public String[] tagsAsPrefix() {
            return new String[0]; // APP
         }
      };
      LOG.info("graphiteConfig: Host={}, Step={}, TagsEnabled={}, config={}", graphiteConfig.host(),
            graphiteConfig.step(), graphiteConfig.graphiteTagsEnabled(), graphiteConfig);
      return graphiteConfig;
   }

   public GraphiteMeterRegistry graphiteMeterRegistry(GraphiteConfig config, Clock clock) {
      LOG.info("graphiteMeterRegistry: Host={}, Step={}, TagsEnabled={}, config={}", config.host(), config.step(),
            config.graphiteTagsEnabled(), config);

      String instanceIdModeDir = Utils.buildMetricsPath(instanceIdModes);
      LOG.info("graphiteMeterRegistry: instanceIdModeDir={}", instanceIdModeDir);

      GraphiteMeterRegistry registry = new GraphiteMeterRegistry(config, // GraphiteConfig.DEFAULT,
            Clock.SYSTEM,
            (id, convention) -> "app.ntuaStreamingIot."
                  + instanceIdModeDir
                  + HierarchicalNameMapper.DEFAULT.toHierarchicalName(id, NamingConvention.dot));

      LOG.info("Registry STARTED: registry={}, config={}", registry, registry.config());

      return registry;
   }

   public JmxMeterRegistry jmxMetricsRegistry() {
      return new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM);
   }

   @Bean
   public MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer() {
      List<String> allowedMetrics = Utils.getCSVContents(allowedMetricsCsv);

      return registry -> {
         for (String metric : allowedMetrics) {
            LOG.info("{} MeterRegistry: metric added={}", registry.getClass().getName(), metric);
            registry.config().meterFilter(MeterFilter.acceptNameStartsWith(metric));
         }

         registry.config().meterFilter(MeterFilter.deny());
      };
   }

   @Bean("compositeMetricsRegistry")
   //public MeterRegistry compositeMetricsRegistry(PrometheusMeterRegistry prometheusMetricsRegistry) {
   public MeterRegistry compositeMetricsRegistry() {
      CompositeMeterRegistry composite = new CompositeMeterRegistry();

      List<String> expectedRegistries = Utils.getCSVContents(registriesCsv);
      LOG.info("expected_registries: {}",
            expectedRegistries);

      if (expectedRegistries.contains("jmx")) {
         JmxMeterRegistry jmxRegistry = jmxMetricsRegistry();
         composite.add(jmxRegistry);
      }
      if (expectedRegistries.contains("graphite")) {
         GraphiteMeterRegistry graphiteRegistry = graphiteMeterRegistry(graphiteConfig(), Clock.SYSTEM);
         composite.add(graphiteRegistry);
      }
      //if (expectedRegistries.contains("prometheus")) {
      //   composite.add(prometheusMetricsRegistry);
      //}

      if (composite.getRegistries().isEmpty()) {
         JmxMeterRegistry jmxRegistry = jmxMetricsRegistry();
         composite.add(jmxRegistry);
      }

      return composite;
   }

   @PostConstruct
   private void init() {
      LOG.info("START init. {}", this);
   }
}
