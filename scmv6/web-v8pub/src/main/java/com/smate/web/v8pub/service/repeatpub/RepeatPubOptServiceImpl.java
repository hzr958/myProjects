package com.smate.web.v8pub.service.repeatpub;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.v8pub.restTemp.service.PubRestemplateService;
import com.smate.web.v8pub.dao.repeatpub.PubSameItemDAO;
import com.smate.web.v8pub.dao.repeatpub.PubSameRecordDAO;
import com.smate.web.v8pub.po.repeatpub.PubSameItemPO;
import com.smate.web.v8pub.po.repeatpub.PubSameRecordPO;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.RepeatPubVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 重复成果 - 操作-实现类
 * 
 * @author zzx
 *
 */
@Service("repeatPubOptService")
@Transactional(rollbackFor = Exception.class)
public class RepeatPubOptServiceImpl implements RepeatPubOptService {
  @Autowired
  private PubSameItemDAO pubSameItemDao;
  @Autowired
  private PubSameRecordDAO pubSameRecordDao;
  @Autowired
  private PubSnsService pubSnsService;
  @Resource
  private PubRestemplateService pubRestemplateService;


  @Override
  public void repeatPubDel(RepeatPubVO repeatPubVO) throws Exception {
    // 必要参数判空
    if (StringUtils.isNotBlank(repeatPubVO.getDes3pubSameItemId())) {
      Long pubSameItemId = Long.parseLong(Des3Utils.decodeFromDes3(repeatPubVO.getDes3pubSameItemId()));
      Long userId = SecurityUtils.getCurrentUserId();
      PubSameItemPO pubSameItem = pubSameItemDao.get(pubSameItemId);
      // 效验
      if (pubSameItem != null && userId != null && userId.equals(pubSameItem.getPsnId())) {
        // 设置重复成果删除
        pubSameItem.setDealStatus(2);
        pubSameItemDao.save(pubSameItem);
        // 是否是成果拥有人，是才能删除真实成果
        if (pubSnsService.queryPubPsnId(pubSameItem.getPubId()).longValue() == userId.longValue()) {
          // 删除真实成果
          pubRestemplateService.delPsnPub(pubSameItem.getPubId(), userId);
        }
        // 如果删除了以后只有一条未处理记录，要自动设置为保留,设置组状态为已处理
        List<PubSameItemPO> items =
            pubSameItemDao.getNoDealPubSameItems(pubSameItem.getRecordId(), userId, pubSameItemId);
        if (items != null && items.size() == 1) {
          // 设置为保留
          PubSameItemPO item = items.get(0);
          item.setDealStatus(1);
          pubSameItemDao.save(item);
          // 设置组状态为已处理
          PubSameRecordPO pubSameRecord = pubSameRecordDao.get(pubSameItem.getRecordId());
          pubSameRecord.setDealStatus(1);
          pubSameRecordDao.save(pubSameRecord);
        }
        repeatPubVO.getResultMap().put("result", "success");
      }
    }
  }

  @Override
  public void repeatPubKeep(RepeatPubVO repeatPubVO) throws Exception {
    // 必要参数判空
    if (StringUtils.isNotBlank(repeatPubVO.getDes3RecordId())) {
      Long recordId = Long.parseLong(Des3Utils.decodeFromDes3(repeatPubVO.getDes3RecordId()));
      Long userId = SecurityUtils.getCurrentUserId();
      PubSameRecordPO pubSameRecord = pubSameRecordDao.get(recordId);
      // 效验
      if (pubSameRecord != null && userId != null && userId.equals(pubSameRecord.getPsnId())) {
        // 保留所有重复成果记录
        pubSameItemDao.setKeepAll(recordId, userId);
        // 设置组状态为已处理
        pubSameRecord.setDealStatus(1);
        pubSameRecordDao.save(pubSameRecord);
        repeatPubVO.getResultMap().put("result", "success");
      }
    }
  }

}
