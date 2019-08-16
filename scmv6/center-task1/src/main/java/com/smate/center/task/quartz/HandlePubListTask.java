package com.smate.center.task.quartz;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.pdwh.pub.PdwhPubSourceDb;
import com.smate.center.task.model.rol.quartz.RolPubIdTmp;
import com.smate.center.task.model.rol.quartz.RolPubXml;
import com.smate.center.task.service.rol.quartz.HandlePubListService;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;

public class HandlePubListTask extends TaskAbstract {
  private final static Integer SIZE = 1000; // 每次刷新获取的个数
  @Autowired
  private HandlePubListService handlePubListService;

  public HandlePubListTask() {
    super();
  }

  public HandlePubListTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========HandlePubListTask 已关闭==========");
      return;
    }
    List<RolPubIdTmp> rolPubIdTmpList = handlePubListService.getRolPubId(SIZE);
    if (CollectionUtils.isEmpty(rolPubIdTmpList)) {
      logger.info(
          "===========================================HandlePubListTask  没有获取到rolPubIdTmp表数据!!!!============, time = "
              + new Date());
      return;
    }
    for (RolPubIdTmp rolPubIdTmp : rolPubIdTmpList) {
      try {
        Long pdwhPubId = handlePubListService.getPdwhPubId(rolPubIdTmp.getPubId());
        if (pdwhPubId == null) {
          logger.info("rolPubId" + rolPubIdTmp.getPubId() + "没有对应的pdwhPubId");
          handlePubListService.saveOptResult(rolPubIdTmp, 4);
          continue;
        }
        PdwhPubSourceDb pdwhPubSourceDb = handlePubListService.getPdwhPubSourceDb(pdwhPubId);
        if (pdwhPubSourceDb == null) {
          handlePubListService.saveOptResult(rolPubIdTmp, 4);
          continue;
        }
        RolPubXml rolPubXml = handlePubListService.getRolPubXml(rolPubIdTmp.getPubId());
        String rolXmlString = rolPubXml.getPubXml();
        if (StringUtils.isEmpty(rolXmlString)) {
          return;
        }
        try {
          PubXmlDocument rolXmldocument = new PubXmlDocument(rolXmlString);
          handlePubListService.fillPubList(rolXmldocument, pdwhPubSourceDb);
          rolPubXml.setPubXml(rolXmldocument.getXmlString());
          handlePubListService.saveRolPubXml(rolPubXml);
          handlePubListService.praseSourcePubList(rolXmldocument);

        } catch (Exception e) {
          logger.info("保存pubList数据出错,出错pubId:" + rolPubXml.getPubId(), e);
          handlePubListService.saveOptResult(rolPubIdTmp, 9);
        }
        handlePubListService.saveOptResult(rolPubIdTmp, 1);
      } catch (Exception e) {
        logger.info("处理pubList数据出错,出错pubId:" + rolPubIdTmp.getPubId(), e);
        handlePubListService.saveOptResult(rolPubIdTmp, 9);
      }
    }
  }
}
