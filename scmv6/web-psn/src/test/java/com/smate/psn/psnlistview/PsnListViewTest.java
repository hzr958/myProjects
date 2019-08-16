package com.smate.psn.psnlistview;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.service.profile.psnListView.KeywordIdentificPsnListService;
import com.smate.web.psn.service.profile.psnListView.PsnListViewService;
import com.smate.web.psn.service.profile.psnListView.PsnListViewServiceImpl;

public class PsnListViewTest {

  public static void main(String[] args) {
    PsnListViewForm form = new PsnListViewForm();
    form.setPsnId(1L);
    form.setServiceType("KwIdentific");
    // form.setDiscId("2");
    PsnListViewServiceImpl service = new PsnListViewServiceImpl();
    KeywordIdentificPsnListService kwWervice = new KeywordIdentificPsnListService();
    Map<String, PsnListViewService> serviceMap = new HashMap<String, PsnListViewService>();
    serviceMap.put("KwIdentific", kwWervice);
    service.setServiceMap(serviceMap);
    try {
      service.doGetPsnListViewInfo(form);
    } catch (ServiceException e) {
      System.out.println("出错了");
    }

  }

  @Test
  public void buildPsnJsonStr() {
    Person psn = new Person();
    psn.setPersonId(1000000727634L);
    psn.setName("夜凌雪2");
    psn.setFirstName("ye");
    psn.setLastName("ling xue");
    psn.setInsId(511500L);
    psn.setInsName("测试单位");
    psn.setDepartment("测试单位");
    psn.setPosition("码农1");
    psn.setPosId(123L);
    psn.setEmail("754446784@qq.com");
    psn.setSex(1);
    psn.setTel("13266893710");
    psn.setRegionId(511500L);
    String psnJson = JacksonUtils.jsonObjectSerializer(psn);
    System.out.println(psnJson);
    // \"psnId\":\"1000000727634\",
    String data =
        "{\"psnData\":\"{\\\"personId\\\":1000000727634,\\\"personDes3Id\\\":\\\"gdC9pv0cs%2BtFxfgIimt6bg%3D%3D\\\",\\\"firstName\\\":\\\"ye\\\",\\\"lastName\\\":\\\"ling xue\\\",\\\"name\\\":\\\"夜凌雪2\\\",\\\"tel\\\":\\\"13266893710\\\",\\\"email\\\":\\\"754446784@qq.com\\\",\\\"regionId\\\":511500,\\\"sex\\\":1,\\\"avatars\\\":\\\"/resmod/smate-pc/img/logo_psndefault.png\\\",\\\"position\\\":\\\"码农1\\\",\\\"posId\\\":123,\\\"insId\\\":511500,\\\"insName\\\":\\\"测试单位\\\",\\\"isRegisterR\\\":false,\\\"isPrivate\\\":0,\\\"isEmailVerify\\\":false,\\\"department\\\":\\\"测试单位\\\",\\\"isFriend\\\":false,\\\"isMyFriend\\\":0,\\\"psnName\\\":\\\"夜凌雪2\\\",\\\"zhName\\\":\\\"夜凌雪2\\\",\\\"enName\\\":\\\"ye ling xue\\\"}\"}";
    System.out.println(JacksonUtils.isJsonString(data));
    Map<String, Object> testMap = new HashMap<String, Object>();
    testMap.put("psnData", psnJson);
    String mapJsonStr = JacksonUtils.mapToJsonStr(testMap);
    System.out.println(mapJsonStr);
    Map<String, Object> map = JacksonUtils.jsonToMap(data);
  }

}
