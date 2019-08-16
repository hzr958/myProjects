package com.smate.web.psn.service.profile.psnListView;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.build.factory.PsnInfoBuildFactory;
import com.smate.web.psn.build.factory.PsnInfoEnum;
import com.smate.web.psn.dao.friend.FriendDao;
import com.smate.web.psn.dao.keywork.IdentificationDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.exception.PsnBuildException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.psninfo.PsnInfo;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.statistics.PsnStatisticsService;

/**
 * 研究领域关键字赞人员列表服务类
 *
 * @author wsn
 *
 */
@Transactional(rollbackFor = Exception.class)
public class KeywordIdentificPsnListService extends PsnListViewBaseService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private IdentificationDao identificationDao;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PsnInfoBuildFactory psnInfoBuildFactory;
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private FriendService friendService;

  /**
   * 研究领域关键字赞人员列表服务参数校验
   */
  @Override
  public String doVerifyData(PsnListViewForm form) {
    // 需要校验人员ID、关键字ID
    String verifyResult = null;
    if (form.getPsnId() == null && StringUtils.isBlank(form.getDes3PsnId())) {
      verifyResult = "psnId is miss";
    }
    if (form.getDiscId() == null) {
      verifyResult = "discId is miss";
    }
    return verifyResult;
  }

  /**
   * 人员列表信息获取处理
   * 
   * @throws ServiceException
   */
  @Override
  public void doGetPsnListViewInfo(PsnListViewForm form) throws ServiceException {
    List<PsnInfo> PsnInfo = new ArrayList<PsnInfo>();
    try {
      identificationDao.findIdentifyPsnIdsByForm(form);
      List<Long> psnIds = form.getPsnIds();
      if (psnIds != null && psnIds.size() > 0) {
        for (Long psnId : psnIds) {
          Person person = personManager.getPerson(psnId);
          if (person != null) {
            PsnInfo psnInfo = new PsnInfo();
            psnInfo.setPsnStatistics(psnStatisticsService.getPsnStatistics(psnId));
            person.setInsInfo(personManager.getPsnViewWorkHisInfo(psnId));
            psnInfo.setPerson(person);
            psnInfoBuildFactory.buildPsnInfo(PsnInfoEnum.mobile.toInt(), psnInfo);
            try {
              Long currentPsnId = SecurityUtils.getCurrentUserId();
              boolean isFriend = false;
              if (currentPsnId != 0L && !currentPsnId.equals(psnId)) {
                isFriend = friendDao.isFriend(currentPsnId, psnId);// 查询是否是好友
              } else if (currentPsnId != 0L && currentPsnId.equals(psnId)) {
                isFriend = true;// 自己也不能显示添加好友按钮
              }
              psnInfo.setIsFriend(isFriend);
            } catch (DaoException e) {
              logger.error("认同关键词查询认同人员列表，查询是否是好友出错,friendPsnId" + psnId);
            }
            PsnInfo.add(psnInfo);
          }

        }
      }
    } catch (PsnBuildException Ex) {
      throw new ServiceException(Ex);
    }
    form.setPsnInfoList(PsnInfo);
  }

}
