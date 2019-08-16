package com.smate.web.v8pub.service.handler;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.exception.PubHandlerException;
import com.smate.web.v8pub.service.PubErrorMessageService;
import com.smate.web.v8pub.service.handler.PubHandlerServiceBaseBean.CheckConfig;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.handler.example.PubHandlerServiceExampleImpl;

class PubHandlerServiceBaseBeanTest extends MockitoBase {

  @InjectMocks
  private PubHandlerServiceExampleImpl pubHandlerServiceBaseBean;
  @Mock
  private ArrayList<CheckConfig> checkConfigList = new ArrayList<CheckConfig>();

  private PubDTO pubDTO = new PubDTO();
  @Mock
  private ApplicationContext applicationContext;
  @Mock
  private PubHandlerAssemblyService pubHandlerAssemblyService;
  @Mock
  private PubErrorMessageService pubErrorMessageService;

  @Test
  @DisplayName("pubHandle执行正常情况")
  void testPubHandle() {
    pubDTO.pubHandlerName = "test";
    pubDTO.pubId = 5L;
    List<Object> ASChain = new ArrayList<Object>();
    ASChain.add(pubHandlerAssemblyService);
    ASChain.add(pubHandlerAssemblyService);
    List<Object> subASChain = new ArrayList<Object>();
    subASChain.add(pubHandlerAssemblyService);
    ASChain.add(subASChain);

    CheckConfig checkConfig = new CheckConfig("pubId", Long.class);
    when(checkConfigList.size()).thenReturn(1);
    when(checkConfigList.get(0)).thenReturn(checkConfig);
    when(applicationContext.getBean(pubDTO.pubHandlerName, List.class)).thenReturn(ASChain);
    doNothing().doThrow(PubHandlerAssemblyException.class).doNothing().when(pubHandlerAssemblyService)
        .checkRebuildParameter(pubDTO);
    doNothing().doNothing().doNothing().when(pubHandlerAssemblyService).checkSourcesParameter(pubDTO);

    doNothing().when(pubErrorMessageService).save(pubDTO, "test");

    when(pubHandlerAssemblyService.excute(pubDTO)).thenReturn(new HashMap<String, String>()).thenReturn(null)
        .thenReturn(null);

    pubHandlerServiceBaseBean.pubHandle(pubDTO);
  }

  @Test
  @DisplayName("pubHandle执行异常情况")
  void testPubHandleExceptin() {
    pubDTO.pubHandlerName = "test";
    pubDTO.pubId = 5L;
    List<Object> ASChain = new ArrayList<Object>();
    ASChain.add(pubHandlerAssemblyService);

    CheckConfig checkConfig = new CheckConfig("pubId", Long.class);
    when(checkConfigList.size()).thenReturn(1);
    when(checkConfigList.get(0)).thenReturn(checkConfig);
    when(applicationContext.getBean(pubDTO.pubHandlerName, List.class)).thenReturn(ASChain);
    doThrow(PubHandlerAssemblyException.class).when(pubHandlerAssemblyService).checkSourcesParameter(pubDTO);

    Throwable exception = assertThrows(PubHandlerException.class, () -> {
      pubHandlerServiceBaseBean.pubHandle(pubDTO);
    });
    assertTrue(exception.getMessage().contains("必要参数校验不通过"));

  }

  @Test
  @DisplayName("pubHandle执行异常情况")
  void testPubHandleExceptin2() {
    pubDTO.pubHandlerName = "test";
    pubDTO.pubId = 5L;
    List<Object> ASChain = new ArrayList<Object>();
    ASChain.add(pubHandlerAssemblyService);

    CheckConfig checkConfig = new CheckConfig("pubId", Long.class);
    when(checkConfigList.size()).thenReturn(1);
    when(checkConfigList.get(0)).thenReturn(checkConfig);
    when(applicationContext.getBean(pubDTO.pubHandlerName, List.class)).thenReturn(ASChain);
    doNothing().when(pubHandlerAssemblyService).checkSourcesParameter(pubDTO);
    doNothing().when(pubHandlerAssemblyService).checkRebuildParameter(pubDTO);
    when(pubHandlerAssemblyService.excute(pubDTO)).thenThrow(PubHandlerAssemblyException.class);

    Throwable exception = assertThrows(PubHandlerException.class, () -> {
      pubHandlerServiceBaseBean.pubHandle(pubDTO);
    });
    assertTrue(exception.getMessage().contains("执行成果组装类业务出错"));

  }

