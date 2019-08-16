package com.smate.web.group.service.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.ConstDictionaryManage;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.group.dao.group.GroupBaseinfoDao;
import com.smate.web.group.dao.group.GroupControlDao;
import com.smate.web.group.dao.group.GroupFilterDao;
import com.smate.web.group.dao.group.GroupKeyDiscDao;
// import com.smate.web.group.dao.group.GroupPsnDao;
import com.smate.web.group.dao.group.GroupStatisticsDao;
import com.smate.web.group.dao.group.invit.GroupInvitePsnDao;
import com.smate.web.group.form.GroupPsnForm;
import com.smate.web.group.model.group.GroupBaseInfo;
import com.smate.web.group.model.group.GroupControl;
import com.smate.web.group.model.group.GroupFilter;
import com.smate.web.group.model.group.GroupKeyDisc;
import com.smate.web.group.model.group.GroupPsn;
import com.smate.web.group.model.group.GroupStatistics;
import com.smate.web.group.model.group.invit.GroupInvitePsn;
import com.smate.web.group.service.group.consts.ConstDisciplineManage;
import com.smate.web.group.service.group.psn.PersonManager;

/**
 * 群组服务实现类
 * 
 * @author tsz
 *
 */
@Service("groupService")
@Transactional(rollbackFor = Exception.class)
public class GroupServiceImpl implements GroupService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  // @Autowired
  // private GroupPsnDao groupPsnDao;
  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;
  @Autowired
  private GroupBaseinfoDao groupBaseInfoDao;
  @Autowired
  private GroupStatisticsDao groupStatisticsDao;
  @Autowired
  private GroupFilterDao groupFilterDao;
  @Autowired
  private GroupControlDao groupControlDao;
  @Autowired
  private GroupKeyDiscDao groupKeyDiscDao;
  @Autowired
  private ConstDictionaryManage constDictionaryManage;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private GroupPsnSearchService groupPsnSearchService;
  @Autowired
  private ConstDisciplineManage constDisciplineManage;

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;

  /**
   * 判断群组 是否存在
   * 
   * @param groupId
   * @return true 表示存在 false 表示不存在
   */
  @Override
  public Boolean checkGroup(Long groupId) {

    GroupBaseInfo groupPsn = groupBaseInfoDao.getGroupBaseInfo(groupId);
    if (groupPsn == null) {
      return false;
    }
    return true;
  }

  /**
   * 获取群组 显示的详细信息
   * 
   * @param groupId
   * @param currentPsnId
   * @return
   */
  @Override
  public void getGroupPsn(GroupPsnForm form) {
    GroupBaseInfo baseInfo = this.getBaseInfo(form.getGroupId());
    // 如果群组基本信息为空，则直接返回，不构建其他参数对象.
    if (baseInfo != null) {
      GroupPsn groupPsn = new GroupPsn();
      groupPsn = this.rebuildGroupBaseInfo(groupPsn, baseInfo);
      groupPsn.setGroupDescription(null);// SCM-6988

      // 群组检索过滤基本信息.
      GroupFilter groupFilter = this.getFileter(form.getGroupId());
      if (groupFilter != null) {
        groupPsn = this.rebuildGroupFilter(groupPsn, groupFilter);
      }
      // 群组控制信息.
      GroupControl groupControl = this.getControl(form.getGroupId());
      if (groupControl != null) {
        groupPsn = this.rebuildGroupControl(groupPsn, groupControl);
      }
      // 群组统计信息.
      GroupStatistics statistics = this.getStatistics(form.getGroupId());
      if (statistics != null) {
        groupPsn = this.rebuildGroupStatistics(groupPsn, statistics);
      }
      // 群组学科关键词信息.
      GroupKeyDisc keyDisc = this.getKeyDisc(form.getGroupId());
      if (keyDisc != null) {
        groupPsn = this.rebuildGroupKeyDisc(groupPsn, keyDisc);
      }

      form.setGroupPsn(groupPsn);
    }


  }


  /**
   * 获取群组基本信息记录.
   * 
   * @param groupId
   * @return
   */
  public GroupBaseInfo getBaseInfo(Long groupId) {
    return groupBaseInfoDao.getGroupBaseInfo(groupId);
  }



  /**
   * 获取群组检索过滤信息.
   * 
   * @param groupId
   * @return
   */

  private GroupFilter getFileter(Long groupId) {
    return groupFilterDao.getGroupFilter(groupId);
  }

  /**
   * 获取群组控制信息.
   * 
   * @param groupId
   * @return
   */

  private GroupControl getControl(Long groupId) {
    return groupControlDao.getGroupControl(groupId);
  }

  /**
   * 获取群组学科关键词信息.
   * 
   * @param groupId
   * @return
   */


  private GroupKeyDisc getKeyDisc(Long groupId) {
    return groupKeyDiscDao.getGroupKeyDisc(groupId);
  }



  private GroupPsn rebuildGroupBaseInfo(GroupPsn groupPsn, GroupBaseInfo baseInfo) {
    groupPsn.setGroupId(baseInfo.getGroupId());
    groupPsn.setGroupAnnouncement(baseInfo.getGroupAnnouncement());
    groupPsn.setGroupCategory(baseInfo.getGroupCategory());
    groupPsn.setGroupDescription(baseInfo.getGroupDescription());
    groupPsn.setGroupImgUrl(baseInfo.getGroupImgUrl());
    groupPsn.setGroupName(baseInfo.getGroupName());
    groupPsn.setGroupNo(baseInfo.getGroupNo());
    groupPsn.setGroupPageUrl(baseInfo.getGroupPageUrl());
    groupPsn.setAddress(baseInfo.getAddress());
    groupPsn.setCreateDate(baseInfo.getCreateDate());
    groupPsn.setEmail(baseInfo.getEmail());
    groupPsn.setFileId(baseInfo.getFileId());
    groupPsn.setFundingTypes(baseInfo.getFundingTypes());
    groupPsn.setLastVisitDate(baseInfo.getLastVisitDate());
    groupPsn.setTel(baseInfo.getTel());
    groupPsn.setUpdateDate(baseInfo.getUpdateDate());
    return groupPsn;
  }

  private GroupPsn rebuildGroupFilter(GroupPsn groupPsn, GroupFilter filter) {
    groupPsn.setGroupCode(filter.getGroupCode());
    groupPsn.setGroupId(filter.getGroupId());
    groupPsn.setOpenType(filter.getOpenType());
    groupPsn.setOwnerPsnId(filter.getOwnerPsnId());
    groupPsn.setStatus(filter.getStatus());
    return groupPsn;
  }


  private GroupPsn rebuildGroupControl(GroupPsn groupPsn, GroupControl groupControl) {
    groupPsn.setGroupId(groupControl.getGroupId());
    groupPsn.setIsDiscuss(groupControl.getIsDiscuss());
    groupPsn.setIsGroupMemberView(groupControl.getIsGroupMemberView());
    groupPsn.setIsIsisPrj(groupControl.getIsIsisPrj());
    groupPsn.setIsMaterialView(groupControl.getIsMaterialView());
    groupPsn.setIsMemberPublish(groupControl.getIsMemberPublish());
    groupPsn.setIsPageDescOpen(groupControl.getIsPageDescOpen());
    groupPsn.setIsPageFileOpen(groupControl.getIsPageFileOpen());
    groupPsn.setIsPageMaterialOpen(groupControl.getIsPageMaterialOpen());
    groupPsn.setIsPageMemberOpen(groupControl.getIsPageMemberOpen());
    groupPsn.setIsPageOpen(groupControl.getIsPageOpen());
    groupPsn.setIsPagePrjOpen(groupControl.getIsPagePrjOpen());
    groupPsn.setIsPagePubOpen(groupControl.getIsPagePubOpen());
    groupPsn.setIsPageRefOpen(groupControl.getIsPageRefOpen());
    groupPsn.setIsPageWorkOpen(groupControl.getIsPageWorkOpen());
    groupPsn.setIsPrjView(groupControl.getIsPrjView());
    groupPsn.setIsPubView(groupControl.getIsPubView());
    groupPsn.setIsRefView(groupControl.getIsRefView());
    // groupPsn.setIsSave(groupControl.getIsSave());
    groupPsn.setIsShareFile(groupControl.getIsShareFile());
    groupPsn.setIsWorkView(groupControl.getIsWorkView());
    groupPsn.setPrjViewType(groupControl.getPrjViewType());
    groupPsn.setPubViewType(groupControl.getPubViewType());
    groupPsn.setRefViewType(groupControl.getRefViewType());
    groupPsn.setShareFileType(groupControl.getShareFileType());
    return groupPsn;
  }

  private GroupPsn rebuildGroupStatistics(GroupPsn groupPsn, GroupStatistics statistics) {
    groupPsn.setGroupId(statistics.getGroupId());
    groupPsn.setSumFiles(statistics.getSumFiles());
    groupPsn.setSumFilesNfolder(statistics.getSumFilesNfolder());
    groupPsn.setSumMaterials(statistics.getSumMaterials());
    groupPsn.setSumMaterialsNfolder(statistics.getSumMaterialsNfolder());
    groupPsn.setSumMembers(statistics.getSumMembers());
    groupPsn.setSumPrjs(statistics.getSumPrjs());
    groupPsn.setSumPrjsNfolder(statistics.getSumPrjsNfolder());
    groupPsn.setSumPubs(statistics.getSumPubs());
    groupPsn.setSumPubsNfolder(statistics.getSumPubsNfolder());
    groupPsn.setSumRefs(statistics.getSumRefs());
    groupPsn.setSumRefsNfolder(statistics.getSumRefsNfolder());
    groupPsn.setSumSubjects(statistics.getSumSubjects());
    groupPsn.setSumToMembers(statistics.getSumToMembers());
    groupPsn.setSumWorks(statistics.getSumWorks());
    groupPsn.setSumWorksNfolder(statistics.getSumWorksNfolder());
    groupPsn.setVisitCount(statistics.getVisitCount());
    return groupPsn;
  }

  private GroupPsn rebuildGroupKeyDisc(GroupPsn groupPsn, GroupKeyDisc keyDisc) {
    groupPsn.setDiscipline1(keyDisc.getDiscipline1());
    groupPsn.setDisciplines(keyDisc.getDisciplines());
    groupPsn.setDiscCodes(keyDisc.getDiscCodes());
    groupPsn.setEnKeyWords(keyDisc.getEnKeyWords());
    groupPsn.setEnKeyWords1(keyDisc.getEnKeyWords1());
    // groupPsn.setEnKeyWordsList(keyDisc.getEnKeyWordsList());
    groupPsn.setGroupId(keyDisc.getGroupId());
    groupPsn.setKeyWords(keyDisc.getKeyWords());
    groupPsn.setKeyWords1(keyDisc.getKeyWords1());
    // groupPsn.setKewWordsList(keyDisc.getKewWordsList());
    return groupPsn;
  }


  /**
   * 获取群组统计记录.
   * 
   * @param groupId
   * @return
   */
  public GroupStatistics getStatistics(Long groupId) {
    return groupStatisticsDao.getStatistics(groupId);
  }

  /**
   * 创建群组
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean createGroupInfo(GroupPsnForm form) throws Exception {
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(saveGroupInfo(form).toString());
    if (resultMap != null) {
      if (resultMap.get("result") != null) {
        String result = resultMap.get("result").toString();
        if (result.length() > 9) {
          String des3GroupId =
              ServiceUtil.encodeToDes3(result.substring(result.indexOf("<groupId>") + 9, result.indexOf("</groupId>")));
          form.setDes3GroupId(des3GroupId);

        }
      }

    }

    if ("success".equals(resultMap.get("status"))) {
      return true;
    }
    return false;
  }

  /**
   * 调restful,创建群组
   */
  private Object saveGroupInfo(GroupPsnForm form) {
    Map<String, Object> mapData = new HashMap<String, Object>();
    mapData.put("openid", form.getOpenId());
    mapData.put("token",
        ((StringUtils.isNotBlank(form.getFromSys()) && form.getFromSys().length() != 16) ? form.getFromSys()
            : "00000000") + "lhd25dhl");
    mapData.put("des3PrjId", form.getDes3PrjId());
    mapData.put("groupCategory", form.getGroupCategory());
    mapData.put("data", buildGroupDataParameter(form));
    return restTemplate.postForObject(SERVER_URL, mapData, Object.class);
  }

  /**
   * 构造创建群组的 xml
   */
  private Object buildGroupDataParameter(GroupPsnForm form) {
    Map<String, Object> map = new HashMap<String, Object>();
    String syncXml = "<root><groupPsn><groupCategory>"
        + (StringUtils.isNotBlank(form.getGroupCategory()) ? form.getGroupCategory() : "")// 群组分类，10兴趣群组，11项目群组
        + "</groupCategory><groupName>" + (StringUtils.isNotBlank(form.getGroupName()) ? form.getGroupName() : "")// 群组名称
        + "</groupName><groupDescription>"
        + (StringUtils.isNotBlank(form.getGroupDescription()) ? form.getGroupDescription() : "")// 群组简介
        + "</groupDescription><openType>" + (StringUtils.isNotBlank(form.getOpenType()) ? form.getOpenType() : "")// 群组开放类型
        + "</openType><keyWords>" + (StringUtils.isNotBlank(form.getKeyWords()) ? form.getKeyWords() : "")
        + "</keyWords><keyWords1>"// 群组关键词
        + (StringUtils.isNotBlank(form.getKeyWords1()) ? form.getKeyWords1() : "") + "</keyWords1><disciplines>"
        + (StringUtils.isNotBlank(form.getDisciplines()) ? form.getDisciplines() : "") + "</disciplines>"// 群组的学科领域
        + "</groupPsn></root>";
    map.put("syncXml", syncXml);
    map.put("psnId", form.getPsnId());
    return JacksonUtils.mapToJsonStr(map);
  }

  @Override
  public List<ConstDictionary> findConstGroupList() throws Exception {
    Locale local = LocaleContextHolder.getLocale();
    List<ConstDictionary> constGroupList = getConstGroupList();
    List<ConstDictionary> resustList = new ArrayList<ConstDictionary>();
    for (ConstDictionary cd : constGroupList) {
      ConstDictionary constDictionary = new ConstDictionary(cd.getKey().getCategory(), cd.getKey().getCode(),
          cd.getEnUsName(), cd.getZhCnName(), cd.getZhTwName(), cd.getSeqNo());
      if ("8".equals(cd.getKey().getCode()) || "9".equals(cd.getKey().getCode()))// 创建群组的时候不能选热门群组
        continue;

      if ("0".equals(cd.getKey().getCode())) {
        constDictionary.setZhCnName(" ");
        constDictionary.getKey().setCode(null);
      } else {
        constDictionary.setZhCnName(local.equals(Locale.US) ? cd.getEnUsName() : cd.getZhCnName());

      }
      resustList.add(constDictionary);
    }
    return resustList;
  }

  private List<ConstDictionary> getConstGroupList() throws Exception {
    return constDictionaryManage.getConstByGategory(GROUP_CATEGORY);
  }

  @Override
  public GroupPsn findMyGroup(Long groupId, Integer nodeId) throws Exception {
    GroupPsn groupPsn = new GroupPsn();
    try {
      groupPsn = groupPsnSearchService.getBuildGroupPsn(groupId);// groupPsnDao.findMyGroup(groupId);
    } catch (Exception e) {
      logger.error("获取我的群组详细出错", e);
      throw new Exception(e);
    }

    return groupPsn;
  }

  @Override
  public GroupPsnForm modelToForm(GroupPsn dataGroupPsn) {
    GroupPsnForm groupPsn = new GroupPsnForm();
    groupPsn.setGroupId(dataGroupPsn.getGroupId());

    groupPsn.setGroupName(dataGroupPsn.getGroupName());

    groupPsn.setGroupDescription(HtmlUtils.Html2Text(dataGroupPsn.getGroupDescription()).trim()
        .replaceAll(" {1,}", "&nbsp;").replaceAll("\n", "&nbsp;"));
    groupPsn.setGroupCategory(dataGroupPsn.getGroupCategory());

    groupPsn.setDisciplines(dataGroupPsn.getDisciplines());

    groupPsn.setGroupAnnouncement(dataGroupPsn.getGroupAnnouncement());

    groupPsn.setEmail(dataGroupPsn.getEmail());

    groupPsn.setTel(dataGroupPsn.getTel());

    groupPsn.setAddress(dataGroupPsn.getAddress());

    groupPsn.setIsGroupMemberView(dataGroupPsn.getIsGroupMemberView());

    groupPsn.setIsDiscuss(dataGroupPsn.getIsDiscuss());

    groupPsn.setIsShareFile(dataGroupPsn.getIsShareFile());

    groupPsn.setShareFileType(dataGroupPsn.getShareFileType());

    groupPsn.setIsPrjView(dataGroupPsn.getIsPrjView());

    groupPsn.setPrjViewType(dataGroupPsn.getPrjViewType());

    groupPsn.setIsPubView(dataGroupPsn.getIsPubView());

    groupPsn.setPubViewType(dataGroupPsn.getPubViewType());

    groupPsn.setIsRefView(dataGroupPsn.getIsRefView());

    groupPsn.setRefViewType(dataGroupPsn.getRefViewType());

    groupPsn.setOpenType(dataGroupPsn.getOpenType());

    groupPsn.setIsMemberPublish(dataGroupPsn.getIsMemberPublish());

    groupPsn.setFileId(dataGroupPsn.getFileId());

    groupPsn.setGroupImgUrl(dataGroupPsn.getGroupImgUrl());

    groupPsn.setGroupPageUrl(dataGroupPsn.getGroupPageUrl());

    groupPsn.setIsPageOpen(dataGroupPsn.getIsPageOpen());

    groupPsn.setIsPageDescOpen(dataGroupPsn.getIsPageDescOpen());

    groupPsn.setIsPageMemberOpen(dataGroupPsn.getIsPageMemberOpen());

    groupPsn.setIsPagePrjOpen(dataGroupPsn.getIsPagePrjOpen());

    groupPsn.setIsPagePubOpen(dataGroupPsn.getIsPagePubOpen());

    groupPsn.setIsPageRefOpen(dataGroupPsn.getIsPageRefOpen());

    groupPsn.setIsPageFileOpen(dataGroupPsn.getIsPageFileOpen());

    groupPsn.setSumMembers(dataGroupPsn.getSumMembers());

    groupPsn.setSumSubjects(dataGroupPsn.getSumSubjects());

    groupPsn.setSumPubs(dataGroupPsn.getSumPubs());

    groupPsn.setSumPrjs(dataGroupPsn.getSumPrjs());

    groupPsn.setSumRefs(dataGroupPsn.getSumRefs());

    groupPsn.setSumFiles(dataGroupPsn.getSumFiles());

    groupPsn.setOwnerPsnId(dataGroupPsn.getOwnerPsnId());

    groupPsn.setCreateDate(dataGroupPsn.getCreateDate());

    groupPsn.setUpdateDate(dataGroupPsn.getUpdateDate());

    groupPsn.setStatus(dataGroupPsn.getStatus());

    // groupPsn.setDisJSON(dataGroupPsn.getDisJSON());

    groupPsn.setDiscipline1(dataGroupPsn.getDiscipline1());

    // groupPsn.setDes3GroupNodeId(dataGroupPsn.getDes3GroupNodeId());

    groupPsn.setDes3GroupId(dataGroupPsn.getDes3GroupId());

    // groupPsn.setNavAction(dataGroupPsn.getNavAction());

    // groupPsn.setGroupNodeId(dataGroupPsn.getGroupNodeId());

    /* groupPsn.setIsSave(dataGroupPsn.getIsSave()); */

    // groupPsn.setMemberFriends(dataGroupPsn.getMemberFriends());

    // groupPsn.setNavGroupName(dataGroupPsn.getNavGroupName());

    // groupPsn.setCurrentGroupRole(dataGroupPsn.getCurrentGroupRole());

    groupPsn.setSumPubsNfolder(dataGroupPsn.getSumPubsNfolder());

    groupPsn.setSumPrjsNfolder(dataGroupPsn.getSumPrjsNfolder());

    groupPsn.setSumRefsNfolder(dataGroupPsn.getSumRefsNfolder());

    groupPsn.setSumFilesNfolder(dataGroupPsn.getSumFilesNfolder());

    groupPsn.setOldGroupId(dataGroupPsn.getOldGroupId());

    groupPsn.setSumToMembers(dataGroupPsn.getSumToMembers());

    groupPsn.setDiscCodes(dataGroupPsn.getDiscCodes());

    groupPsn.setIsIsisPrj(dataGroupPsn.getIsIsisPrj());

    groupPsn.setKeyWords1(dataGroupPsn.getKeyWords1());

    groupPsn.setKeyWords(dataGroupPsn.getKeyWords());

    // groupPsn.setKewWordsList(dataGroupPsn.getKewWordsList());

    // groupPsn.setAllKeyWordsList(dataGroupPsn.getAllKeyWordsList());

    groupPsn.setEnKeyWords1(dataGroupPsn.getEnKeyWords1());

    groupPsn.setEnKeyWords(dataGroupPsn.getEnKeyWords());


    // groupPsn.setEnKeyWordsList(dataGroupPsn.getEnKeyWordsList());

    // groupPsn.setJoinInGroupStyle(dataGroupPsn.getJoinInGroupStyle());

    // groupPsn.setExeIsJoinInGroupPsnId(dataGroupPsn.getExeIsJoinInGroupPsnId());

    // groupPsn.setIsisGuid(dataGroupPsn.getIsisGuid());

    groupPsn.setIsWorkView(dataGroupPsn.getIsWorkView());

    groupPsn.setIsMaterialView(dataGroupPsn.getIsMaterialView());

    groupPsn.setIsPageWorkOpen(dataGroupPsn.getIsPageWorkOpen());

    groupPsn.setIsPageMaterialOpen(dataGroupPsn.getIsPageMaterialOpen());

    groupPsn.setSumWorks(dataGroupPsn.getSumWorks());

    groupPsn.setSumMaterials(dataGroupPsn.getSumMaterials());

    groupPsn.setSumWorksNfolder(dataGroupPsn.getSumWorksNfolder());

    groupPsn.setSumMaterialsNfolder(dataGroupPsn.getSumMaterialsNfolder());

    groupPsn.setGroupDescriptionClob(dataGroupPsn.getGroupDescription());// liangguokeng群组简介改造

    groupPsn.setFundingTypes(dataGroupPsn.getFundingTypes());

    groupPsn.setGroupNo(dataGroupPsn.getGroupNo());
    groupPsn.setGroupCode(dataGroupPsn.getGroupCode());

    return groupPsn;
  }

  @Override
  public String findDisciplineNameList(List<Long> disciplineList) throws Exception {
    Locale local = LocaleContextHolder.getLocale();
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      for (Long descipline : disciplineList) {
        if (descipline != null && descipline > 0) {
          String disciplineName = constDisciplineManage.getDisciplineName(descipline, local);// 获取学科名称
          resultMap.put(descipline.toString(), disciplineName);
        }
      }
    } catch (Exception e) {
      logger.error(" 获取学科名称出错", e);
      throw new Exception(e);
    }
    return JacksonUtils.jsonMapSerializer(resultMap);
  }

  @Override
  public GroupInvitePsn findGroupInvitePsn(Long groupId) throws Exception {
    return findGroupInvitePsn(groupId, false);
  }

  @Override
  public GroupInvitePsn findGroupInvitePsn(Long groupId, boolean isActivity) throws Exception {
    Long userId = SecurityUtils.getCurrentUserId();
    GroupInvitePsn groupInvitePsn = null;
    try {
      groupInvitePsn = groupInvitePsnDao.findGroupInvitePsn(groupId, userId);

      Person person = personManager.getPersonByRecommend(userId);

      if (groupInvitePsn != null) {
        groupInvitePsn.setPsnName(person.getName());
        groupInvitePsn.setPsnFirstName(person.getFirstName());
        groupInvitePsn.setPsnLastName(person.getLastName());
        if (isActivity) {
          groupInvitePsn.setIsActivity("1");// 激活
          groupInvitePsnDao.save(groupInvitePsn);
        }
      }

    } catch (Exception e) {
      logger.error("获取群组个人出错", e);
      throw new Exception(e);
    }

    return groupInvitePsn;
  }

  @Override
  public GroupPsn formToModel(GroupPsnForm groupPsn) {
    GroupPsn dataGroupPsn = new GroupPsn();
    dataGroupPsn.setGroupId(groupPsn.getGroupId());

    if (groupPsn.getGroupName() != null && groupPsn.getGroupName().length() > 40) {
      groupPsn.setGroupName(groupPsn.getGroupName().substring(0, 40));
    }
    dataGroupPsn.setGroupName(groupPsn.getGroupName());

    if (groupPsn.getGroupDescription() != null && groupPsn.getGroupDescription().length() > 120) {
      groupPsn.getGroupDescription().substring(0, 120);
    }
    dataGroupPsn.setGroupDescription(groupPsn.getGroupDescription());

    dataGroupPsn.setGroupCategory(groupPsn.getGroupCategory());

    dataGroupPsn.setDisciplines(groupPsn.getDisciplines());

    dataGroupPsn.setGroupAnnouncement(groupPsn.getGroupAnnouncement());

    dataGroupPsn.setEmail(groupPsn.getEmail());

    dataGroupPsn.setTel(groupPsn.getTel());

    dataGroupPsn.setAddress(groupPsn.getAddress());

    dataGroupPsn.setIsGroupMemberView(groupPsn.getIsGroupMemberView());

    dataGroupPsn.setIsDiscuss(groupPsn.getIsDiscuss());

    dataGroupPsn.setIsShareFile(groupPsn.getIsShareFile());

    dataGroupPsn.setShareFileType(groupPsn.getShareFileType());

    dataGroupPsn.setIsPrjView(groupPsn.getIsPrjView());

    dataGroupPsn.setPrjViewType(groupPsn.getPrjViewType());

    dataGroupPsn.setIsPubView(groupPsn.getIsPubView());

    dataGroupPsn.setPubViewType(groupPsn.getPubViewType());

    dataGroupPsn.setIsRefView(groupPsn.getIsRefView());

    dataGroupPsn.setRefViewType(groupPsn.getRefViewType());

    dataGroupPsn.setOpenType(groupPsn.getOpenType());

    dataGroupPsn.setIsMemberPublish(groupPsn.getIsMemberPublish());

    dataGroupPsn.setFileId(groupPsn.getFileId());

    dataGroupPsn.setGroupImgUrl(groupPsn.getGroupImgUrl());

    dataGroupPsn.setGroupPageUrl(groupPsn.getGroupPageUrl());

    dataGroupPsn.setIsPageOpen(groupPsn.getIsPageOpen());

    dataGroupPsn.setIsPageDescOpen(groupPsn.getIsPageDescOpen());

    dataGroupPsn.setIsPageMemberOpen(groupPsn.getIsPageMemberOpen());

    dataGroupPsn.setIsPagePrjOpen(groupPsn.getIsPagePrjOpen());

    dataGroupPsn.setIsPagePubOpen(groupPsn.getIsPagePubOpen());

    dataGroupPsn.setIsPageRefOpen(groupPsn.getIsPageRefOpen());

    dataGroupPsn.setIsPageFileOpen(groupPsn.getIsPageFileOpen());

    dataGroupPsn.setSumMembers(groupPsn.getSumMembers());

    dataGroupPsn.setSumSubjects(groupPsn.getSumSubjects());

    dataGroupPsn.setSumPubs(groupPsn.getSumPubs());

    dataGroupPsn.setSumPrjs(groupPsn.getSumPrjs());

    dataGroupPsn.setSumRefs(groupPsn.getSumRefs());

    dataGroupPsn.setSumFiles(groupPsn.getSumFiles());

    dataGroupPsn.setOwnerPsnId(groupPsn.getOwnerPsnId());

    dataGroupPsn.setCreateDate(groupPsn.getCreateDate());

    dataGroupPsn.setUpdateDate(groupPsn.getUpdateDate());

    dataGroupPsn.setStatus(groupPsn.getStatus());

    /* dataGroupPsn.setDisJSON(groupPsn.getDisJSON()); */

    dataGroupPsn.setDiscipline1(groupPsn.getDiscipline1());

    /* dataGroupPsn.setDes3GroupNodeId(groupPsn.getDes3GroupNodeId()); */

    dataGroupPsn.setDes3GroupId(groupPsn.getDes3GroupId());

    // dataGroupPsn.setNavAction(groupPsn.getNavAction());

    // dataGroupPsn.setGroupNodeId(groupPsn.getGroupNodeId());

    // dataGroupPsn.setIsSave(groupPsn.getIsSave());

    // dataGroupPsn.setMemberFriends(groupPsn.getMemberFriends());

    // dataGroupPsn.setNavGroupName(groupPsn.getNavGroupName());

    // dataGroupPsn.setCurrentGroupRole(groupPsn.getCurrentGroupRole());

    dataGroupPsn.setSumPubsNfolder(groupPsn.getSumPubsNfolder());

    dataGroupPsn.setSumPrjsNfolder(groupPsn.getSumPrjsNfolder());

    dataGroupPsn.setSumRefsNfolder(groupPsn.getSumRefsNfolder());

    dataGroupPsn.setSumFilesNfolder(groupPsn.getSumFilesNfolder());

    dataGroupPsn.setOldGroupId(groupPsn.getOldGroupId());

    dataGroupPsn.setSumToMembers(groupPsn.getSumToMembers());

    dataGroupPsn.setDiscCodes(groupPsn.getDiscCodes());

    dataGroupPsn.setIsIsisPrj(groupPsn.getIsIsisPrj());

    dataGroupPsn.setKeyWords1(groupPsn.getKeyWords1());

    dataGroupPsn.setKeyWords(groupPsn.getKeyWords());

    dataGroupPsn.setEnKeyWords1(groupPsn.getEnKeyWords1());

    dataGroupPsn.setEnKeyWords(groupPsn.getEnKeyWords());

    // dataGroupPsn.setEnKeyWordsList(groupPsn.getEnKeyWordsList());

    // dataGroupPsn.setJoinInGroupStyle(groupPsn.getJoinInGroupStyle());

    // dataGroupPsn.setExeIsJoinInGroupPsnId(groupPsn.getExeIsJoinInGroupPsnId());

    // dataGroupPsn.setIsisGuid(groupPsn.getIsisGuid());

    dataGroupPsn.setIsWorkView(groupPsn.getIsWorkView());

    dataGroupPsn.setIsMaterialView(groupPsn.getIsMaterialView());

    dataGroupPsn.setIsPageWorkOpen(groupPsn.getIsPageWorkOpen());

    dataGroupPsn.setIsPageMaterialOpen(groupPsn.getIsPageMaterialOpen());

    dataGroupPsn.setSumWorks(groupPsn.getSumWorks());

    dataGroupPsn.setSumMaterials(groupPsn.getSumMaterials());

    dataGroupPsn.setSumWorksNfolder(groupPsn.getSumWorksNfolder());

    dataGroupPsn.setSumMaterialsNfolder(groupPsn.getSumMaterialsNfolder());

    // dataGroupPsn.setGroupUrl(groupPsn.getGroupUrl());

    dataGroupPsn.setGroupDescription(groupPsn.getGroupDescription());

    dataGroupPsn.setFundingTypes(groupPsn.getFundingTypes());

    dataGroupPsn.setGroupNo(groupPsn.getGroupNo());
    dataGroupPsn.setGroupCode(groupPsn.getGroupCode());

    return dataGroupPsn;
  }

  @Override
  public void updateGroupPsn(GroupPsn groupPsn) throws Exception {}


}
