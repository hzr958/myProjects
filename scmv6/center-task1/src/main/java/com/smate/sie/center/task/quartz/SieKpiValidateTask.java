package com.smate.sie.center.task.quartz;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.service.KpiValidateDetailService;
import com.smate.sie.center.task.service.KpiValidateMainService;
import com.smate.sie.core.base.utils.consts.validate.SieKpiVdConstant;
import com.smate.sie.core.base.utils.model.validate.KpiValidateDetail;
import com.smate.sie.core.base.utils.model.validate.KpiValidateLog;
import com.smate.sie.core.base.utils.model.validate.KpiValidateMain;

/**
 * 轮询kpi_validate_detail表并根据业务类型调用具体的验证接口
 * 
 * @author ztg
 *
 */
public class SieKpiValidateTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 100;// 一次最多处理数量

  @Autowired
  private KpiValidateMainService kpiValidateMainServiceImpl;

  @Autowired
  private KpiValidateDetailService kpiValidateDetailServiceImpl;

  public SieKpiValidateTask() {
    super();
  }


  public SieKpiValidateTask(String beanName) {
    super(beanName);
  }


  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    // 获取表中待处理梳理的条数
    Long count = kpiValidateMainServiceImpl.countNeedHandleKeyId();
    if (count.intValue() == 0) {
      return;
    }
    while (true) {
      // 读取需要统计Id
      List<KpiValidateMain> kpiVdMains = kpiValidateMainServiceImpl.loadNeedHandleKeyId(BATCH_SIZE);
      if (kpiVdMains == null || kpiVdMains.size() == 0) {
        return;
      }
      for (KpiValidateMain kpiVdMain : kpiVdMains) {
        try {
          Date smDate = kpiVdMain.getSmDate();
          if (smDate == null) {
            continue;
          }
          long smDateMs = smDate.getTime();
          long currentTimeMs = new Date().getTime();
          int secondInterval = (int) ((currentTimeMs - smDateMs) / 1000); // 时间间隔, 暂时控制为跑5秒之前的数据
          if (secondInterval < 60) {
            continue;
          }

          List<KpiValidateDetail> kpiVdDetails = kpiValidateDetailServiceImpl.getByUUID(kpiVdMain.getUuId());
          if (kpiVdDetails != null && kpiVdDetails.size() != 0) {
            for (KpiValidateDetail kpiVdDetail : kpiVdDetails) {
              try {
                kpiValidateDetailServiceImpl.doValidate(kpiVdDetail);
              } catch (Exception e) {
                kpiVdDetail.setInterfaceStatus(SieKpiVdConstant.REQ_ERROR);// 调用异常或者失败
                kpiVdDetail.setParamsOut(null);
                kpiVdDetail.setStatus(null);
                kpiValidateDetailServiceImpl.saveKpiValidateDetail(kpiVdDetail);
                // 保存接口调用日志
                String msg = "";
                String resultMapStr = "";
                int detailLogLength = e.getMessage().length() > 2000 ? 2000 : (e.getMessage().length() - 1);
                String detailLog = e.getMessage().substring(0, detailLogLength);
                KpiValidateLog kpiVdLog =
                    new KpiValidateLog(kpiVdDetail.getId(), new Date(), msg, resultMapStr, detailLog);
                kpiValidateDetailServiceImpl.saveKpiValidateLog(kpiVdLog);
                logger.error("处理调用{}验证接口返回数据出错, KPI_VALIDATE_DETAIL.ID = {} ",
                    SieKpiVdConstant.INTERFACE_TYPE_MAP.get(kpiVdDetail.getType()), kpiVdDetail.getId(), e);
              }
            }
          }
          // 根据uuId查询KPI_VALIDATE_DETAIL待处理数据个数（interface_status==0）
          Long detailCount = kpiValidateDetailServiceImpl.countNeedHandleKeyId(kpiVdMain.getUuId());
          // 根据uuId查询KPI_VALIDATE_DETAIL是否存在验证不通过的数据(status==0)
          Long detailStatusNullCount = kpiValidateDetailServiceImpl.countStatusIsNull(kpiVdMain.getUuId()); // 0L;//
          if (detailCount.intValue() == 0 && detailStatusNullCount.intValue() == 0) {
            kpiVdMain.setStatus(1);
            kpiVdMain.setEndDate(new Date());
          } else {
            kpiVdMain.setStatus(0);
          }
          kpiValidateMainServiceImpl.updateKpiValidateMain(kpiVdMain);
        } catch (Exception e) {
          logger.error("轮询kpi_validate_detail表，进行接口调用出错, uuId: {}", kpiVdMain.getUuId(), e);
        }
      }
    }
  }

}
