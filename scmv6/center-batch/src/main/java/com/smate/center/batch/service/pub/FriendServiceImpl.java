package com.smate.center.batch.service.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.mail.InviteInboxDao;
import com.smate.center.batch.dao.sns.prj.FriendDao;
import com.smate.center.batch.dao.sns.psn.FriendTempDao;
import com.smate.center.batch.dao.sns.psn.SyncPersonDao;
import com.smate.center.batch.dao.sns.pub.FriendFappraisalRecDao;
import com.smate.center.batch.dao.sns.pub.FriendFappraisalSendDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.InviteInbox;
import com.smate.center.batch.model.sns.pub.FriendTemp;
import com.smate.center.batch.model.sns.pub.SyncPerson;
import com.smate.core.base.utils.common.HqlUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 好友服务接口实现类.
 * 
 * @author lichangwen
 * 
 */
@Service("friendService")
@Transactional(rollbackFor = Exception.class)
public class FriendServiceImpl implements FriendService {
  /**
   *
   */
  private static final long serialVersionUID = 5147494499557288575L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private FriendTempDao friendTempDao;
  @Autowired
  private InviteInboxDao inviteInboxDao;
  @Autowired
  private FriendFappraisalRecDao friendFappraisalRecDao;
  @Autowired
  private FriendFappraisalSendDao friendFappraisalSendDao;
  @Autowired
  private SyncPersonDao syncPersonDao;

  @Override
  public int isPsnFirend(Long psnId, boolean isLocale) throws ServiceException {
    Long userId = SecurityUtils.getCurrentUserId();
    boolean res = false;
    res = this.isPsnFirend(userId, psnId);
    return res ? 1 : 0;
  }

  @Override
  public boolean isPsnFirend(Long curPsnId, Long psnId) throws ServiceException {
    try {
      Long count = friendDao.isFriend(curPsnId, psnId);
      if (count != null && count > 0)
        return true;
      else
        return false;
    } catch (Exception e) {
      throw new ServiceException(String.format("查询传入的psnId=%s,是否是登录者好友出错", psnId), e);
    }
  }

  @Override
  public Map<Long, Integer> isPsnFirend(Long curPsnId, List<Long> psnIds) throws ServiceException {

    Map<Long, Integer> map = new HashMap<Long, Integer>();
    if (psnIds == null || psnIds.size() == 0) {
      return map;
    }
    // 初始化默认为不是好友
    for (Long psnId : psnIds) {
      map.put(psnId, 0);
    }
    // 替换是好友的人员
    List<Long> friendPsnIds = this.friendDao.filterFriendPsn(curPsnId, psnIds);
    for (Long fpsnId : friendPsnIds) {
      map.put(fpsnId, 1);
    }
    return map;
  }

  @Override
  public FriendTemp getFriendTempByInviteId(Long inviteId) throws ServiceException {

    return this.friendTempDao.get(inviteId);
  }

  @Override
  public boolean checkInviteIsValid(Long inboxId, Long inviteId) throws ServiceException {
    if (inboxId != null) {
      InviteInbox inviteInbox = this.inviteInboxDao.get(inboxId);
      if (inviteInbox.getPsnId() != -1) {
        return false;
      }
    }
    FriendTemp friendTmp = getFriendTempByInviteId(inviteId);
    return (friendTmp != null);
  }

  @Override
  public void updateFriendInvite(Long inviteId, Long tempPsnId) throws ServiceException {

    FriendTemp friendTemp = friendTempDao.get(inviteId);

    friendTemp.setTempPsnId(tempPsnId);
    friendTempDao.save(friendTemp);

  }

  @Override
  public void syncPersonFappraisal(Person person) {
    try {
      this.friendFappraisalRecDao.syncPersonFappraisalRec(person);
      this.friendFappraisalSendDao.syncPersonFappraisalSend(person);
    } catch (Exception e) {
      logger.error("更新人员的评价信息", e);
    }

  }

  @Override
  public void updatePersonInfo(Person person) {
    try {
      SyncPerson syncPerson = syncPersonDao.get(person.getPersonId());
      if (syncPerson != null) {
        syncPerson.setPsnName(person.getName());
        // 提取姓名首字母
        Character c = this.getFirstLetterByName(syncPerson.getPsnName());
        syncPerson.setFirstLetter(c != null ? c.toString() : "0");
        syncPerson.setPsnFirstName(person.getFirstName());
        syncPerson.setPsnLastName(person.getLastName());
        syncPerson.setPsnOtherName(person.getOtherName());
        syncPerson.setPsnTitle(person.getTitolo());
        syncPerson.setPsnHeadUrl(person.getAvatars());
        syncPerson.setPsnTel(person.getTel());
        syncPerson.setPsnMobile(person.getMobile());
        syncPerson.setPsnQQ(person.getQqNo());
        syncPerson.setPsnMsn(person.getMsnNo());
        syncPerson.setPsnEmail(person.getEmail());
        syncPerson.setPsnInsName(person.getInsName());
        syncPerson.setPsnSkype(person.getSkype());
        if (person.getIsSyncIns() != null && person.getIsSyncIns()) {
          syncPerson.setRegionId(person.getRegionId());
          syncPerson.setPsnInsIdList(HqlUtils.insIdsFormat(person.getInsIdList()));
          if (person.getInsNameList() != null) {
            syncPerson
                .setPsnInsNameList(person.getInsNameList().length() > 500 ? person.getInsNameList().substring(0, 500)
                    : person.getInsNameList());
          }
        }
        syncPersonDao.save(syncPerson);
      }
    } catch (Exception e) {
      logger.error("同步个人信息出错", e);
    }
  }

  /**
   * 提取名称首字母.
   * 
   * @param name
   * @return
   */
  private Character getFirstLetterByName(String name) {
    if (name != null && name.length() > 0) {
      HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
      format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

      char[] names = name.trim().toCharArray();
      for (char c : names) {
        if (Character.isLetter(c)) {
          if (String.valueOf(c).matches("^[a-zA-Z]{1}$")) {
            return Character.toUpperCase(c);
          }
          try {
            String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
            if (pinyin != null && pinyin.length > 0) {
              return (StringUtils.upperCase((pinyin[0])).toCharArray())[0];
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }

    return null;
  }
}
