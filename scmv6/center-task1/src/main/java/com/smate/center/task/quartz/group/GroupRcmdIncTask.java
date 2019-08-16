package com.smate.center.task.quartz.group;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.grp.GrpBaseinfo;
import com.smate.center.task.model.grp.GrpKwDisc;
import com.smate.center.task.service.group.GrpService;

/**
 * 更新、新增群组推荐任务
 * 
 * @author zll
 *
 */
public class GroupRcmdIncTask extends TaskAbstract {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 1000; // 每次处理的个数
  @Autowired
  private GrpService grpService;

  public GroupRcmdIncTask() {

  }

  public GroupRcmdIncTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========GroupRcmdIncTask 已关闭==========");
      return;
    }
    try {
      List<Long> grpIds = grpService.getbatchhandleIdList(SIZE);
      if (CollectionUtils.isEmpty(grpIds)) {
        logger.info("=========群组已推荐完==========");
        return;
      } else {
        for (Long grpId : grpIds) {
          try {
            GrpBaseinfo grpBaseInfo = grpService.getGrpBaseInfo(grpId);
            if (grpBaseInfo == null) {
              grpService.updateTaskStatus(grpId, 9, "群组不存在");
              continue;
            }
            if ("99".equals(grpBaseInfo.getStatus())) {// 已删除的群组将没有加入群组的推荐记录设置为99
              grpService.updateGrpRcmd(grpId);
              grpService.updateTaskStatus(grpId, 1, "处理成功");
            } else {
              grpService.deleteGrpRcmd(grpId);// 删除这个群组之前所有未加入群组的推荐记录
              GrpKwDisc grpKwDisc = grpService.getGrpKwDisc(grpId);
              if (grpKwDisc != null && grpKwDisc.getSecondCategoryId() != null) {
                List<String> grpKwList = this.SplitGrpkeyWords(grpKwDisc.getKeywords()); // 群组关键词
                List<Long> grpPubPsnList = grpService.getGrpPubAuthor(grpId); // 群组成果作者
                List<Long> psnIdList = grpService.getPsnIdByCatId(grpKwDisc.getSecondCategoryId());// 领域相同的系统人员
                List<Long> memberIdList = grpService.getGrpMemberIdByGrpId(grpId);// 群组现有成员
                for (Long psnId : psnIdList) {
                  Boolean isGrpmember = false;
                  Integer rcmdScore = 3;// 领域相同+3
                  if (12 == grpBaseInfo.getGrpCategory()) {
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
                  grpService.saveGrpRcmd(psnId, grpId, rcmdScore, isGrpmember);
                }
              }
            }
          } catch (Exception e) {
            logger.error("群组推荐出错-----grpId" + grpId);
            grpService.updateTaskStatus(grpId, 9, e.getMessage());
          }
          grpService.updateTaskStatus(grpId, 1, "处理成功");
        }
      }
    } catch (Exception e) {
      logger.error("GroupRcmdIncTask出错------", e);
    }
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
