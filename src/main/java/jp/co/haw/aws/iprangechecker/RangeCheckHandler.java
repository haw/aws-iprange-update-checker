package jp.co.haw.aws.iprangechecker;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * AWSリソースのIPのレンジをチェックして更新されていれば更新内容を通知するLambda Function
 * @author azuchi
 */
public class RangeCheckHandler {

  private static final String IP_RANGE_URL = "https://ip-ranges.amazonaws.com/ip-ranges.json";

  public String handleRequest(int count, Context context) throws IOException, URISyntaxException {
    ObjectMapper mapper = new ObjectMapper();
    JsonObject json = mapper.readValue(new URL(IP_RANGE_URL), JsonObject.class);
    return "";
  }
}
