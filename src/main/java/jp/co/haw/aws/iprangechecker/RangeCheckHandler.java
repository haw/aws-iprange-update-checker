package jp.co.haw.aws.iprangechecker;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;

/**
 * AWSリソースのIPのレンジをチェックして更新されていれば更新内容を通知するLambda Function
 * @author azuchi
 */
public class RangeCheckHandler {

  private static final String IP_RANGE_URL = "https://ip-ranges.amazonaws.com/ip-ranges.json";
  private static final String CHECKED_FILE_NAME = "checked-ip-ranges.json";
  private static final String S3_BUCKETNAME_KEY = "s3.bucketname";
  private static final String SNS_SUBJECT = "AWS IP range updated.";

  public void handleRequest(S3Event event, Context context) throws IOException {
    LambdaLogger logger = context.getLogger();
    logger.log("handleRequest start. event = " + event.toJson());
    ObjectMapper mapper = new ObjectMapper();
    JsonObject json = mapper.readValue(new URL(IP_RANGE_URL), JsonObject.class);

    AmazonS3Client s3 = getS3Client();
    Properties props = getProperties();
    s3.setEndpoint(props.getProperty("s3.endpoint"));

    GetObjectRequest request = new GetObjectRequest(props.getProperty(S3_BUCKETNAME_KEY), CHECKED_FILE_NAME);
    try {
      S3Object beforeObject = s3.getObject(request);
      InputStream is = beforeObject.getObjectContent();
      JsonObject beforeJson = mapper.readValue(is, JsonObject.class);
      Optional<String> diff = beforeJson.getDiff(json);
      if (diff.isPresent()) {
        AmazonSNSClient sns = getSNSClient();
        sns.setRegion(Region.getRegion(Regions.fromName(props.getProperty("sns.region"))));
        PublishRequest publishRequest = new PublishRequest(props.getProperty("sns.topic.arn"), diff.get(), SNS_SUBJECT);
        PublishResult result = sns.publish(publishRequest);
        logger.log("send sns message. messageId = " + result.getMessageId());
      }
    } catch (AmazonS3Exception e) {
      logger.log("before checked-ip-ranges.json does not exist.");
    }
    storeCheckedIpRange(json);
    logger.log("stored checked-ip-ranges.json");
  }

  private void storeCheckedIpRange(JsonObject json) {
    Properties props = getProperties();
    getS3Client().putObject(
      props.getProperty(S3_BUCKETNAME_KEY), CHECKED_FILE_NAME, json.toInputStream(), new ObjectMetadata());
  }

  private AmazonS3Client getS3Client() {
    Optional<AWSCredentials> credentials = getCredentials();
    if (credentials.isPresent()) {
      return new AmazonS3Client(credentials.get());
    } else {
      return new AmazonS3Client(new EnvironmentVariableCredentialsProvider());
    }
  }

  private AmazonSNSClient getSNSClient() {
    Optional<AWSCredentials> credentials = getCredentials();
    if (credentials.isPresent()) {
      return new AmazonSNSClient(credentials.get());
    } else {
      return new AmazonSNSClient(new EnvironmentVariableCredentialsProvider());
    }
  }

  private Optional<AWSCredentials> getCredentials() {
    Properties props = getProperties();
    if (props.containsKey("access.key")) {
      AWSCredentials credentials = new AWSCredentials() {
        @Override
        public String getAWSAccessKeyId() {
          return props.getProperty("access.key");
        }

        @Override
        public String getAWSSecretKey() {
          return props.getProperty("secret.key");
        }
      };
      return Optional.of(credentials);
    }
    return Optional.empty();
  }

  private Properties getProperties() {
    Properties props = new Properties();
    try {
      props.load(getClass().getResourceAsStream("/aws.properties"));
    } catch (IOException e) {
    }
    return props;
  }

}
