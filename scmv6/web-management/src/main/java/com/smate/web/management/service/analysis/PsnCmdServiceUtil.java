package com.smate.web.management.service.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.web.management.dao.analysis.sns.PsnDisciplineSuperDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.analysis.ExpertDiscForm;


/**
 * 人员推荐.
 * 
 * @author pwl
 * 
 */
public class PsnCmdServiceUtil {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public final static int CMD_PSN_COUNT = 100; // 推荐人数
  public final static int PAPER_COOPERATOR_CMD = 1; // 论文专家推荐
  public final static int PROPOSAL_COOPERATOR_CMD = 2; // 申请书专家推荐
  public final static int EXPERT_CMD = 3; // 同行专家
  public final static String PSN_ID = "_PSNID"; // 人员id
  public final static String RECMD_DEGREE = "_recmdDegree";// 推荐度
  public final static String EXPERT_PSN_CACHE = "sns_expert_psncache";

  // 保存60分钟时间
  public final static int EXPERT_PSN_CACHE_TIMEOUT = 60 * 60;
  public final static String EXPERT_PSN_CACHE_KEY_FORMAT = "%s_%s";

  @Autowired
  private PsnDisciplineSuperDao psnDisciplineSuperDao;


  /**
   * 取得推荐用户的研究领域信息.
   * 
   * @param psnIdList
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  protected List<ExpertDiscForm> getPsnCmdDisc(List<Long> psnIdList) throws Exception {
    try {
      List<ExpertDiscForm> formList = new ArrayList<ExpertDiscForm>();
      if (CollectionUtils.isNotEmpty(psnIdList)) {
        List discIdList = psnDisciplineSuperDao.getDiscIdByPsns(psnIdList);
        List<ExpertDiscForm> subFormList = null;
        Map<String, Object> map = null;
        Map<String, Object> subMap = null;
        ExpertDiscForm form = null;

        Long discId = null;
        Long superDiscId = null;
        for (int i = 0, size = discIdList.size(); i < size; i++) {
          map = (Map<String, Object>) discIdList.get(i);
          if (map.get("SUPER_ID") == null) {
            discId = Long.valueOf(map.get("ID").toString());
            form = new ExpertDiscForm();
            form.setDiscId(discId);
            form.setDiscName(map.get("ZH_NAME").toString());
            form.setDiscEnName(map.get("EN_NAME").toString());
            form.setDiscCode(map.get("DISC_CODE").toString());

            subFormList = new ArrayList<ExpertDiscForm>();
            for (int j = 0, size2 = discIdList.size(); j < size2; j++) {
              subMap = (Map) discIdList.get(j);
              Object suberDiscIdObj = subMap.get("SUPER_ID");

              if (suberDiscIdObj != null) {
                superDiscId = Long.valueOf(suberDiscIdObj.toString());
                if (superDiscId.longValue() == discId.longValue()) {
                  ExpertDiscForm subForm = new ExpertDiscForm();
                  subForm.setDiscId(Long.valueOf(subMap.get("ID").toString()));
                  subForm.setDiscName(subMap.get("ZH_NAME").toString());
                  subForm.setDiscEnName(subMap.get("EN_NAME").toString());
                  subForm.setDiscCode(subMap.get("DISC_CODE").toString());
                  subForm.setExpertNum(Integer.valueOf(subMap.get("TOTAL").toString()));
                  subFormList.add(subForm);
                }
              }
            }
            form.setExpertNum(Integer.valueOf(map.get("TOTAL").toString()));
            form.setSuberDiscList(subFormList);
            formList.add(form);
          }
        }

        int noDiscCount = psnIdList.size() - this.psnDisciplineSuperDao.getHasDiscCount(psnIdList);
        if (noDiscCount > 0) {
          form = new ExpertDiscForm();
          form.setDiscId(-1L);
          form.setDiscName("未分配研究领域");
          form.setDiscEnName("Other");
          form.setDiscCode("");
          form.setExpertNum(noDiscCount);
          formList.add(form);
        }
      }
      return formList;
    } catch (DaoException e) {
      logger.error("加载研究领域时出错啦：", e);
      throw new Exception(e);
    }
  }

}
