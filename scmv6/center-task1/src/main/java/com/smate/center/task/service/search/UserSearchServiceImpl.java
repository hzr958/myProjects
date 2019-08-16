package com.smate.center.task.service.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.psn.UserSearchDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.search.UserSearch;
import com.smate.center.task.model.search.UserSearchDataForm;
import com.smate.center.task.model.sns.quartz.Institution;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.core.base.utils.model.security.Person;


/**
 * 检索用户.
 * 
 * @author liqinghua
 * 
 */
@Service("userSearchService")
@Transactional(rollbackFor = Exception.class)
public class UserSearchServiceImpl implements UserSearchService {

  /**
   * 
   */
  private static final long serialVersionUID = 853036418355892270L;
  private static final String EN_WORD = "[a-zA-Z_0-9]{1,}";
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private UserSearchDao userSearchDao;
  @Autowired
  private PsnPrivateDao psnPrivateDao;


  /**
   * 首页-页脚链接检索人员.
   * 
   * @param maxSize
   * @return
   * @throws DaoException
   * @throws ServcieException
   */
  @Override
  public List<UserSearchDataForm> findIndexUserList(String userInfo, String locale, int maxSize)
      throws ServiceException {
    List<UserSearchDataForm> dataFormList = null;
    try {
      List<UserSearch> list = userSearchDao.findIndexUserList(userInfo, locale, maxSize);
      dataFormList = this.transferUserList(list);
    } catch (DaoException e) {
      logger.error("检索公开用户可以检索的用户列表(无条件检索)", e);
      throw new ServiceException(e);
    }

    return dataFormList;
  }

  /**
   * 将用户列表转换封装<不对列表截取列表数以免过滤掉符合请求信息但分数较低的记录>_MJG_SCM-5909.
   * 
   * @param list
   * @return
   */
  private List<UserSearchDataForm> transferUserList(List<UserSearch> list) {
    List<UserSearchDataForm> dataFormList = null;
    if (CollectionUtils.isNotEmpty(list)) {
      dataFormList = new ArrayList<UserSearchDataForm>();
      List<Long> psnIdList = new ArrayList<Long>();
      for (UserSearch userSearch : list) {
        if (psnIdList.contains(userSearch.getPsnId())) {
          continue;
        }
        // 构建用户检索form对象.
        dataFormList.add(this.buildDataForm(userSearch));
        psnIdList.add(userSearch.getPsnId());
      }
    }
    return dataFormList;
  }

  @Override
  public void saveUserSearch(Person psn, String zhName, String enName, Integer nodeId, Integer isPrivate,
      Institution ins, int scoreNum) throws ServiceException {
    try {
      UserSearch user = userSearchDao.get(psn.getPersonId());
      if (user == null) {
        user = new UserSearch();
        user.setPsnId(psn.getPersonId());
      }
      if (isPrivate == null) {
        isPrivate = 0;
      }
      user.setIsPrivate(isPrivate);
      user.setNodeId(nodeId);
      user.setZhInfo(zhName);
      user.setEnInfo(StringUtils.substring(enName, 0, 100));
      user.setPubFlag(0);
      user.setScmFlag(0);
      user.setIndexFlag(1);
      user.setScoreNum(scoreNum);
      user.setSelfLogin(psn.getIsLogin());
      // 增加非空判断处理_MJG_SCM-5211_20140526.
      if (ins != null) {
        user.setInsNameZh(ins.getZhName());
        user.setInsNameEn(ins.getEnName());
        user.setInsNameAbbr(ins.getAbbreviation());
        user.setInsId(ins.getId());
      } else {
        user.setInsNameZh(psn.getZhName());
        user.setInsNameEn(psn.getEnName());
      }
      this.userSearchDao.save(user);
    } catch (Exception e) {
      logger.error("保存用户检索信息", e);
      throw new ServiceException(e);
    }
  }



  /**
   * 根据人员ID检查其是否设置为隐私(因谢玉寿修改person.is_private到psn_private表后，隐私设置是后台手动处理，不适合同步) _MJG_SCM-5534.
   * 
   * @param psnId
   * @return true-已设置为隐私不可见;false-公开可见.
   * @throws DaoException
   */
  public boolean isPsnPrivate(Long psnId) throws ServiceException {
    try {
      Long count = psnPrivateDao.isPsnPrivate(psnId);
      if (count != null && count.longValue() > 0) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      logger.error("判断是否隐私人员出错,pnsId=" + psnId, e);
      throw new ServiceException(e);
    }
  }


  /**
   * 构建用户检索form对象.
   * 
   * @param userSearch
   * @return
   */
  private UserSearchDataForm buildDataForm(UserSearch userSearch) {
    UserSearchDataForm form = new UserSearchDataForm();
    Long psnId = userSearch.getPsnId();
    // 如果人员未设置为隐私，才进行转换封装_MJG_SCM-5534.
    if (!this.isPsnPrivate(psnId)) {
      form.setPsnId(userSearch.getPsnId());
      form.setZhInfo(userSearch.getZhInfo());
      form.setEnInfo(userSearch.getEnInfo());
      form.setScoreNum(userSearch.getScoreNum());
      form.setInsId(userSearch.getInsId());
      form.setInsNameAbbr(userSearch.getInsNameAbbr());
      form.setInsNameEn(userSearch.getInsNameEn());
      form.setInsNameZh(userSearch.getInsNameZh());
    }
    return form;
  }



}
