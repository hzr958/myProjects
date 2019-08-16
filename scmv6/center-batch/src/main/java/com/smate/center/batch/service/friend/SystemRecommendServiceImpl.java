package com.smate.center.batch.service.friend;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.dao.sns.friend.FriendSysRecommendDao;
import com.smate.center.batch.model.friend.FriendSysRecommend;
import com.smate.center.batch.model.psn.register.PersonRegister;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;

/**
 * @author lcw
 * 
 */
@Service("systemRecommendService")
public class SystemRecommendServiceImpl implements SystemRecommendService {

  private static final long serialVersionUID = -6165310015131587881L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private FriendSysRecommendDao friendSysRecommendDao;

  @Override
  public void instantRecommend(PersonRegister person) throws Exception {
    try {
      if (person == null || person.getPersonId() == null || person.getInsId() == null) {
        return;
      }
      Long psnId = person.getPersonId();
      Page<WorkHistory> page = new Page<WorkHistory>(1000);
      page = workHistoryDao.findWork(person.getInsId(), person.getPersonId(), page);
      if (page.getTotalCount() <= 0)
        return;
      Long totalPage = page.getTotalPages();
      for (int i = 1; i <= totalPage; i++) {
        if (i > 1) {
          page.setPageNo(i);
          page = workHistoryDao.findWork(person.getInsId(), person.getPersonId(), page);
        }
        List<WorkHistory> list = page.getResult();
        for (WorkHistory temp : list) {
          WorkHistory psnWork = new WorkHistory();
          psnWork.setPsnId(person.getPersonId());
          if (person.getInsId() != null)
            psnWork.setInsId(person.getInsId());
          if (StringUtils.isNotBlank(person.getInsName()))
            psnWork.setInsName(person.getInsName());
          if (StringUtils.isNotBlank(person.getUnit()))
            psnWork.setDepartment(person.getUnit());
          if (StringUtils.isNotBlank(person.getFromYear()))
            psnWork.setFromYear(Long.valueOf(person.getFromYear()));
          if (StringUtils.isNotBlank(person.getFromMonth()))
            psnWork.setFromMonth(Long.valueOf(person.getFromMonth()));
          if (StringUtils.isNotBlank(person.getStudyToYear()))
            psnWork.setToYear((Long.valueOf(person.getStudyToYear())));
          if (StringUtils.isNotBlank(person.getStudyToMonth()))
            psnWork.setToMonth(Long.valueOf(person.getStudyToMonth()));
          double workScore = RecommendAlgorithmUtils.workSecore(psnWork, temp);
          if (workScore == 0)
            continue;
          FriendSysRecommend sysRecommend = new FriendSysRecommend();
          sysRecommend.setRecommendType(String.valueOf(RecommendType.RECOMMEND_WORK));// 工作经历
          sysRecommend.setScore(workScore);// 推荐度得分
          boolean isMax = friendSysRecommendDao.isMax(psnId);
          if (!isMax) {
            sysRecommend = this.psnToSysRecommend(sysRecommend, psnId, null, temp.getPsnId());
            FriendSysRecommend recommend = friendSysRecommendDao.getFriendSysRecommend(psnId, temp.getPsnId());
            if (sysRecommend != null && recommend == null)
              friendSysRecommendDao.save(sysRecommend);
          } else {// 如果超出100，则替换分数较小的
            FriendSysRecommend recommendMin = friendSysRecommendDao.getRecommendMin(psnId);
            if (recommendMin != null && recommendMin.getScore() < sysRecommend.getScore()) {
              recommendMin.setScore(sysRecommend.getScore());
              friendSysRecommendDao.save(recommendMin);
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("即时智能推荐好友出错", e);
      throw new Exception(e);
    }
  }

  private FriendSysRecommend psnToSysRecommend(FriendSysRecommend newRecommend, Long psnId, Integer tempNode,
      Long tempPsnId) {
    try {
      Person tempPerson = personManager.getPersonByRecommend(tempPsnId);
      if (tempPerson == null)
        return null;
      newRecommend.setPsnId(psnId);
      newRecommend.setTempPsnId(tempPerson.getPersonId());
      newRecommend.setTempPsnName(tempPerson.getName());
      newRecommend.setTempPsnFirstName(tempPerson.getFirstName());
      newRecommend.setTempPsnLastName(tempPerson.getLastName());
      newRecommend.setTempPsnHeadUrl(tempPerson.getAvatars());
      newRecommend.setTempPsnTitel(tempPerson.getViewTitolo());
      return newRecommend;
    } catch (Exception e) {
      logger.error("根据psnId:{},tempNode:{},tempPsnId:{}查询person出错", new Object[] {psnId, tempNode, tempPsnId}, e);
      return null;
    }

  }

}
