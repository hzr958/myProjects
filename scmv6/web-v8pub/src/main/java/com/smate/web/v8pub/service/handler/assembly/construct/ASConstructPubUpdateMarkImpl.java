package com.smate.web.v8pub.service.handler.assembly.construct;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.enums.PubHandlerEnum;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;


/**
 * 构造UpdateMark字段 <br/>
 * 更新的规则： <br/>
 * 1----在线导入，且未修改; <br/>
 * 2----在线导入，并已修改; <br/>
 * 3----手工录入 基准库成果默认为1<br/>
 * 
 * @author YJ
 *
 *         2018年9月27日
 */
@Transactional(rollbackFor = Exception.class)
public class ASConstructPubUpdateMarkImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      // 标识是否是手工录入的情况：文件导入也算是手工录入
      boolean importFlag = (pub.recordFrom == PubSnsRecordFromEnum.MANUAL_INPUT
          || pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FORM_FILE
          || pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FORM_PDF);
      if (importFlag) {
        pub.updateMark = 3;
      }
      // 判断是保存sns库成果
      if (pub.pubHandlerName.equalsIgnoreCase(PubHandlerEnum.SAVE_SNS.name)) {
        importFlag = (pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FROM_DATABASE
            || pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FROM_PDWH);
        if (importFlag && pub.updateMark == null) {
          pub.updateMark = 1;
        }
      }
      // 判断是更新sns库成果
      if (pub.pubHandlerName.equalsIgnoreCase(PubHandlerEnum.UPDATE_SNS.name)) {
        importFlag = (pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FROM_DATABASE
            || pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FROM_PDWH
            || pub.recordFrom == PubSnsRecordFromEnum.SYNC_FROM_INS
            || pub.recordFrom == PubSnsRecordFromEnum.SYNC_FROM_SNS);
        // 20190319-只有用户手动编辑之后，才标示为2状态
        pub.updateMark = NumberUtils.isNotNullOrZero(pub.updateMark) ? pub.updateMark : 1;
        if (importFlag && pub.updateMark == 1 && pub.isEdit != null && pub.isEdit == 1) {
          pub.updateMark = 2;
        } else {
          pub.updateMark = NumberUtils.isNotNullOrZero(pub.updateMark) ? pub.updateMark : 1;
        }
      }
    } catch (Exception e) {
      logger.error("构造成果更新标记字段出错！", e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }

}
