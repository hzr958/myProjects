package com.smate.web.psn.service.personsync;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.dao.profile.SyncPersonDao;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psninfo.SyncPerson;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.grp.GroupSnsService;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * 个人修改基本信息，同步到其他节点、单位.
 * 
 * @author aijiangbin
 *
 */
@Service("personSyncService")
@Transactional(rollbackOn = Exception.class)
public class PersonSyncServiceImpl implements PersonSyncService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SyncPersonDao syncPersonDao;
  @Autowired
  private FriendService friendService;
  @Autowired
  private GroupSnsService groupSnsService;



  @Override
  public void dealPersonSync(Person person, SyncPerson syncPerson) throws ServiceException {
    if (syncPerson.getPsnId() == null) {
      syncPerson = syncPersonDao.get(person.getPersonId());
    }
    if (syncPerson != null) {
      syncPerson.setPsnTitle(person.getTitolo());
      syncPerson.setRegionId(person.getRegionId());
      syncPersonDao.save(syncPerson);
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
            logger.error("提取名称首字母,name=" + name);
          }
        }
      }
    }
    return null;
  }

  @Override
  public void snsPersonSync(SnsPersonSyncMessage message) throws ServiceException {
    logger.debug("成功接收到人员信息同步psnId:{}", message.getPsnId());

    // 各自更新相应的冗余字段,李昌文补充
    updateSyncPerson(message);
    // 收件箱人员数据冗余 -不需要ajb
    // updateIndexBoxPerson(message);

    // 发件箱人员数据冗余 -不需要ajb
    // updateMailBoxPerson(message);

    // 动态消息人员数据冗余
    // updateDynMessagePerson(message);

    // 关注人员数据同步 不需要ajb---
    // updateUserSetting(message);

    // 同步人员信息到群组冗余字段---以前的表不用了
    // updateGroupMember(message);

    // 同步人员信息到项目评论 不需要ajb
    // updatePrjComment(message);
    // 同步人员信息到成果，文献评论 不需要ajb
    // updatePubComment(message);
    // 更新评价人信息 不需要ajb
    // updateFappraisal(message);
    // 更新赞的人员信息
    // updateAwardSyncPerson(message);

  }


  private void updateGroupMember(SnsPersonSyncMessage message) {
    try {
      groupSnsService.syncGroupMember(message);
    } catch (Exception e1) {
      logger.error("同步人员信息到群组冗余字段", e1);
    }
  }



  private void updateSyncPerson(SnsPersonSyncMessage message) {
    try {
      friendService.updatePersonInfo(message);
    } catch (Exception e1) {
      logger.error("sync_person", e1);
    }
  }


}
