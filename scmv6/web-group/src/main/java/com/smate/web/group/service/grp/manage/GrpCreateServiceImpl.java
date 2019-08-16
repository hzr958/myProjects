package com.smate.web.group.service.grp.manage;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.statistics.PsnStatisticsUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.group.action.grp.form.GrpMainForm;
import com.smate.web.group.constant.grp.GrpConstant;
import com.smate.web.group.dao.grp.file.GrpFileDao;
import com.smate.web.group.dao.grp.grpbase.GrpBaseInfoDao;
import com.smate.web.group.dao.grp.grpbase.GrpControlDao;
import com.smate.web.group.dao.grp.grpbase.GrpIndexUrlDao;
import com.smate.web.group.dao.grp.grpbase.GrpKwDiscDao;
import com.smate.web.group.dao.grp.grpbase.GrpStatisticsDao;
import com.smate.web.group.dao.grp.member.GrpMemberDao;
import com.smate.web.group.dao.grp.pub.GrpPubIndexUrlDao;
import com.smate.web.group.dao.grp.pub.GrpPubsDao;
import com.smate.web.group.model.group.GrpPubs;
import com.smate.web.group.model.grp.file.GrpFile;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.model.grp.grpbase.GrpControl;
import com.smate.web.group.model.grp.grpbase.GrpIndexUrl;
import com.smate.web.group.model.grp.grpbase.GrpKwDisc;
import com.smate.web.group.model.grp.grpbase.GrpStatistics;
import com.smate.web.group.model.grp.member.GrpMember;
import com.smate.web.group.model.grp.pub.GrpPubIndexUrl;
import com.smate.web.group.service.open.OpenUserUnionService;

/**
 * 群组创建业务处理service实现
 * 
 * @author zzx
 *
 */
