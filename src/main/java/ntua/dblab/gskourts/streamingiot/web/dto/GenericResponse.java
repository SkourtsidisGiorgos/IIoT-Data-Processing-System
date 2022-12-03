package ntua.dblab.gskourts.streamingiot.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import ntua.dblab.gskourts.streamingiot.util.EmptyUtils;

@JsonInclude(Include.NON_NULL)
public class GenericResponse<V, X> {

   public static final int CODE_OK_DFLT = Code.SUCCESS.code();
   public static final int CODE_ERROR_DFLT = Code.ERROR.code();
   // public static final String CODE_WARN_DFLT = "-1";

   private int code = CODE_OK_DFLT;
   private V data;
   private X info;

   private String description;
   private String errorCause;
   private Exception exception;

   public enum Code {

      SUCCESS(0, "OK"),
      ERROR(1, "ERROR"),
      SERVER_ERROR(100, "SERVER_ERROR"),
      UNAVAILABLE(200, "UNAVAILABLE"),
      INVALID_PARAM(300, "INVALID_PARAM"),
      TOO_MANY_ITEMS(400, "TOO_MANY_ITEMS");

      private final int code;
      private final String desc;

      private Code(int c, String d) {
         code = c;
         desc = d;
      }

      public int code() {
         return code;
      }

      public String desc() {
         return desc;
      }
   }

   public GenericResponse() {
   }

   public GenericResponse(int code, V data, String description, String errorCause) {
      this.code = code;
      this.data = data;
      this.description = description;
      this.errorCause = errorCause;
   }

   public GenericResponse(int code, V data, String description, String errorCause, X info) {
      this.code = code;
      this.data = data;
      this.info = info;
      this.description = description;
      this.errorCause = errorCause;
   }

   public GenericResponse(int code, X info) {
      this.code = code;
      this.info = info;
   }

   public static final <V, X> GenericResponse<V, X> factoryData(V data) {
      return factoryData(CODE_OK_DFLT, data);
   }

   public static final <V, X> GenericResponse<V, X> factoryInfo(X info) {
      return factoryInfo(CODE_OK_DFLT, info);
   }

   public static final <V, X> GenericResponse<V, X> factoryDataInfo(V data, X info) {
      return factoryDataInfo(CODE_OK_DFLT, data, info);
   }

   public static final <V, X> GenericResponse<V, X> factoryData(int code, V data) {
      return new GenericResponse<V, X>(code, data, null, null);
   }

   public static final <V, X> GenericResponse<V, X> factoryInfo(int code, X info) {
      return new GenericResponse<V, X>(code, info);
   }

   public static final <V, X> GenericResponse<V, X> factoryDataInfo(int code, V data, X info) {
      return new GenericResponse<V, X>(code, data, null, null, info);
   }

   public static final <V, X> GenericResponse<V, X> factoryError(int errorCode, String errorMessage,
         String errorCause, Exception cause) {

      return factoryError(errorCode, errorMessage, errorCause, cause, true);
   }

   public static final <V, X> GenericResponse<V, X> factoryErrorAPI(int errorCode, String errorMessage,
         String errorCause, Exception cause) {
      return factoryError(errorCode, errorMessage, errorCause, cause, false);
   }

   public static final <V, X> GenericResponse<V, X> factoryError(int errorCode, String errorMessage,
         String errorCause, Exception cause, boolean applyException) {

      GenericResponse<V, X> x = new GenericResponse<V, X>(errorCode, null, removeNewlines(errorMessage),
            errorCause != null ? errorCause : buildErrorCauseMessage(cause));
      if (applyException) {
         x.setException(cause);
      }

      return x;
   }

   public static final <V, X> GenericResponse<V, X> factoryError(String errorMessage, String errorCause) {
      return factoryError(CODE_ERROR_DFLT, errorMessage, errorCause, null);
   }

   public static final <V, X> GenericResponse<V, X> factoryError(String errorMessage) {
      return factoryError(CODE_ERROR_DFLT, errorMessage, null, null);

   }

   public static final <V, X> GenericResponse<V, X> factoryError(String errorMessage, Exception cause) {
      return factoryError(CODE_ERROR_DFLT, errorMessage, null, cause);
   }

   public static final <V, X> GenericResponse<V, X> factoryError(Exception cause) {
      return factoryError(CODE_ERROR_DFLT, cause.getMessage(), cause.getMessage(), cause);
   }

   public void applyCustomCode(int code, String description) {
      this.code = code;
      this.description = description;
   }

   public void applyCode(Code e) {
      applyCustomCode(e.code(), e.desc());
   }

   public void applyCode(Code e, String extraDesc) {
      applyCustomCode(e.code(), e.desc() + ": " + extraDesc);
   }

   public int getCode() {
      return code;
   }

   public void setCode(int code) {
      this.code = code;
   }

   public V getData() {
      return data;
   }

   public void setData(V data) {
      this.data = data;
   }

   public X getInfo() {
      return info;
   }

   public void setInfo(X info) {
      this.info = info;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getErrorCause() {
      return errorCause;
   }

   public void setErrorCause(String errorCause) {
      this.errorCause = errorCause;
   }

   public Exception getException() {
      return exception;
   }

   public void setException(Exception exception) {
      this.exception = exception;
   }

   private static String removeNewlines(String errorMessage) {
      return !EmptyUtils.nullOrEmpty(errorMessage) ? errorMessage.replaceAll("\\r|\\n", " __NEWLINE__ ") : null;
   }

   private static String buildErrorCauseMessage(Throwable cause) {
      if (cause == null) {
         return null;
      }
      return cause.getClass().getSimpleName()
            + (EmptyUtils.nullOrEmptyTrimmed(cause.getMessage()) ? "" : (" : " + cause.getMessage()));
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("GenericResponse [code=");
      builder.append(code);
      builder.append(", ");
      if (data != null) {
         builder.append("data=");
         builder.append(data);
         builder.append(", ");
      }
      if (info != null) {
         builder.append("info=");
         builder.append(info);
         builder.append(", ");
      }
      if (description != null) {
         builder.append("description=");
         builder.append(description);
         builder.append(", ");
      }
      if (errorCause != null) {
         builder.append("errorCause=");
         builder.append(errorCause);
         builder.append(", ");
      }
      if (exception != null) {
         builder.append("exception=");
         builder.append(exception);
      }
      builder.append("]");
      return builder.toString();
   }
}
