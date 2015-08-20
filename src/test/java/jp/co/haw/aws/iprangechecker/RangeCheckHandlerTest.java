package jp.co.haw.aws.iprangechecker;

import org.junit.Test;

/**
 * {@link RangeCheckHandler}のテスト
 * @author azuchi
 */
public class RangeCheckHandlerTest {

  @Test
  public void test() {
    RangeCheckHandler handler = new RangeCheckHandler();
    handler.handleRequest(1, null);
  }
}
