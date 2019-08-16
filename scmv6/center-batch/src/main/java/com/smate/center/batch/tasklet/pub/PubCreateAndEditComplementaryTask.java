package com.smate.center.batch.tasklet.pub;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.PubSimple;
import com.smate.center.batch.service.pub.PubSimpleService;
import com.smate.center.batch.service.pub.ScholarPublicationXmlManager;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.center.batch.util.pub.PubSimpleUtils;
import com.smate.core.base.utils.exception.BatchTaskException;

public class PubCreateAndEditComplementaryTask extends BaseTasklet {

  @Autowired
  private PubSimpleService pubSimpleService;

  @Autowired
  private ScholarPublicationXmlManager scholarPublicationXmlManager;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    Long id = Long.valueOf(withData);
    PubSimple pubSimple = pubSimpleService.queryPubSimpleAndXml(id);

    if (pubSimple == null) {
      return DataVerificationStatus.NULL;
    }

    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    Long id = Long.parseLong(String.valueOf(jobContentMap.get("msg_id")));
    // 仍然检查版本号，如果新建完pub，立即又对pub进行修改，接在新建pub后complementaryTask就可以先取消；等完成修改后再完成后续补充任务
    Long version = Long.parseLong(String.valueOf(jobContentMap.get("version")));

    Long groupId = Long.parseLong(String.valueOf(jobContentMap.get("groupId")));

    String actionType = String.valueOf(jobContentMap.get("actionType"));
    PubSimple pubSimple = pubSimpleService.queryPubSimpleAndXml(id);
    if (pubSimple == null) {
      return;
    }

    pubSimple.setGroupId(groupId);

    // 版本不一致,或者被删除,取消处理
    if (!pubSimple.getSimpleVersion().equals(version) || PubSimpleUtils.checkStatus(pubSimple)) {
      return;
    }
    this.scholarPublicationXmlManager.createOrUpdateXmlComplementaryProcesses(actionType, pubSimple.getPubType(),
        pubSimple.getArticleType(), pubSimple);
  }

}
