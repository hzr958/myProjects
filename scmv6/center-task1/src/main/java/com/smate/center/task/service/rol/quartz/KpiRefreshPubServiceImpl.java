package com.smate.center.task.service.rol.quartz;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rol.quartz.KpiRefreshPubDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.KpiRefreshPub;

@Service("kpiRefreshPubService")
@Transactional(rollbackFor = Exception.class)
public class KpiRefreshPubServiceImpl implements KpiRefreshPubService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private KpiRefreshPubDao kpiRefreshPubDao;

  @Override
  public void addPubRefresh(Long pubId, boolean isDel) throws ServiceException {

    try {
      KpiRefreshPub refPub = kpiRefreshPubDao.get(pubId);
      if (refPub == null) {
        refPub = new KpiRefreshPub(pubId);
      }
      refPub.setLastDate(new Date());
      refPub.setIsDel(isDel ? 1 : 0);
      this.kpiRefreshPubDao.save(refPub);
    } catch (Exception e) {
      logger.warn("添加成果统计更新重复pub_id" + pubId);
      if (isDel) {
        try {
          this.kpiRefreshPubDao.updatePubDel(pubId);
        } catch (Exception e1) {
          logger.warn("添加成果统计更新重复标记位为删除失败pub_id" + pubId);
        }
      }
    }
  }

}
