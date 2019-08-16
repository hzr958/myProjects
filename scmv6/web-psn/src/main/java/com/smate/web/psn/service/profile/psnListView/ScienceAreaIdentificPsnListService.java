package com.smate.web.psn.service.profile.psnListView;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.dao.friend.FriendDao;
import com.smate.web.psn.dao.keywork.ScienceAreaIdentificationDao;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.psninfo.PsnInfo;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.PsnDisciplineKeyService;

/**
 * 科技领域认同人员列表服务类
 *
 * @author wsn
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ScienceAreaIdentificPsnListService extends PsnListViewBaseService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonManager personManager;
  @Autowired
  private ScienceAreaIdentificationDao scienceAreaIdentificationDao;
  @Autowired
  private PsnDisciplineKeyService psnDisciplineKeyService;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private FriendService friendService;
  @Autowired
  private FriendDao friendDao;

  @Override
  public String doVerifyData(PsnListViewForm form) {
    // 需要校验人员ID、科技领域ID
    String verifyResult = null;
    if (form.getPsnId() == null && StringUtils.isBlank(form.getDes3PsnId())) {
      verifyResult = "psnId is miss";
    }
    if (form.getScienceAreaId() == null) {
      verifyResult = "scienceAreaId is miss";
    }
    return verifyResult;
  }

  @Override
  public void doGetPsnListViewInfo(PsnListViewForm form) throws ServiceException {
    List<PsnInfo> psnInfoList = new ArrayList<PsnInfo>();
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    try {
      form = this.scienceAreaIdentificationDao.findIdentifyPsnIdsByForm(form);
      List<Long> friendIds = friendDao.getFriendListByPsnId(currentPsnId);
      List<Long> psnIds = form.getPsnIds();
      if (psnIds != null && psnIds.size() > 0) {
        for (Long psnId : psnIds) {
          Person person = personManager.getPerson(psnId);
          if (person != null) {
            PsnInfo psnInfo = new PsnInfo();
            if (friendIds.contains(psnId) || currentPsnId.equals(psnId)) {
              psnInfo.setIsFriend(true);
            }
            psnInfo.setInsInfo(personManager.getPsnViewWorkHisInfo(psnId));
            psnInfo.setName(person.getName() == null ? person.getEname() : person.getName());
            psnInfo.setDes3PsnId(ServiceUtil.encodeToDes3(psnId.toString()));
            psnInfo.setAvatarUrl(person.getAvatars());
            psnInfo = this.buildPsnTitoloInfo(psnInfo, person);
            psnInfo = this.buildPsnKeyWords(psnInfo, psnId);
            psnInfo = this.buildPsnStatisticsInfo(psnInfo, psnId);
            psnInfo.setPerson(person);
            // psnInfoBuildFactory.buildPsnInfo(PsnInfoEnum.mobile.toInt(),
            // psnInfo);
            psnInfoList.add(psnInfo);
          }

        }
      }
    } catch (Exception Ex) {
      throw new ServiceException(Ex);
    }
    form.setPsnInfoList(psnInfoList);
  }

  /**
   * 构建人员单位+部门 职称+头衔信息
   * 
   * @param psnInfo
   * @param psn
   * @return
   */
  private PsnInfo buildPsnTitoloInfo(PsnInfo psnInfo, Person psn) {
    String insName = psn.getInsName();
    String department = psn.getDepartment();
    String position = psn.getPosition();
    String titolo = psn.getTitolo();
    // 构建单位+部门信息
    if (StringUtils.isBlank(insName) || StringUtils.isBlank(department)) {
      psnInfo.setInsAndDep(
          (StringUtils.isBlank(insName) ? "" : insName) + (StringUtils.isBlank(department) ? "" : department));
    } else {
      psnInfo.setInsAndDep(insName + ", " + department);
    }
    // 构建职称+头衔信息
    if (StringUtils.isBlank(position) || StringUtils.isBlank(titolo)) {
      psnInfo.setPosAndTitolo(
          (StringUtils.isBlank(position) ? "" : position) + (StringUtils.isBlank(titolo) ? "" : titolo));
    } else {
      psnInfo.setPosAndTitolo(position + ", " + titolo);
    }
    return psnInfo;
  }

  /**
   * 构建人员关键词信息
   * 
   * @param psnInfo
   * @param psnId
   * @return
   */
  private PsnInfo buildPsnKeyWords(PsnInfo psnInfo, Long psnId) {
    List<PsnDisciplineKey> keyList = psnDisciplineKeyService.findPsnKeyWords(psnId);
    psnInfo.setDiscList(keyList);
    return psnInfo;
  }

  /**
   * 构建人员统计信息
   * 
   * @param psnInfo
   * @param psnId
   * @return
   */
  private PsnInfo buildPsnStatisticsInfo(PsnInfo psnInfo, Long psnId) {
    // TODO 取公开的成果和项目数
    PsnStatistics psnSta = psnStatisticsDao.getPubAndPrjNum(psnId);
    // psnSta，getPubSum，getPrjSum都要做非空判断，防止出现空指针问题
    if (psnSta == null) {
      psnInfo.setPubSum(0);
      psnInfo.setPrjSum(0);
    } else {
      psnInfo.setPubSum(psnSta.getPubSum() == null ? 0 : psnSta.getPubSum());
      psnInfo.setPrjSum(psnSta.getPrjSum() == null ? 0 : psnSta.getPrjSum());
    }
    return psnInfo;
  }

}
