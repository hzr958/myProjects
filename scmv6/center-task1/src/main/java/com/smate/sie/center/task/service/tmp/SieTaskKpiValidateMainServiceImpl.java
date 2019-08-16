package com.smate.sie.center.task.service.tmp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.sie.core.base.utils.dao.validate.tmp.SieTaskKpiValidateMainDao;
import com.smate.sie.core.base.utils.dao.validate.tmp.SieTaskKpiVdmainSplitDao;
import com.smate.sie.core.base.utils.model.validate.tmp.SieTaskKpiValidateMain;
import com.smate.sie.core.base.utils.model.validate.tmp.SieTaskKpiVdmainSplit;

/**
 * TASK_KPI_VALIDATE_MAIN 表处理
 * 
 * @author ztg
 *
 */
@Service("sieTaskKpiValidateMainService")
@Transactional(rollbackOn = Exception.class)
public class SieTaskKpiValidateMainServiceImpl implements SieTaskKpiValidateMainService {
  private Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  private SieTaskKpiValidateMainDao sieTaskKpiValidateMainDao;
  @Autowired
  private SieTaskKpiVdmainSplitDao sieTaskKpiVdmainSplitDao;

  @Override
  public Long countNeedHandleKeyId() {
    try {
      return sieTaskKpiValidateMainDao.countNeedHandleKeyId();
    } catch (Exception e) {
      logger.error("读取TASK_KPI_VALIDATE_MAIN表待处理处理的总数出错 ！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<SieTaskKpiValidateMain> loadNeedHandleKeyId(int maxSize) {
    List<SieTaskKpiValidateMain> tmpkpiVdMains = null;
    try {
      tmpkpiVdMains = sieTaskKpiValidateMainDao.loadNeedHandleKeyId(maxSize);
    } catch (Exception e) {
      logger.error("读取TASK_KPI_VALIDATE_MAIN表中需要处理的记录出错 ！", e);
      throw new ServiceException(e);
    }
    return tmpkpiVdMains;
  }


  /**
   * 拆分到TMP_KPI_VDMAIN_SPLIT
   */
  @SuppressWarnings("unchecked")
  @Override
  public void doSplit(SieTaskKpiValidateMain tmpkpiVdMain) {
    SieTaskKpiVdmainSplit tmpKpiVMSplit = new SieTaskKpiVdmainSplit();
    try {
      tmpKpiVMSplit.setUuId(tmpkpiVdMain.getUuId());
      tmpKpiVMSplit.setTitle(tmpkpiVdMain.getTitle());

      Map<String, Object> dataMap = new HashMap<String, Object>();
      Map<String, Object> basicInfo = new HashMap<>();
      if (tmpkpiVdMain.getData() != null) {
        try {
          dataMap = JacksonUtils.json2HashMap(tmpkpiVdMain.getData());
          basicInfo = (Map<String, Object>) dataMap.get("basic_info");
        } catch (Exception e) {
          basicInfo = new HashMap<>();;
        }
      }
      String orgName = (String) basicInfo.get("org_name");
      String psnName = (String) basicInfo.get("psn_name");
      String deptName = (String) basicInfo.get("dept_name");
      String grantName = (String) basicInfo.get("grant_name");
      tmpKpiVMSplit.setOrgName(orgName == null ? "" : orgName);// org_name
      tmpKpiVMSplit.setPsnName(psnName == null ? "" : psnName);// psn_name
      tmpKpiVMSplit.setDeptName(deptName == null ? "" : deptName);// deptName
      tmpKpiVMSplit.setGrantName(grantName == null ? "" : grantName);// grantName
      sieTaskKpiVdmainSplitDao.saveOrUpdate(tmpKpiVMSplit);
    } catch (Exception e) {
      logger.error("拆分TASK_KPI_VALIDATE_MAIN表中数据出错,uuId:{},title:{}",
          new Object[] {tmpkpiVdMain.getUuId(), tmpkpiVdMain.getTitle(), e});
      // 拆分TASK_KPI_VALIDATE_MAIN 报错， 向上抛异常
      throw new ServiceException(
          "拆分TASK_KPI_VALIDATE_MAIN表中数据出错,uuId:" + tmpkpiVdMain.getUuId() + ",title:" + tmpkpiVdMain.getTitle());
    }
  }

  @Override
  public void saveTmpMain(SieTaskKpiValidateMain tmpkpiVdMain) {
    sieTaskKpiValidateMainDao.saveOrUpdate(tmpkpiVdMain);
  }

}
