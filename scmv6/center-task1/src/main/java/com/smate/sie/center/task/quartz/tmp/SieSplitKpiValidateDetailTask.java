package com.smate.sie.center.task.quartz.tmp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.service.tmp.SieTaskKpiValidateDetailService;
import com.smate.sie.center.task.service.tmp.SieTaskKpiValidateMainService;
import com.smate.sie.core.base.utils.model.validate.tmp.SieTaskKpiValidateDetail;
import com.smate.sie.core.base.utils.model.validate.tmp.SieTaskKpiValidateMain;

/**
 * 科研验证生产机，TASK_KPI_VALIDATE_DETAIL 表拆到临时表做统计， 对临时表的数据再进行拆分
 * 
 * @author ztg
 *
 */
public class SieSplitKpiValidateDetailTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 100;// 一次最多处理数量

  @Autowired
  private SieTaskKpiValidateMainService sieTaskKpiValidateMainServiceImpl;

  @Autowired
  private SieTaskKpiValidateDetailService sieTaskKpiValidateDetailServiceImpl;

  public SieSplitKpiValidateDetailTask() {
    super();
  }


  public SieSplitKpiValidateDetailTask(String beanName) {
    super(beanName);
  }


  @SuppressWarnings("unused")
  public void doRun() throws Exception {

    if (!super.isAllowExecution()) {
      return;
    }

    Long count = sieTaskKpiValidateMainServiceImpl.countNeedHandleKeyId();
    if (count.intValue() == 0) {
      return;
    }
    try {
      while (true) {
        // 读取需要统计Id
        List<SieTaskKpiValidateMain> tmpkpiVdMains = sieTaskKpiValidateMainServiceImpl.loadNeedHandleKeyId(BATCH_SIZE);
        if (tmpkpiVdMains == null || tmpkpiVdMains.size() == 0) {
          return;
        }
        for (SieTaskKpiValidateMain tmpkpiVdMain : tmpkpiVdMains) {
          // 遍历main表数据
          try {
            // 外层循环每次开始时间
            Long startTime = System.currentTimeMillis();

            // 根据uuid 拿到detail表待处理记录总数
            Long detailCount = sieTaskKpiValidateDetailServiceImpl.getAllCountByUUID(tmpkpiVdMain.getUuId());


            // 根据uuid 拿到 detail表待处理记录
            List<SieTaskKpiValidateDetail> kpiVdDetails =
                sieTaskKpiValidateDetailServiceImpl.getByUUID(tmpkpiVdMain.getUuId());

            if (kpiVdDetails != null && kpiVdDetails.size() != 0) {
              for (SieTaskKpiValidateDetail kpiVdDetail : kpiVdDetails) {
                // 遍历kpiVdDetails 进行拆分处理
                try {
                  sieTaskKpiValidateDetailServiceImpl.doSplit(kpiVdDetail);
                } catch (Exception e) {
                  // 因为在doSplit 不能捕获一些异常，所以在这里对异常进行处理
                  sieTaskKpiValidateDetailServiceImpl.saveErrorForDetail(kpiVdDetail);
                  logger.error("拆分TASK_KPI_VALIDATE_DETAIL表数据出错失败,数据主键id:{},uuid:{}",
                      new Object[] {kpiVdDetail.getId(), kpiVdDetail.getUuId(), e});
                }
              }
            }

            // 统计detail表中拆分: 成功数successCount, 错误数 errorCount
            Long successCount = sieTaskKpiValidateDetailServiceImpl.getSuccessCountByUUID(tmpkpiVdMain.getUuId());
            Long errorCount = sieTaskKpiValidateDetailServiceImpl.getErrorCountByUUID(tmpkpiVdMain.getUuId());

            // 存入main表
            tmpkpiVdMain.setSplitCount(detailCount);// 处理总数
            tmpkpiVdMain.setSplitSuccessCount(successCount);// 成功数
            tmpkpiVdMain.setSplitErrorCount(errorCount);// 错误数


            // 再次查询: detail表中待处理记录
            List<SieTaskKpiValidateDetail> kpiVdDetails2 =
                sieTaskKpiValidateDetailServiceImpl.getByUUID(tmpkpiVdMain.getUuId());


            if (kpiVdDetails2.isEmpty() && errorCount == 0) {
              // 当前uuid 对应的 detail 表记录，拆分完成，无错误拆分

              // 对main 进行拆分
              sieTaskKpiValidateMainServiceImpl.doSplit(tmpkpiVdMain);

              // 拆分成功状态保存: 1
              tmpkpiVdMain.setSplitStatus(1);
            } else if (errorCount > 0) {
              // detail表中存在错误拆分情况
              // main表 拆分状态 设置为9， 失败
              tmpkpiVdMain.setSplitStatus(9);
            }


            sieTaskKpiValidateMainServiceImpl.saveTmpMain(tmpkpiVdMain);

            // 外层循环每次结束时间
            Long endTime = System.currentTimeMillis();
            Long useTime = (endTime - startTime) / 1000;
          } catch (Exception e) {

            tmpkpiVdMain.setSplitStatus(9);// 拆分出错
            sieTaskKpiValidateMainServiceImpl.saveTmpMain(tmpkpiVdMain);

            logger.error("拆分TASK_KPI_VALIDATE_MAIN表中数据出错,uuId:{},title:{}",
                new Object[] {tmpkpiVdMain.getUuId(), tmpkpiVdMain.getTitle(), e});
          }
        }
      }
    } catch (Exception e) {
      logger.error("对task_kpi_validate_detail、 task_kpi_validate_main的数据进行拆分时出错");
    }
  }
}
