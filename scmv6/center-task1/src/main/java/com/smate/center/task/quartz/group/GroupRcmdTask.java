package com.smate.center.task.quartz.group;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.base.AppSettingConstants;
import com.smate.center.task.base.AppSettingContext;
import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.grp.GrpBaseinfo;
import com.smate.center.task.model.grp.GrpKwDisc;
import com.smate.center.task.service.group.GrpService;

/**
 * 历史群组推荐任务
 * 
 * @author zll
 *
 */
public class GroupRcmdTask extends TaskAbstract {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  public static String GRP_RCMD_GRP_ID_CACHE = "Grp_rcmd_grp_id_cache";
  private final static Integer SIZE = 2000; // 每次处理的个数
  @Autowired
  private GrpService grpService;

  public GroupRcmdTask() {

  }

  public GroupRcmdTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========GroupRcmdTask 已关闭==========");
      return;
    }

    try {
      Long startGrpId = startGrpId() == null ? 0L : startGrpId();
      List<GrpBaseinfo> grpList = grpService.getNeedRcmdGroup(startGrpId, SIZE);
      if (CollectionUtils.isEmpty(grpList)) {
        super.closeOneTimeTask();
      } else {
        for (GrpBaseinfo grpBaseinfo : grpList) {
          try {
            GrpKwDisc grpKwDisc = grpService.getGrpKwDisc(grpBaseinfo.getGrpId());
            if (grpKwDisc != null && grpKwDisc.getSecondCategoryId() != null) {
              List<String> grpKwList = this.SplitGrpkeyWords(grpKwDisc.getKeywords()); // 群组关键词
              List<Long> grpPubPsnList = grpService.getGrpPubAuthor(grpBaseinfo.getGrpId()); // 群组成果作者
              List<Long> psnIdList = grpService.getPsnIdByCatId(grpKwDisc.getSecondCategoryId());// 领域相同的系统人员
              List<Long> memberIdList = grpService.getGrpMemberIdByGrpId(grpBaseinfo.getGrpId());// 群组现有成员
              for (Long psnId : psnIdList) {
                Boolean isGrpmember = false;
                Integer rcmdScore = 3;// 领域相同+3
                if (12 == grpBaseinfo.getGrpCategory()) {
                  rcmdScore += 3;
                }
                // 关键词相同个数
                if (!CollectionUtils.isEmpty(grpKwList)) {
                  Integer sameKeywordCount = grpService.getSameKeywords(grpKwList, psnId);
                  rcmdScore += sameKeywordCount;
                }
                if (grpPubPsnList != null && grpPubPsnList.size() > 0 && grpPubPsnList.contains(psnId)) {
                  rcmdScore += 1;// 群组内成果的作者+1
                }
                if (memberIdList.contains(psnId)) {
                  isGrpmember = true;
                }
                grpService.saveGrpRcmd(psnId, grpBaseinfo.getGrpId(), rcmdScore, isGrpmember);
              }
            }
          } catch (Exception e) {
            logger.error("群组推荐出错-----grpId" + grpBaseinfo.getGrpId());
          }

        }
        grpService.upAppSettingConstant(AppSettingConstants.GRP_RCMD_START, grpList.get(grpList.size() - 1).getGrpId());
      }
    } catch (Exception e) {
      logger.error("InstrestGrpRcmdTask出错------", e);
    }
  }

  private Long startGrpId() {
    return AppSettingContext.getLongValue(AppSettingConstants.GRP_RCMD_START);
  }

  private List<String> SplitGrpkeyWords(String keywords) {
    List<String> grpKwList = new ArrayList<String>();
    if (StringUtils.isNotBlank(keywords)) {
      String[] str = keywords.split(";");
      if (str != null && str.length > 0) {
        for (String s : str) {
          if (StringUtils.isNotBlank(s)) {
            grpKwList.add(s.replaceAll("(\\*|/|\\<|\\>|\\^|\\[|\\])", "").replaceAll("77&", ";").toLowerCase());
          }
        }
      }
    }
    return grpKwList;
  }
}
