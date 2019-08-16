package com.smate.center.batch.service.mail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.constant.InsideMessageConstants;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.center.batch.service.pub.PublicationService;
import com.smate.center.batch.service.pub.archiveFiles.ArchiveFilesService;
import com.smate.center.batch.util.pub.PublicationDetailFrom;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 短信－回复全文请求站内信.
 * 
 * @author pwl
 * 
 */
@Service("fulltextReqInsideMsgParamBuilder")
public class FulltextReqInsideMsgParamBuilder extends CommonInsideMailParamBuilder implements InsideMailParamBuilder {


  @Autowired
  PublicationService publicationService;
  @Autowired
  private ArchiveFilesService archiveFilesService;
  @Autowired
  EtemplateDealUrlMethod etemplateDealUrlMethod;
  @Autowired
  PersonManager personManager;
  @Autowired
  private FileService fileService;
  @Autowired
  private SysDomainConst sysDomainConst;

  @Override
  public Map<String, Object> builderParam(Message message, List<Long> receiverIdList) throws ServiceException {
    try {
      Map<String, Object> map = this.buildCommonParam(message, receiverIdList);
      // 全文请求使用新模板
      map.put("template", "person_reply_fulltext_request_${locale}.ftl");
      map.put("isSendMsgMail", 0);
      map.put("msgType", String.valueOf(InsideMessageConstants.MSG_TYPE_REPLAY_FULLTEXT_REQUEST));

      // 得到成果信息
      Map<String, String> msgParams = JacksonUtils.jsonMapUnSerializer(message.getJsonParam());
      Long pubId = Long.valueOf(msgParams.get("resId"));
      Map<String, String> pubTitleMap = publicationService.getPubTitleById(pubId);

      if (MapUtils.isNotEmpty(pubTitleMap)) {
        map.put("enPubTitle", pubTitleMap.get("enTitle"));
        map.put("zhPubTitle", pubTitleMap.get("zhTitle"));
      } else {
        map.put("enPubTitle", "");
        map.put("zhPubTitle", "");
      }
      map.put("authorNames", pubTitleMap.get("authorNames"));
      map.put("briefDesc", pubTitleMap.get("briefDesc"));
      map.put("briefDescEn", pubTitleMap.get("briefDescEn"));

      // 发件人头衔
      Person person = personManager.getPersonByRecommend(Long.valueOf(message.getPsnId()));

      map.put("frdTitlo", StringUtils.isBlank(person.getViewTitolo()) ? null : person.getViewTitolo());
      map.put("frdEnName", message.getPsnLastName());
      map.put("frdName", message.getPsnName());
      // 主题
      String des3Id = ServiceUtil.encodeToDes3(pubId + "," + PublicationDetailFrom.FULLTEXT_REQUEST + ","
          + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
      String enTitle = "Reply to your fulltext request";
      String title = "回复你的全文请求";
      map.put("enTitle", enTitle);
      map.put("enSubject", message.getPsnLastName() + " uploaded full text for you" + "：" + pubTitleMap.get("enTitle"));
      map.put("title", title);
      map.put("zhSubject", message.getPsnName() + "为你上传了论文全文" + ":" + pubTitleMap.get("zhTitle"));

      String content = "";
      // 后台任务，应该获取用户xml记录的当时操作语言环境，而不是当前环境。如果为空，默认为中文
      String locale =
          StringUtils.isNotBlank(String.valueOf(msgParams.get("locale"))) ? String.valueOf(msgParams.get("locale"))
              : "zh_CN";
      if ("zh_CN".equals(locale)) {
        content =
            "你好！<p style=\"padding-left:2em;\">已为你上传了\"<span class=\"notPrintLinkSpan_title\" style=\"cursor:pointer\" onclick=\"javascript:ScholarView.viewPubDetail('"
                + des3Id + ",1',this)\"><font color=\"#005eac\">"
                + (StringUtils.isBlank(ObjectUtils.toString(map.get("zhPubTitle"))) ? map.get("enPubTitle")
                    : map.get("zhPubTitle"))
                + "</font></span>\"的全文。</p>";
      } else {
        content =
            "Dear!<p style=\"padding-left:2em;\">uploaded the full-text of publication \"<span class=\"notPrintLinkSpan_title\" style=\"cursor:pointer\" onclick=\"javascript:ScholarView.viewPubDetail('"
                + des3Id + ",1',this)\"><font color=\"#005eac\">"
                + (StringUtils.isBlank(ObjectUtils.toString(map.get("enPubTitle"))) ? map.get("zhPubTitle")
                    : map.get("enPubTitle"))
                + "</font></span>\" for you.</p>";
      }
      map.put("content", content);

      String domainUrl = sysDomainConst.getSnsDomain();

      // 发件人主页
      String frdUrl =
          etemplateDealUrlMethod.getFrdUrl(null, Long.valueOf(message.getReceivers()), message.getPsnId(), null, 0);
      map.put("frdUrl", frdUrl);

      // 成果详情
      String pubUrl =
          domainUrl + "/scmwebsns/publication/view?des3Id=" + des3Id + "," + SecurityUtils.getCurrentAllNodeId().get(0);
      map.put("pubsUrl", pubUrl);

      map.put("mailSetUrl", "/scmwebsns/user/setting/getMailTypeList");

      // 下载全文按钮跳转的链接
      Map<String, String> paramMap = JacksonUtils.jsonMapUnSerializer(message.getJsonParam());
      int resNode = NumberUtils.toInt(paramMap.get("resNode"), SecurityUtils.getCurrentAllNodeId().get(0));
      Long resId = NumberUtils.toLong(paramMap.get("resId"));
      Map<String, String> fullTextMap = new HashMap<String, String>();
      // 与旧系统区别，需要去PubDataStore中提前获取xml中fulltext信息，此时后台任务可能没有执行完毕
      fullTextMap = this.publicationService.getPubFullTextInfoFromPubDataStore(resId);
      String fullTextId = fullTextMap.get("fullTextId");
      if (StringUtils.isNotBlank(fullTextId)) {
        ArchiveFile archiveFile = this.archiveFilesService.getArchiveFile(Long.valueOf(fullTextId));
        JSONArray jsonArray = new JSONArray();
        JSONObject fulltext = new JSONObject();

        String fullTextUrl = domainUrl + "/" + ServiceConstants.DIR_STATIONFILES_UPFILE
            + fileService.getFilePath(archiveFile.getFilePath());
        fulltext.accumulate("fileId", "");
        fulltext.accumulate("attachFileId", fullTextId);
        fulltext.accumulate("des3AttachFileId", ServiceUtil.encodeToDes3(fullTextId));
        fulltext.accumulate("fileName", fullTextMap.get("fullTextName"));
        fulltext.accumulate("downloadUrl", fullTextUrl);
        jsonArray.add(fulltext);
        JSONObject extOtherInfoJson = JSONObject.fromObject(map.get("extOtherInfo"));
        extOtherInfoJson.accumulate("fileList", jsonArray.toString());
        map.put("extOtherInfo", extOtherInfoJson.toString());
        map.put("domainUrl", domainUrl);
      }

      return map;
    } catch (Exception e) {
      logger.error("短信－构造普通站内信所需参数出现异常：", e);
      throw new ServiceException(e);
    }
  }
}