  @Test
  @DisplayName("参数正确的情况")
  void testCheckParameterByConfig() {
    pubDTO.pubId = 5L;
    pubDTO.publishYear = 2016;
    pubDTO.title = "testlength";
    List<Object> test = new ArrayList<Object>();
    test.add("1");
    test.add("5");
    JSONArray jsonA = new JSONArray(test);
    pubDTO.members = jsonA;
    CheckConfig checkConfig = new CheckConfig("pubId", Long.class);
    CheckConfig checkConfig1 = new CheckConfig("pubId", Long.class, false);
    CheckConfig checkConfig2 = new CheckConfig("pubId", 0, 10, Long.class);
    CheckConfig checkConfig3 = new CheckConfig("pubId", false, 2, 10, Long.class);
    CheckConfig checkConfig4 = new CheckConfig("publishYear", false, 1901, 2018, Integer.class);
    CheckConfig checkConfig5 = new CheckConfig("title", false, 5, 100, String.class);
    CheckConfig checkConfig6 = new CheckConfig("members", false, 1, 3, JSONArray.class);
    CheckConfig checkConfig7 = new CheckConfig("publishDate", 1, 3, String.class);
    CheckConfig checkConfig8 = new CheckConfig("members", false, 4, 3, JSONArray.class);
    CheckConfig checkConfig9 = new CheckConfig("title", false, -4, 100, String.class);

    when(checkConfigList.size()).thenReturn(10);
    when(checkConfigList.get(0)).thenReturn(checkConfig);
    when(checkConfigList.get(1)).thenReturn(checkConfig1).thenReturn(checkConfig1);
    when(checkConfigList.get(2)).thenReturn(checkConfig2).thenReturn(checkConfig2);
    when(checkConfigList.get(3)).thenReturn(checkConfig3).thenReturn(checkConfig3);
    when(checkConfigList.get(4)).thenReturn(checkConfig4).thenReturn(checkConfig4);
    when(checkConfigList.get(5)).thenReturn(checkConfig5).thenReturn(checkConfig5);
    when(checkConfigList.get(6)).thenReturn(checkConfig6).thenReturn(checkConfig6);
    when(checkConfigList.get(7)).thenReturn(checkConfig7).thenReturn(checkConfig7);
    when(checkConfigList.get(8)).thenReturn(checkConfig8).thenReturn(checkConfig8);
    when(checkConfigList.get(9)).thenReturn(checkConfig9).thenReturn(checkConfig9);
    pubHandlerServiceBaseBean.checkParameterByConfig(pubDTO);
  }

  @Test
  @DisplayName("参数类型出错的情况")
  void testCheckParameterByConfig4() {
    pubDTO.pubId = 0L;
    CheckConfig checkConfig = new CheckConfig("pubId", false);
    when(checkConfigList.size()).thenReturn(1);
    when(checkConfigList.get(0)).thenReturn(checkConfig).thenReturn(checkConfig);
    Throwable exception = assertThrows(PubHandlerCheckParameterException.class, () -> {
      pubHandlerServiceBaseBean.checkParameterByConfig(pubDTO);
    });
    assertTrue(exception.getMessage().contains("类型不正确"));
  }

  @Test
  @DisplayName("参数非空出错的情况")
  void testCheckParameterByConfig3() {
    CheckConfig checkConfig = new CheckConfig("pubId", Long.class, false);
    when(checkConfigList.size()).thenReturn(1);
    when(checkConfigList.get(0)).thenReturn(checkConfig).thenReturn(checkConfig);
    Throwable exception = assertThrows(PubHandlerCheckParameterException.class, () -> {
      pubHandlerServiceBaseBean.checkParameterByConfig(pubDTO);
    });
    assertTrue(exception.getMessage().contains("不能为空"));
  }

