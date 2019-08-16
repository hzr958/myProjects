package com.smate.web.psn.service.cooperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.dao.cooperation.PsnCopartnerDao;
import com.smate.web.psn.dao.friend.FriendDao;
import com.smate.web.psn.dao.friend.FriendReqRecordDao;
import com.smate.web.psn.model.cooperation.PsnKnowCopartnerForm;

/**
 * 
 * 合作者信息列表
 * 
 * @author zx
 *
 */
@Service("cooperationService")
@Transactional(rollbackFor = Exception.class)
public class CooperationServiceImpl implements CooperationService {
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private FriendReqRecordDao friendReqRecordDao;
  @Autowired
  private PsnCopartnerDao psnCopartnerDao;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 项目合作者
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Page findPsnKnowCopartner(Page page, PsnKnowCopartnerForm form, Integer cptType) throws ServiceException {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      Long currentUserId = Long.valueOf(ServiceUtil.decodeFromDes3(form.getDes3CurrentId()));
      if (!currentUserId.equals(psnId)) {
        form.setPsnId(currentUserId);
      } else {
        form.setPsnId(psnId);
      }
      List<Long> copartnerIdList = new ArrayList<Long>();
      List<Long> strangeList = new ArrayList<Long>();
      List<Long> requestList = new ArrayList<Long>();
      List<Long> friendIds = new ArrayList<Long>();
      // 第一档;不是好友且没发送过好友请求的
      strangeList = psnCopartnerDao.getCopartner(page, cptType, form.getPsnId(), "1");
      // 第二档;不是好友且发送过好友请求的
      // 1.先查出不是好友的合作者
      List<Long> notFriendIds = psnCopartnerDao.getCopartner(page, cptType, form.getPsnId(), "2");
      if (CollectionUtils.isNotEmpty(notFriendIds)) {
        // 2,找出发送过好友请求的合作者
        requestList = friendReqRecordDao.orderByIds(notFriendIds, form.getPsnId());
      }
      // 第三档;是好友的
      friendIds = psnCopartnerDao.getCopartner(page, cptType, form.getPsnId(), "3");

      if (CollectionUtils.isEmpty(copartnerIdList)) {
        copartnerIdList = new ArrayList<Long>();
      }
      if (CollectionUtils.isEmpty(requestList)) {
        requestList = new ArrayList<Long>();
      }
      if (CollectionUtils.isEmpty(friendIds)) {
        friendIds = new ArrayList<Long>();
      }
      copartnerIdList.addAll(strangeList);
      copartnerIdList.addAll(requestList);
      copartnerIdList.addAll(friendIds);

      if (CollectionUtils.isNotEmpty(copartnerIdList)) {
        List<Person> personList = new ArrayList<Person>();
        // 把psn里面排序
        List<Person> psnList = personProfileDao.personByPsnIdsList(copartnerIdList);
        if (psnList != null) {
          {
            for (int i = 0; i < copartnerIdList.size(); i++) {
              for (Person person : psnList) {
                if (copartnerIdList.get(i).equals(person.getPersonId())) {
                  personList.add(person);
                }
              }
            }

          }
        }
        // 设置person里面好友
        if (CollectionUtils.isNotEmpty(personList)) {
          List<Long> psnFreindIds = friendDao.getFriendId(psnId);
          if (CollectionUtils.isNotEmpty(psnFreindIds)) {
            // 排除自己
            psnFreindIds.add(psnId);
            for (int i = 0; i < psnFreindIds.size(); i++) {
              for (Person person : personList) {
                if (psnFreindIds.get(i).equals(person.getPersonId())) {
                  person.setIsFriend(true);
                }
              }
            }
          }
          bulidBaseInfo(personList);
        }
        if (form.getFirstPage()) {// 查5个
          personList = personList.stream().limit(5).collect(Collectors.toList());
        } else {// 查全部
          Integer count = personList.size();
          page.setTotalCount(count);
          Integer pageNo = page.getPageNo();
          personList = personList.stream().skip((pageNo - 1) * 10).limit(10).parallel().collect(Collectors.toList());
        }
        page.setResult(personList);
      } else {
        page.setTotalCount(0);
      }
      // 基础信息的封装
    } catch (Exception e) {
      logger.error("查询合作者出错,psnId=" + form.getPsnId(), e);
    }
    return page;
  }

  private void bulidBaseInfo(List<Person> person) {
    Locale locale = LocaleContextHolder.getLocale();
    try {
      for (int i = 0; i < person.size(); i++) {
        Person item = person.get(i);
        this.bulidInformation(item, locale);
      }
    } catch (Exception e) {
      logger.error("构建基础信息出错", e);
    }

  }

  /**
   * 基本信息的拼接
   */
  private void bulidInformation(Person item, Locale locale) throws Exception {

    String name = "";
    String insName = "";
    if ("zh".equalsIgnoreCase(locale.getLanguage())) {
      name = StringUtils.isNotBlank(item.getName()) ? item.getName() : item.getEname();
      /* insName = StringUtils.isNotBlank(item.getInsName()) ? item.getInsName() : item.getEnInsName(); */
      item.setPersonName(name);
      /* item.setInsName(insName); */
    } else if ("en".equalsIgnoreCase(locale.getLanguage())) {
      name = item.getLastName() + item.getFirstName();
      name = StringUtils.isNotBlank(item.getEname()) ? item.getEname() : item.getName();
      /*
       * insName = StringUtils.isNotBlank(item.getEnInsName()) ? item.getEnInsName() : item.getInsName();
       */
      item.setPersonName(name);
      /* item.setInsName(insName); */
    }
  }
}
