package com.smate.web.dyn.service.dynamic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.dyn.dao.rcmd.ImpactGuideRecordDAO;
import com.smate.web.dyn.form.dynamic.DynPubInfoForm;
import com.smate.web.dyn.form.dynamic.DynamicInfluenceForm;
import com.smate.web.dyn.model.psn.rcmd.ImpactGuideRecord;

@Service("dynamicInfluenceService")
@Transactional(rollbackFor = Exception.class)
public class DynamicInfluenceServiceImpl implements DynamicInfluenceService {
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Resource
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private ImpactGuideRecordDAO impactGuideRecordDao;

  /**
   * 动态认领全文获取
   */
  @SuppressWarnings("unchecked")
  @Override
  public void dynConfirmPaper(DynamicInfluenceForm form) throws Exception {
    List<DynPubInfoForm> list = new ArrayList<>();
    List<DynPubInfoForm> newlist = new ArrayList<>();
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("des3SearchPsnId", Des3Utils.encodeToDes3(String.valueOf(form.getPsnId())));
    params.put("serviceType", "pubConfirmList");
    params.put("isAll", "1");
    params.put("orderBy", "pubId");
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(domainscm + "/data/pub/query/list", params, Object.class);
    if (object != null && object.get("status").equals("success")) {
      List<Map<String, Object>> listResult = (List<Map<String, Object>>) object.get("resultList");
      if (listResult != null && listResult.size() > 0) {
        for (Map<String, Object> map : listResult) {
          DynPubInfoForm vo = new DynPubInfoForm();
          BeanUtils.populate(vo, map);
          list.add(vo);
        }
      }
    }
    if (list != null && list.size() > 0) {// 只要前10条数据
      for (DynPubInfoForm pub : list) {
        if (newlist.size() < 10) {
          newlist.add(pub);
        }
      }
    }
    form.setConfirmList(newlist);
  }

  /**
   * 动态上传全文获取
   */
  @SuppressWarnings("unchecked")
  @Override
  public void dynUploadPaper(DynamicInfluenceForm form) throws Exception {
    List<DynPubInfoForm> list = new ArrayList<>();
    List<DynPubInfoForm> newlist = new ArrayList<>();
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("des3SearchPsnId", Des3Utils.encodeToDes3(String.valueOf(form.getPsnId())));
    params.put("self", true);
    params.put("serviceType", "pubList");
    params.put("orderBy", "publishYear");
    params.put("pageSize", 100);
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(domainscm + "/data/pub/query/list", params, Object.class);
    if (object != null && object.get("status").equals("success")) {
      List<Map<String, Object>> listResult = (List<Map<String, Object>>) object.get("resultList");
      if (listResult != null && listResult.size() > 0) {
        for (Map<String, Object> map : listResult) {
          DynPubInfoForm vo = new DynPubInfoForm();
          BeanUtils.populate(vo, map);
          list.add(vo);
        }
      }
    }
    if (list != null && list.size() > 0) {
      for (DynPubInfoForm pub : list) {
        if (pub.getHasFulltext() == 0 && newlist.size() < 10) {
          newlist.add(pub);
        }
      }
    }

    form.setUploadList(newlist);
  }

  public String dynSharePsnUrl(Long psnId) throws Exception {
    String psnUrl = "";
    PsnProfileUrl profileUrl = psnProfileUrlDao.get(psnId);
    if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
      psnUrl = domainscm + "/P/" + profileUrl.getPsnIndexUrl();
    } else {
      psnUrl = domainscm + "/psnweb/homepage/show?des3PsnId=" + Des3Utils.encodeToDes3(psnId.toString());
    }
    return psnUrl;
  }

  @Override
  public boolean judgeImpact(Long psnId) throws Exception {
    boolean flag = true;
    ImpactGuideRecord impact = impactGuideRecordDao.findRecordByPsnId(psnId);
    if (impact == null) {
      impact = new ImpactGuideRecord();
      impact.setPsnId(psnId);
      impact.setGmtCreate(new Date());
      impact.setStatus(0);
      impactGuideRecordDao.saveOrUpdate(impact);
    } else {
      int status = impact.getStatus();
      if (status == 0) {// 先判断状态
        Date currDate = new Date();
        Date createDate = impact.getGmtCreate();
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long diff = currDate.getTime() - createDate.getTime();
        long day = diff / nd;// 计算差多少天
        if (day < 60) {// 每两个月弹出框
          flag = false;
        } else {
          flag = true;
          impact.setGmtCreate(currDate);
          impactGuideRecordDao.saveOrUpdate(impact);
        }
      } else {// 状态为1的 不弹出提示框
        flag = false;
      }

    }

    return flag;
  }

}
