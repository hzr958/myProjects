package com.smate.center.batch.chain.pubassign.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreDetail;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.service.psn.InsPersonService;
import com.smate.center.batch.service.rol.pub.PubAssignCnkiMatchService;

/**
 * CNKI成果匹配-成果指派，匹配部门任务.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignMatchCnkiDeptTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = -3921200446814677299L;
  private final String name = "PubAssignMatchCnkiDeptTask";
  @Autowired
  private PubAssignCnkiMatchService pubAssignCnkiMatchService;
  @Autowired
  private InsPersonService insPersonService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubAssginMatchContext context) {
    return context.hasMatchedPsn();
  }

  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {

    // 成果部门匹配用户所在机构部门
    pubDeptMatchPsnUnit(context, context.getPubAssignScoreMap());
    return true;
  }

  /**
   * 如果单位用户所在部门匹配上成果作者部门，加分.
   * 
   * @param context
   * @param scoreMap
   * @param seqNos
   * @throws ServiceException
   */
  private void pubDeptMatchPsnUnit(PubAssginMatchContext context, Map<Integer, PubAssignScoreMap> scoreMap)
      throws ServiceException {

    // seqNo
    List<Integer> seqNos = new ArrayList<Integer>(scoreMap.keySet());
    // 获取作者地址列表
    Map<Integer, String> deptMap =
        pubAssignCnkiMatchService.getInsPubDept(seqNos, context.getPubId(), context.getInsId());

    if (MapUtils.isEmpty(deptMap)) {
      return;
    }
    // 获取人员部门名称
    Set<Long> psnIds = context.getMatchedPsnIds();
    // String[0:unitname,1:parentUnitName]
    Map<Long, String[]> psnUnitNameMap = insPersonService.getPsnUnitName(context.getInsId(), psnIds);

    if (MapUtils.isEmpty(psnUnitNameMap)) {
      return;
    }
    // 遍历各个成果作者，匹配部门是否相同
    Iterator<Integer> iter = scoreMap.keySet().iterator();
    while (iter.hasNext()) {
      Integer seqNo = iter.next();
      String authorAddr = deptMap.get(seqNo);
      if (StringUtils.isBlank(authorAddr)) {
        continue;
      }
      Map<Long, PubAssignScoreDetail> detailMap = scoreMap.get(seqNo).getDetailMap();
      if (MapUtils.isEmpty(detailMap)) {
        continue;
      }
      Iterator<Long> psnIter = detailMap.keySet().iterator();
      while (psnIter.hasNext()) {
        Long psnId = psnIter.next();
        String[] unitNames = psnUnitNameMap.get(psnId);
        if (unitNames == null) {
          continue;
        }
        PubAssignScoreDetail detail = detailMap.get(psnId);
        String unitName = StringUtils.trimToNull(StringUtils.lowerCase(unitNames[0]));
        String superUnitName = StringUtils.trimToNull(StringUtils.lowerCase(unitNames[1]));
        boolean matchedUnit = false;
        if (unitName != null && authorAddr.toLowerCase().indexOf(unitName) > -1) {
          matchedUnit = true;
        }
        if (!matchedUnit && superUnitName != null && authorAddr.toLowerCase().indexOf(superUnitName) > -1) {
          matchedUnit = true;
        }
        if (matchedUnit) {
          detail.setDept(context.getSettingPubAssignMatchScore().getDept());
        }
      }
    }
  }
}
