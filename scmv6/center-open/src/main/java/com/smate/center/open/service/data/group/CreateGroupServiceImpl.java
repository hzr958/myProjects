package com.smate.center.open.service.data.group;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.grp.GrpConstant;
import com.smate.center.open.dao.grp.*;
import com.smate.center.open.dao.job.TmpTaskInfoRecordDao;
import com.smate.center.open.dao.publication.CategoryMapBaseDao;
import com.smate.center.open.dao.publication.CategoryMapScmNsfcDao;
import com.smate.center.open.form.grp.GrpMainForm;
import com.smate.center.open.model.grp.*;
import com.smate.center.open.model.job.TmpTaskInfoRecord;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.data.ThirdDataTypeService;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 群组创建服务实现
 * 
 * 参数只接收 json字符串 别在用xml 字符串
 * 
 * @author
 *
 */
@Transactional(rollbackFor = Exception.class)
public class CreateGroupServiceImpl extends ThirdDataTypeBase {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpControlDao grpControlDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private GrpStatisticsDao grpStatisticsDao;
  @Autowired
  private GrpKwDiscDao grpKwDiscDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Autowired
  private CategoryMapScmNsfcDao categoryMapScmNsfcDao;
  @Autowired
  private GrpIndexUrlDao grpIndexUrlDao;
  @Resource(name = "getShortUrlServiceImpl")
  private ThirdDataTypeService getShortUrlServiceImpl;
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    // 校验服务参数 serviceType
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    Object grpDataObj = serviceData.get(GroupInfoConst.GRP_DATA);
    if (grpDataObj == null || !JacksonUtils.isJsonString(grpDataObj.toString())) {
      logger.error("服务参数格式不正确 ,grpData 必须是json格式");
      temp.putAll(errorMap(OpenMsgCodeConsts.SCM_283, paramet, ""));
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
      return temp;
    }
    Map<String, Object> grpData = JacksonUtils.jsonToMap(grpDataObj.toString());
    checkServiceData(grpData, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    paramet.putAll(serviceData);
    paramet.put(GroupInfoConst.GRP_DATA, grpData);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  /**
   * 检查群组的一些参数
   * 
   * @param grpData
   * @param temp
   * @return
   */
  private Map<String, Object> checkServiceData(Map<String, Object> grpData, Map<String, Object> temp) {
    if (grpData.get(GroupInfoConst.GRP_NAME) == null
        || StringUtils.isBlank(grpData.get(GroupInfoConst.GRP_NAME).toString())) {
      logger.error("服务参数不能为空--群组名称");
      temp.putAll(errorMap(OpenMsgCodeConsts.SCM_279, grpData, ""));
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
      return null;
    }
    if (grpData.get(GroupInfoConst.GRP_CATEGORY) == null
        || StringUtils.isBlank(grpData.get(GroupInfoConst.GRP_CATEGORY).toString())) {
      logger.error("服务参数不能为空--群组类别");
      temp.putAll(errorMap(OpenMsgCodeConsts.SCM_280, grpData, ""));
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
      return null;
    }
    if (!GrpConstant.GRP_CATEGOTY.contains(Integer.parseInt(grpData.get(GroupInfoConst.GRP_CATEGORY).toString()))) {
      logger.error("服务参数不能为空--群组类别");
      temp.putAll(errorMap(OpenMsgCodeConsts.SCM_280, grpData, ""));
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
      return null;
    }
    /*
     * if (grpData.get(GroupInfoConst.FIRST_CATEGORYID) == null || !NumberUtils
     * .isDigits(grpData.get(GroupInfoConst.FIRST_CATEGORYID).toString())) {
     * logger.error("服务参数不能为空--firstCategoryId为空 或格式不正确");
     * temp.putAll(errorMap(OpenMsgCodeConsts.SCM_281, grpData, "")); temp.put(OpenConsts.RESULT_STATUS,
     * OpenConsts.RESULT_STATUS_ERROR); return null; }
     */
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> infoMap = new HashMap<String, Object>();
    GrpMainForm form = new GrpMainForm();
    buildGrpMainForm(paramet, form);
    createGrp(form);
    infoMap.put(GroupInfoConst.GRP_ID, form.getGrpId());
    dataList.add(infoMap);
    temp = super.successMap(OpenMsgCodeConsts.SCM_000, dataList);
    return temp;
  }

  private String getShortUrl(GrpMainForm form) {
    String shortUrl = "";
    Map<String, Object> dataMap = new HashMap<String, Object>();
    Map<String, Object> shortUrlParametMap = new HashMap<String, Object>();
    Map<String, Object> paramMap = new HashMap<String, Object>();
    Map<String, Object> resultMap = new HashMap<String, Object>();
    paramMap.put("openid", "99999999");
    paramMap.put("token", "00000000");
    dataMap.put("createPsnId", "0");
    dataMap.put(ShortUrlConst.TYPE, "G");
    shortUrlParametMap.put(ShortUrlConst.DES3_GRP_ID, Des3Utils.encodeToDes3(form.getGrpId().toString()));
    dataMap.put(ShortUrlConst.SHORT_URL_PARAMET, JacksonUtils.mapToJsonStr(shortUrlParametMap));
    paramMap.put("data", JacksonUtils.mapToJsonStr(dataMap));
    resultMap = getShortUrlServiceImpl.handleOpenDataForType(paramMap);
    // {msg=获取短地址数据成功, data=[{shortUrl=AFja6f}], status=success}
    if (resultMap.get("data") != null) {
      List<Map<String, Object>> list = (List<Map<String, Object>>) resultMap.get("data");
      if (list != null && list.size() > 0 && list.get(0).get("shortUrl") != null) {
        shortUrl = list.get(0).get("shortUrl").toString();
      }
    }
    return shortUrl;
  }

  private void buildGrpMainForm(Map<String, Object> paramet, GrpMainForm form) {
    Map<String, Object> grpData = (Map<String, Object>) paramet.get(GroupInfoConst.GRP_DATA);
    Object grpId = grpData.get(GroupInfoConst.GRP_ID);
    Object grpName = grpData.get(GroupInfoConst.GRP_NAME);
    Object grpDescription = grpData.get(GroupInfoConst.GRP_DESCRIPTION);
    Object openType = grpData.get(GroupInfoConst.OPEN_TYPE);
    Object keyWords = grpData.get(GroupInfoConst.KEY_WORDS);
    Object grpCategory = grpData.get(GroupInfoConst.GRP_CATEGORY);
    Object firstCategoryId = grpData.get(GroupInfoConst.FIRST_CATEGORYID);
    Object secondCategoryId = grpData.get(GroupInfoConst.SECOND_CATEGORYID);
    Object projectNo = grpData.get(GroupInfoConst.PROJECT_NO);
    Object projectStatus = grpData.get(GroupInfoConst.PROJECT_STATUS);
    Object nsfcCategoryId = grpData.get(GroupInfoConst.NSFC_CATEGORY_ID);
    Object ownerPsnId = grpData.get(GroupInfoConst.OWNER_PSN_ID);
    if (ownerPsnId != null && NumberUtils.isDigits(ownerPsnId.toString())) {
      form.setPsnId(NumberUtils.toLong(ownerPsnId.toString())); // 群组管理，创建群组
    } else {
      form.setPsnId(NumberUtils.toLong(paramet.get(OpenConsts.MAP_PSNID).toString()));
    }
    if (grpId != null) {
      form.setGrpId(NumberUtils.toLong(grpId.toString()));
    }
    if (grpName != null) {
      form.setGrpName(grpName.toString());
    }
    if (grpDescription != null) {
      form.setGrpDescription(grpDescription.toString());
    }
    if (openType != null && StringUtils.isNotBlank(openType.toString())) {
      form.setOpenType(openType.toString());
    } else {
      form.setOpenType(GrpConstant.GRP_BASEINFO_OPENTYPE_H);
    }
    // 最多10个关键词
    if (keyWords != null) {
      String[] strArr = keyWords.toString().split(";");
      String temp = "";
      int count = 0;
      if (strArr != null && strArr.length > 10) {
        for (int i = 0; i < strArr.length && count < 10; i++) {
          if (StringUtils.isNotBlank(strArr[i])) {
            count++;
            temp += strArr[i] + ";";
          }
        }
        form.setKeywords(temp);
      } else {
        form.setKeywords(keyWords.toString());
      }
    }
    if (projectNo != null) {
      form.setProjectNo(projectNo.toString());
    }
    if (projectStatus != null && NumberUtils.isNumber(projectStatus.toString())) {
      form.setProjectStatus(NumberUtils.toInt(projectStatus.toString()));
    }
    if (grpCategory != null && NumberUtils.isNumber(grpCategory.toString())) {
      form.setGrpCategory(Integer.parseInt(grpCategory.toString()));
    }
    if (firstCategoryId != null && NumberUtils.isNumber(firstCategoryId.toString())) {
      form.setFirstCategoryId(Integer.parseInt(firstCategoryId.toString()));
    }
    if (secondCategoryId != null && NumberUtils.isNumber(secondCategoryId.toString())) {
      form.setSecondCategoryId(Integer.parseInt(secondCategoryId.toString()));
    }
    if (nsfcCategoryId != null && StringUtils.isNotBlank(nsfcCategoryId.toString())) {
      form.setNsfcCategoryId(nsfcCategoryId.toString());
    }
  }

  public void createGrp(GrpMainForm form) {
    form.setGrpId(grpBaseInfoDao.getGrpIdBySeq());
    form.setDate(new Date());
    // 构建群组基本信息
    buildGrpBaseinfo(form);
    // 构建群组成员信息
    buildGrpMember(form);
    // 构建群组统计信息
    buildGrpStatistics(form);
    // 构建群组领域关键词信息
    buildGrpKwDisc(form);
    // 构建群组模块权限信息
    buildGrpControl(form);
    // 构建群组站外地址
    buildGrpIndexUrl(form);
    PsnStatistics statistics = psnStatisticsDao.get(form.getPsnId());
    if (statistics != null) {
      statistics.setGroupSum(statistics.getGroupSum() == null ? 1 : (statistics.getGroupSum() + 1));
    } else {
      statistics = new PsnStatistics();
      statistics.setPsnId(form.getPsnId());
      statistics.setGroupSum(1);
    }

    // 属性为null的保存为0
    buildZero(statistics);
    psnStatisticsDao.save(statistics);
  }

  private void buildZero(PsnStatistics p) {
    // 1.成果总数
    if (p.getPubSum() == null) {
      p.setPubSum(0);
    }
    // 2.成果引用次数总数
    if (p.getCitedSum() == null) {
      p.setCitedSum(0);
    }
    // 3.hindex指数
    if (p.getHindex() == null) {
      p.setHindex(0);
    }
    // 4.中文成果数
    if (p.getZhSum() == null) {
      p.setZhSum(0);
    }
    // 5.英文成果数
    if (p.getEnSum() == null) {
      p.setEnSum(0);
    }
    // 6.项目总数
    if (p.getPrjSum() == null) {
      p.setPrjSum(0);
    }
    // 7.好友总数
    if (p.getFrdSum() == null) {
      p.setFrdSum(0);
    }
    // 8.群组总数
    if (p.getGroupSum() == null) {
      p.setGroupSum(0);
    }
    // 9.成果被赞的总数
    if (p.getPubAwardSum() == null) {
      p.setPubAwardSum(0);
    }
    // 10.专利数
    if (p.getPatentSum() == null) {
      p.setPatentSum(0);
    }
    // 11.待认领成果数
    if (p.getPcfPubSum() == null) {
      p.setPcfPubSum(0);
    }
    // 12.成果全文推荐数
    if (p.getPubFullTextSum() == null) {
      p.setPubFullTextSum(0);
    }
    // 13.公开成果总数
    if (p.getOpenPubSum() == null) {
      p.setOpenPubSum(0);
    }
    // 14.公开项目总数
    if (p.getOpenPrjSum() == null) {
      p.setOpenPrjSum(0);
    }
    // 15.访问总数
    if (p.getVisitSum() == null) {
      p.setVisitSum(0);
    }
  }

  /**
   * 构建群组模块权限信息
   */
  private void buildGrpControl(GrpMainForm form) {
    GrpControl gc = new GrpControl();
    gc.setGrpId(form.getGrpId());
    gc.setIsDiscussShow(GrpConstant.GRP_CONTROL_DEFAULT_NUMBER);
    gc.setIsFileShow(GrpConstant.GRP_CONTROL_DEFAULT_NUMBER);
    gc.setIsIndexDiscussOpen(GrpConstant.GRP_CONTROL_DEFAULT_NUMBER);
    gc.setIsIndexFileOpen(GrpConstant.GRP_CONTROL_DEFAULT_NUMBER);
    gc.setIsIndexMemberOpen(GrpConstant.GRP_CONTROL_DEFAULT_NUMBER);
    if ("H".equals(form.getOpenType())) {
      gc.setIsPrjRefShow(GrpConstant.GRP_CONTROL_SPECIAL_NUMBER);// 文献
      gc.setIsIndexPubOpen(GrpConstant.GRP_CONTROL_SPECIAL_NUMBER);
    } else {
      gc.setIsPrjRefShow(GrpConstant.GRP_CONTROL_DEFAULT_NUMBER);// 文献
      gc.setIsIndexPubOpen(GrpConstant.GRP_CONTROL_DEFAULT_NUMBER);
    }
    gc.setIsMemberShow(GrpConstant.GRP_CONTROL_DEFAULT_NUMBER);
    gc.setIsPubShow(GrpConstant.GRP_CONTROL_DEFAULT_NUMBER);
    grpControlDao.save(gc);
  }

  /**
   * 构建群组基本信息
   */
  private void buildGrpBaseinfo(GrpMainForm form) {
    GrpBaseinfo grpBaseinfo = new GrpBaseinfo();
    grpBaseinfo.setGrpId(form.getGrpId());
    grpBaseinfo.setGrpName(form.getGrpName());
    grpBaseinfo.setGrpDescription(form.getGrpDescription());
    grpBaseinfo.setProjectNo(form.getProjectNo());
    grpBaseinfo.setProjectStatus(form.getProjectStatus());
    grpBaseinfo.setGrpCategory(form.getGrpCategory());
    grpBaseinfo.setOpenType(
        StringUtils.isNotBlank(form.getOpenType()) ? form.getOpenType() : GrpConstant.GRP_BASEINFO_OPENTYPE_H);
    grpBaseinfo.setCreatePsnId(form.getPsnId());
    grpBaseinfo.setOwnerPsnId(form.getPsnId());
    grpBaseinfo.setStatus(GrpConstant.GRP_BASEINFO_STATUS_NORMAL);
    grpBaseinfo.setCreateDate(form.getDate());
    // 群组头像随机 通过一级学科 随机
    if (form.getFirstCategoryId() != null) {
      String AUATARS = GrpConstant.GRP_BASEINFO_DEFAULT_LOGO_PATH + "/" + form.getFirstCategoryId() + "_"
          + RandomUtils.nextInt(1, 7) + ".jpg";
      grpBaseinfo.setGrpAuatars(AUATARS);
    } else {
      grpBaseinfo.setGrpAuatars(GrpConstant.GRP_BASEINFO_DEFAULT_AUATARS);
    }
    // grpBaseinfo.setGrpIndexUrl();
    grpBaseInfoDao.save(grpBaseinfo);
    // 保存到TMP_TASK_INFO_RECORD，群组推荐任务
    TmpTaskInfoRecord record = tmpTaskInfoRecordDao.getJobByhandleId(grpBaseinfo.getGrpId(), 24);
    if (record != null) {
      record.setStatus(0);
      record.setHandletime(new Date());
    } else {
      record = new TmpTaskInfoRecord(grpBaseinfo.getGrpId(), 24, new Date());
    }
    tmpTaskInfoRecordDao.saveOrUpdate(record);
  }

  /**
   * 构建群组成员信息
   */
  private void buildGrpMember(GrpMainForm form) {
    GrpMember gm = new GrpMember();
    gm.setGrpId(form.getGrpId());
    gm.setPsnId(form.getPsnId());
    gm.setGrpRole(GrpConstant.GRP_ROLE_OWNER);
    gm.setStatus(GrpConstant.GRP_MEMBER_STATUS_NORMAL);
    gm.setCreateDate(form.getDate());
    gm.setLastVisitDate(form.getDate());
    grpMemberDao.save(gm);
  }

  /**
   * 构建群组统计信息
   */
  private void buildGrpStatistics(GrpMainForm form) {
    GrpStatistics gs = new GrpStatistics();
    gs.setGrpId(form.getGrpId());
    gs.setSumMember(GrpConstant.GRP_STATISTICS_DEFAULT_MEMBERCOUNT);
    grpStatisticsDao.save(gs);
  }

  /**
   * 构建群组领域关键词信息
   */
  private void buildGrpKwDisc(GrpMainForm form) {
    GrpKwDisc gkd = new GrpKwDisc();
    gkd.setGrpId(form.getGrpId());
    if (form.getFirstCategoryId() != null && form.getSecondCategoryId() != null) {
      gkd.setDisciplines(form.getFirstCategoryId() + "," + form.getSecondCategoryId());
    }
    gkd.setFirstCategoryId(form.getFirstCategoryId());
    gkd.setSecondCategoryId(form.getSecondCategoryId());
    gkd.setKeywords(form.getKeywords());
    gkd.setNsfcCategoryId(form.getNsfcCategoryId());
    keywordCheckRepeat(gkd);
    dealNsfcCategoryId(gkd);
    grpKwDiscDao.save(gkd);
  }

  private void dealNsfcCategoryId(GrpKwDisc gkd) {
    if (StringUtils.isNotBlank(gkd.getNsfcCategoryId()) && gkd.getFirstCategoryId() == null
        && gkd.getSecondCategoryId() == null) {
      List<Long> list = categoryMapScmNsfcDao.getScmCategoryByNsfcCategory(gkd.getNsfcCategoryId());
      if (CollectionUtils.isNotEmpty(list)) {
        if (list.get(0).toString().length() > 0) {
          gkd.setFirstCategoryId(Integer.parseInt(list.get(0).toString().substring(0, 1)));
        }
        if (list.get(0).toString().length() > 2) {
          gkd.setSecondCategoryId(Integer.parseInt(list.get(0).toString().substring(0, 3)));
        }
        if(gkd.getFirstCategoryId() != null && gkd.getSecondCategoryId() != null){
          gkd.setDisciplines(gkd.getFirstCategoryId() + "," + gkd.getSecondCategoryId());
        }

      }
    }
  }

  /**
   * 关键词去重
   * 
   * @param grpKwDisc
   */
  private void keywordCheckRepeat(GrpKwDisc grpKwDisc) {
    if (grpKwDisc == null || StringUtils.isBlank(grpKwDisc.getKeywords())) {
      return;
    }
    String[] split = grpKwDisc.getKeywords().split(";");
    List<String> keywordList = new ArrayList<>();
    for (String keyword : split) {
      keyword = keyword.trim();
      if (keywordList.contains(keyword)) {
        continue;
      }
      keywordList.add(keyword);
    }
    StringBuilder sb = new StringBuilder();
    for (String keyword : keywordList) {
      sb.append(keyword);
      sb.append(";");
    }
    grpKwDisc.setKeywords(sb.substring(0, sb.length() - 1));
  }

  /**
   * 构建群组站外地址
   * 
   * @param form
   */
  private void buildGrpIndexUrl(GrpMainForm form) {
    GrpIndexUrl grpIndexUrl = new GrpIndexUrl();
    grpIndexUrl.setGrpId(form.getGrpId());
    grpIndexUrl.setPsnId(form.getPsnId());
    grpIndexUrl.setUpdateDate(new Date());
    grpIndexUrl.setGrpIndexUrl(getShortUrl(form));
    grpIndexUrlDao.save(grpIndexUrl);
  }

  public static void main(String[] args) {
    for (int i = 0; i < 100; i++) {
      System.out.println(RandomUtils.nextInt(1, 7));
    }
  }
}
