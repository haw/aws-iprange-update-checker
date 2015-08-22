package jp.co.haw.aws.iprangechecker;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

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

    @Override
    public String toString() {
      return "\"ip_prefix\": \"" + ipPrefix + "\", \"region\": \"" + region + "\", \"service\": \"" + service + "\"";
    }

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof Prefix)) {
        return false;
      }
      return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
      return HashCodeBuilder.reflectionHashCode(this);
    }
  }

  public InputStream toInputStream() {
    StringWriter writer = new StringWriter();
    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.writeValue(writer, this);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ByteArrayInputStream(writer.toString().getBytes());
  }

  public Optional<String> getDiff(JsonObject json) {
    if (json.syncToken.equals(syncToken)) {
      return Optional.empty();
    }
    Patch<Prefix> patch = DiffUtils.diff(prefixes, json.prefixes);
    StringBuilder builder = new StringBuilder();
    for (Delta<Prefix> delta: patch.getDeltas()) {
      delta.getOriginal().getLines().forEach(p -> builder.append("- ").append(p.toString()).append("\n"));
      delta.getRevised().getLines().forEach(p -> builder.append("+ ").append(p.toString()).append("\n"));
    }
    return Optional.of(builder.toString());
  }
}
