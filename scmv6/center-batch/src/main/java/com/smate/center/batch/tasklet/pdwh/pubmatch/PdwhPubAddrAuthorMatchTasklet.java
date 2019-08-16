package com.smate.center.batch.tasklet.pdwh.pubmatch;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.pubimport.PdwhPubAddrAuthorMatchService;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.v8pub.dom.PubMemberBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

/**
 * 基准库成果地址和作者信息匹配任务链
 * 
 * @author LIJUN
 * @date 2018年3月19日
 */
public class PdwhPubAddrAuthorMatchTasklet extends BaseTasklet {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPubAddrAuthorMatchService pdwhPubAddrAuthorMatchService;
  @Autowired
  private PdwhPublicationService pdwhPublicationService;


  @Override
  public DataVerificationStatus dataVerification(String msgId) throws BatchTaskException {

    if (StringUtils.isBlank(msgId) || NumberUtils.toLong(msgId) <= 0) {
      return DataVerificationStatus.FALSE;
    }
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    Long pubId = NumberUtils.toLong(jobContentMap.get("msg_id").toString());
    String operate = String.valueOf(jobContentMap.get("operate_type"));
    PubPdwhDetailDOM pdwhPub = pdwhPublicationService.getFullPdwhPubInfoById(pubId);
    List<PubMemberBean> members = pdwhPub.getMembers();
    if (CollectionUtils.isNotEmpty(members) && members.size() > 2000) {
      throw new BatchTaskException("作者数大于10000，先不匹配");
    }
    if (pdwhPub == null) {
      logger.error("基准库成果地址和作者信息匹配任务链,获取到的成果为空，pub_id:" + pubId);
    }
    try {
      pdwhPubAddrAuthorMatchService.saveOrUpdateData(pdwhPub, operate);
      // 同时要实时指派到人，即pub_assign_log表要有记录
      pdwhPublicationService.saveTmpTaskInfoRecord(pubId);
    } catch (Exception e) {
      logger.error("基准库成果地址和作者信息匹配任务链出错,pub_id：" + pubId, e);
      throw new BatchTaskException(e);
    }
  }

}
