package com.smate.sie.center.task.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.sie.center.task.dao.SiePdwhInsAddrConstDao;
import com.smate.sie.center.task.dao.SiePdwhInsAddrConstRefreshDao;
import com.smate.sie.center.task.model.SiePdwhInsAddrConst;
import com.smate.sie.center.task.model.SiePdwhInsAddrConstRefresh;

@Service("SiePdwhInsAddrConstService")
@Transactional(rollbackOn = Exception.class)
public class SiePdwhInsAddrConstServiceImple implements SiePdwhInsAddrConstService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SiePdwhInsAddrConstDao pdwhInsAddrConstDao;

  @Autowired
  private SiePdwhInsAddrConstRefreshDao pdwhInsAddrConstRefreshDao;

  @Override
  public List<SiePdwhInsAddrConst> getNeedRefresh(int maxSize) throws ServiceException {
    try {
      return this.pdwhInsAddrConstDao.queryNeedRefresh(maxSize);
    } catch (Exception e) {
      logger.error("处理单位别名表时，获取需要刷新的数据时出现异常", e);
      throw new ServiceException("", e);
    }
  }

  @Override
  public void refreshInsName(SiePdwhInsAddrConstRefresh pdwhInsAddrConstRefresh) throws ServiceException {
    try {
      SiePdwhInsAddrConst pdwhInsAddrConst = null;
      pdwhInsAddrConst = new SiePdwhInsAddrConst();
      pdwhInsAddrConst.setInsId(pdwhInsAddrConstRefresh.getPk().getInsId());
      pdwhInsAddrConst.setInsName(pdwhInsAddrConstRefresh.getPk().getInsName());
      pdwhInsAddrConst.setInsNameHash(PubHashUtils.cleanPubAddrHash(pdwhInsAddrConst.getInsName()));
      pdwhInsAddrConst.setUpdateTime(new Date());
      pdwhInsAddrConst.setEnabled(1);
      if (pdwhInsAddrConstRefresh.getLanguage() != null && pdwhInsAddrConstRefresh.getLanguage() > 0) {
        pdwhInsAddrConst.setLanguage(pdwhInsAddrConstRefresh.getLanguage());
      }
      pdwhInsAddrConst.setConstStatus(0);
      pdwhInsAddrConst.setLastOperator(0);
      pdwhInsAddrConst.setAddrStatus(0);
      this.pdwhInsAddrConstDao.save(pdwhInsAddrConst);
      // 更新临时处理单位别名表的数据
      pdwhInsAddrConstRefresh.setRefreshStatus(1);
      this.saveStRefresh(pdwhInsAddrConstRefresh);
    } catch (Exception e) {
      logger.error("更新单位别名数据时出现异常，异常数据中的ID：" + pdwhInsAddrConstRefresh.getPk().getInsId(), e);
      throw new ServiceException("", e);
    }
  }

  @Override
  public List<SiePdwhInsAddrConstRefresh> loadNeedDealSizeData(int maxSize) {
    List<SiePdwhInsAddrConstRefresh> pdwhInsAddrConstRefreshsList = null;
    try {
      pdwhInsAddrConstRefreshsList = pdwhInsAddrConstRefreshDao.loadNeedCountUnitId(maxSize);
    } catch (Exception e) {
      logger.error("读取PDWH_INS_ADDR_CONST_REFRESH表中需要处理的List数据出错 ！", e);
      throw new ServiceException(e);
    }
    return pdwhInsAddrConstRefreshsList;
  }

  @Override
  public void saveStRefresh(SiePdwhInsAddrConstRefresh pdwhInsAddrConstRefresh) {
    try {
      pdwhInsAddrConstRefreshDao.updateDoneStatus(pdwhInsAddrConstRefresh);
    } catch (Exception e) {
      logger.error("单位别名处理：根据处理结果更新PDWH_INS_ADDR_CONST_REFRESH表状态出错。IndId: "
          + pdwhInsAddrConstRefresh.getPk().getInsId() + ",status: " + pdwhInsAddrConstRefresh.getRefreshStatus(), e);
      throw new ServiceException(e);
    }

  }
}
