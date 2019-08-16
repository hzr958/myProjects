package com.smate.center.task.service.sns.psn;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 处理isis英文名字数据服务
 * 
 * @author zzx
 */
@Service("handleISISEnameService")
@Transactional(rollbackFor = Exception.class)
public class HandleISISEnameServiceImpl implements HandleISISEnameService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonDao personDao;
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;

  @Override
  public List<Long> findList(int batchSize) throws Exception {
    return tmpTaskInfoRecordDao.getbatchhandleIdList(batchSize, 21);
  }

  public static void main(String[] args) {

  }

  @Override
  public void handle(Person psn) throws Exception {
    String ename = psn.getEname();
    String firstName = psn.getFirstName();
    String lastName = psn.getLastName();
    psn.setEname(toPinyin(ename));
    psn.setFirstName(toPinyin(firstName));
    psn.setLastName(toPinyin(lastName));
    personDao.save(psn);
  }

  private String toPinyin(String str) throws Exception {
    String newStr = "";
    if (StringUtils.isNotBlank(str)) {
      char[] ones = str.trim().toCharArray();
      for (int i = 0; i < ones.length; i++) {
        String one = String.valueOf(ones[i]);
        String npy = "";
        if (StringUtils.isNotBlank(one) && isAllChinese(one)) {
          npy = ServiceUtil.parseWordPinyin(ones[i]);
        } else {
          npy = one;
        }
        newStr += npy;
      }
    }
    return newStr;
  }

  private boolean isAllChinese(String ename) throws Exception {
    String regEx = "[\\u4e00-\\u9fa5]+";
    return ename.matches(regEx);
  }

  @Override
  public List<Person> findPsnList(List<Long> list) throws Exception {
    return personDao.findByIds(list);
  }

  @Override
  public void update(Long personId, int i, String msg) throws Exception {
    tmpTaskInfoRecordDao.updateTaskStatus(personId, i, msg, 21);
  }
}