@Service("grpCreateService")
@Transactional(rollbackFor = Exception.class)
public class GrpCreateServiceImpl implements GrpCreateService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OpenUserUnionService openUserUnionService;
  @Autowired
  private GrpBaseService grpBaseService;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private GrpKwDiscDao grpKwDiscDao;
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private GrpStatisticsDao grpStatisticsDao;
  @Autowired
  private GrpControlDao grpControlDao;
  @Autowired
  private GrpPubsDao grpPubsDao;
  @Autowired
  private GrpFileDao grpFileDao;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;
  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;
  @Autowired
  private BatchJobsService batchJobsService;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private GrpIndexUrlDao grpIndexUrlDao;
  @Autowired
  private GrpPubIndexUrlDao grpPubIndexUrlDao;

  @Override
  public void updateGrp(GrpMainForm form) throws Exception {
    GrpBaseinfo grpBaseinfo = grpBaseInfoDao.get(form.getGrpId());
    if (grpBaseinfo != null) {
      grpBaseinfo.setGrpName(HtmlUtils.htmlUnescape(form.getGrpName()));
      grpBaseinfo.setGrpDescription(HtmlUtils.htmlUnescape(form.getGrpDescription()));
      grpBaseinfo.setProjectNo(HtmlUtils.htmlUnescape(form.getProjectNo()));
      grpBaseinfo.setOpenType(
          StringUtils.isNotBlank(form.getOpenType()) ? form.getOpenType() : GrpConstant.GRP_BASEINFO_OPENTYPE_H);
      // 判断群组是否修改 群组默认logo
      GrpKwDisc grpKwDisc = grpKwDiscDao.get(form.getGrpId());
      // 修改了领域 并且 原来没有手动上传过logo的
      if (grpKwDisc != null && grpKwDisc.getFirstCategoryId() != null
          && grpKwDisc.getFirstCategoryId().equals(form.getFirstCategoryId())
          && grpBaseinfo.getGrpAuatars().indexOf(GrpConstant.GRP_BASEINFO_DEFAULT_LOGO_PATH) >= 0) {
        String AUATARS = GrpConstant.GRP_BASEINFO_DEFAULT_LOGO_PATH + "/" + form.getFirstCategoryId() + "_"
            + RandomUtils.nextInt(1, 7) + ".jpg";
        grpBaseinfo.setGrpAuatars(AUATARS);
      }
      grpBaseinfo.setUpdateDate(new Date());
      grpBaseInfoDao.save(grpBaseinfo);
      this.saveGroupPubInfoReCalTask(grpBaseinfo.getGrpId());
      if (grpKwDisc == null) {
        // demo 接口创建时，关键词会为空
        grpKwDisc = new GrpKwDisc();
        grpKwDisc.setGrpId(form.getGrpId());
      }
      if (grpKwDisc != null) {
        grpKwDisc.setDisciplines(form.getFirstCategoryId() + "," + form.getSecondCategoryId());
        if (StringUtils.isNotBlank(form.getKeywords())) {
          grpKwDisc.setKeywords(URLDecoder.decode(form.getKeywords(), "utf-8"));
        } else {
          grpKwDisc.setKeywords("");
        }
        keywordCheckRepeat(grpKwDisc);
        grpKwDisc.setFirstCategoryId(form.getFirstCategoryId());
        grpKwDisc.setSecondCategoryId(form.getSecondCategoryId());
        grpKwDiscDao.save(grpKwDisc);
      }
      grpBaseService.saveTmpTaskInfoRecord(grpBaseinfo.getGrpId());
    }
  }

  /**
   * 关键词去重
   *
   * @param grpKwDisc
   */
  public void keywordCheckRepeat(GrpKwDisc grpKwDisc) {
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

  public void saveGroupPubInfoReCalTask(Long groupId) {
    try {
      String jobContext = "{\"msg_id\":" + groupId + "}";
      BatchJobs job = batchJobsFactory.getBatchJob1(jobContext, "B", BatchOpenCodeEnum.GROUP_PUB_RECALCULATE);
      this.batchJobsService.saveJob(job);
    } catch (Exception e) {
      logger.error("更新群组成果相关信息不完全,群组id是" + groupId, e);
    }

  }

  @Override
  public void createGrp(GrpMainForm form) throws Exception {

    Long openId = openUserUnionService.buildOpenId(form.getPsnId(), "00000000");
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("openid", openId);
    map.put("token", "00000000lhd25dhl");

    Map<String, Object> dataMap = new HashMap<String, Object>();
    Map<String, Object> grpDataMap = new HashMap<String, Object>();
    grpDataMap.put("grpName", HtmlUtils.htmlUnescape(form.getGrpName()));
    grpDataMap.put("grpCategory", form.getGrpCategory());
    grpDataMap.put("grpDescription", HtmlUtils.htmlUnescape(form.getGrpDescription()));
    grpDataMap.put("openType", form.getOpenType());
    if (StringUtils.isNotBlank(form.getKeywords())) {
      form.setKeywords(URLDecoder.decode(form.getKeywords(), "utf-8"));
    }
    grpDataMap.put("keyWords", form.getKeywords());
    grpDataMap.put("projectNo", HtmlUtils.htmlUnescape(form.getProjectNo()));
    grpDataMap.put("projectStatus", "");
    grpDataMap.put("firstCategoryId", form.getFirstCategoryId());
    grpDataMap.put("secondCategoryId", form.getSecondCategoryId());
    dataMap.put("grpData", JacksonUtils.mapToJsonStr(grpDataMap));
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    Object obj = restTemplate.postForObject(this.openResfulUrl, map, Object.class);
    Long grpId = getGrpId(JacksonUtils.jsonToMap(obj.toString()));
    if (grpId == null || grpId == 0L) {
      throw new Exception("调用接口创建群组失败");
    }
    form.setGrpId(grpId);
    modifyGrpPermissions(form);
  }

  public void modifyGrpPermissions(GrpMainForm form) throws Exception {
    GrpControl control = grpControlDao.get(form.getGrpId());
    if (control != null) {
      control.setIsIndexDiscussOpen(form.getIsIndexDiscussOpen());
      control.setIsIndexFileOpen(form.getIsIndexFileOpen());
      control.setIsIndexMemberOpen(form.getIsIndexMemberOpen());
      control.setIsIndexPubOpen(form.getIsIndexPubOpen());
      control.setIsCurwareFileShow(form.getIsCurwareFileShow());
      control.setIsWorkFileShow(form.getIsWorkFileShow());
      control.setIsPrjPubShow(form.getIsPrjPubShow());
      control.setIsPrjRefShow(form.getIsPrjRefShow());
      grpControlDao.save(control);
    }
  }

  private Long getGrpId(Map<String, Object> resultMap) {
    Long grpId = 0L;
    if (resultMap != null && "success".equals(resultMap.get("status"))) {
      List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
      if (result != null && result.size() > 0) {
        grpId = NumberUtils.toLong(result.get(0).get("grpId").toString());
      }
    }
    return grpId;

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
    gc.setIsIndexPubOpen(GrpConstant.GRP_CONTROL_DEFAULT_NUMBER);
    gc.setIsMemberShow(GrpConstant.GRP_CONTROL_DEFAULT_NUMBER);
    gc.setIsPubShow(GrpConstant.GRP_CONTROL_DEFAULT_NUMBER);
    grpControlDao.save(gc);
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
    gkd.setDisciplines(form.getFirstCategoryId() + "," + form.getSecondCategoryId());
    gkd.setFirstCategoryId(form.getFirstCategoryId());
    gkd.setSecondCategoryId(form.getSecondCategoryId());
    gkd.setKeywords(form.getKeywords());
    grpKwDiscDao.save(gkd);
  }

  @Override
  public void doCopyGrp(GrpMainForm form) throws Exception {
    if (StringUtils.isNotBlank(form.getGrpName())) {
      GrpBaseinfo baseinfo = grpBaseInfoDao.get(form.getTargetGrpId());
      if (baseinfo != null) {
        form.setDate(new Date());
        form.setGrpId(grpBaseInfoDao.getGrpIdBySeq());
        /**
         * 先复制群组科技领域信息，然后根据科技领域的id保存群组头像基本信息。 SCM-14313
         */
        // 复制群组领域关键词信息和群组模块权限信息
        copyGrpKwDiscAndGrpControl(form);
        // 构建群组基本信息
        copyGrpBaseinfo(baseinfo, form);
        // 构建群组成员信息
        buildGrpMember(form);
        // 构建群组统计信息
        buildGrpStatistics(form);
        // <!-- 11 项目群组 成果，文献，文件 -->
        // <!-- 10 课程群组 ，文献，课件，作业 -->
        switch (baseinfo.getGrpCategory()) {
          case 10:
            copyCourseGrpInfo(form);
            break;
          case 11:
            copyProjectGrpInfo(form);
            break;
          default:
            copyOtherGrpInfo(form);
            break;
        }
        // 构建群组站外地址
        buildGrpIndexUrl(form);
        PsnStatistics statistics = psnStatisticsDao.get(form.getPsnId());
        if (statistics != null) {
          statistics.setGroupSum(statistics.getGroupSum() == null ? 1 : (statistics.getGroupSum() + 1));
          // 属性为null的保存为0
          PsnStatisticsUtils.buildZero(statistics);
          psnStatisticsDao.save(statistics);
        }
      }
    }
  }

  /**
   * 复制其他群组的信息
   *
   * @param form
   */
  private void copyOtherGrpInfo(GrpMainForm form) {
    // 复制成果信息
    if ("1".equals(form.getIsCopyGrpPubs())) {
      copyGrpPubs(form);
    }
    // 复制课件信息
    if ("1".equals(form.getIsCopyGrpCourseware())) {
      copyGrpRes(form);
    }
  }

  /**
   * 复制课程群组的信息
   *
   * @param form
   */
  private void copyCourseGrpInfo(GrpMainForm form) {
    // 复制成果信息
    if ("1".equals(form.getIsCopyGrpPubs())) {
      copyGrpPubs(form);
    }
    // 复制课件信息
    if ("1".equals(form.getIsCopyCourseGrpCourseware())) {
      copyCourseGrpCourse(form);
    }
    // 复制作业信息
    if ("1".equals(form.getIsCopyCourseGrpWork())) {
      copyCourseGrpWork(form);
    }
  }

  /**
   * 复制项目群组的信息
   *
   * @param form
   */
  private void copyProjectGrpInfo(GrpMainForm form) {
    // 复制项目成果信息
    if ("1".equals(form.getIsCopyProjectGrpPubs())) {
      copyProjectGrpPubs(form);
    }
    // 复制项目文献信息
    if ("1".equals(form.getIsCopyProjectGrpRefs())) {
      copyProjectGrpRefs(form);
    }
    // 复制课件信息
    if ("1".equals(form.getIsCopyGrpCourseware())) {
      copyGrpRes(form);
    }
  }

  /**
   * 保存群组短地址--复制群组用--
   *
   * @param form
   */
  private void buildGrpIndexUrl(GrpMainForm form) {
    GrpIndexUrl grpIndexUrl = new GrpIndexUrl();
    grpIndexUrl.setGrpId(form.getGrpId());
    grpIndexUrl.setPsnId(form.getPsnId());
    grpIndexUrl.setUpdateDate(new Date());
    Map<String, Object> shortUrlParametMap = new HashedMap();
    shortUrlParametMap.put("des3GrpId", form.getDes3GrpId());
    grpIndexUrl.setGrpIndexUrl(getShortUrl(form, "G", shortUrlParametMap));
    grpIndexUrlDao.save(grpIndexUrl);

  }

  /**
   * 保存群组成果短地址--复制群组用--
   *
   * @param form
   * @param pubId
   */
  private void buildGrpPubIndexUrl(GrpMainForm form, Long pubId) {
    GrpPubIndexUrl grpIndexUrl = new GrpPubIndexUrl();
    grpIndexUrl.setGrpId(form.getGrpId());
    grpIndexUrl.setPsnId(form.getPsnId());
    grpIndexUrl.setPubId(pubId);
    grpIndexUrl.setUpdateDate(new Date());
    Map<String, Object> shortUrlParametMap = new HashedMap();
    shortUrlParametMap.put("des3GrpId", form.getDes3GrpId());
    shortUrlParametMap.put("des3PubId", Des3Utils.encodeToDes3(pubId.toString()));
    grpIndexUrl.setPubIndexUrl(getShortUrl(form, "B", shortUrlParametMap));
    grpPubIndexUrlDao.save(grpIndexUrl);

  }

  /**
   * 获取短地址--复制群组用--
   *
   * @param form
   * @param type
   * @param shortUrlParametMap
   * @return
   */
  private String getShortUrl(GrpMainForm form, String type, Map<String, Object> shortUrlParametMap) {
    String shortUrl = "";
    Map<String, Object> map = new HashedMap();
    Map<String, Object> dataMap = new HashedMap();
    map.put("openid", "99999999");
    map.put("token", "00000000sht22url");
    dataMap.put("createPsnId", "0");
    dataMap.put("type", type);
    dataMap.put("shortUrlParamet", JacksonUtils.mapToJsonStr(shortUrlParametMap));
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    // 访问Open系统接口获取ShortUrl
    Object obj = restTemplate.postForObject(this.openResfulUrl, map, Object.class);
    // 接口返回数据处理
    Map<String, Object> objMap = JacksonUtils.jsonToMap(obj.toString());
    // 获取短地址值
    if (objMap.get("result") != null) {
      List<Map<String, Object>> list = (List<Map<String, Object>>) objMap.get("result");
      if (list != null && list.size() > 0 && list.get(0).get("shortUrl") != null) {
        shortUrl = list.get(0).get("shortUrl").toString();
      }
    }
    return shortUrl;
  }

  private void copyGrpRes(GrpMainForm form) {
    List<GrpFile> grpFileList = grpFileDao.getGrpFileList(form.getTargetGrpId(), form.getPsnId());
    if (CollectionUtils.isNotEmpty(grpFileList)) {
      for (GrpFile g : grpFileList) {
        g.setGrpId(form.getGrpId());
        grpFileDao.save(g);
      }
      grpStatisticsDao.get(form.getGrpId()).setSumFile(grpFileList.size());
    }
  }

  // 课程群组课件fileModuleType 0=文件 ， 1=作业 ， 2=课件
  private void copyCourseGrpCourse(GrpMainForm form) {
    List<GrpFile> grpFileList = grpFileDao.getGrpFileList(form.getTargetGrpId(), 2, form.getPsnId());
    if (CollectionUtils.isNotEmpty(grpFileList)) {
      for (GrpFile g : grpFileList) {
        g.setGrpId(form.getGrpId());
        grpFileDao.save(g);
      }
      grpStatisticsDao.get(form.getGrpId()).setSumFile(grpFileList.size());
    }
  }

  // 课程群组作业fileModuleType 0=文件 ， 1=作业 ， 2=课件
  private void copyCourseGrpWork(GrpMainForm form) {
    List<GrpFile> grpFileList = grpFileDao.getGrpFileList(form.getTargetGrpId(), 1, form.getPsnId());
    if (CollectionUtils.isNotEmpty(grpFileList)) {
      for (GrpFile g : grpFileList) {
        g.setGrpId(form.getGrpId());
        grpFileDao.save(g);
      }
      grpStatisticsDao.get(form.getGrpId()).setSumFile(grpFileList.size());
    }
  }

  /**
   * 复制成果信息
   *
   * @param form
   */
  private void copyGrpPubs(GrpMainForm form) {
    List<GrpPubs> pubsList = grpPubsDao.getGrpPubsList(form.getTargetGrpId(), form.getPsnId());
    if (pubsList != null && pubsList.size() > 0) {
      for (GrpPubs g : pubsList) {
        g.setGrpId(form.getGrpId());
        buildGrpPubIndexUrl(form, g.getPubId());
        grpPubsDao.save(g);
      }
      grpStatisticsDao.get(form.getGrpId()).setSumPubs(pubsList.size());
    }

  }

  /**
   * 复制项目成果信息isProjectPub 是否项目成果 是否项目成果 (0否)（1是）
   *
   * @param form
   */
  private void copyProjectGrpPubs(GrpMainForm form) {
    List<GrpPubs> pubsList = grpPubsDao.getProjectGrpPubsList(form.getTargetGrpId(), form.getPsnId(), 1);
    if (pubsList != null && pubsList.size() > 0) {
      for (GrpPubs g : pubsList) {
        g.setGrpId(form.getGrpId());
        buildGrpPubIndexUrl(form, g.getPubId());
        grpPubsDao.save(g);
      }
      grpStatisticsDao.get(form.getGrpId()).setSumPubs(pubsList.size());
    }

  }

  /**
   * 复制项目成果信息 项目文献isProjectPub 是否项目成果 是否项目成果 (0否)（1是）
   *
   * @param form
   */
  private void copyProjectGrpRefs(GrpMainForm form) {
    List<GrpPubs> pubsList = grpPubsDao.getProjectGrpPubsList(form.getTargetGrpId(), form.getPsnId(), 0);
    if (pubsList != null && pubsList.size() > 0) {
      for (GrpPubs g : pubsList) {
        g.setGrpId(form.getGrpId());
        buildGrpPubIndexUrl(form, g.getPubId());
        grpPubsDao.save(g);
      }
      grpStatisticsDao.get(form.getGrpId()).setSumPubs(pubsList.size());
    }

  }

  /**
   * 复制群组领域关键词信息和群组模块权限信息
   *
   * @param form
   */
  private void copyGrpKwDiscAndGrpControl(GrpMainForm form) {
    // 构建群组领域关键词信息
    GrpKwDisc gkd = grpKwDiscDao.get(form.getTargetGrpId());
    if (gkd != null) {
      GrpKwDisc newgkd = new GrpKwDisc();
      newgkd.setGrpId(form.getGrpId());
      newgkd.setDisciplines(gkd.getDisciplines());
      newgkd.setFirstCategoryId(gkd.getFirstCategoryId());
      newgkd.setSecondCategoryId(gkd.getSecondCategoryId());
      form.setFirstCategoryId(gkd.getFirstCategoryId());
      if ("1".equals(form.getIsCopyBaseinfo())) {// 是否复制群组基本信息
        newgkd.setKeywords(gkd.getKeywords());
      }
      grpKwDiscDao.save(newgkd);
    } else {
      buildGrpKwDisc(form);
    }
    // 构建群组模块权限信息
    GrpControl gc = grpControlDao.get(form.getTargetGrpId());
    if (gc != null && "1".equals(form.getIsCopyBaseinfo())) {
      GrpControl newgc = new GrpControl();
      newgc.setGrpId(form.getGrpId());
      newgc.setIsDiscussShow(gc.getIsDiscussShow());
      newgc.setIsFileShow(gc.getIsFileShow());
      newgc.setIsIndexDiscussOpen(gc.getIsIndexDiscussOpen());
      newgc.setIsIndexFileOpen(gc.getIsIndexFileOpen());
      newgc.setIsIndexMemberOpen(gc.getIsIndexMemberOpen());
      newgc.setIsIndexPubOpen(gc.getIsIndexPubOpen());
      newgc.setIsMemberShow(gc.getIsMemberShow());
      newgc.setIsPubShow(gc.getIsPubShow());
      newgc.setIsPrjPubShow(gc.getIsPrjPubShow());
      newgc.setIsPrjRefShow(gc.getIsPrjRefShow());
      newgc.setIsCurwareFileShow(gc.getIsCurwareFileShow());
      newgc.setIsWorkFileShow(gc.getIsWorkFileShow());
      grpControlDao.save(newgc);
    } else {
      buildGrpControl(form);
    }
  }

  /**
   * 复制 GrpBaseinfo对象,并保存
   *
   * @param g
   * @return
   */
  private void copyGrpBaseinfo(GrpBaseinfo g, GrpMainForm form) {
    GrpBaseinfo ng = new GrpBaseinfo();
    ng.setGrpId(form.getGrpId());
    ng.setGrpName(form.getGrpName());
    ng.setGrpDescription(g.getGrpDescription());
    ng.setGrpCategory(g.getGrpCategory());
    ng.setOwnerPsnId(form.getPsnId());
    ng.setCreatePsnId(form.getPsnId());
    ng.setCreateDate(form.getDate());
    ng.setStatus(GrpConstant.GRP_BASEINFO_STATUS_NORMAL);
    ng.setUpdateDate(form.getDate());
    if ("1".equals(form.getIsCopyBaseinfo())) {// 是否复制群组基本信息
      ng.setProjectNo(g.getProjectNo());
      ng.setProjectStatus(g.getProjectStatus());
      ng.setGrpAuatars(g.getGrpAuatars());
      ng.setOpenType(g.getOpenType());
    } else {
      // 群组头像随机 通过一级学科 随机
      if (form.getFirstCategoryId() != null) {
        String AUATARS = GrpConstant.GRP_BASEINFO_DEFAULT_LOGO_PATH + "/" + form.getFirstCategoryId() + "_"
            + RandomUtils.nextInt(1, 7) + ".jpg";
        ng.setGrpAuatars(AUATARS);
      } else {
        ng.setGrpAuatars(GrpConstant.GRP_BASEINFO_DEFAULT_AUATARS);
      }
      ng.setOpenType(GrpConstant.GRP_BASEINFO_OPENTYPE_H);
    }
    grpBaseInfoDao.save(ng);
    grpBaseService.saveTmpTaskInfoRecord(ng.getGrpId());
  }

  @Override
  public void saveGrpAvartars(GrpMainForm form) throws Exception {
    GrpBaseinfo grpBaseinfo = grpBaseInfoDao.get(form.getGrpId());
    if (grpBaseinfo != null) {
      grpBaseinfo.setGrpAuatars(form.getGrpAvartars());
      grpBaseInfoDao.save(grpBaseinfo);
    }
  }
}
