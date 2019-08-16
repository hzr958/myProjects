package com.smate.web.psn.service.sciencearea;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.psn.PsnSolrInfoModifyService;
import com.smate.web.psn.dao.keywork.CategoryMapBaseDao;
import com.smate.web.psn.dao.keywork.CategoryScmDao;
import com.smate.web.psn.dao.keywork.PsnScienceAreaDao;
import com.smate.web.psn.dao.keywork.ScienceAreaIdentificationDao;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.keyword.CategoryMapBase;
import com.smate.web.psn.model.keyword.CategoryScm;
import com.smate.web.psn.model.keyword.PsnScienceArea;
import com.smate.web.psn.model.keyword.PsnScienceAreaInfo;
import com.smate.web.psn.model.keyword.ScienceAreaIdentification;
import com.smate.web.psn.model.profile.PsnScienceAreaForm;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.PersonalManager;

/**
 * 科技领域服务实现
 *
 * @author wsn
 * @createTime 2017年3月14日 下午6:30:45
 *
 */
@Service("scienceAreaService")
@Transactional(rollbackFor = Exception.class)
public class ScienceAreaServiceImpl implements ScienceAreaService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnSolrInfoModifyService psnSolrInfoModifyService;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Autowired
  private ScienceAreaIdentificationDao scienceAreaIdentificationDao;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PersonalManager personalManager;
  @Autowired
  private CategoryScmDao categoryScmDao;

  @Override
  public List<PsnScienceArea> findPsnScienceAreaList(Long psnId, Integer status) {
    List<PsnScienceArea> list = psnScienceAreaDao.findPsnScienceAreaList(psnId, status);
    if (CollectionUtils.isNotEmpty(list)) {
      for (PsnScienceArea area : list) {
        if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
          area.setShowScienceArea(
              StringUtils.isNotBlank(area.getEnScienceArea()) ? area.getEnScienceArea() : area.getScienceArea());
        } else {
          area.setShowScienceArea(
              StringUtils.isNotBlank(area.getScienceArea()) ? area.getScienceArea() : area.getEnScienceArea());
        }
      }
    }
    return list;
  }

  @Override
  public String savePsnScienceArea(Long psnId, String ids) {
    try {
      if (StringUtils.isNotBlank(ids)) {
        String[] idArr = ids.split(",");
        List<Integer> idList = new ArrayList<Integer>();
        if (idArr != null && ids.length() > 0) {
          for (String id : idArr) {
            if (StringUtils.isNotBlank(id)) {
              idList.add(NumberUtils.toInt(id.replaceAll(" ", "")));
            }
          }
        }
        List<CategoryMapBase> categoryList = categoryMapBaseDao.findCategoryByIds(idList);
        int areaOrder = 1;
        if (categoryList != null && categoryList.size() > 0) {
          // 先将人员所有科技领域记录置为无效状态
          this.psnScienceAreaDao.updateSAStatusByPsnId(psnId, 0);
          for (CategoryMapBase ca : categoryList) {
            PsnScienceArea area = psnScienceAreaDao.findPsnScienceAreaByPsnIdAndId(psnId, ca.getCategryId());
            if (area == null) {
              area = new PsnScienceArea();
            }
            area.setStatus(1);
            area.setPsnId(psnId);
            area.setScienceArea(ca.getCategoryZh());
            area.setScienceAreaId(ca.getCategryId());
            area.setEnScienceArea(ca.getCategoryEn());
            area.setAreaOrder(areaOrder);
            area.setUpdateDate(new Date());
            psnScienceAreaDao.save(area);
            areaOrder++;
          }
        }
      } else {
        // 将人员所有科技领域记录置为无效状态
        this.psnScienceAreaDao.updateSAStatusByPsnId(psnId, 0);
      }
      // 更新人员solr信息
      // psnSolrInfoModifyService.updateSolrPsnInfo(psnId);
      personalManager.refreshPsnSolrInfoByTask(psnId);
    } catch (Exception e) {
      logger.error("保存人员科技邻域信息出错， psnId=" + psnId + ", ids=" + ids, e);
      return "error";
    }
    return "success";
  }

  @Override
  public Integer saveIdentificationScienceArea(Long psnId, Integer scienceAreaId, Long friendId, Integer idenSum) {
    Integer sum = null;
    try {
      ScienceAreaIdentification sai =
          scienceAreaIdentificationDao.findScienceAreaIdentificationRecord(psnId, scienceAreaId, friendId);
      if (sai == null) {
        sai = new ScienceAreaIdentification();
        sai.setPsnId(psnId);
        sai.setScienceAreaId(scienceAreaId);
        sai.setOperatePsnId(friendId);
        sai.setOperateDate(new Date());
        scienceAreaIdentificationDao.save(sai);
      }
      PsnScienceArea psa = psnScienceAreaDao.findPsnScienceAreaByPsnIdAndId(psnId, scienceAreaId);
      if (psa != null) {
        // 赞的数量从后台统计
        Integer identificSum = psa.getIdentificationSum();
        if (identificSum == null) {
          sum = 1;
        } else {
          sum = identificSum + 1;
        }
        psa.setIdentificationSum(sum);
        psnScienceAreaDao.save(psa);
      }
    } catch (Exception e) {
      logger.error("认同人员科技领域出错, psnId=" + psnId + ", scienceAreaId=" + scienceAreaId, e);
    }
    return sum;
  }

  /**
   * 构建人员科技领域信息
   * 
   * @param form
   * @return
   */
  @Override
  public PersonProfileForm buildPsnScienceAreaInfo(PersonProfileForm form) {
    // 获取人员科技领域列表
    List<PsnScienceArea> areaList = findPsnScienceAreaList(form.getPsnId(), 1);
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    // 获取认同的人员ID列表
    for (PsnScienceArea area : areaList) {
      List<Long> identifyPsnIds =
          this.scienceAreaIdentificationDao.findIdentifyPsnIds(form.getPsnId(), area.getScienceAreaId());
      if (identifyPsnIds != null) {
        area.setIdentifyPsnIds(identifyPsnIds.toString());
      }
      // 不是自己的科技领域要判断是否认同过
      if (!form.getIsMySelf() && identifyPsnIds.contains(currentPsnId)) {
        area.setHasIdentified(true);
      }
    }
    form.setScienceAreaList(areaList);
    return form;
  }

  @Override
  public PersonProfileForm findPsnScienceArea(PersonProfileForm form) {
    // 获取人员科技领域列表
    List<PsnScienceArea> areaList = findPsnScienceAreaList(form.getPsnId(), 1);
    form.setScienceAreaList(areaList);
    return form;
  }

  @Override
  public void myScienceArea(PsnListViewForm form) throws Exception {
    List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
    List<PsnScienceArea> scienceAreaList = psnScienceAreaDao.queryScienceArea(form.getPsnId(), 1);
    if (CollectionUtils.isNotEmpty(scienceAreaList)) {
      Locale locale = LocaleContextHolder.getLocale();
      for (PsnScienceArea psnScienceArea : scienceAreaList) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("scienceAreaId", psnScienceArea.getScienceAreaId());
        if (Locale.US.equals(locale)) {
          map.put("name", psnScienceArea.getEnScienceArea());
        } else {
          map.put("name", psnScienceArea.getScienceArea());
        }
        listMap.add(map);
      }
    }
    form.setScienceAreaList(listMap);
  }

  @Override
  public List<Person> getIdentificationPerson(Long psnId, Integer areaId) throws Exception {
    List<Long> psnIdList = scienceAreaIdentificationDao.findIdentifyPsnIds(psnId, areaId);
    List<Person> friendList = new ArrayList<Person>();
    if (CollectionUtils.isNotEmpty(psnIdList)) {
      for (Long friendId : psnIdList) {
        Person temp = this.personProfileDao.get(friendId);
        if (temp != null) {
          Person person = new Person();
          person.setPersonId(temp.getPersonId());
          person.setName(temp.getName());
          person.setAvatars(temp.getAvatars());
          person.setSex(temp.getSex());
          person.setName(temp.getName());
          person.setFirstName(temp.getFirstName());
          person.setLastName(temp.getLastName());

          // 无头像，设置默认头像
          if (StringUtils.isBlank(person.getAvatars())) {
            person.setAvatars(personManager.refreshRemoteAvatars(person.getPersonId(), person.getSex(), null));
          }

          Locale locale = LocaleContextHolder.getLocale();
          String name = null;
          if (locale.equals(Locale.US)) {
            name = person.getFirstName() == null && person.getLastName() == null ? person.getName()
                : person.getFirstName() + " " + person.getLastName();
          } else {
            name = person.getName() == null ? person.getLastName() + " " + person.getFirstName() : person.getName();
          }
          person.setName(name);
          friendList.add(person);
        }
      }
    }
    return friendList;
  }

  @Override
  public List<PsnScienceAreaForm> getPsnScienceAreaFormList(Long psnId) {
    List<PsnScienceAreaForm> psnScienceAreaFormList = new ArrayList<PsnScienceAreaForm>();// 返回到页面的数据
    List<PsnScienceArea> psnScienceAreaList = findPsnScienceAreaList(psnId, 1); // 获取有效的研究领域
    PsnScienceAreaForm psnScienceAreaForm = null;
    for (PsnScienceArea psnScienceArea : psnScienceAreaList) {
      psnScienceAreaForm = new PsnScienceAreaForm();
      psnScienceAreaForm.setPsnId(psnId);
      psnScienceAreaForm.setPsnScienceArea(psnScienceArea);// 设置研究领域
      List<Person> psnList = null;
      try {
        psnList = getIdentificationPerson(psnId, psnScienceArea.getScienceAreaId());// 获取这个人这个研究领域的认同人
      } catch (Exception e) {
        // TODO Auto-generated catch block
        logger.error("获取研究领域认同人出错 psnId = " + psnId + ",AreaId=" + psnScienceArea.getScienceAreaId(), e);
      }
      psnScienceAreaForm.setFriendList(psnList);
      psnScienceAreaFormList.add(psnScienceAreaForm);
    }
    return psnScienceAreaFormList;
  }

  @Override
  public List<PsnScienceAreaInfo> findPsnScienceAreaInfo(Long psnId, Integer status) {
    List<PsnScienceArea> list = psnScienceAreaDao.findPsnScienceAreaList(psnId, status);
    List<PsnScienceAreaInfo> areaInfo = new ArrayList<PsnScienceAreaInfo>();
    if (CollectionUtils.isNotEmpty(list)) {
      for (PsnScienceArea area : list) {
        if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
          area.setShowScienceArea(
              StringUtils.isNotBlank(area.getEnScienceArea()) ? area.getEnScienceArea() : area.getScienceArea());
        } else {
          area.setShowScienceArea(
              StringUtils.isNotBlank(area.getScienceArea()) ? area.getScienceArea() : area.getEnScienceArea());
        }
        areaInfo.add(new PsnScienceAreaInfo(area));
      }
    }
    return areaInfo;
  }

  @Override
  public List<PsnScienceArea> findPsnScienceAreaById(PersonProfileForm form) throws Exception {
    String scienceAreaIds = form.getScienceAreaIds();
    String[] strScienceAreaIdArray = scienceAreaIds.split(",");
    Long[] scienceAreaIdArray = com.smate.core.base.utils.number.NumberUtils.parseLongArry(strScienceAreaIdArray);
    List<PsnScienceArea> list = new ArrayList<PsnScienceArea>();
    List<String> areaList = new ArrayList<String>();
    for (Long categoryId : scienceAreaIdArray) {
      PsnScienceArea psa = new PsnScienceArea();
      CategoryScm cs = categoryScmDao.findCategoryById(categoryId);
      psa.setScienceAreaId(Integer.parseInt(cs.getCategoryId().toString()));
      psa.setEnScienceArea(cs.getCategoryEn());
      psa.setScienceArea(cs.getCategoryZh());
      if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
        psa.setShowScienceArea(cs.getCategoryEn());
      } else {
        psa.setShowScienceArea(cs.getCategoryZh());
      }
      areaList.add(psa.getShowScienceArea());
      list.add(psa);
    }
    form.setAreaStr(areaList.toString());
    return list;
  }

  @Override
  public List<Integer> findPsnAreaId(Long psnId) throws Exception {
    return psnScienceAreaDao.findPsnScienceAreaIdList(psnId);
  }

}