  @Test
  @DisplayName("参数非空出出错的情况")
  void testCheckParameterByConfig6() {
    pubDTO.title = "";
    CheckConfig checkConfig1 = new CheckConfig("title", String.class, false);
    when(checkConfigList.size()).thenReturn(1);
    when(checkConfigList.get(0)).thenReturn(checkConfig1).thenReturn(checkConfig1);
    Throwable exception = assertThrows(PubHandlerCheckParameterException.class, () -> {
      pubHandlerServiceBaseBean.checkParameterByConfig(pubDTO);
    });
    assertTrue(exception.getMessage().contains("不能为空"));
  }

  @Test
  @DisplayName("参数名字不对出错")
  void testCheckParameterByConfig5() {
    pubDTO.pubId = 0L;
    CheckConfig checkConfig = new CheckConfig("pubIdd");
    when(checkConfigList.size()).thenReturn(1);
    when(checkConfigList.get(0)).thenReturn(checkConfig).thenReturn(checkConfig);
    Throwable exception = assertThrows(PubHandlerCheckParameterException.class, () -> {
      pubHandlerServiceBaseBean.checkParameterByConfig(pubDTO);
    });
    assertTrue(exception.getMessage().contains("参数对象出错"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"te", "testtest"})
  @DisplayName("参数范围不对情况")
  void testCheckParameterByConfig7(String candidate) {
    pubDTO.title = candidate;
    CheckConfig checkConfig = new CheckConfig("title", false, 3, 5, String.class);
    when(checkConfigList.size()).thenReturn(1);
    when(checkConfigList.get(0)).thenReturn(checkConfig).thenReturn(checkConfig);
    Throwable exception = assertThrows(PubHandlerCheckParameterException.class, () -> {
      pubHandlerServiceBaseBean.checkParameterByConfig(pubDTO);
    });
    assertTrue(exception.getMessage().contains("参数超出范围"));
  }

  @ParameterizedTest
  @ValueSource(longs = {12, 122222433243334L})
  @DisplayName("参数范围不对情况")
  void testCheckParameterByConfig8(Long candidate) {
    pubDTO.pubId = candidate;
    CheckConfig checkConfig = new CheckConfig("pubId", false, 18, 12000, Long.class);
    when(checkConfigList.size()).thenReturn(1);
    when(checkConfigList.get(0)).thenReturn(checkConfig).thenReturn(checkConfig);
    Throwable exception = assertThrows(PubHandlerCheckParameterException.class, () -> {
      pubHandlerServiceBaseBean.checkParameterByConfig(pubDTO);
    });
    assertTrue(exception.getMessage().contains("参数超出范围"));
  }

  @Test
  @DisplayName("参数范围不对情况")
  void testCheckParameterByConfig9() {
    List<Object> test = new ArrayList<Object>();
    test.add("1");
    test.add("5");
    JSONArray jsonA = new JSONArray(test);
    pubDTO.members = jsonA;
    CheckConfig checkConfig = new CheckConfig("members", false, 3, 12000, JSONArray.class);
    when(checkConfigList.size()).thenReturn(1);
    when(checkConfigList.get(0)).thenReturn(checkConfig).thenReturn(checkConfig);
    Throwable exception = assertThrows(PubHandlerCheckParameterException.class, () -> {
      pubHandlerServiceBaseBean.checkParameterByConfig(pubDTO);
    });
    assertTrue(exception.getMessage().contains("参数超出范围"));
    test.add("7");
    test.add("7");
    test.add("7");
    test.add("7");
    jsonA = new JSONArray(test);
    pubDTO.members = jsonA;
    checkConfig = new CheckConfig("members", false, 3, 5, JSONArray.class);
    when(checkConfigList.size()).thenReturn(1);
    when(checkConfigList.get(0)).thenReturn(checkConfig).thenReturn(checkConfig);
    exception = assertThrows(PubHandlerCheckParameterException.class, () -> {
      pubHandlerServiceBaseBean.checkParameterByConfig(pubDTO);
    });
    assertTrue(exception.getMessage().contains("参数超出范围"));

  }

  @Test
  void testAfterPropertiesSet() {
    try {
      pubHandlerServiceBaseBean.afterPropertiesSet();
    } catch (Exception e) {
      fail("There can be no exceptions");
    }
  }

  @Test
  void testSetApplicationContext() {
    pubHandlerServiceBaseBean.setApplicationContext(null);
  }

}
