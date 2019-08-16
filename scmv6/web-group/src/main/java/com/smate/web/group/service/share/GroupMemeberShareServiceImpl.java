package com.smate.web.group.service.share;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
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
import com.smate.web.group.dao.share.GroupMemeberShareDao;
import com.smate.web.group.form.GroupShareForm;
import com.smate.web.group.model.group.psn.PsnInfo;

@Service("groupMemeberShareService")
@Transactional(rollbackFor = Exception.class)
public class GroupMemeberShareServiceImpl implements GroupMemeberShareService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GroupMemeberShareDao groupMemeberShareDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private InstitutionDao institutionDao;

  @Override
  public void getRecommendPsnIds(GroupShareForm form) throws Exception {

    List<PsnInfo> psnInfoList = null; // 存储人员信息
    List<Object> recommends = null;

    // 区分推荐，还是检索人员
    if (form.getType().equals("1")) {
      // 检索人员的功能
      recommends = groupMemeberShareDao.getSearchPsnIds(form);
    } else if (form.getType().equals("2")) {
      // 推荐人员
      recommends = groupMemeberShareDao.getRecommendPsnIds(form.getPsnId(), form.getGroupId());
    }

    // 2.根据psnId在person表中进行信息的提取
    // 2.1 判断非空
    if (recommends != null && recommends.size() > 0) {
      psnInfoList = new ArrayList<PsnInfo>();
      PsnInfo psnInfo = null;
      Person person = null;
      for (Object psnId : recommends) {
        // 2.2 进行遍历，完善人员信息
        psnInfo = new PsnInfo();
        // 2.3 根据人员id查找人员表
        person = personDao.findPersonBaseIncludeIns(Long.valueOf(psnId.toString()));
        if (person != null) {
          // 2.4 设置psnInfo属性
          psnInfo.setPerson(person);
          psnInfo.setAvatarUrl(person.getAvatars());
          Locale locale = LocaleContextHolder.getLocale();
          // 检索判断,有关键词是，不包含则继续下一个人员
          if (StringUtils.isNotBlank(form.getSearchKey())) {
            if (StringUtils.isNotBlank(person.getName())
                && StringUtils.containsIgnoreCase(person.getName(), form.getSearchKey())) {

            } else if (StringUtils.isNotBlank(person.getEname())
                && StringUtils.containsIgnoreCase(person.getEname(), form.getSearchKey())) {

            } else if (StringUtils.isNotBlank(person.getLastName()) && StringUtils.isNotBlank(person.getFirstName())
                && StringUtils.containsIgnoreCase(person.getFirstName() + " " + person.getLastName(),
                    form.getSearchKey())) {

            } else {
              continue;
            }
          }

          if (Locale.US.equals(locale)) {
            if (StringUtils.isNotBlank(person.getEname())) {
              psnInfo.setName(person.getEname());
            } else if (StringUtils.isNotBlank(person.getLastName()) && StringUtils.isNotBlank(person.getFirstName())) {
              psnInfo.setName(person.getFirstName() + " " + person.getLastName());
            } else if (StringUtils.isNotBlank(person.getLastName()) && StringUtils.isBlank(person.getFirstName())) {
              psnInfo.setName(person.getLastName());
            } else if (StringUtils.isBlank(person.getLastName()) && StringUtils.isNotBlank(person.getFirstName())) {
              psnInfo.setName(person.getFirstName());
            } else if (StringUtils.isNotBlank(person.getName())) {
              psnInfo.setName(person.getName());
            } else {
              psnInfo.setName("");
            }
            if (person.getInsId() != null) {
              Institution institution = institutionDao.get(person.getInsId());
              if (institution != null) {
                psnInfo.setInsName(institution.getEnName());
              }
            }
          } else {
            psnInfo.setInsName(person.getInsName());
            if (StringUtils.isNotBlank(person.getName())) {
              psnInfo.setName(person.getName());
            } else if (StringUtils.isNotBlank(person.getEname())) {
              psnInfo.setName(person.getEname());
            } else if (StringUtils.isNotBlank(person.getLastName()) && StringUtils.isNotBlank(person.getFirstName())) {
              psnInfo.setName(person.getFirstName() + " " + person.getLastName());
            } else if (StringUtils.isNotBlank(person.getLastName()) && StringUtils.isBlank(person.getFirstName())) {
              psnInfo.setName(person.getLastName());
            } else if (StringUtils.isBlank(person.getLastName()) && StringUtils.isNotBlank(person.getFirstName())) {
              psnInfo.setName(person.getFirstName());
            } else {
              psnInfo.setName("");
            }
          }
          psnInfo.setDes3PsnId(person.getPersonDes3Id());
          psnInfoList.add(psnInfo);
        }
      }
      form.setPsnInfoList(psnInfoList);
    }
  }

}
