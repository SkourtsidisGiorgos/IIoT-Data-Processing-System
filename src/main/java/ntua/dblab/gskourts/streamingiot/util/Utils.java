package ntua.dblab.gskourts.streamingiot.util;

import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

   private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
   private static String miniHostname = AppConstants.UNKNOWN;
   private static String ip = AppConstants.UNKNOWN;
   private static String hostname = AppConstants.UNKNOWN;
   private static String jvmId = AppConstants.UNKNOWN;
   private static String pid = AppConstants.UNKNOWN;
   private static String uuid = UUID.randomUUID().toString();

   static {
      LOG.info("Start static init");
      try {
         InetAddress addr = InetAddress.getLocalHost();
         hostname = addr.getCanonicalHostName();
         LOG.info("hostname={}", hostname);
         miniHostname = addr.getHostName();
         LOG.info("miniHostname={}", miniHostname);
         ip = addr.getHostAddress();
         LOG.info("IP={}", ip);
      } catch (UnknownHostException e) {
         LOG.warn("Cannot get local hostname");
      }

      jvmId = ManagementFactory.getRuntimeMXBean().getName();
      LOG.info("jvmId={}, uuid={}", jvmId, uuid);
      LOG.info("End static init");
   }

   public static String getIp() {
      return ip;
   }

   public static String getHostName() {
      return hostname;
   }

   public static String getMiniHostName() {
      return miniHostname;
   }

   public static String getJvmId() {
      return jvmId;
   }

   public static String getUuid() {
      return uuid;
   }

   public static String getInstanceIdModeValue(String mode) {
      if (mode.equalsIgnoreCase("ip")) {
         return getIp().replace(".", "_");
      } else if (mode.equalsIgnoreCase("fqdn")) {
         return getHostName();
      } else if (mode.equalsIgnoreCase("host")) {
         return getMiniHostName();
      } else if (mode.equalsIgnoreCase("jvmid")) {
         return getJvmId();
      } else if (mode.equalsIgnoreCase("uuid")) {
         return getUuid();
      }
      return mode.replace(".", "_");
   }

   public static String buildMetricsPath(String instanceIdModes) {
      String dirPath = "";
      try {
         String[] modeArr = instanceIdModes.trim().split("\\s*,\\s*");
         for (String mode : modeArr) {
            dirPath += getInstanceIdModeValue(mode) + ".";
         }
      } catch (NullPointerException e) {
         LOG.warn("buildMetricsPath: Null instance-id parameter. Return empty.");
      }
      return dirPath;
   }

   public static List<String> getCSVContents(String stringCsv) {
      try {
         return Arrays.asList(stringCsv.trim().split("\\s*,\\s*"));
      } catch (NullPointerException e) {
         LOG.warn("getCSVContents: Null string_csv parameter. Return empty.");
         return Collections.emptyList();
      }
   }

   public static String getAppVersionOfClass(Class<? extends Object> c) {
      LOG.debug("Getting version of class: {}", c);
      String specVersion = c.getPackage().getSpecificationVersion();
      String implVersion = c.getPackage().getImplementationVersion();
      return (specVersion == null ? "" : specVersion) + (implVersion == null ? "" : implVersion);
   }

   public static Manifest getManifest(Class<?> clz) {
      String resource = "/" + clz.getName().replace(".", "/") + ".class";
      String fullPath = clz.getResource(resource).toString();
      String archivePath = fullPath.substring(0, fullPath.length() - resource.length());

      // Double check because it may return Unix-style seperators even on Windows
      // (e.g. in VFS)
      if (archivePath.endsWith("/WEB-INF/classes") || archivePath.endsWith("\\WEB-INF\\classes")) {
         // Required for wars
         archivePath = archivePath.substring(0, archivePath.length() - "/WEB-INF/classes".length());
      }

      try (InputStream input = new URL(archivePath + "/META-INF/MANIFEST.MF").openStream()) {
         return new Manifest(input);
      } catch (Exception e) {
         LOG.warn("Failed to get manifest for class " + clz, e);
         return null;
      }
   }

   /**
    * Converts a list of strings, into a single string where each list element is
    * separated by each
    * other with a specified delimiter.
    */
   public static String convertListOfStrToStrWithDelimiter(List<String> list, String delimiter) {
      StringBuilder result = new StringBuilder();
      for (String str : list) {
         result.append(str);
         result.append(delimiter);
      }
      return result.toString();
   }

   public static String exceptionNameWithMessage(Exception e) {
      if (e == null) {
         return "";
      }
      if (e.getCause() != null) {
         return e.getClass().getSimpleName() + ":" + e.getMessage() + " - Cause:"
               + e.getCause().getClass().getSimpleName() + ":" + e.getCause().getMessage();
      } else {
         return e.getClass().getSimpleName() + ":" + e.getMessage();
      }
   }

   public static void logJavaInfo() {
      LOG.debug("Java Home = {}", System.getProperty("java.home"));
      LOG.debug("Java version = {}", System.getProperty("java.version"));
   }

   public static Map<String, String> getHostPortFromJdbcUrl(String url) {
      String cleanURI = url.substring(5);

      URI uri = URI.create(cleanURI);
      String host = uri.getHost();
      String port = Integer.toString(uri.getPort());

      if (host == null) {
         String regex = ".*://(.*):(\\d+)(.*)";
         Pattern p = Pattern.compile(regex);

         Matcher matcher = p.matcher(url);
         if (matcher.find()) {
            host = matcher.group(1);
            port = matcher.group(2);
         } else {
            host = null;
            port = null;
         }
      }
      HashMap<String, String> result = new HashMap<>();
      result.put("host", host);
      result.put("port", port);
      LOG.trace("result={}", result);
      return result;

   }

   public static String getCurrentDateTime() {
      return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
   }

   public static String getCurrentUser() {
      return System.getProperty("user.name");
   }

    public static String getPid() {
        String processName =
                java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return processName.split("@")[0];

   }
}