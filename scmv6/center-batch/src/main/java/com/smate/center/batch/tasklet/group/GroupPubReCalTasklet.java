package com.smate.center.batch.tasklet.group;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.batch.model.sns.pub.GrpPubs;
import com.smate.center.batch.service.pub.GrpPubsService;
import com.smate.center.batch.service.pubfulltexttoimage.ScmPubXmlService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 群组信息改变后，更新群组成果信息：1计算群组成果与群组的关键词相同数（相关性），2成果重新标注（项目群组批准号与成果基金资助信息）
 * 
 * 
 **/
public class GroupPubReCalTasklet extends BaseTasklet {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpPubsService grpPubsService;
  @Autowired
  private ScmPubXmlService scmPubXmlService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    Long groupId = Long.parseLong(withData);
    if (groupId == null || groupId == 0L) {
      return DataVerificationStatus.NULL;
    }
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    try {
      Long groupId = Long.parseLong(String.valueOf(jobContentMap.get("msg_id")));
      List<GrpPubs> grpPubList = grpPubsService.getGrpPubsList(groupId);
      if (CollectionUtils.isEmpty(grpPubList)) {
        return;
      }
      for (GrpPubs grpPubs : grpPubList) {

        Map<String, Object> map = this.grpPubsService.getPubDetails(grpPubs.getPubId());
        Integer pubShareCount = this.grpPubsService.getNewPubGroupShareKwCount(grpPubs, map);
        Integer labeled = this.grpPubsService.groupNewPubIsLabeled(grpPubs, map);// 0未标注；1已标注

        if (pubShareCount != null) {
          grpPubs.setRelevance(pubShareCount);
        }

        if (labeled != null) {
          grpPubs.setLabeled(labeled);
        }

        this.grpPubsService.saveGrpPubs(grpPubs);
      }
    } catch (Exception e) {
      throw new BatchTaskException("群组成果相关性更新失败， 群组Id :" + String.valueOf(jobContentMap.get("msg_id")), e);
    }
  }

}
