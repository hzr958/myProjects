package com.smate.center.task.service.rol.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rol.quartz.PublicationRolDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.PublicationRol;

@Service("publicationRolService")
@Transactional(rollbackFor = Exception.class)
public class PublicationRolServiceImpl implements PublicationRolService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationRolDao publicationRolDao;

  /**
   * 获取成果实体，先从自定义缓存读取，如果不存在直接查询数据库.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  @Override
  public PublicationRol getPublicationById(Long id) throws ServiceException {

    try {
      return this.publicationRolDao.get(id);
    } catch (Exception e) {
      logger.error("getPublicationById获取成果实体id=" + id, e);
      throw new ServiceException(e);
    }

  }

}
