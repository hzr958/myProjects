package com.smate.center.task.service.bdspimp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.dao.bdsp.BdspPaperAuthorDao;
import com.smate.center.task.dao.bdsp.BdspPaperBaseDao;
import com.smate.center.task.dao.bdsp.BdspPaperCategoryDao;
import com.smate.center.task.dao.bdsp.BdspPaperCollectionDao;
import com.smate.center.task.dao.bdsp.BdspPaperGamDao;
import com.smate.center.task.dao.bdsp.BdspPaperUnitDao;
import com.smate.center.task.dao.bdsp.BdspPatentBaseDao;
import com.smate.center.task.dao.bdsp.BdspPatentCategoryDao;
import com.smate.center.task.dao.bdsp.BdspPatentGamDao;
import com.smate.center.task.dao.bdsp.BdspPatentInventorDao;
import com.smate.center.task.dao.bdsp.BdspPatentUnitDao;
import com.smate.center.task.dao.bdsp.BdspPrjBaseDao;
import com.smate.center.task.dao.bdsp.BdspPrjCategoryDao;
import com.smate.center.task.dao.bdsp.BdspPrjMemberDao;
import com.smate.center.task.dao.bdsp.BdspPrjUnitDao;
import com.smate.center.task.dao.bdsp.BdspPushPaperDataLogDao;
import com.smate.center.task.dao.bdsp.BdspPushPatentDataLogDao;
import com.smate.center.task.dao.bdsp.BdspPushPrjDataLogDao;
import com.smate.center.task.dao.bdsp.BdspPushPsnDataLogDao;
import com.smate.center.task.dao.bdsp.BdspResearchPsnBaseDao;
import com.smate.center.task.dao.bdsp.BdspResearchPsnCategoryDao;
import com.smate.center.task.dao.bdsp.BdspResearchPsnUnitDao;
import com.smate.center.task.model.bdsp.BdspInterfacePushDataForm;
import com.smate.center.task.model.bdsp.BdspPaperAuthor;
import com.smate.center.task.model.bdsp.BdspPaperBase;
import com.smate.center.task.model.bdsp.BdspPaperCategory;
import com.smate.center.task.model.bdsp.BdspPaperCollection;
import com.smate.center.task.model.bdsp.BdspPaperGam;
import com.smate.center.task.model.bdsp.BdspPaperUnit;
import com.smate.center.task.model.bdsp.BdspPatentBase;
import com.smate.center.task.model.bdsp.BdspPatentCategory;
import com.smate.center.task.model.bdsp.BdspPatentGam;
import com.smate.center.task.model.bdsp.BdspPatentInventor;
import com.smate.center.task.model.bdsp.BdspPatentUnit;
import com.smate.center.task.model.bdsp.BdspPrjBase;
import com.smate.center.task.model.bdsp.BdspPrjCategory;
import com.smate.center.task.model.bdsp.BdspPrjMember;
import com.smate.center.task.model.bdsp.BdspPrjUnit;
import com.smate.center.task.model.bdsp.BdspPushPaperDataLog;
import com.smate.center.task.model.bdsp.BdspPushPatentDataLog;
import com.smate.center.task.model.bdsp.BdspPushPrjDataLog;
import com.smate.center.task.model.bdsp.BdspPushPsnDataLog;
import com.smate.center.task.model.bdsp.BdspResearchPsnBase;
import com.smate.center.task.model.bdsp.BdspResearchPsnCategory;
import com.smate.center.task.model.bdsp.BdspResearchPsnUnit;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.core.base.utils.json.JacksonUtils;

