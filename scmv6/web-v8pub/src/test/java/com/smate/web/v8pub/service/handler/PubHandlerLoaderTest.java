package com.smate.web.v8pub.service.handler;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;

import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.exception.PubHandlerException;
import com.smate.web.v8pub.test.PubHandlerServiceExampleImpl;

/**
 * 成果处理器 测试类
 * 
 * @author tsz
 *
 * @date 2018年8月23日
 */
class PubHandlerLoaderTest extends MockitoBase {

  @InjectMocks
  private PubHandlerLoader pubHandlerLoader;
  @Mock
  private HashMap<String, PubHandlerService> pubHandlers;
  @Mock
  private ApplicationContext applicationContext;

  @ParameterizedTest
  @ValueSource(strings = {"", "test"})
  @DisplayName("测试获取处理器方法")
  void testGetPubHandlerByPubHandlerName(String candidate) {
    when(pubHandlers.get(candidate)).thenReturn(null).thenReturn(mock(PubHandlerService.class));
    Throwable exception = assertThrows(PubHandlerCheckParameterException.class, () -> {
      pubHandlerLoader.getPubHandlerByPubHandlerName(candidate);
    });
    assertTrue(exception.getMessage().contains("没有定义" + candidate + "成果处理器"));
    assertNotEquals(null, pubHandlerLoader.getPubHandlerByPubHandlerName(candidate));

  }

  @Test
  @DisplayName("测试成果处理器加载器，null,跟空对象的情况")
  void testLoadPubHandlerBlank() {
    HashMap<String, Object> beansMap = new HashMap<String, Object>();
    when(applicationContext.getBeansWithAnnotation(PubHandlerMapping.class)).thenReturn(beansMap).thenReturn(null);
    pubHandlerLoader.loadPubHandler();
    pubHandlerLoader.loadPubHandler();
  }

  @Test
  @DisplayName("测试成果处理器加载器,出错情况")
  void testLoadPubHandlerException() {
    HashMap<String, Object> beansMap = new HashMap<String, Object>();
    beansMap.put("test", "ERROR");
    when(applicationContext.getBeansWithAnnotation(PubHandlerMapping.class)).thenReturn(beansMap);
    Throwable exception = assertThrows(PubHandlerException.class, () -> {
      pubHandlerLoader.loadPubHandler();
    });
    assertTrue(exception.getMessage().contains("使用了 @PubHandlermapping 注解 但没有继承 PubHandlerServiceBaseBean.class"));
  }

  @Test
  @DisplayName("测试成果处理器加载器,出错情况")
  void testLoadPubHandlerException1() {
    HashMap<String, Object> beansMap = new HashMap<String, Object>();
    beansMap.put("test", new PubHandlerServiceExampleImpl());
    when(applicationContext.getBeansWithAnnotation(PubHandlerMapping.class)).thenReturn(beansMap);
    Throwable exception = assertThrows(PubHandlerException.class, () -> {
      pubHandlerLoader.loadPubHandler();
    });
    assertTrue(exception.getMessage().contains("使用了 @PubHandlermapping 注解,该类的包必须为 PubHandlerServiceBaseBean 类包的子包"));
  }

  @Test
  @DisplayName("测试成果处理器加载器,正确情况")
  void testLoadPubHandlerException2() {
    HashMap<String, Object> beansMap = new HashMap<String, Object>();
    beansMap.put("test", new com.smate.web.v8pub.service.handler.example.PubHandlerServiceExampleImpl());
    when(applicationContext.getBeansWithAnnotation(PubHandlerMapping.class)).thenReturn(beansMap);
    pubHandlerLoader.loadPubHandler();
  }

  @Test
  void testCheckPubDTO() {
    pubHandlerLoader.checkPubDTO();
  }
}
