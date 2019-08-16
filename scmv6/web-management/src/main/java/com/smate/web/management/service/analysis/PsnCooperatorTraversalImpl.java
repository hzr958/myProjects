package com.smate.web.management.service.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.smate.web.management.model.analysis.sns.PsnInsDetail;
import com.smate.web.management.model.analysis.sns.RecommendScore;

/**
 * 
 * 基金、论文合作者推荐：遍历所有符合必要条件的人员实现.
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCooperatorLevel")
// 不加事务
public class PsnCooperatorTraversalImpl implements PsnCooperatorTraversal {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  // 大类相同
  // 表：psn_area_classify(psn_id,classify);

  // 级别>=2
  // //单位级别，表：scholar2.psn_ins_detail(psn_id,ins_id,grade)
  // //职称级别，表：schoalr2.psn_postion_grade(psn_id,grade)
  // //发表期刊级别,表：scholar2.psn_journal_grade(psn_id,grade)
  // //同单位，表：scholar2.psn_ins_detail(psn_id,ins_id)
  // //申请级别，表：psn_nsfcprj_grade(psn_id,prj_grade)

  @Autowired
  private PsnCooperatorService psnCooperatorService;

  @Resource(name = "psnCooperatorRelevance")
  private PsnCooperator next;

  // 可能合作者推荐计算(大类和级别不足的人员忽略，不进行计算；计算耗时人员先挂起)
  @Override
  public void psnCooperatorRun(Long psnId, Integer status) throws Exception {
    Assert.notNull(psnId, "PsnCooperatorTraversal.psnCooperatorRun.psnId参数不能为空");
    Assert.notNull(status, "PsnCooperatorTraversal.psnCooperatorRun.status参数不能为空");
    // 人员大类
    List<String> classifyList = psnCooperatorService.findClassifyList(psnId);
    if (CollectionUtils.isEmpty(classifyList) && PsnCooperatorService.STATUS_RERUN.intValue() != status.intValue()) {
      if (!psnCooperatorService.isClassifyPass(classifyList, psnId)) {// 无大类信息，先挂起，最后执行
        return;
      }
    }

    PsnInsDetail pid = psnCooperatorService.getPsnInsDetail(psnId);
    Integer insGrade = psnCooperatorService.insGrade(pid);
    Integer posGrade = psnCooperatorService.posGrade(psnId);
    Integer jnlGrade = psnCooperatorService.jnlGrade(psnId);
    Long insId = psnCooperatorService.insId(pid);
    Integer prjGrade = psnCooperatorService.prjGrade(psnId);

    // //提前抽取相关度查询条件，优化性能
    // 部门
    Long deptEnHash = -1L;
    Long deptZhHash = -1L;
    if (pid != null) {
      if (pid.getDeptEnHash() != null) {
        deptEnHash = pid.getDeptEnHash();
      }
      if (pid.getDeptZhHash() != null) {
        deptZhHash = pid.getDeptZhHash();
      }
    }
    // 获取人员的issn列表
    List<String> issnTxtList = psnCooperatorService.getPsnIssnTxtList(psnId);
    // 获取人员的所教课程hash列表
    List<Long> tauhtHashList = psnCooperatorService.getPersonTaughHashList(psnId);

    Object[] checks = new Object[] {insGrade, posGrade, jnlGrade, insId, prjGrade};
    if (!psnCooperatorService.isGradePass(checks, psnId)) {// 无足够级别信息，不继续执行
      psnCooperatorService.delCoRecord(psnId);// 删除合作者数据
      return;
    }

    // 统计可能合作者的数量
    Map<String, Long> sumMap = psnCooperatorService.cooperatorMayCount(classifyList, insGrade, posGrade, jnlGrade,
        insId, prjGrade, psnId, deptEnHash, deptZhHash, issnTxtList, tauhtHashList);

    if (!psnCooperatorService.isNoRecord(sumMap, psnId)) {// 无可能合作者
      psnCooperatorService.delCoRecord(psnId);// 删除合作者数据
      return;
    }

    if (PsnCooperatorService.STATUS_RERUN.intValue() != status.intValue()
        && psnCooperatorService.isSuspend(sumMap, psnId)) {// 计算耗时超过允许范围，先挂起
      return;
    }

    this.psnCooperatorCalc(classifyList, insGrade, posGrade, jnlGrade, insId, prjGrade, sumMap, psnId, deptEnHash,
        deptZhHash, issnTxtList, tauhtHashList);
  }

  // 可能合作者推荐计算(控制单次匹配推荐人员规模，防止jvm异常)
  private void psnCooperatorCalc(List<String> classifyList, Integer insGrade, Integer posGrade, Integer jnlGrade,
      Long insId, Integer prjGrade, Map<String, Long> sumMap, Long psnId, Long deptEnHash, Long deptZhHash,
      List<String> issnTxtList, List<Long> tauhtHashList) throws Exception {
    List<Long> unionCml = new ArrayList<Long>();
    try {
      while (true) {

        List<List<Long>> cml = psnCooperatorService.cooperatorMayList(classifyList, insGrade, posGrade, jnlGrade, insId,
            prjGrade, sumMap, psnId, deptEnHash, deptZhHash, issnTxtList, tauhtHashList);// 可能合作者人员id列表
        if (cml.size() == 0) {// 无数据时终止
          break;
        }

        for (List<Long> sub : cml) {
          for (Long v : sub) {
            if (!unionCml.contains(v) && !psnCooperatorService.isPsnPrivate(v)) {// 非重复与非隐私用户
              unionCml.add(v);
            }
          }
        }

        if (unionCml.size() > 10 * PsnCooperatorService.MAX_RECORD_FOUND) {// 防止数组过大，导致jvm异常，最大10倍
          // MAX_RECORD_FOUND
          this.nextAndSaveMayScore(unionCml, psnId);// 计算可能合作者，并保存分数
          unionCml.clear();
        }
      }

      // 数据不足10倍MAX_RECORD_FOUND时
      this.nextAndSaveMayScore(unionCml, psnId);// 计算可能合作者，并保存分数
      psnCooperatorService.delPassCoVersion(psnId);// 删除过时的合作者数据
      // 执行成功
      psnCooperatorService.taskRunSucc(psnId);

    } catch (Exception e) {
      // 执行失败
      psnCooperatorService.taskRunFail(psnId, e.getMessage());
    }
  }

  // 可能合作者推荐计算(大类和级别不足的人员忽略，不进行计算；计算耗时人员先挂起)
  @Override
  public boolean psnCooperatorRun(Long psnId, Map<Long, RecommendScore> rsMap) throws Exception {
    Assert.notNull(psnId, "PsnCooperatorTraversal.psnCooperatorRun.psnId参数不能为空");
    if (rsMap == null || rsMap.size() == 0) {// 无人员信息时，不进行后续计算
      return true;
    }
    Set<Long> kwPsnIdList = rsMap.keySet();

    // 人员大类
    List<String> classifyList = psnCooperatorService.findClassifyList(psnId);
    // if (!psnCooperatorService.isClassifyPass(classifyList, psnId)) {//
    // 无大类信息，不继续执行
    // return true;
    // }

    PsnInsDetail pid = psnCooperatorService.getPsnInsDetail(psnId);
    Integer insGrade = psnCooperatorService.insGrade(pid);
    Integer posGrade = psnCooperatorService.posGrade(psnId);
    Integer jnlGrade = psnCooperatorService.jnlGrade(psnId);
    Long insId = psnCooperatorService.insId(pid);
    Integer prjGrade = psnCooperatorService.prjGrade(psnId);

    Object[] checks = new Object[] {insGrade, posGrade, jnlGrade, insId, prjGrade};

    if (!psnCooperatorService.isGradePass(checks, psnId, false)) {// 无足够级别信息，不继续执行
      return true;
    }

    // 统计可能合作者的数量
    Map<String, Long> sumMap = psnCooperatorService.cooperatorMayCount(classifyList, kwPsnIdList, insGrade, posGrade,
        jnlGrade, insId, prjGrade, psnId);

    return this.psnCooperatorCalc(classifyList, kwPsnIdList, rsMap, insGrade, posGrade, jnlGrade, insId, prjGrade,
        sumMap, psnId);
  }

  // 可能合作者推荐计算(关键词专用)
  @SuppressWarnings("unchecked")
  private boolean psnCooperatorCalc(List<String> classifyList, Set<Long> kwPsnIdList, Map<Long, RecommendScore> rsMap,
      Integer insGrade, Integer posGrade, Integer jnlGrade, Long insId, Integer prjGrade, Map<String, Long> sumMap,
      Long psnId) throws Exception {
    List<Long> unionCml = new ArrayList<Long>();
    try {

      int size = kwPsnIdList.size();
      for (int i = 0; i < size; i++) {

        List<List<Long>> cml = psnCooperatorService.cooperatorMayList(classifyList, kwPsnIdList, insGrade, posGrade,
            jnlGrade, insId, prjGrade, sumMap, psnId);// 可能合作者人员id列表
        if (cml.size() == 0) {// 无数据时终止
          continue;
        }

        for (List<Long> sub : cml) {
          for (Long v : sub) {
            if (!unionCml.contains(v) && !psnCooperatorService.isPsnPrivate(v)) {// 非重复与非隐私用户
              unionCml.add(v);
            }
          }
        }
        if (unionCml.size() > PsnCooperatorService.MAX_KW_PSN) {
          unionCml = unionCml.subList(0, PsnCooperatorService.MAX_KW_PSN);
        }
      }

      this.nextAndSaveMayScoreByKw(unionCml, psnId, rsMap);// 计算可能合作者，并保存分数

      return unionCml.size() == PsnCooperatorService.MAX_KW_PSN;// 已经计算关键词匹配上，并且满足必要条件的30人

    } catch (Exception e) {
      // 执行失败
      logger.error("基金、论文关键词计算可能合作者失败，psnId={}", psnId, e);
      return true;
    }
  }

  // 具体计算可能合作者，并保存分数
  private void nextAndSaveMayScore(List<Long> unionCml, Long psnId) throws Exception {
    for (Long coPsnId : unionCml) {
      RecommendScore rs = new RecommendScore();
      rs.setPsnId(psnId);
      rs.setCoPsnId(coPsnId);
      next.handler(rs);// 计算可能合作者推荐
      try {
        // 更新可能合作者推荐记录
        psnCooperatorService.saveCMR(psnId, coPsnId, rs);

      } catch (Exception e) {
        throw new Exception(String.format(PsnCooperatorService.MSG_SOME_ERROR, coPsnId));
      }
    }
    psnCooperatorService.delLowScoreRecord(psnId);// 删除分数值低的数据，只保留分数靠前30个人员
  }

  // 具体计算可能合作者，并保存分数
  private void nextAndSaveMayScoreByKw(List<Long> unionCml, Long psnId, Map<Long, RecommendScore> rsMap)
      throws Exception {
    for (Long coPsnId : unionCml) {
      RecommendScore rs = new RecommendScore();
      rs.setPsnId(psnId);
      rs.setCoPsnId(coPsnId);
      RecommendScore extRs = rsMap.get(coPsnId);
      if (extRs != null) {// 合并关键词分数
        rs.getRelevanceScore().setKwScore(extRs.getRelevanceScore().getKwScore());
      }
      next.handler(rs);// 计算可能合作者推荐
      try {
        // 更新可能合作者推荐记录
        psnCooperatorService.saveCMR(psnId, coPsnId, rs);

      } catch (Exception e) {
        throw new Exception(String.format(PsnCooperatorService.MSG_SOME_ERROR, coPsnId));
      }
    }
  }

}
