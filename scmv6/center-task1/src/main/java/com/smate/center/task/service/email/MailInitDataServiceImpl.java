package com.smate.center.task.service.email;

import java.util.Date;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.dao.email.MailInitDataDao;
import com.smate.center.task.model.email.MailInitData;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 邮件初始数据服务类
 * 
 * @author zk
 *
 */
@Service("mailInitDataService")
@Transactional(rollbackOn = Exception.class)
public class MailInitDataServiceImpl implements MailInitDataService {

  @Autowired
  private MailInitDataDao mailInitDataDao;

  @Override
  public void saveMailInitData(Map<String, Object> dataMap) throws Exception {
    MailInitData mid = new MailInitData();
    mid.setCreateDate(new Date());
    mid.setFromNodeId(1);
    mid.setMailData(JacksonUtils.jsonObjectSerializer(dataMap));
    mid.setStatus(1);
    mid.setToAddress(dataMap.get("email_receiveEmail").toString());
    mailInitDataDao.saveMailData(mid);
  }
}
