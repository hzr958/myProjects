package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubAccessoryPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubAccessoryService;
import com.smate.web.v8pub.vo.PubAccessoryVO;

/**
 * 个人库成果附件更新/保存
 * 
 * @author YJ
 *
 *         2018年7月24日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubAccessorySaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAccessoryService pubAccessoryService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub
  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      if (pub.accessorys != null) {
        Long fileId = null;
        List<PubAccessoryVO> accessoryList =
            JacksonUtils.jsonToCollection(pub.accessorys.toJSONString(), List.class, PubAccessoryVO.class);
        List<PubAccessoryPO> oldAccessorys = pubAccessoryService.findByPubId(pub.pubId);// 获取插入数据前的所有记录
        Map<Long, PubAccessoryPO> oldAccessoryMap = null;
        if (CollectionUtils.isNotEmpty(oldAccessorys)) {
          oldAccessoryMap = new HashMap<>();
          for (PubAccessoryPO po : oldAccessorys) {
            if (po != null && po.getFileId() != null) {
              oldAccessoryMap.put(po.getFileId(), po);
            }
          }
        }
        pubAccessoryService.deleteAll(pub.pubId);// 删除之前的所有附件记录
        if (accessoryList != null && accessoryList.size() > 0) {
          for (PubAccessoryVO p : accessoryList) {
            PubAccessoryPO pubAccessoryPO = new PubAccessoryPO();
            if (StringUtils.isEmpty(p.getDes3fileId())) {
              continue;
            }
            if (StringUtils.isNumeric(p.getDes3fileId())) {
              fileId = Long.valueOf(p.getDes3fileId());
            }
            fileId = Long.valueOf(Des3Utils.decodeFromDes3(p.getDes3fileId()));
            if (NumberUtils.isNullOrZero(fileId)) {
              continue;
            }
            pubAccessoryPO.setFileId(fileId);
            pubAccessoryPO.setFileName(p.getFileName());
            pubAccessoryPO.setPermission(p.getPermission());
            pubAccessoryPO.setPubId(pub.pubId);
            if (oldAccessoryMap != null && oldAccessoryMap.keySet().contains(fileId)) {// 判断当前要插入的附件记录是否在之前已经存在，如果已经存在不修改创建日期
              pubAccessoryPO.setGmtCreate(oldAccessoryMap.get(fileId).getGmtCreate());
            } else {
              pubAccessoryPO.setGmtCreate(new Date());
            }
            pubAccessoryPO.setGmtModified(new Date());
            pubAccessoryService.save(pubAccessoryPO);
          }
        }
        logger.debug("保存或更新sns库成果附件成功");
      }
    } catch (Exception e) {
      logger.error("保存或更新个人库成果附件出错！accessorys={}", pub.accessorys, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库成果附件出错!", e);
    }
    return null;
  }

}
