package jp.co.haw.aws.iprangechecker;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * https://ip-ranges.amazonaws.com/ip-ranges.json のJsonObject
 * @author azuchi
 */
public class JsonObject {

  public String syncToken;

  public String createDate;

  public List<Prefix> prefixes;

  public static class Prefix{
    @JsonProperty("ip_prefix")
    public String ipPrefix;

    public String region;

    public String service;
  }
}
