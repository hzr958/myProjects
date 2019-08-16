package com.smate.web.v8pub.service.handler;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

/**
 * 使用 mokito 测试类 的父类
 * 
 * @author tsz
 *
 * @date 2018年8月5日
 */
public class MockitoBase {

  @BeforeEach
  void initMockito() {
    MockitoAnnotations.initMocks(this);
  }
}
