package com.smate.web.psn.service.friend;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.build.factory.PsnInfoBuildFactory;
import com.smate.web.psn.build.factory.PsnInfoEnum;
import com.smate.web.psn.dao.attention.AttPersonDao;
import com.smate.web.psn.dao.friend.FriendDao;
import com.smate.web.psn.dao.recommend.FriendSysRecommendDao;
import com.smate.web.psn.dao.recommend.GroupPsnRecommendDao;
import com.smate.web.psn.exception.PsnBuildException;
import com.smate.web.psn.model.friend.InviteJionForm;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 群组-成员-邀请好友加入接口实现类
 * 
 * @author lhd
 */
@Service("invitefriendJionService")
@Transactional(rollbackFor = Exception.class)
public class InvitefriendJionServiceImpl implements InvitefriendJionService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnInfoBuildFactory psnInfoBuildFactory;
  @Autowired
  private GroupPsnRecommendDao groupPsnRecommendDao;
  @Autowired
  private FriendSysRecommendDao friendSysRecommendDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private AttPersonDao attPersonDao;

  /** 群组-成员-邀请成员加入 */
  @Override
  public void getFriendJionList(InviteJionForm form) throws Exception {
    List<PsnInfo> psnInfos = new ArrayList<PsnInfo>();
    // 1.查询群组人员推荐表
    List<Long> recommendPsnIdList = groupPsnRecommendDao.findRecommendPsnIdByGroupId(form);
    // 2.不足10人,查询个人好友表
    if (recommendPsnIdList.size() < 10) {
      List<Long> friendIds = friendDao.getFriendIds(form, 10 - recommendPsnIdList.size());
      if (CollectionUtils.isNotEmpty(friendIds)) {
        for (Long friendId : friendIds) {
          if (!recommendPsnIdList.contains(friendId)) {
            recommendPsnIdList.add(friendId);
          }
        }
      }
    }
    // 3.再不足10人,查询个人系统好友推荐表
    if (recommendPsnIdList.size() < 10) {
      List<Long> sysFriendIds = friendSysRecommendDao.findFriendSysRecommend(form, 10 - recommendPsnIdList.size());
      if (CollectionUtils.isNotEmpty(sysFriendIds)) {
        for (Long sysFriendId : sysFriendIds) {
          if (!recommendPsnIdList.contains(sysFriendId)) {
            recommendPsnIdList.add(sysFriendId);
          }
        }
      }
    }
    // List<Long> fiveFriendIdList = friendDao.getFiveFriend(form);
    // // 不足5人时,补充推荐人员
    // if (fiveFriendIdList.size() < 5) {
    // List<Long> personIds = friendDao.findLocalPersonIds(form.getPsnId(),
    // 5-fiveFriendIdList.size(), 0l,form.getGroupId());
    // if (CollectionUtils.isNotEmpty(personIds)) {
    // for (Long friendId : personIds) {
    // fiveFriendIdList.add(friendId);
    // }
    // }
    // }
    try {
      if (CollectionUtils.isNotEmpty(recommendPsnIdList)) {
        for (Long psnId : recommendPsnIdList) {
          PsnInfo psnInfo = new PsnInfo();
          Person person = personDao.findPersonInsAndPos(psnId);
          psnInfo.setPerson(person);
          psnInfo.setDes3PsnId(ServiceUtil.encodeToDes3(person.getPersonId() + ""));
          psnInfoBuildFactory.buildPsnInfo(PsnInfoEnum.groupFriend.toInt(), psnInfo);
          psnInfos.add(psnInfo);
        }
      }
      form.setPsnInfoList(psnInfos);
    } catch (PsnBuildException e) {
      logger.error("装饰模式构建人员信息失败，psnId=" + form.getPsnId());
      throw new PsnBuildException(e);
    }
  }

  @Override
  public List<Map<String, Object>> getFriendNames(InviteJionForm form) throws Exception {
    HttpServletRequest request = Struts2Utils.getRequest();
    String parameter = request.getParameter("q");
    if (StringUtils.isBlank(parameter)) {
      parameter = form.getSearchKey();
    }
    List<Long> excludePsnIds = buildExcludePsnIdList(form.getDes3PsnId());
    // 关注页面的要排除已经关注的
    if (form.getFromAttention()) {
      // 默认10个SCM-15114
      return friendDao.findFriendNamesExcludeAttention(form.getPsnId(), parameter, excludePsnIds, 10);
    }
    // 默认10个SCM-15114
    return friendDao.findFriendNames(form.getPsnId(), parameter, excludePsnIds, 10);
    // return friendDao.getFriendNames(form.getPsnId(), parameter, 4);
  }

  // 构建排除的人员id
  private List<Long> buildExcludePsnIdList(String psnIds) {
    List<Long> excludePsnIds = null;
    if (StringUtils.isNotBlank(psnIds)) {
      String[] splitPsnIds = psnIds.split(",");
      excludePsnIds = new ArrayList<>();
      for (String psnIdStr : splitPsnIds) {
        long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(psnIdStr));
        excludePsnIds.add(psnId);
      }
      if (excludePsnIds.size() > 1000) {
        excludePsnIds.subList(0, 1000);
      }
    }
    return excludePsnIds;
  }

  @Override
  public void getMyFriendNames(InviteJionForm form) throws Exception {
    List<PsnInfo> psnInfoList = null;
    List<BigDecimal> friendList = friendDao.getFriendList(form);
    if (CollectionUtils.isNotEmpty(friendList)) {
      psnInfoList = new ArrayList<PsnInfo>();
      PsnInfo psnInfo = null;
      Person p = null;
      // 关注页面的要排除已经关注的
      if (form.getFromAttention()) {
        List<Long> attentionPsnIds = attPersonDao.getFollowingPsnIds(SecurityUtils.getCurrentUserId());
        friendList = friendList.stream().filter(aPsnId -> !attentionPsnIds.contains(Long.valueOf(aPsnId.longValue())))
            .collect(Collectors.toList());
        // if (CollectionUtils.isNotEmpty(attentionPsnIds)) {
        // for (Long aPsnId : attentionPsnIds) {
        // friendList.remove(BigDecimal.valueOf(aPsnId));
        // }
        // }
      }
      Locale locale = LocaleContextHolder.getLocale();
      for (BigDecimal psnId : friendList) {
        psnInfo = new PsnInfo();
        p = personDao.findPsnInfoWithInsInfo(NumberUtils.toLong(psnId.toString()));
        if (p != null) {
          psnInfo.setPerson(p);
          psnInfo.setAvatarUrl(p.getAvatars());
          // 检索判断,有关键词是，不包含则继续下一个人员
          if (StringUtils.isNotBlank(form.getSearchKey())) {
            boolean containsSearchKey = StringUtils.containsIgnoreCase(p.getName(), form.getSearchKey())
                || StringUtils.containsIgnoreCase(p.getEname(), form.getSearchKey())
                || StringUtils.containsIgnoreCase(
                    Objects.toString(p.getFirstName(), "") + " " + Objects.toString(p.getLastName(), ""),
                    form.getSearchKey());
            if (!containsSearchKey) {
              continue;
            }
          }
          if (Locale.US.equals(locale)) {
            // 名称就用name和ename字段就行
            psnInfo.setName(StringUtils.defaultIfBlank(p.getEname(), p.getName()));
            if (p.getInsId() != null) {
              Institution institution = institutionDao.get(p.getInsId());
              psnInfo.setInsName(institution != null ? institution.getEnName() : "");
            }
          } else {
            psnInfo.setName(StringUtils.defaultIfBlank(p.getName(), p.getEname()));
          }
          psnInfo.setInsName(StringUtils.isBlank(psnInfo.getInsName()) ? p.getInsName() : "");
          psnInfo.setDepartment(StringUtils.defaultIfEmpty(p.getDepartment(), ""));
          psnInfo.setPosition(StringUtils.defaultIfEmpty(p.getPosition(), ""));
          psnInfo.setDes3PsnId(p.getPersonDes3Id());
          psnInfoList.add(psnInfo);
        }
      }
      form.setPsnInfoList(psnInfoList);
    }
  }

}
