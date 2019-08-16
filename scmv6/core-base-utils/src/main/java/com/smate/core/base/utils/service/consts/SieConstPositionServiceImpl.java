package com.smate.core.base.utils.service.consts;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.consts.SieConstPositionDao;
import com.smate.core.base.utils.exception.SmateException;
import com.smate.core.base.utils.model.consts.SieConstPosition;

/**
 * 职务的公共读取业务模块.
 * 
 * @author hd
 * 
 */
@Service("sieConstPositionService")
@Transactional(rollbackFor = Exception.class)
public class SieConstPositionServiceImpl implements SieConstPositionService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieConstPositionDao sieConstPositionDao;

  @Override
  public List<SieConstPosition> getPosLike(String pos) throws SmateException {
    try {
      return sieConstPositionDao.getPosLike(pos);
    } catch (Exception e) {
      logger.error("获取自动匹配的职务列表.", e);
      throw new SmateException(e);
    }
  }

  @Override
  public SieConstPosition getPosByName(String name) throws SmateException {
    try {
      return sieConstPositionDao.getPosByName(name);
    } catch (Exception e) {
      logger.error("通过职务名称，获取职务实体.", e);
      throw new SmateException(e);
    }
  }

  @Override
  public List<SieConstPosition> getAllFirstPos() throws SmateException {
    try {
      return sieConstPositionDao.getAllFirstPos();
    } catch (Exception e) {
      logger.error("获取所有一级职称", e);
      throw new SmateException(e);
    }
  }

  @Override
  public String getSecondPosByFirst(String superIdStr) throws SmateException {
    StringBuffer result = new StringBuffer("");
    if (StringUtils.isNotBlank(superIdStr)) {
      Long superId = Long.valueOf(superIdStr);
      List<SieConstPosition> list = sieConstPositionDao.getSecondPosByFirst(superId);
      if (list != null && list.size() > 0) {
        for (int i = 0; i < list.size(); i++) {
          result.append(list.get(i).getId());
          if (i < list.size() - 1) {
            result.append(",");
          }

        }
      }
    }
    return result.toString();
  }

  @Override
  public String getPosGrades(Long id) throws SmateException {
    try {
      return sieConstPositionDao.getPosGrades(id);
    } catch (Exception e) {
      logger.error("获取指定职务ID的等级.", e);
      throw new SmateException(e);
    }
  }


}
