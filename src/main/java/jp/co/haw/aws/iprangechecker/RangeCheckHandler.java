package jp.co.haw.aws.iprangechecker;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;


import java.io.IOException;

/**
 * AWSリソースのIPのレンジをチェックして更新されていれば更新内容を通知するLambda Function
 * @author azuchi
 */
public class RangeCheckHandler {

  private static final String IP_RANGE_URL = "https://ip-ranges.amazonaws.com/ip-ranges.json";

  public String handler(int count, Context context) {
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
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }
}
