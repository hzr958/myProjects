package com.smate.center.task.service.email;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.dao.email.PromoteMailInitDataDao;
import com.smate.center.task.model.email.PromoteMailInitData;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 邮件初始数据服务类
 * 
 * @author zk
 *
 */
@Service("promoteMailInitDataService")
@Transactional(rollbackOn = Exception.class)
public class PromoteMailInitDataServiceImpl implements PromoteMailInitDataService {

  @Autowired
  private PromoteMailInitDataDao promoteMailInitDataDao;

  @Override
  public void saveMailInitData(Map<String, Object> dataMap) throws Exception {
    PromoteMailInitData mid = new PromoteMailInitData();
    mid.setCreateDate(new Date());
    mid.setFromNodeId(5);
    mid.setMailData(JacksonUtils.jsonObjectSerializerNoNull(dataMap));
    mid.setStatus(1);
    mid.setToAddress(dataMap.get("email_receiveEmail").toString());
    promoteMailInitDataDao.save(mid);
  }

  @Override
  public boolean getDataByEmail(String email) {
    List<Long> ids = promoteMailInitDataDao.getDataByEmail(email);
    if (CollectionUtils.isNotEmpty(ids)) {
      return false;
    }
    return true;
  }
}
