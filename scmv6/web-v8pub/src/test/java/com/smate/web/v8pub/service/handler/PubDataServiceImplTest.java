package com.smate.web.v8pub.service.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.smate.web.v8pub.enums.PubHandlerStatusEnum;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.exception.PubHandlerException;

/**
 * 测试类 成果保存更新入口
 * 
 * @author tsz
 *
 * @date 2018年8月23日
 */
class PubDataServiceImplTest extends MockitoBase {

  @InjectMocks
  private PubDataServiceImpl pubDataServiceImpl;
  @Mock
  private PubHandlerLoader pubHandlerLoader;
  @Mock
  private PubHandlerService pubhandlerService;
  @Mock
  private PubDTO pub;

  @Test
  @DisplayName("测试空对象")
  void testCheckCommonParameterNull() {
    Throwable exception = assertThrows(PubHandlerCheckParameterException.class, () -> {
      pubDataServiceImpl.checkCommonParameter(pub);
    });
    assertTrue(exception.getMessage().contains("参数 pubHandlerName 不能为空"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"test"})
  @DisplayName("测试空对象")
  void testCheckCommonParameterString(String candidate) {
    pub.pubHandlerName = candidate;
    pubDataServiceImpl.checkCommonParameter(pub);
  }

  @ParameterizedTest
  @ValueSource(strings = {""})
  @DisplayName("测试空字符串")
  void testCheckCommonParameterBlankString(String candidate) {
    pub.pubHandlerName = candidate;
    Throwable exception = assertThrows(PubHandlerCheckParameterException.class, () -> {
      pubDataServiceImpl.checkCommonParameter(pub);
    });
    assertTrue(exception.getMessage().contains("参数 pubHandlerName 不能为空"));
  }

  @Test
  @DisplayName("测试执行PubHandleByType异常")
  void testPubHandleByTypeError() {
    pub.pubHandlerName = "errorHandler";
    when(pubHandlerLoader.getPubHandlerByPubHandlerName(pub.pubHandlerName))
        .thenThrow(new PubHandlerCheckParameterException("没有定义" + pub.pubHandlerName + "成果处理器"))
        .thenThrow(new PubHandlerException("成果处理出错")).thenThrow(new RuntimeException("调用成果处理器出错"));

    String result = pubDataServiceImpl.pubHandleByType(pub);
    assertTrue(result.contains(PubHandlerStatusEnum.ERROR.getValue()));
    assertTrue(result.contains("没有定义" + pub.pubHandlerName + "成果处理器"));

    result = pubDataServiceImpl.pubHandleByType(pub);
    assertTrue(result.contains(PubHandlerStatusEnum.ERROR.getValue()));
    assertTrue(result.contains("成果处理出错"));

    result = pubDataServiceImpl.pubHandleByType(pub);
    assertTrue(result.contains(PubHandlerStatusEnum.ERROR.getValue()));
    assertTrue(result.contains("调用成果处理器出错"));
  }

  @Test
  @DisplayName("测试执行PubHandleByType成功")
  void testPubHandleByTypeSuccess() {
    pub.pubHandlerName = "successHandler";
    when(pubHandlerLoader.getPubHandlerByPubHandlerName(pub.pubHandlerName)).thenReturn(pubhandlerService);
    when(pubhandlerService.pubHandle(pub)).thenReturn(new HashMap<String, String>());

    String result = pubDataServiceImpl.pubHandleByType(pub);
    assertTrue(result.contains(PubHandlerStatusEnum.SUCCESS.getValue()));
  }

  @Test
  @DisplayName("测试初始化方法")
  void afterPropertiesSet() {
    doThrow(PubHandlerException.class).doNothing().when(pubHandlerLoader).loadPubHandler();
    doNothing().when(pubHandlerLoader).checkPubDTO();
    Throwable exception = assertThrows(Exception.class, () -> {
      pubDataServiceImpl.afterPropertiesSet();
    });
    assertEquals(PubHandlerException.class, exception.getClass());
    try {
      pubDataServiceImpl.afterPropertiesSet();
    } catch (Exception e) {
      fail("此处不能有异常");
    }
  }

  @Test
  void setApplicationContext() {
    pubDataServiceImpl.setApplicationContext(null);
  }

}
