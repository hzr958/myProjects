package com.smate.center.task.service.sns.quartz;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.PubAssignSyncMessage;
import com.smate.center.task.service.rcmd.quartz.PublicationConfirmService;
import com.smate.center.task.single.constants.PublicationArticleType;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.service.pub.ScholarPublicationXmlManager;
import com.smate.core.base.utils.constant.PubXmlConstants;

@Service("pubConfirmSyncService")
@Transactional(rollbackFor = Exception.class)
public class PubConfirmSyncServiceImpl implements PubConfirmSyncService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ScholarPublicationXmlManager scholarPublicationXmlManager;
  @Autowired
  private PubConfirmRecordService pubConfirmRecordService;
  @Autowired
  private PublicationConfirmService publicationConfirmService;

  @Value("${realTimeDyn.restful.url}")
  private String initNewDynUrl;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @Override
  public Long receiveSyncPub(PubAssignSyncMessage message) {
    try {
      PubXmlDocument doc = new PubXmlDocument(message.getPubXml());
      doc.cleanPubMembersPmId();
      // 重置dup_pub_id
      doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dup_pub_id", "");
      doc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "dup_pub_id", "");
      // 设置当前用户标记位owner=1
      Element e = (Element) doc
          .getNode(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH + "[@member_psn_id=" + message.getPsnId() + "]");
      if (e != null) {
        e.addAttribute("owner", "1");
      }
      Long confirmPubId = this.scholarPublicationXmlManager.syncPubConfirmXmlFromIns(message.getInsId(),
          message.getPsnId(), message.getInsPubId(), doc, PublicationArticleType.OUTPUT);
      Long recordId = pubConfirmRecordService.savePubConfirmRecord(message.getPsnId(), message.getInsId(),
          message.getInsPubId(), confirmPubId);

      try {
        publicationConfirmService.setPubConfirmSuccess(message.getPsnId(), confirmPubId, message.getInsPubId(),
            message.getInsId());
        // 标记同步成功
        pubConfirmRecordService.setSyncRcmdStatus(recordId, 1);
      } catch (Exception e1) {
        logger.error("远程调用推荐系统标记成果已经认领出现异常：psnid=" + message.getPsnId() + " rolpubid=" + message.getInsPubId(), e1);
        pubConfirmRecordService.setSyncRcmdStatus(recordId, 9);
      }

      // 产生动态
      if (confirmPubId != null && confirmPubId != 0L && initNewDynUrl != null) {
        Map<String, Object> dynMap = new HashMap<String, Object>();
        dynMap.put("pubId", confirmPubId);
        dynMap.put("psnId", message.getPsnId());
        dynMap.put("dynType", "B3TEMP");
        dynMap.put("resType", 1);
        // 得到restful的返回值;注意要返回Object类型，如果返回String类型，就会导致字符串被多次转译,转化为map的时候报错
        try {
          restTemplate.postForObject(initNewDynUrl, dynMap, Object.class);
        } catch (Exception e1) {
          logger.error("确认成果(单位指派成果导入成果到我的成果库添加动态出现异常)-生成动态出错pubId=" + confirmPubId, e1);
        }
      }
      return confirmPubId;
    } catch (DocumentException e) {
      throw new ServiceException("确认成果XML同步：XMl解析失败:" + message.getPubXml(), e);
    } catch (Exception e) {
      throw new ServiceException("确认成果XML同步失败.", e);
    }

  }

}
