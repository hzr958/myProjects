package com.smate.test.example;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.smate.test.utils.JunitPropertiesUtils;

/**
 * 
 * @author wsn
 * @date Jan 10, 2019
 */
public class TestExample {

  private static Map<String, String> proMap;
  static String runEnv = System.getenv("RUN_ENV");
  final static String filePath = "src/test/java/com/smate/test/example/" + runEnv + "_example_param.csv";

  /**
   * 根据环境变量加载不同的properties文件
   * 
   * @throws IOException
   */
  @BeforeAll
  public static void loadProperties() throws IOException {
    proMap = JunitPropertiesUtils
        .loadProperties("src/test/java/com/smate/test/example/properties/" + runEnv + "_test.properties");
  }


  @Test
  public void testCase() {
    System.out.println("------------" + proMap.get("junit_domain") + "-----------");
    System.out.println("------------" + proMap.get("junit_domainMobile") + "-----------");
  }

  /**
   * csv数据驱动例子
   * 
   * @param argument
   * @param bb
   * @param cc
   */
  @ParameterizedTest
  @MethodSource("stringProvider")
  void testWithCsvFileSource(String argument, String bb, String cc) {
    assertNotNull(argument);
    assertNotEquals(0, argument);
  }

  static Stream<String[]> stringProvider() {
    return JunitPropertiesUtils.getCSVFileStream(filePath);
  }
}
