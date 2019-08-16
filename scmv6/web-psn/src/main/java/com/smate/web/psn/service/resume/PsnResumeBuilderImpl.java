package com.smate.web.psn.service.resume;

import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.dao.pub.PubSimpleDao;
import com.smate.web.psn.dao.resume.CVModuleInfoDao;
import com.smate.web.psn.dao.resume.PsnResumeDao;
import com.smate.web.psn.dao.resume.ResumeModuleDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.form.resume.PersonalResumeForm;
import com.smate.web.psn.model.newresume.CVModuleInfo;
import com.smate.web.psn.model.newresume.PsnResume;
import com.smate.web.psn.model.newresume.ResumeModule;
import com.smate.web.psn.model.resume.CVPubInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 简历建造者
 * 
 * @author wsn
 *
 */
@Transactional(rollbackFor = Exception.class)
public class PsnResumeBuilderImpl implements PsnResumeBuilder {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private Map<String, PsnResumeBuildService> serviceMap;
  @Autowired
  private PsnResumeDao psnResumeDao;
  @Autowired
  private ResumeModuleDao resumeModuleDao;
  @Autowired
  private CVModuleInfoDao cVModuleInfoDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  private PsnConfigDao psnConfigDao;

  @Override
  public PsnResumeBuildService initPsnResumeService(String serviceType) {
    return serviceMap.get(serviceType);
  }

  @Override
  public void findPsnResumeList(PersonalResumeForm form) throws PsnException {
    List<PsnResume> resumeList = psnResumeDao.queryPsnResumeList(form.getPsnId());
    form.setResumeList(resumeList);
  }

  @Override
  public void deletePsnResume(Long resumeId) throws PsnException {
    // 先校验是否是该简历拥有者
    if (isPsnResumeOwner(resumeId)) {
      // 删除主表记录
      psnResumeDao.delete(resumeId);
      // 删除具体json信息
      cVModuleInfoDao.deleteAllCVModuleInfoByCvId(resumeId);
      // 删除简历ID对应的模块记录
      resumeModuleDao.deleteResumeModuleByResumeId(resumeId);
    }

  }

  public Map<String, PsnResumeBuildService> getServiceMap() {
    return serviceMap;
  }

  public void setServiceMap(Map<String, PsnResumeBuildService> serviceMap) {
    this.serviceMap = serviceMap;
  }

  @Override
  public void savePsnResumeModule(PersonalResumeForm form) throws PsnException {
    try {
      Long cvId = form.getResumeId();
      Integer moduleId = form.getModuleId();
      // 先判断是否是简历拥有者
      if (isPsnResumeOwner(form.getResumeId())) {
        // 保存对应的模块信息
        ResumeModule module = resumeModuleDao.findResumeModuleByResumeIdAndModuleId(cvId, moduleId);
        if (module == null) {
          module = new ResumeModule();
          module.setResumeId(cvId);
          module.setModuleId(moduleId);
        }
        // 保存模块json信息
        CVModuleInfo info = null;
        if (module.getModuleInfoId() != null) {
          info = cVModuleInfoDao.get(module.getModuleInfoId());
        }
        if (info == null) {
          info = new CVModuleInfo();
        }
        List<Map<String, String>> infoJsonList = JacksonUtils.jsonToList(form.getModuleInfo());
        info.setModuleInfo(form.getModuleInfo());
        cVModuleInfoDao.save(info);
        // 保存模块基本信息
        module.setModuleSeq(form.getModuleSeq());
        module.setModuleTitle(form.getModuleTitle());
        module.setUpdateTime(new Date());
        module.setStatus(0);
        module.setModuleInfoId(info.getId());
        resumeModuleDao.save(module);
        psnResumeDao.updatePsnResumeUpdateTime(cvId);
      }
    } catch (Exception e) {
      throw new PsnException(e);
    }
  }

  /**
   * 是否是简历拥有者
   * 
   * @param resumeId
   * @return
   */
  @Override
  public boolean isPsnResumeOwner(Long resumeId) throws PsnException {
    boolean isOwner = false;
    PsnResume resume = psnResumeDao.findPsnResumeByPsnIdAndResumeId(SecurityUtils.getCurrentUserId(), resumeId);
    if (resume != null) {
      isOwner = true;
    }
    return isOwner;
  }

  /**
   * 校验成果是否都属于当前用户
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean checkPubInfo(PersonalResumeForm form) throws PsnException {
    boolean result = true;
    try {
      if (StringUtils.isNotBlank(form.getModuleInfo())) {
        List<CVPubInfo> pubInfoList =
            (List<CVPubInfo>) JacksonUtils.jsonToCollection(form.getModuleInfo(), ArrayList.class, CVPubInfo.class);
        if (CollectionUtils.isNotEmpty(pubInfoList)) {
          List<Long> pubIds = new ArrayList<Long>();
          for (CVPubInfo info : pubInfoList) {
            info.setPubId(NumberUtils.toLong(Des3Utils.decodeFromDes3(info.getDes3PubId())));
            pubIds.add(info.getPubId());
          }
          // 校验全部的成果是否是当前用户的
          Long count = pubSimpleDao.getPsnOwnerPubCount(pubIds, SecurityUtils.getCurrentUserId());
          if (count != pubInfoList.size()) {
            result = false;
          }
        }
        form.setModuleInfo(JacksonUtils.listToJsonStr(pubInfoList));
      }
    } catch (Exception e) {
      throw new PsnException(e);
    }
    return result;
  }

  @Override
  public void initPsnCVInfo(PersonalResumeForm form) throws PsnException {
    Long psnId = SecurityUtils.getCurrentUserId();
    form.setPsnId(psnId);
    Long psnCnfId = psnConfigDao.getPsnConfId(psnId);
    if (psnCnfId != null) {
      form.setDes3CnfId(Des3Utils.encodeToDes3(psnCnfId.toString()));
    }
    if (form.getResumeId() != null) {
      // 获取简历标题
      PsnResume psnResume = psnResumeDao.get(form.getResumeId());
      form.setResumeName(psnResume.getResumeName());
      // 获取简历各个模块的状态
      List<ResumeModule> moduleList = resumeModuleDao.findCVModuleStatus(form.getResumeId());
      this.initCVModuleStatus(moduleList, form);
    }
  }

  /**
   * 初始化简历模块的状态
   */
  private void initCVModuleStatus(List<ResumeModule> moduleList, PersonalResumeForm form) {
    if (CollectionUtils.isNotEmpty(moduleList)) {
      for (ResumeModule cv : moduleList) {
        switch (cv.getModuleId()) {
          case 1:
            form.setBaseInfoModuleStatus(cv.getStatus());
            break;
          case 2:
            form.setEduModuleStatus(cv.getStatus());
            break;
          case 3:
            form.setWorkModuleStatus(cv.getStatus());
            break;
          case 4:
            form.setPrjModuleStatus(cv.getStatus());
            break;
          case 5:
            form.setPubModuleStatus(cv.getStatus());
            break;
          case 6:
            form.setOtherPubModuleStatus(cv.getStatus());
            break;
          default:
            break;
        }
      }
    }
  }
}
