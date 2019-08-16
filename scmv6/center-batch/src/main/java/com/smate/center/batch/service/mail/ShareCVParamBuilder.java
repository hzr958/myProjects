package com.smate.center.batch.service.mail;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.smate.center.batch.constant.InsideMessageConstants;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Message;

/**
 * 短信－分享简历.
 * 
 * @author pwl
 * 
 */
@Service("shareCVParamBuilder")
public class ShareCVParamBuilder extends CommonInsideMailParamBuilder implements InsideMailParamBuilder {

  @Override
  public Map<String, Object> builderParam(Message message, List<Long> receiverIdList) throws ServiceException {
    try {
      Map<String, Object> map = this.buildCommonParam(message, receiverIdList);
      map.put("isProduceDynamic", 1);
      map.put("msgType", String.valueOf(InsideMessageConstants.MSG_TYPE_SHARE_CV));
      map.put("tmpId", InsideMessageConstants.MSG_TEMPLATE_INSIDE_SHARE_CV);
      map.put("notSysUserTemplate", "Email_Share_CV_NotSysUser_Template_${locale}.ftl");
      map.put("title", "分享简历");
      map.put("enTitle", "Share Resume");
      map.put("zhSubject", StringUtils.isBlank(message.getTitle()) ? message.getEnTitle() : message.getTitle());
      map.put("enSubject", StringUtils.isBlank(message.getEnTitle()) ? message.getTitle() : message.getEnTitle());
      map.put("recommendUrl", message.getRecommendUrl());
      map.put("cvName", message.getCvName());
      map.put("content", message.getContent());
      JSONObject extOtherInfoJson = JSONObject.fromObject(map.get("extOtherInfo"));
      JSONObject cvInfoJson = new JSONObject();
      cvInfoJson.accumulate("cvName", message.getCvName());
      if (StringUtils.isNotBlank(message.getPdfUrl()) || StringUtils.isNotBlank(message.getWordUrl())) {
        JSONArray attachFiles = new JSONArray();
        if (StringUtils.isNotBlank(message.getPdfUrl())) {
          JSONObject pdfCV = new JSONObject();
          map.put("pdfUrl", message.getPdfUrl());
          cvInfoJson.accumulate("pdfUrl", message.getPdfUrl());
          pdfCV.accumulate("fileId", "");
          pdfCV.accumulate("attachFileId", "");
          pdfCV.accumulate("des3AttachFileId", "");
          pdfCV.accumulate("fileName", message.getCvName() + ".pdf");
          pdfCV.accumulate("downloadUrl", message.getPdfUrl());
          attachFiles.add(pdfCV);
        }

        if (StringUtils.isNotBlank(message.getWordUrl())) {
          JSONObject wordCV = new JSONObject();
          map.put("wordUrl", message.getWordUrl());
          cvInfoJson.accumulate("wordUrl", message.getWordUrl());
          wordCV.accumulate("fileId", "");
          wordCV.accumulate("attachFileId", "");
          wordCV.accumulate("des3AttachFileId", "");
          wordCV.accumulate("fileName", message.getCvName() + ".doc");
          wordCV.accumulate("downloadUrl", message.getWordUrl());
          attachFiles.add(wordCV);
        }
        extOtherInfoJson.accumulate("fileList", attachFiles.toString());
      }
      extOtherInfoJson.accumulate("cvInfo", cvInfoJson);
      map.put("extOtherInfo", extOtherInfoJson.toString());
      return map;
    } catch (Exception e) {
      logger.error("构造分享简历所需参数出现异常：", e);
      throw new ServiceException(e);
    }
  }
}
