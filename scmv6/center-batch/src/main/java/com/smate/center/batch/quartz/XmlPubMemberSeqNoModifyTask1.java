package com.smate.center.batch.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pub.pubtopubsimple.PubToPubSimpleErrorLog;
import com.smate.center.batch.model.sns.pub.PubDataStore;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.PubSimpleService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.exception.BatchTaskException;

public class XmlPubMemberSeqNoModifyTask1 {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 5000; // 每次刷新获取的个数
  private final static String PUB_MEMBER_MODIFY = "pub_member_modify";

  private Long startPubId = 1200006114474L;
  private Long endPubId = 1200006114478L;

  @Autowired
  private PubSimpleService pubSimpleService;

  @Autowired
  private CacheService cacheService;

  public void run() throws BatchTaskException {
    logger.debug("====================================XmlPubMemberSeqNoModifyTask1===开始运行");
    if (isRun() == false) {
      logger.debug("====================================XmlPubMemberSeqNoModifyTask1===开关关闭");
      return;
    } else {
      try {

        doRun();

      } catch (BatchTaskException e) {
        logger.error("XmlPubMemberSeqNoModifyTask1,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    try {


      Long lastPubId = (Long) this.cacheService.get(PUB_MEMBER_MODIFY, "XmlPubMemberSeqNoModifyTask1");

      if (lastPubId == null) {
        lastPubId = 0L;
      }
      lastPubId = 0L;
      logger.debug("===========================================XmlPubMemberSeqNoModifyTask1=========开始1");
      List<PubDataStore> pubList = pubSimpleService.getXmlPubMemberRefreshList(SIZE, lastPubId, startPubId, endPubId);
      logger.debug(
          "===========================================XmlPubMemberSeqNoModifyTask1  获取PubDataStore表数据=========2");

      if (CollectionUtils.isNotEmpty(pubList)) {

        Integer lastIndex = pubList.size() - 1;
        lastPubId = pubList.get(lastIndex).getPubId();

        this.cacheService.put(PUB_MEMBER_MODIFY, "XmlPubMemberSeqNoModifyTask1", lastPubId);

        for (PubDataStore pub : pubList) {
          Long pubId = pub.getPubId();
          try {
            if (StringUtils.isEmpty(pub.getData())) {
              throw new Exception("PubId = " + pubId + " 的xml为空");
            }
            PubXmlDocument xmlDocument = new PubXmlDocument(pub.getData());
            List<Element> pubMembers = new ArrayList<Element>();
            Element pubMemberEle = (Element) xmlDocument.getNode(PubXmlConstants.PUB_MEMBERS_XPATH);
            if (pubMemberEle == null) {
              continue;
            } else {
              pubMembers = pubMemberEle.elements();
              if (CollectionUtils.isEmpty(pubMembers)) {
                continue;
              }
            }
            // 重新赋值seqNo的顺序与xml中保持一致
            for (Integer i = 0; i < pubMembers.size(); i++) {
              Element node = pubMembers.get(i);

              if (node == null) {
                continue;
              }
              String memberPsnName = node.attributeValue("member_psn_name");
              if (!StringUtils.isBlank(memberPsnName)) {
                Attribute seqNo = node.attribute("seq_no");
                if (seqNo != null) {
                  node.remove(seqNo);
                }
                node.addAttribute("seq_no", i.toString());
              }
            }
            pub.setData(xmlDocument.getXmlString());
            this.pubSimpleService.savePubDataStore(pub);

          } catch (Exception e) {

            PubToPubSimpleErrorLog error = new PubToPubSimpleErrorLog();
            error.setPubId(pubId);
            String errorMsg = "更新PubMembers出错！==" + e.toString();
            error.setErrorMsg(errorMsg);
            logger.debug("XmlPubMemberSeqNoModifyTask1出错==============", e);
          }
        }
      } else {
        logger.debug(
            "===========================================XmlPubMemberSeqNoModifyTask1  没有获取到PubDataStore表数据!!!!============3");

      }



    } catch (Exception e) {
      logger.error("XmlPubMemberSeqNoModifyTask1,运行异常", e);
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    // return true;
    return false;
  }
}
