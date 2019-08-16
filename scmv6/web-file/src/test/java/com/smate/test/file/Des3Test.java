package com.smate.test.file;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.smate.core.base.utils.security.Des3Utils;

/**
 *
 * @author houchuanjie
 * @date 2017年12月4日 下午5:47:58
 */
public class Des3Test {

  @Test
  public void test() {
    System.err.println("------------------------------");
    System.err.println(Des3Utils.encodeToDes3("1111111111111"));
    System.out.println(Des3Utils.decodeFromDes3(null));
    System.err.println("------------------------------");
  }

  @Test
  public void testDateFormat() {
    System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
  }
}
