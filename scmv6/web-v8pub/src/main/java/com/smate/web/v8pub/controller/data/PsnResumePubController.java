package com.smate.web.v8pub.controller.data;



import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.service.sns.resume.PersonalResumePubService;
import com.smate.web.v8pub.vo.PersonalResumePubVO;

@Controller
public class PsnResumePubController {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonalResumePubService personalResumePubService;

  /**
   * 简历成果信息
   * 
   * @return
   */
  @RequestMapping("/data/pub/resume/pubinfo")
  @ResponseBody()
  public Object buildPsnResumePubInfo(@RequestBody Map<String, Object> map) {
    PersonalResumePubVO form = new PersonalResumePubVO();
    try {
      String des3PsnId = Objects.toString(map.get("des3PsnId"), "");
      String resumeId = Objects.toString(map.get("resumeId"), "");
      Integer moduleId = (Integer) map.get("moduleId");
      if (StringUtils.isNotBlank(des3PsnId) && moduleId != null) {
        form.setPsnId(NumberUtils.parseLong(Des3Utils.decodeFromDes3(des3PsnId), 0L));
        form.setModuleId(moduleId);
        form.setCvId(NumberUtils.parseLong(Des3Utils.decodeFromDes3(resumeId), 0L));

        personalResumePubService.buildPsnResumePubInfo(form);
      }
    } catch (Exception e) {
      logger.error("获取简历代表性成果出错， psnId = " + SecurityUtils.getCurrentUserId() + ", resumeId=" + form.getResumeId(), e);
    }
    return form.getPubInfoList();
  }

}