@Service("bdspInterfacePushDataService")
@Transactional(rollbackFor = Exception.class)
public class BdspInterfacePushDataServiceImpl implements BdspInterfacePushDataService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private BdspResearchPsnBaseDao bdspResearchPsnBaseDao;
  @Autowired
  private BdspPrjBaseDao bdspPrjBaseDao;
  @Autowired
  private BdspPaperBaseDao bdspPaperBaseDao;
  @Autowired
  private BdspPatentBaseDao bdspPatentBaseDao;
  @Autowired
  private BdspResearchPsnCategoryDao bdspResearchPsnCategoryDao;
  @Autowired
  private BdspResearchPsnUnitDao bdspResearchPsnUnitDao;
  @Autowired
  private BdspPrjCategoryDao bdspPrjCategoryDao;
  @Autowired
  private BdspPrjUnitDao bdspPrjUnitDao;
  @Autowired
  private BdspPrjMemberDao bdspPrjMemberDao;
  @Autowired
  private BdspPaperCollectionDao bdspPaperCollectionDao;
  @Autowired
  private BdspPaperCategoryDao bdspPaperCategoryDao;
  @Autowired
  private BdspPaperUnitDao bdspPaperUnitDao;
  @Autowired
  private BdspPaperAuthorDao bdspPaperAuthorDao;
  @Autowired
  private BdspPaperGamDao bdspPaperGamDao;
  @Autowired
  private BdspPatentCategoryDao bdspPatentCategoryDao;
  @Autowired
  private BdspPatentGamDao bdspPatentGamDao;
  @Autowired
  private BdspPatentUnitDao bdspPatentUnitDao;
  @Autowired
  private BdspPatentInventorDao bdspPatentInventorDao;
  @Autowired
  private BdspPushPsnDataLogDao bdspPushPsnDataLogDao;
  @Autowired
  private BdspPushPrjDataLogDao bdspPushPrjDataLogDao;
  @Autowired
  private BdspPushPaperDataLogDao bdspPushPaperDataLogDao;
  @Autowired
  private BdspPushPatentDataLogDao bdspPushPatentDataLogDao;

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  private static final String accessToken = "iris2";
  private static final String domainPush = "http://192.168.10.67";
  private static final String pushPsnUri = "/datain/v1/import/person";
  private static final String pushPrjUri = "/datain/v1/import/project";
  private static final String pushPaperUri = "/datain/v1/import/paper";
  private static final String pushPatentUri = "/datain/v1/import/patent";


  @Override
  public void findUpdateDataType(BdspInterfacePushDataForm form) {
    Integer statusPsn = taskMarkerService.getApplicationQuartzSettingValue("bdspUpdateDataTypePsn");
    Integer statusPrj = taskMarkerService.getApplicationQuartzSettingValue("bdspUpdateDataTypePrj");
    Integer statusPaper = taskMarkerService.getApplicationQuartzSettingValue("bdspUpdateDataTypePaper");
    Integer statusPatent = taskMarkerService.getApplicationQuartzSettingValue("bdspUpdateDataTypePatent");
    form.setUpdateDataTypePsn(statusPsn == 1);
    form.setUpdateDataTypePrj(statusPrj == 1);
    form.setUpdateDataTypePaper(statusPaper == 1);
    form.setUpdateDataTypePatent(statusPatent == 1);
  }

  @Override
  public void checkDataHasUpdate(BdspInterfacePushDataForm form) {
    if (form.isUpdateDataTypePsn()) {
      // 查找更新的人员信息
      List<BdspResearchPsnBase> list = bdspResearchPsnBaseDao.findUpdateList(form.getPushSize());
      if (!CollectionUtils.isEmpty(list)) {
        form.setPsnlist(list);
        // 构建待推送的人员数据
        buildPushPsnData(form, list);
      }
    }
    if (form.isUpdateDataTypePrj()) {
      // 查找更新的项目信息
      List<BdspPrjBase> list = bdspPrjBaseDao.findUpdateList(form.getPushSize());
      if (!CollectionUtils.isEmpty(list)) {
        form.setPrjlist(list);
        // 构建待推送的项目数据
        buildPushPrjData(form, list);
      }
    }
    if (form.isUpdateDataTypePaper()) {
      // 查找更新的论文信息
      List<BdspPaperBase> list = bdspPaperBaseDao.findUpdateList(form.getPushSize());
      if (!CollectionUtils.isEmpty(list)) {
        form.setPaperlist(list);
        // 构建待推送的论文数据
        buildPushPaperData(form, list);
      }
    }
    if (form.isUpdateDataTypePatent()) {
      // 查找更新的专利信息
      List<BdspPatentBase> list = bdspPatentBaseDao.findUpdateList(form.getPushSize());
      if (!CollectionUtils.isEmpty(list)) {
        form.setPatentlist(list);
        // 构建待推送的专利数据
        buildPushPatentData(form, list);
      }
    }
  }

  private void buildPushPatentData(BdspInterfacePushDataForm form, List<BdspPatentBase> list) {
    List<Map<String, Object>> patentBaseList = new ArrayList<Map<String, Object>>();
    for (BdspPatentBase patent : list) {
      // 专利基本信息
      Map<String, Object> patentBaseMap = new HashMap<String, Object>();
      patentBaseMap.put("pat_id", patent.getPubId());
      patentBaseMap.put("pat_year", patent.getPrpYear());
      patentBaseMap.put("pat_type_id", patent.getPubType());
      patentBaseMap.put("prj_support_no", patent.getFundinfo());
      // 专利分类信息
      List<BdspPatentCategory> cList = bdspPatentCategoryDao.findListByPubId(patent.getPubId());
      if (!CollectionUtils.isEmpty(cList)) {
        List<Map<String, Object>> cateList = new ArrayList<Map<String, Object>>();
        for (BdspPatentCategory patentCate : cList) {
          Map<String, Object> patentCateMap = new HashMap<String, Object>();
          patentCateMap.put("pat_id", patent.getPubId());
          patentCateMap.put("domain_type_id", patentCate.getTechTypeId());
          patentCateMap.put("domain_id", patentCate.getTechId());
          cateList.add(patentCateMap);
        }
        patentBaseMap.put("classify", cateList);
      }
      // 专利单位信息
      List<BdspPatentUnit> uList = bdspPatentUnitDao.findListByPubId(patent.getPubId());
      if (!CollectionUtils.isEmpty(uList)) {
        List<Map<String, Object>> unitList = new ArrayList<Map<String, Object>>();
        for (BdspPatentUnit patentUnit : uList) {
          Map<String, Object> patentCateMap = new HashMap<String, Object>();
          patentCateMap.put("pat_id", patent.getPubId());
          patentCateMap.put("org_id", patentUnit.getInsId());
          unitList.add(patentCateMap);
        }
        patentBaseMap.put("organization", unitList);
      }
      // 专利发明人信息
      List<BdspPatentInventor> iList = bdspPatentInventorDao.findListByPubId(patent.getPubId());
      if (!CollectionUtils.isEmpty(iList)) {
        List<Map<String, Object>> inventorList = new ArrayList<Map<String, Object>>();
        for (BdspPatentInventor inventor : iList) {
          Map<String, Object> patentInventorMap = new HashMap<String, Object>();
          patentInventorMap.put("pat_id", patent.getPubId());
          patentInventorMap.put("psn_id", inventor.getPsnId());
          patentInventorMap.put("org_id", inventor.getInsId());
          inventorList.add(patentInventorMap);
        }
        patentBaseMap.put("person", inventorList);
      }
      // 专利社交信息
      List<BdspPatentGam> gList = bdspPatentGamDao.findListByPubId(patent.getPubId());
      if (!CollectionUtils.isEmpty(gList)) {
        List<Map<String, Object>> gamList = new ArrayList<Map<String, Object>>();
        for (BdspPatentGam gam : gList) {
          Map<String, Object> patentGamMap = new HashMap<String, Object>();
          patentGamMap.put("pat_id", patent.getPubId());
          patentGamMap.put("amt", gam.getAmt());
          patentGamMap.put("download", gam.getDownload());
          patentGamMap.put("view", gam.getView());
          gamList.add(patentGamMap);
        }
        patentBaseMap.put("socontact", gamList);
      }
      patentBaseList.add(patentBaseMap);
    }
    form.setPatentBaseList(patentBaseList);
    form.setHasDataPatentToUpdate(true);
  }

  private void buildPushPaperData(BdspInterfacePushDataForm form, List<BdspPaperBase> list) {
    List<Map<String, Object>> paperBaseList = new ArrayList<Map<String, Object>>();
    for (BdspPaperBase paper : list) {
      // 论文基本信息
      Map<String, Object> paperBaseMap = new HashMap<String, Object>();
      paperBaseMap.put("pub_id", paper.getPubId());
      paperBaseMap.put("publish_year", paper.getPublishYear());
      paperBaseMap.put("publish_journal_id", paper.getJid());
      paperBaseMap.put("pub_type_id", paper.getPubType());
      paperBaseMap.put("prj_support_no", paper.getFundinfo());
      // 论文分类信息
      List<BdspPaperCategory> cList = bdspPaperCategoryDao.findListByPubId(paper.getPubId());
      if (!CollectionUtils.isEmpty(cList)) {
        List<Map<String, Object>> cateList = new ArrayList<Map<String, Object>>();
        for (BdspPaperCategory paperCate : cList) {
          Map<String, Object> paperCateMap = new HashMap<String, Object>();
          paperCateMap.put("prj_id", paper.getPubId());
          paperCateMap.put("domain_type_id", paperCate.getTechTypeId());
          paperCateMap.put("domain_id", paperCate.getTechId());
          cateList.add(paperCateMap);
        }
        paperBaseMap.put("classify", cateList);
      }
      // 论文单位信息
      List<BdspPaperUnit> uList = bdspPaperUnitDao.findListByPubId(paper.getPubId());
      if (!CollectionUtils.isEmpty(uList)) {
        List<Map<String, Object>> unitList = new ArrayList<Map<String, Object>>();
        for (BdspPaperUnit paperUnit : uList) {
          Map<String, Object> paperUnitMap = new HashMap<String, Object>();
          paperUnitMap.put("prj_id", paper.getPubId());
          paperUnitMap.put("org_id", paperUnit.getInsId());
          unitList.add(paperUnitMap);
        }
        paperBaseMap.put("organization", unitList);
      }
      // 论文作者信息
      List<BdspPaperAuthor> aList = bdspPaperAuthorDao.findListByPubId(paper.getPubId());
      if (!CollectionUtils.isEmpty(aList)) {
        List<Map<String, Object>> authorList = new ArrayList<Map<String, Object>>();
        for (BdspPaperAuthor paperAuthor : aList) {
          Map<String, Object> paperAuthorMap = new HashMap<String, Object>();
          paperAuthorMap.put("pub_id", paper.getPubId());
          paperAuthorMap.put("psn_id", paperAuthor.getPsnId());
          paperAuthorMap.put("org_id", paperAuthor.getInsId());
          authorList.add(paperAuthorMap);
        }
        paperBaseMap.put("person", authorList);
      }
      // 论文收录信息
      List<BdspPaperCollection> jList = bdspPaperCollectionDao.findListByPubId(paper.getPubId());
      if (!CollectionUtils.isEmpty(jList)) {
        List<Map<String, Object>> paperJournalList = new ArrayList<Map<String, Object>>();
        for (BdspPaperCollection paperJour : jList) {
          Map<String, Object> paperJournalMap = new HashMap<String, Object>();
          paperJournalMap.put("pub_id", paper.getPubId());
          paperJournalMap.put("publish_journal_id", paperJour.getJid());
          paperJournalMap.put("region_id", paperJour.getRegionId());
          paperJournalList.add(paperJournalMap);
        }
        paperBaseMap.put("journal", paperJournalList);
      }
      // 论文社交信息
      List<BdspPaperGam> gList = bdspPaperGamDao.findListByPubId(paper.getPubId());
      if (!CollectionUtils.isEmpty(gList)) {
        List<Map<String, Object>> gamList = new ArrayList<Map<String, Object>>();
        for (BdspPaperGam paperGam : gList) {
          Map<String, Object> prjCateMap = new HashMap<String, Object>();
          prjCateMap.put("pub_id", paper.getPubId());
          prjCateMap.put("citation", paperGam.getCitation());
          prjCateMap.put("download", paperGam.getDownload());
          prjCateMap.put("view", paperGam.getView());
          gamList.add(prjCateMap);
        }
        paperBaseMap.put("socontact", gamList);
      }
      paperBaseList.add(paperBaseMap);
    }
    form.setPaperBaseList(paperBaseList);
    form.setHasDataPaperToUpdate(true);
  }

  private void buildPushPrjData(BdspInterfacePushDataForm form, List<BdspPrjBase> list) {
    List<Map<String, Object>> prjBaseList = new ArrayList<Map<String, Object>>();
    for (BdspPrjBase prj : list) {
      // 项目基本信息
      Map<String, Object> prjBaseMap = new HashMap<String, Object>();
      prjBaseMap.put("prj_id", prj.getPrjId());
      prjBaseMap.put("org_id", prj.getOrgId());
      prjBaseMap.put("psn_id", prj.getPsnId());
      prjBaseMap.put("prj_year", prj.getStatYear());
      prjBaseMap.put("prj_support_no", prj.getSupportId());
      prjBaseMap.put("prj_amt", prj.getTotalAmt());
      // 项目分类
      List<BdspPrjCategory> cList = bdspPrjCategoryDao.findListByPrjId(prj.getPrjId());
      if (!CollectionUtils.isEmpty(cList)) {
        List<Map<String, Object>> cateList = new ArrayList<Map<String, Object>>();
        for (BdspPrjCategory prjCate : cList) {
          Map<String, Object> prjCateMap = new HashMap<String, Object>();
          prjCateMap.put("prj_id", prj.getPrjId());
          prjCateMap.put("domain_type_id", prjCate.getTechTypeId());
          prjCateMap.put("domain_id", prjCate.getTechId());
          cateList.add(prjCateMap);
        }
        prjBaseMap.put("classify", cateList);
      }
      // 项目合作单位
      List<BdspPrjUnit> uList = bdspPrjUnitDao.findListByPrjId(prj.getPrjId());
      if (!CollectionUtils.isEmpty(uList)) {
        List<Map<String, Object>> unitList = new ArrayList<Map<String, Object>>();
        for (BdspPrjUnit prjUnit : uList) {
          Map<String, Object> prjUnitMap = new HashMap<String, Object>();
          prjUnitMap.put("prj_id", prj.getPrjId());
          prjUnitMap.put("org_id", prjUnit.getInsId());
          unitList.add(prjUnitMap);
        }
        prjBaseMap.put("organization", unitList);
      }
      // 项目成员
      List<BdspPrjMember> mList = bdspPrjMemberDao.findListByPrjId(prj.getPrjId());
      if (!CollectionUtils.isEmpty(mList)) {
        List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
        for (BdspPrjMember prjMember : mList) {
          Map<String, Object> prjMemberMap = new HashMap<String, Object>();
          prjMemberMap.put("prj_id", prj.getPrjId());
          prjMemberMap.put("psn_id", prjMember.getPsnId());
          prjMemberMap.put("org_id", prjMember.getInsId());
          memberList.add(prjMemberMap);
        }
        prjBaseMap.put("person", memberList);
      }
      prjBaseList.add(prjBaseMap);
    }
    form.setPrjBaseList(prjBaseList);
    form.setHasDataPrjToUpdate(true);
  }

  private void buildPushPsnData(BdspInterfacePushDataForm form, List<BdspResearchPsnBase> list) {
    List<Map<String, Object>> psnBaseList = new ArrayList<Map<String, Object>>();
    for (BdspResearchPsnBase psn : list) {
      Map<String, Object> psnBaseMap = new HashMap<String, Object>();
      psnBaseMap.put("psn_id", psn.getPsnId());
      psnBaseMap.put("edu_id", psn.getEduId());
      psnBaseMap.put("degree_id", psn.getDegreeId());
      psnBaseMap.put("gender_id", psn.getGenderId());
      psnBaseMap.put("pos_id", psn.getPosId());
      psnBaseMap.put("birthday", psn.getBirthday());
      // 人员分类信息
      List<BdspResearchPsnCategory> cList = bdspResearchPsnCategoryDao.findListByPsnId(psn.getPsnId());
      if (!CollectionUtils.isEmpty(cList)) {
        List<Map<String, Object>> cateList = new ArrayList<Map<String, Object>>();
        for (BdspResearchPsnCategory psnCate : cList) {
          Map<String, Object> psnCateMap = new HashMap<String, Object>();
          psnCateMap.put("psn_id", psn.getPsnId());
          psnCateMap.put("domain_type_id", psnCate.getFromId());
          psnCateMap.put("domain_id", psnCate.getTypeId());
          cateList.add(psnCateMap);
        }
        psnBaseMap.put("classify", cateList);
      }
      // 人员单位信息
      List<BdspResearchPsnUnit> uList = bdspResearchPsnUnitDao.findListByPsnId(psn.getPsnId());
      if (!CollectionUtils.isEmpty(uList)) {
        List<Map<String, Object>> unitList = new ArrayList<Map<String, Object>>();
        for (BdspResearchPsnUnit psnUnit : uList) {
          Map<String, Object> psnUnitMap = new HashMap<String, Object>();
          psnUnitMap.put("psn_id", psn.getPsnId());
          psnUnitMap.put("org_id", psnUnit.getInsId());
          unitList.add(psnUnitMap);
        }
        psnBaseMap.put("organization", unitList);
      }
      psnBaseList.add(psnBaseMap);
    }
    form.setPsnBaseList(psnBaseList);
    form.setHasDataPsnToUpdate(true);
  }

  @Override
  public void pushData(BdspInterfacePushDataForm form) {
    String serverUrl = domainPush;
    if (form.isHasDataPsnToUpdate()) {
      serverUrl = serverUrl + pushPsnUri + "?accessToken=" + accessToken;
      HashMap<String, Object> psnBack = restTemplate.postForObject(serverUrl, form.getPsnBaseList(), HashMap.class);
      // psn接口返回解析
      handlePsnBack(psnBack, form);
    }
    if (form.isHasDataPrjToUpdate()) {
      serverUrl = serverUrl + pushPrjUri + "?accessToken=" + accessToken;
      HashMap<String, Object> prjBack = restTemplate.postForObject(serverUrl, form.getPrjBaseList(), HashMap.class);
      // prj接口返回解析
      handlePrjBack(prjBack, form);
    }
    if (form.isHasDataPaperToUpdate()) {
      serverUrl = serverUrl + pushPaperUri + "?accessToken=" + accessToken;
      HashMap<String, Object> paperBack = restTemplate.postForObject(serverUrl, form.getPaperBaseList(), HashMap.class);
      // paper接口返回解析
      handlePaperBack(paperBack, form);
    }
    if (form.isHasDataPatentToUpdate()) {
      serverUrl = serverUrl + pushPatentUri + "?accessToken=" + accessToken;
      HashMap<String, Object> patentBack =
          restTemplate.postForObject(serverUrl, form.getPatentBaseList(), HashMap.class);
      // patent接口返回解析
      handlePatentBack(patentBack, form);
    }
  }

  private void handlePatentBack(HashMap<String, Object> patentBack, BdspInterfacePushDataForm form) {
    if (patentBack != null) {
      String code = objToStr(patentBack.get("code"));
      form.setErrorMsgPatentMap(new HashMap<String, String>());
      form.setErrorPatentlist(new ArrayList<Long>());
      if (code.equals("2000")) {
        // 成功的目前没有处理
      } else {
        // 记录错误信息-子关系出错-必须返回basic的信息-没有就是他们提供的接口返回的数据有问题
        HashMap<String, Object> detMap = (HashMap<String, Object>) patentBack.get("details");
        if (detMap != null) {
          List<HashMap<String, Object>> basicMap = (List<HashMap<String, Object>>) detMap.get("basic");
          if (basicMap != null) {
            for (HashMap<String, Object> one : basicMap) {
              String pub_id = objToStr(one.get("pub_id"));
              String errorMsg = objToStr(one.get("errorMsg"));
              if (StringUtils.isNotBlank(pub_id)) {
                form.getErrorPatentlist().add(Long.parseLong(pub_id));
                form.getErrorMsgPatentMap().put(pub_id, code + "--" + errorMsg);
              }
            }
          }
        }
      }
    }
  }

  private void handlePaperBack(HashMap<String, Object> paperBack, BdspInterfacePushDataForm form) {
    if (paperBack != null) {
      String code = objToStr(paperBack.get("code"));
      form.setErrorMsgPaperMap(new HashMap<String, String>());
      form.setErrorPaperlist(new ArrayList<Long>());
      if (code.equals("2000")) {
        // 成功的目前没有处理
      } else {
        // 记录错误信息-子关系出错-必须返回basic的信息-没有就是他们提供的接口返回的数据有问题
        HashMap<String, Object> detMap = (HashMap<String, Object>) paperBack.get("details");
        if (detMap != null) {
          List<HashMap<String, Object>> basicMap = (List<HashMap<String, Object>>) detMap.get("basic");
          if (basicMap != null) {
            for (HashMap<String, Object> one : basicMap) {
              String pub_id = objToStr(one.get("pub_id"));
              String errorMsg = objToStr(one.get("errorMsg"));
              if (StringUtils.isNotBlank(pub_id)) {
                form.getErrorPaperlist().add(Long.parseLong(pub_id));
                form.getErrorMsgPaperMap().put(pub_id, code + "--" + errorMsg);
              }
            }
          }
        }
      }
    }
  }

  private void handlePrjBack(HashMap<String, Object> prjBack, BdspInterfacePushDataForm form) {
    if (prjBack != null) {
      String code = objToStr(prjBack.get("code"));
      form.setErrorMsgPrjMap(new HashMap<String, String>());
      form.setErrorPrjlist(new ArrayList<Long>());
      if (code.equals("2000")) {
        // 成功的目前没有处理
      } else {
        // 记录错误信息-子关系出错-必须返回basic的信息-没有就是他们提供的接口返回的数据有问题
        HashMap<String, Object> detMap = (HashMap<String, Object>) prjBack.get("details");
        if (detMap != null) {
          List<HashMap<String, Object>> basicMap = (List<HashMap<String, Object>>) detMap.get("basic");
          if (basicMap != null) {
            for (HashMap<String, Object> one : basicMap) {
              String prj_id = objToStr(one.get("prj_id"));
              String errorMsg = objToStr(one.get("errorMsg"));
              if (StringUtils.isNotBlank(prj_id)) {
                form.getErrorPrjlist().add(Long.parseLong(prj_id));
                form.getErrorMsgPrjMap().put(prj_id, code + "--" + errorMsg);
              }
            }
          }
        }
      }
    }
  }

  private void handlePsnBack(HashMap<String, Object> psnBack, BdspInterfacePushDataForm form) {
    if (psnBack != null) {
      String code = objToStr(psnBack.get("code"));
      form.setErrorMsgPsnMap(new HashMap<String, String>());
      form.setErrorPsnlist(new ArrayList<Long>());
      if (code.equals("2000")) {
        // 成功的目前没有处理
      } else {
        // 记录错误信息-子关系出错-必须返回basic的信息-没有就是他们提供的接口返回的数据有问题
        HashMap<String, Object> detMap = (HashMap<String, Object>) psnBack.get("details");
        if (detMap != null) {
          List<HashMap<String, Object>> basicMap = (List<HashMap<String, Object>>) detMap.get("basic");
          if (basicMap != null) {
            for (HashMap<String, Object> one : basicMap) {
              String psn_id = objToStr(one.get("psn_id"));
              String errorMsg = objToStr(one.get("errorMsg"));
              if (StringUtils.isNotBlank(psn_id)) {
                form.getErrorPsnlist().add(Long.parseLong(psn_id));
                form.getErrorMsgPsnMap().put(psn_id, code + "--" + errorMsg);
              }
            }
          }
        }
      }
    }
  }

  @Override
  public boolean findTaskStatus() {
    Integer taskStatus = taskMarkerService.getApplicationQuartzSettingValue("bdspUpdateDataTaskStatus");
    return taskStatus == 1;
  }

  private String objToStr(Object obj) {
    if (obj != null) {
      return obj.toString().trim();
    } else {
      return "";
    }
  }

  @Override
  public void savePushPsnLog(BdspInterfacePushDataForm form) {
    if (form.isHasDataPsnToUpdate()) {
      for (BdspResearchPsnBase one : form.getPsnlist()) {
        Date date = new Date();
        BdspPushPsnDataLog log = bdspPushPsnDataLogDao.get(one.getPsnId());
        if (log == null) {
          log = new BdspPushPsnDataLog();
          log.setPsnId(one.getPsnId());
          log.setCreateDate(date);
        }
        log.setUpdateDate(one.getCreateDate() == null ? date : one.getCreateDate());
        if (form.getErrorPsnlist().contains(one.getPsnId())) {
          String pushMsg = form.getErrorMsgPaperMap().get(one.getPsnId().toString());
          if (StringUtils.isBlank(pushMsg)) {
            pushMsg = "推送失败";
          }
          log.setPushStatus(2);
          log.setPushMsg(pushMsg);
        } else {
          log.setPushStatus(1);
          log.setPushMsg("2000--推送成功");
        }
        bdspPushPsnDataLogDao.save(log);
      }
    }
  }

  @Override
  public void savePushPrjLog(BdspInterfacePushDataForm form) {
    if (form.isHasDataPrjToUpdate()) {
      for (BdspPrjBase one : form.getPrjlist()) {
        Date date = new Date();
        BdspPushPrjDataLog log = bdspPushPrjDataLogDao.get(one.getPrjId());
        if (log == null) {
          log = new BdspPushPrjDataLog();
          log.setPrjId(one.getPrjId());
          log.setCreateDate(date);
        }
        log.setUpdateDate(one.getCreateDate() == null ? date : one.getCreateDate());
        if (form.getErrorPrjlist().contains(one.getPrjId())) {
          String pushMsg = form.getErrorMsgPaperMap().get(one.getPrjId().toString());
          if (StringUtils.isBlank(pushMsg)) {
            pushMsg = "推送失败";
          }
          log.setPushStatus(2);
          log.setPushMsg(pushMsg);
        } else {
          log.setPushStatus(1);
          log.setPushMsg("2000--推送成功");
        }
        bdspPushPrjDataLogDao.save(log);
      }
    }
  }

  @Override
  public void savePushPaperLog(BdspInterfacePushDataForm form) {
    if (form.isHasDataPaperToUpdate()) {
      for (BdspPaperBase one : form.getPaperlist()) {
        Date date = new Date();
        BdspPushPaperDataLog log = bdspPushPaperDataLogDao.get(one.getPubId());
        if (log == null) {
          log = new BdspPushPaperDataLog();
          log.setPubId(one.getPubId());
          log.setCreateDate(date);
        }
        log.setUpdateDate(one.getCreateDate() == null ? date : one.getCreateDate());
        if (form.getErrorPaperlist().contains(one.getPubId())) {
          String pushMsg = form.getErrorMsgPaperMap().get(one.getPubId().toString());
          if (StringUtils.isBlank(pushMsg)) {
            pushMsg = "推送失败";
          }
          log.setPushStatus(2);
          log.setPushMsg(pushMsg);
        } else {
          log.setPushStatus(1);
          log.setPushMsg("2000--推送成功");
        }
        bdspPushPaperDataLogDao.save(log);
      }
    }
  }

  @Override
  public void savePushPatentLog(BdspInterfacePushDataForm form) {
    if (form.isHasDataPatentToUpdate()) {
      for (BdspPatentBase one : form.getPatentlist()) {
        Date date = new Date();
        BdspPushPatentDataLog log = bdspPushPatentDataLogDao.get(one.getPubId());
        if (log == null) {
          log = new BdspPushPatentDataLog();
          log.setPubId(one.getPubId());
          log.setCreateDate(date);
        }
        log.setUpdateDate(one.getCreateDate() == null ? date : one.getCreateDate());
        if (form.getErrorPatentlist().contains(one.getPubId())) {
          String pushMsg = form.getErrorMsgPaperMap().get(one.getPubId().toString());
          if (StringUtils.isBlank(pushMsg)) {
            pushMsg = "推送失败";
          }
          log.setPushStatus(2);
          log.setPushMsg(pushMsg);
        } else {
          log.setPushStatus(1);
          log.setPushMsg("2000--推送成功");
        }
        bdspPushPatentDataLogDao.save(log);
      }
    }
  }

  @Override
  public void closeTask() {
    taskMarkerService.closeQuartzApplication("bdspUpdateDataTaskStatus");
  }

  @Override
  public void findUpdatePsnData(BdspInterfacePushDataForm form) {
    bdspPushPsnDataLogDao.updateData();
    bdspPushPsnDataLogDao.addData();
  }

  @Override
  public void findUpdatePrjData(BdspInterfacePushDataForm form) {
    bdspPushPrjDataLogDao.updateData();
    bdspPushPrjDataLogDao.addData();
  }

  @Override
  public void findUpdatePaperData(BdspInterfacePushDataForm form) {
    bdspPushPaperDataLogDao.updateData();
    bdspPushPaperDataLogDao.addData();

  }

  @Override
  public void findUpdatePatentData(BdspInterfacePushDataForm form) {
    bdspPushPatentDataLogDao.updateData();
    bdspPushPatentDataLogDao.addData();
  }



}
