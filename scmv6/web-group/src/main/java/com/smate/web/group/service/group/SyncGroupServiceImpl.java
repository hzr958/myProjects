package com.smate.web.group.service.group;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.group.form.GroupPsnForm;

/**
 * 同步群组信息实现类
 * 
 * @author Administrator
 *
 */
@Service("syncGroupService")
@Transactional(rollbackOn = Exception.class)
public class SyncGroupServiceImpl implements SyncGroupService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;

  @Override
  public Object syncGroupInfo(GroupPsnForm form) throws Exception {
    Map<String, Object> mapData = new HashMap<String, Object>();
    mapData.put("openid", form.getOpenId());
    mapData.put("token",
        ((StringUtils.isNotBlank(form.getFromSys()) && form.getFromSys().length() != 16) ? form.getFromSys()
            : "00000000") + "csg53dfk");
    mapData.put("groupId", form.getGroupId());
    mapData.put("data", buildGroupDataParameter(form));
    return restTemplate.postForObject(SERVER_URL, mapData, Object.class);
  }

  /**
   * 构造创建群组并保存第三方系统项目信息的 xml
   */
  private Object buildGroupDataParameter(GroupPsnForm form) {
    // form.setGroupName(form.getPrjName());//群组名称使用项目名称
    Map<String, Object> map = new HashMap<String, Object>();
    String syncXml = "<root><groupPsn><groupCategory>"
        + (StringUtils.isNotBlank(form.getGroupCategory()) ? form.getGroupCategory() : "")
        + "</groupCategory><groupName>"// 群组分类，10兴趣群组，11项目群组
        + (StringUtils.isNotBlank(form.getGroupName()) ? form.getGroupName() : "") + "</groupName><groupDescription>"// 群组简介
        + (StringUtils.isNotBlank(form.getGroupDescription()) ? form.getGroupDescription() : "")
        + "</groupDescription><groupImgUrl>"// 群组头像
        + (StringUtils.isNotBlank(form.getGroupImgUrl()) ? form.getGroupImgUrl() : "") + "</groupImgUrl><openType>"// 群组开放类型
        + (StringUtils.isNotBlank(form.getOpenType()) ? form.getOpenType() : "") + "</openType><keyWords>"// 群组关键词
        + (StringUtils.isNotBlank(form.getKeyWords()) ? form.getKeyWords() : "") + "</keyWords><keyWords1>"
        + (StringUtils.isNotBlank(form.getKeyWords1()) ? form.getKeyWords1() : "") + "</keyWords1><disciplines>"// 群组的学科领域
        + (StringUtils.isNotBlank(form.getDisciplines()) ? form.getDisciplines() : "")
        + "</disciplines></groupPsn></root>";
    map.put("syncXml", syncXml);
    map.put("psnId", form.getPsnId());
    return JacksonUtils.mapToJsonStr(map);
  }


}
