package jp.co.haw.aws.iprangechecker;

import com.google.api.client.util.Key;

import java.util.List;

/**
 * https://ip-ranges.amazonaws.com/ip-ranges.json のJsonObject
 * @author azuchi
 */
public class JsonObject {

  @Key
  public String syncToken;

  @Key
  public String createDate;

  @Key("prefixes")
  public List<Prefix> prefixes;

  public static class Prefix{
    @Key("ip_prefix")
    public String ipPrefix;

    @Key
    public String region;

    @Key
    public String service;
  }
}
