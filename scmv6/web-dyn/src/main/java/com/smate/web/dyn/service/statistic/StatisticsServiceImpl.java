package com.smate.web.dyn.service.statistic;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.dyn.dao.DynamicMsgDao;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.url.HttpRequestUtils;
import com.smate.web.dyn.service.pub.PublicationService;

/**
 * 统计信息服务实现类
 * 
 * @author ajb
 * 
 */
@Service("statisticsService")
@Transactional(rollbackFor = Exception.class)
public class StatisticsServiceImpl implements StatisticsService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private DynamicMsgDao dynamicMsgDao;
  @Autowired
  private ProjectDao ProjectDao;
  @Value("${domainrol.https}")
  private String domainrol;

  @Override
  public Long findPsnId(Long actionKey, Integer resNodeId, Integer actionType) throws DynException {
    Long psnId = null;
    try {
      if (actionKey == null || actionKey <= 0) {
        return null;
      }
      switch (actionType) {
        case DynamicConstants.RES_TYPE_NORMAL:// 普通动态
          return dynamicMsgDao.getDynProducer(actionKey);
        // 成果
        case DynamicConstants.RES_TYPE_PUB:
          return publicationService.getPubOwner(actionKey);
        // 文献
        case DynamicConstants.RES_TYPE_REF:
          return publicationService.getPubOwner(actionKey);
        case DynamicConstants.RES_TYPE_FUND:
          return dynamicMsgDao.getDynProducer(actionKey);
        case DynamicConstants.RES_TYPE_PRJ:
          return ProjectDao.findPsnId(actionKey);
        case DynamicConstants.RES_TYPE_AGENCY:
          return dynamicMsgDao.getDynProducer(actionKey);
        default:
          logger.warn("该资源类型没有拥有者，actionKey=" + actionKey + " resNodeId=" + resNodeId + " actionType=" + actionType);
          return null;
      }
    } catch (Exception e) {
      logger.error(
          "根据资源ID，查找资源的拥有者出现异常，actionKey=" + actionKey + " resNodeId=" + resNodeId + " actionType=" + actionType, e);
    }
    return psnId;
  }


  /**
   * 分享机构 psn_id=1100000000812&type=2&ins_id=108&data_from=1&client_ip=192.168.173.43&platform=1
   * 
   * @param psnId
   * @param platform
   * @param insId
   * @return
   */
  public boolean addInsRecord(Long psnId, String platform, Long insId) {
    String SERVER_URL = domainrol + "/common/insindexsocial";
    // String SERVER_URL = "http://sieuat.scholarmate.com/common/insindexsocial";
    String ip = Struts2Utils.getRemoteAddr();
    StringBuilder sb = new StringBuilder();
    sb.append("psn_id=" + psnId);
    sb.append("&ins_id=" + insId);
    sb.append("&platform=" + platform);
    sb.append("&client_ip=" + ip);
    sb.append("&type=2");
    sb.append("&data_from=2");
    String result = HttpRequestUtils.sendPost(SERVER_URL, sb.toString());
    if (StringUtils.isNotBlank(result) && JacksonUtils.isJsonString(result)) {
      Map<String, String> resultMap = JacksonUtils.jsonToMap(result);
      if (resultMap != null && resultMap.get("status") != null && resultMap.get("status").equalsIgnoreCase("success")) {
        return true;
      }
    }
    return false;
  }
}
