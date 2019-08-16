package com.smate.core.base.utils.service.consts;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.constant.SieDiscipline;
import com.smate.core.base.utils.dao.consts.SieDisciplineDao;

@Service("sieDiscipline")
@Transactional(rollbackOn = Exception.class)
public class SieDisciplineServiceImpl implements SieDisciplineService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SieDisciplineDao sieDisciplineDao;

  /**
   * 
   *
   * @descript获取学科列表
   */
  @Override
  public List<SieDiscipline> getDisciplinetNames(String superCode) {
    return sieDisciplineDao.getDisciplinetNames(superCode);
  }

  /**
   * 
   *
   * @descript 获取第二级下的所有code
   */
  @Override
  public List<String> getSecondCodeList(String superCode) {
    if (StringUtils.isNotBlank(superCode)) {
      List<String> list = new ArrayList<String>();
      List<SieDiscipline> dis = getDisciplinetNames(superCode);
      for (SieDiscipline discipline : dis) {
        list.add(discipline.getDisCode());
      }
      return list;
    }
    return null;
  }

}
