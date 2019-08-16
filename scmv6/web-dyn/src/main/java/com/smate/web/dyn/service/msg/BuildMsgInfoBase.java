package com.smate.web.dyn.service.msg;

import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.url.RestUtils;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.form.msg.mobile.MobileMsgShowForm;
import com.smate.web.dyn.model.msg.MsgRelation;
import com.smate.web.dyn.model.msg.MsgShowInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class BuildMsgInfoBase implements BuildMsgInfoService {
  @Resource
  private PsnProfileUrlDao psnProfileUrlDao;
  @Resource
  private PersonDao personDao;
  @Value("${domainscm}")
  public String domainscm;
  @Resource
  public RestTemplate restTemplate;

  /**
   * 构建数据显示对象
   * 
   * @param parameter
   */
  public abstract void buildMsgShowInfo(MsgShowForm form, MsgRelation m) throws Exception;

  /**
   * 构建Msg.psnInfo
   */
  public void buildPsnInfo(MsgShowInfo msi, Person p, String subName) {
    if (p == null) {
      return;
    }
    if ("sender".equals(subName)) {
      msi.setSenderAvatars(p.getAvatars());
      msi.setSenderEnName(p.getEnName());
      msi.setSenderZhName(p.getZhName());
      PsnProfileUrl profileUrl = psnProfileUrlDao.get(p.getPersonId());
      if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
        msi.setSenderShortUrl(domainscm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl());
      } else {
        String psnUrl =
            domainscm + "/psnweb/homepage/show?des3PsnId=" + Des3Utils.encodeToDes3(p.getPersonId().toString());
        msi.setSenderShortUrl(psnUrl);
      }
    } else {
      msi.setReceiverAvatars(p.getAvatars());
      msi.setReceiverEnName(p.getEnName());
      msi.setReceiverZhName(p.getZhName());
      PsnProfileUrl profileUrl = psnProfileUrlDao.get(p.getPersonId());
      if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
        msi.setReceiverShortUrl(profileUrl.getPsnIndexUrl());
      } else {
        String psnUrl =
            domainscm + "/psnweb/homepage/show?des3PsnId=" + Des3Utils.encodeToDes3(p.getPersonId().toString());
        msi.setReceiverShortUrl(psnUrl);
      }
    }
  }

  /**
   * 移动端-构建数据显示对象
   * 
   * @param form
   * @param msgRelation
   */
  public void buildMobileMsgShowInfo(MobileMsgShowForm form, MsgRelation msgRelation) throws Exception {

  }

  public Map<String, Object> findPubInfoByPubId(Long pubId) {
    Map<String, Object> pubInfoMap = new HashMap<>();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPubId(pubId);
    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    pubQueryDTO.setServiceType(V8pubQueryPubConst.QUERY_PUB_BY_PUB_ID_SERVICE);
    Map<String, Object> remoteInfo =
        (Map<String, Object>) RestUtils.getRemoteInfo(pubQueryDTO, SERVER_URL, restTemplate);
    List<Map<String, Object>> resultList = null;
    if (remoteInfo.get("status").toString().equalsIgnoreCase("success")) {
      resultList = (List<Map<String, Object>>) remoteInfo.get("resultList");
      if (CollectionUtils.isNotEmpty(resultList)) {
        pubInfoMap = resultList.get(0);
      }
    }
    return pubInfoMap;
  }
}
