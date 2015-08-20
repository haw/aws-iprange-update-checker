package jp.co.haw.aws.iprangechecker;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.util.json.JSONObject;
import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;


import java.io.IOException;

/**
 * AWSリソースのIPのレンジをチェックして更新されていれば更新内容を通知するLambda Function
 * @author azuchi
 */
public class RangeCheckHandler {

  private static final String IP_RANGE_URL = "http://ip-ranges.amazonaws.com/ip-ranges.json";

  public String handleRequest(int count, Context context) {
    System.setProperty("jsse.enableSNIExtension", "false");
    ApacheHttpTransport transport = new ApacheHttpTransport();
    HttpRequestFactory factory = transport.createRequestFactory(new HttpRequestInitializer() {
      @Override
      public void initialize(HttpRequest request) throws IOException {
        request.setConnectTimeout(0);
        request.setReadTimeout(0);
        request.setParser(new JacksonFactory().createJsonObjectParser());
      }
    });
    try {
      HttpRequest request = factory.buildGetRequest(new GenericUrl(IP_RANGE_URL));
      HttpResponse response = request.execute();
      JsonObject json = response.parseAs(JsonObject.class);
      json.prefixes.forEach(p -> System.out.println(p.ipPrefix));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }
}
