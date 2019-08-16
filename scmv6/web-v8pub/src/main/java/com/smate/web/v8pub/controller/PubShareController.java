package com.smate.web.v8pub.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.service.sns.PubShareService;
import com.smate.web.v8pub.vo.PubShareVo;

@Controller
public class PubShareController {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubShareService pubShareService;

  /**
   * 分享成果给好友
   * 
   * @return
   */
  @RequestMapping(value = "/pub/ajaxSendSharePubMail", method = RequestMethod.POST)
  @ResponseBody
  public String sharePubToPsn(PubShareVo pubShareVo) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      pubShareVo.setPsnId(SecurityUtils.getCurrentUserId());
      if (pubShareVo.getPsnId() != null && pubShareVo.getPsnId() != 0L
          && StringUtils.isNoneBlank(pubShareVo.getDes3PubIds())
          && StringUtils.isNoneBlank(pubShareVo.getReceivers())) {
        String[] receiverStrArry = pubShareVo.getReceivers().split(",");
        int i = 0;
        for (String psnIdOrMail : receiverStrArry) {// 两套逻辑，1.传递过来的收件人为psnId，2，传递过来的收件人为站外人员的邮箱地址（直接发送邮件，有可能传递过来的包含psn_id和邮件地址
          i++;
          pubShareService.sendShareEmail(pubShareVo, psnIdOrMail, i);
        }
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      logger.error(
          "分享成果到好友出错， fundId = " + pubShareVo.getDes3PubIds() + ", sharePsnId = " + SecurityUtils.getCurrentUserId(),
          e);
      map.put("result", "error");
    }
    return JacksonUtils.jsonObjectSerializer(map);
  }

}
