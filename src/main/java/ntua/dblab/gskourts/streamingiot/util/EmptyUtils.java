package ntua.dblab.gskourts.streamingiot.util;

import java.util.Collection;
import java.util.Map;

/**
 * Utilities to check if Object is null or empty.
 */
public class EmptyUtils {

   private EmptyUtils() {
   }

   public static boolean nullOrEmpty(String... in) {
      return (in == null || in.length == 0);
   }

   public static boolean nullOrEmptyAnyElement(String... in) {
      if (in == null || in.length == 0) {
         return true;
      }
      for (String x : in) {
         if (x == null || x.length() == 0) {
            return true;
         }
      }
      return false;
   }

   public static boolean nullOrEmpty(String in) {
      return (in == null || in.isEmpty());
   }

   public static boolean nullOrEmptyTrimmed(String in) {
      return (in == null || in.trim().length() == 0);
   }

   public static <E> boolean nullOrEmpty(final Collection<E> in) {
      return (in == null || in.isEmpty());
   }

   public static <E, V> boolean nullOrEmpty(final Map<E, V> in) {
      return (in == null || in.size() == 0);
   }

   public static <T> boolean nullOrEmpty(T[] in) {
      return (in == null || in.length == 0);
   }

   public static boolean nullOrEmpty(byte[] in) {
      return (in == null || in.length == 0);
   }

   public static boolean nullOrEmpty(short[] in) {
      return (in == null || in.length == 0);
   }

   public static boolean nullOrEmpty(int[] in) {
      return (in == null || in.length == 0);
   }

   public static boolean nullOrEmpty(long[] in) {
      return (in == null || in.length == 0);
   }

   public static boolean nullOrEmpty(float[] in) {
      return (in == null || in.length == 0);
   }

   public static boolean nullOrEmpty(double[] in) {
      return (in == null || in.length == 0);
   }

   public static boolean nullOrEmpty(boolean[] in) {
      return (in == null || in.length == 0);
   }

   public static boolean nullOrEmpty(char[] in) {
      return (in == null || in.length == 0);
   }

   /**
    * Transforms a given input String to a version WITHOUT:
    *
    * -Newlines at any position.
    * -Trailing or leading whitespaces.
    * -Excessive whitespaces at other positions (limits to one at a time).
    *
    * @param in the string to be transformed
    * @return a transformed version of the string.
    */
   public static String filterNewlinesWhitespaces(String in) {
      return in == null ? null : in.replace("\n", "").replaceAll(" +", " ").trim();
   }

   /**
    * Transforms a given input String to a version WITHOUT:
    *
    * -Newlines at any position.
    * -Trailing or leading whitespaces.
    * -Whitespaces at other positions (one or more).
    *
    * @param in the string to be transformed
    * @return a transformed version of the string
    */
   public static String removeNewlinesWhitespaces(String in) {
      return in == null ? null : in.replace("\n", "").replaceAll(" +", "");
   }

   public static String removeExtraWhitespaces(String in) {
      return in == null ? null : in.replaceAll(" +", " ");
   }

   public static String removeNewlines(String in) {
      return in == null ? null : in.replace("\n", "");
   }

   public static String stringOrEmpty(String str, boolean trim) {
      if (str == null)
         return "";

      if (trim)
         return str.trim();
      return str;
   }
}