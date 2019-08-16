package com.smate.center.batch.service.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.smate.center.batch.constant.InsideMessageConstants;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 短信－请求某人帮助检索成果.
 * 
 * @author pwl
 * 
 */
@Service("reqSearchPubParamBulider")
public class ReqSearchPubParamBulider extends CommonInsideMailParamBuilder implements InsideMailParamBuilder {

  @Override
  public Map<String, Object> builderParam(Message message, List<Long> receiverIdList) throws ServiceException {
    Map<String, Object> map = this.buildCommonParam(message, receiverIdList);
    map.put("msgType", String.valueOf(InsideMessageConstants.MSG_TYPE_REQUEST_COL_PUB));
    map.put("template", "Request_SearchPub_Mail_Template_${locale}.ftl");
    map.put("isSendMsgMail", 0);
    map.put("zhSubject", map.get("senderZhName") + "请求您为其检索个人成果");
    map.put("enSubject", map.get("senderEnName") + " request you to collect publication");
    List<String> storageList = this.getStorage(message.getContent());
    String zhStorages = "";
    String enStorages = "";
    if (CollectionUtils.isNotEmpty(storageList)) {
      zhStorages = StringUtils.join(storageList, "，");
      enStorages = StringUtils.join(storageList, " , ");
    }
    map.put("zhStorages", zhStorages);
    map.put("enStorages", enStorages);
    // map.put("recommendReason", storages);
    String pubUrl = "/publication/friend/collect?psnId="
        + ServiceUtil.encodeToDes3(String.valueOf(SecurityUtils.getCurrentUserId())) + "&locale=";
    map.put("pubUrl", pubUrl);
    return map;
  }

  private List<String> getStorage(String sourseStr) {
    List<String> result = new ArrayList<String>();
    String[] sourseArr = sourseStr.split("<br/>");
    if (sourseArr.length > 0) {
      for (String icontent : sourseArr) {
        if (icontent.indexOf(":") > 0 || icontent.indexOf("：") > 0) {
          Integer startIndex = (icontent.indexOf(":") > 0) ? icontent.indexOf(":") : icontent.indexOf("：");
          String iCon = icontent.substring(startIndex + 1, icontent.length());
          if (iCon != null && !"".equals(iCon.trim()))
            result.add(iCon);
        }
      }
    }
    return result;
  }

}
