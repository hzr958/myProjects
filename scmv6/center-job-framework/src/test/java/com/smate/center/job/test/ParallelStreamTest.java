package com.smate.center.job.test;

import java.util.stream.IntStream;
import org.junit.Test;

/**
 * @author Created by hcj
 * @date 2018/07/18 10:42
 */
public class ParallelStreamTest {

  @Test
  public void testParallelStream() {
    IntStream.range(0, 100).parallel().forEach(n -> System.out.print(n + " "));
    System.out.println();
    System.out.println("Hello!");
    IntStream.range(0, 100).parallel().forEach(n -> System.out.print(n + " "));
    System.out.println();
    System.out.println("Hi~");
  }
}
