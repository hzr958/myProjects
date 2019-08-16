package com.smate.center.task.service.sns.quartz;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.dao.sns.quartz.Des3PubIdDao;
import com.smate.center.task.model.sns.pub.Des3PubId;
import com.smate.core.base.utils.security.Des3Utils;

@Service("generateDes3PubIdService")
@Transactional(rollbackOn = Exception.class)
public class GenerateDes3PubIdServiceImpl implements GenerateDes3PubIdService {

  @Autowired
  private Des3PubIdDao des3PubIdDao;

  @Override
  public List<Des3PubId> getPubIdList(int index, int batchSize) {
    return des3PubIdDao.getPubList(index, batchSize);
  }

  @Override
  public void GenerateDesPubId(Des3PubId des3PubId) throws Exception {
    int index = (int) (Math.random() * 9000 + 1000);// 生成随机数

    String desPubString = index + "|" + des3PubId.getPubId();
    String encodeToDes3 = Des3Utils.encodeToDes3(desPubString);// “随机数|desid”加密pubid
    des3PubId.setDes3Id(encodeToDes3);
    des3PubIdDao.save(des3PubId);

  }



}
