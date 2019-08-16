package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.pdwh.quartz.TemTaskPdwhBriefDao;
import com.smate.center.task.model.pdwh.quartz.TemTaskPdwhBrief;
import com.smate.center.task.single.dao.solr.PdwhPublicationDao;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.util.pub.PdwhBriefUtils;

@Service("updatePdwhPubBrieService")
@Transactional(rollbackFor = Exception.class)
public class UpdatePdwhPubBrieServiceImpl implements UpdatePdwhPubBrieService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private TemTaskPdwhBriefDao temTaskPdwhBriefDao;

  @Override
  public List<TemTaskPdwhBrief> getUpdatePubIdList(Integer size) throws Exception {
    return temTaskPdwhBriefDao.getUpdatePublist(size);
  }

  @Override
  public void updatePdwhPubBrie(List<TemTaskPdwhBrief> pubList) throws Exception {
    if (CollectionUtils.isEmpty(pubList)) {
      return;
    }
    for (TemTaskPdwhBrief tempPub : pubList) {
      try {
        String pubxmlStr = pdwhPubXmlDao.getXmlStringByPubId(tempPub.getPubId());
        if (StringUtils.isNotBlank(pubxmlStr)) {
          String zhBrief = this.generateBrief(pubxmlStr, "zh");
          String enBrief = this.generateBrief(pubxmlStr, "en");
          pdwhPublicationDao.updateBrief(zhBrief, enBrief, tempPub.getPubId());
          saveSuccessMsg(tempPub);// 更新状态
        } else {
          saveErrorMsg(tempPub, "XML获取为空 pub_id=" + tempPub.getPubId());
        }
      } catch (Exception e) {
        String errorStr = e.toString();
        if (e.toString().length() > 700) {
          errorStr = e.toString().substring(0, 700);
        }
        saveErrorMsg(tempPub, errorStr);
        logger.error("更新基准库成果的brief_desc字段出错，pub_id=" + tempPub.getPubId(), e);
        continue;
      }
    }
  }

  private String generateBrief(String xmlString, String language) {
    String brief = "";
    try {
      PubXmlDocument doc = new PubXmlDocument(xmlString);
      brief = PdwhBriefUtils.getBrief(doc, language);

    } catch (DocumentException e) {
      logger.error("pdwhPubImport 生成成果brief错误，" + e);
    }

    return brief;
  }

  /**
   * 保存错误信息
   * 
   * @param tempPub
   * @param errMsg
   */
  private void saveErrorMsg(TemTaskPdwhBrief tempPub, String errMsg) {
    tempPub.setStatus(2);
    errMsg = ObjectUtils.toString(errMsg);
    if (errMsg.length() > 700) {
      errMsg = errMsg.substring(0, 700);
    }
    tempPub.setErrMsg(errMsg);
    temTaskPdwhBriefDao.save(tempPub);
  }

  /**
   * 保存成功信息
   * 
   * @param tempPub
   */
  private void saveSuccessMsg(TemTaskPdwhBrief tempPub) {
    tempPub.setStatus(1);
    temTaskPdwhBriefDao.save(tempPub);
  }
}
