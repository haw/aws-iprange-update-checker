package jp.co.haw.aws.iprangechecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;

/**
 * The test for {@link JsonObject}
 * @author azuchi
 */
public class JsonObjectTest {

  @Test
  public void diff() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    JsonObject json1 = mapper.readValue(getClass().getResourceAsStream("/ip-ranges1.json"), JsonObject.class);
    JsonObject json2 = mapper.readValue(getClass().getResourceAsStream("/ip-ranges2.json"), JsonObject.class);
    Optional<String> diff1 = json1.getDiff(json2);
    Optional<String> diff2 = json1.getDiff(json1);
    assertThat(diff1.get(),
      is(
        "- \"ip_prefix\": \"54.168.0.0/16\", \"region\": \"ap-northeast-1\", \"service\": \"AMAZON\"\n" +
        "+ \"ip_prefix\": \"54.192.0.0/16\", \"region\": \"GLOBAL\", \"service\": \"CLOUDFRONT\"\n"
      ));
    assertFalse(diff2.isPresent());

    // IPv6 support
    JsonObject json3 = mapper.readValue(getClass().getResourceAsStream("/ip-ranges3.json"), JsonObject.class);
    JsonObject json4 = mapper.readValue(getClass().getResourceAsStream("/ip-ranges4.json"), JsonObject.class);
    Optional<String> diff3 = json3.getDiff(json4);
    assertThat(diff3.get(),
      is(
        "+ \"ip_prefix\": \"13.32.0.0/15\", \"region\": \"GLOBAL\", \"service\": \"AMAZON\"\n"+
          "- \"ipv6_prefix\": \"2a05:d018:7ff:f800::/53\", \"region\": \"eu-west-1\", \"service\": \"ROUTE53_HEALTHCHECKS\"\n" +
          "+ \"ipv6_prefix\": \"2a05:d018:fff:f800::/53\", \"region\": \"eu-west-1\", \"service\": \"ROUTE53_HEALTHCHECKS\"\n"
      ));
  }

}
