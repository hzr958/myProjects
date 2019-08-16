package com.smate.center.open.service.group.psn;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.group.GroupBaseinfoDao;
import com.smate.center.open.dao.group.GroupControlDao;
import com.smate.center.open.dao.group.GroupFilterDao;
import com.smate.center.open.dao.group.GroupKeyDiscDao;
import com.smate.center.open.dao.group.GroupPrjExtendDao;
// import com.smate.center.open.dao.group.GroupPsnDao;
import com.smate.center.open.dao.group.GroupStatisticsDao;
import com.smate.center.open.model.group.GroupBaseInfo;
import com.smate.center.open.model.group.GroupControl;
import com.smate.center.open.model.group.GroupFilter;
import com.smate.center.open.model.group.GroupKeyDisc;
import com.smate.center.open.model.group.GroupPsn;
import com.smate.center.open.model.group.GroupStatistics;
import com.smate.center.open.model.group.prj.GroupPrjExtend;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;

@Service("groupPsnEditService")
@Transactional(rollbackFor = Exception.class)
public class GroupPsnEditServiceImpl implements GroupPsnEditService {
  @Autowired
  private GroupBaseinfoDao groupBaseInfoDao;
  @Autowired
  private GroupControlDao groupControlDao;
  @Autowired
  private GroupFilterDao groupFilterDao;
  @Autowired
  private GroupKeyDiscDao groupKeyDiscDao;
  @Autowired
  private GroupStatisticsDao groupStatisticsDao;
  @Autowired
  private GroupPrjExtendDao groupPrjExtendDao;
  // @Autowired
  // private GroupPsnDao groupPsnDao;
  @Value("${domainscm}")
  private String domain;


  /**
   * 保存群组记录
   * 
   * @author lhd
   */
  @Override
  public void saveGroupPsn(GroupPsn groupPsn, Long openId) throws Exception {
    if (groupPsn != null) {
      // 保存群组基本信息.
      GroupBaseInfo baseInfo = buildBaseInfo(groupPsn);
      if (baseInfo != null) {
        saveBaseInfo(baseInfo);
      }
      // 保存群组控制信息
      GroupControl groupControl = buildControl(groupPsn);
      if (groupControl != null) {
        saveControl(groupControl);
      }
      // 保存群组检索过滤信息
      GroupFilter groupFilter = buildFilter(groupPsn);
      if (groupFilter != null) {
        saveFilter(groupFilter);
      }
      // 保存群组学科关键词信息.
      GroupKeyDisc keyDisc = buildKeyDisc(groupPsn);
      if (openId == 99999999) {
        keyDisc.setCategoryId(
            NumberUtils.toLong(groupPsn.getDisciplines()) == 0l ? null : NumberUtils.toLong(groupPsn.getDisciplines()));
      }
      if (keyDisc != null) {
        saveKeyDisc(keyDisc);
      }
      // 保存群组统计信息.
      GroupStatistics statistics = buildStatistics(groupPsn);
      if (statistics != null) {
        saveStatistics(statistics);
      }
      // 保存群组项目扩展表信息(是项目群组才会保存)
      if ("11".equals(groupPsn.getGroupCategory())) {
        GroupPrjExtend groupPrjExtend = buildGroupPrjExtend(groupPsn);
        if (groupPrjExtend != null) {
          saveGroupPrjExtend(groupPrjExtend);
        }
      }

      // 保存群组主表信息
      // groupPsnDao.save(groupPsn);
    }
  }

  /**
   * 转换封装群组基本信息
   * 
   * @author lhd
   * @param groupPsn
   * @return
   */
  private GroupBaseInfo buildBaseInfo(GroupPsn groupPsn) {
    GroupBaseInfo baseInfo = groupBaseInfoDao.getGroupBaseInfo(groupPsn.getGroupId());
    boolean flag = false;
    if (baseInfo == null) {
      baseInfo = new GroupBaseInfo();
      // 群组ID
      if (groupPsn.getGroupId() != null) {
        baseInfo.setGroupId(groupPsn.getGroupId());
      }
      // 群组编号
      if (groupPsn.getGroupNo() != null) {
        baseInfo.setGroupNo(groupPsn.getGroupNo());
      }
    }
    // 群组公告
    if (StringUtils.isNotBlank(groupPsn.getGroupAnnouncement())) {
      flag = true;
      baseInfo.setGroupAnnouncement(groupPsn.getGroupAnnouncement());
    }
    // 群组分类
    if (StringUtils.isNotBlank(groupPsn.getGroupCategory())) {
      flag = true;
      baseInfo.setGroupCategory(groupPsn.getGroupCategory());
    }
    // 群组介绍
    if (StringUtils.isNotBlank(groupPsn.getGroupDescription())) {
      flag = true;
      String shortBrief = HtmlUtils
          .replaceBlank(HtmlUtils.Html2Text(groupPsn.getGroupDescription()).toString().replaceAll(" {1,}", "&nbsp;"))
          .replaceAll("&nbsp;", " ");
      int end = shortBrief.length() > 200 ? 200 : shortBrief.length();
      baseInfo.setGroupDescription(shortBrief.substring(0, end));
    }
    // 群组图片
    if (StringUtils.isNotBlank(groupPsn.getGroupImgUrl())) {
      flag = true;
      baseInfo.setGroupImgUrl(groupPsn.getGroupImgUrl());
    } else {
      baseInfo.setGroupImgUrl(domain + "/resscmwebsns/images_v5/50X50g.gif");
    }
    // 群组名称
    if (StringUtils.isNotBlank(groupPsn.getGroupName())) {
      flag = true;
      baseInfo.setGroupName(groupPsn.getGroupName());
    }
    // 群组主页地址
    if (StringUtils.isNotBlank(groupPsn.getGroupPageUrl())) {
      flag = true;
      baseInfo.setGroupPageUrl(groupPsn.getGroupPageUrl());
    }
    // 联系地址
    if (StringUtils.isNotBlank(groupPsn.getAddress())) {
      flag = true;
      baseInfo.setAddress(groupPsn.getAddress());
    }
    // 创建时间
    if (groupPsn.getCreateDate() != null) {
      flag = true;
      baseInfo.setCreateDate(groupPsn.getCreateDate());
    }
    // Email
    if (StringUtils.isNotBlank(groupPsn.getEmail())) {
      flag = true;
      baseInfo.setEmail(groupPsn.getEmail());
    }
    // 图片文件编号
    if (groupPsn.getFileId() != null) {
      flag = true;
      baseInfo.setFileId(groupPsn.getFileId());
    }
    // 科研群组-资助类别
    // if (StringUtils.isNotBlank(groupPsn.getFundingTypes())) {
    // flag = true;
    // baseInfo.setFundingTypes(groupPsn.getFundingTypes());
    // }
    // GROUP_BASEINFO表的FUNDING_TYPES字段存储的是项目批准号
    if (StringUtils.isNotBlank(groupPsn.getPrjExternalNo())) {
      flag = true;
      baseInfo.setFundingTypes(groupPsn.getPrjExternalNo());
    }
    // 群组最后一次访问日期
    if (groupPsn.getLastVisitDate() != null) {
      flag = true;
      baseInfo.setLastVisitDate(groupPsn.getLastVisitDate());
    } else {
      // 如果群组基本信息保存时未设置最后访问日期，则将其默认设置为群组创建时间.
      baseInfo.setLastVisitDate(groupPsn.getCreateDate());
    }
    // 联系电话
    if (StringUtils.isNotBlank(groupPsn.getTel())) {
      flag = true;
      baseInfo.setTel(groupPsn.getTel());
    }
    // 更新时间
    if (groupPsn.getUpdateDate() != null) {
      flag = true;
      baseInfo.setUpdateDate(groupPsn.getUpdateDate());
    }
    if (!flag) {
      return null;
    }
    return baseInfo;
  }

  /**
   * 保存群组基本信息
   * 
   * @author lhd
   */
  @Override
  public void saveBaseInfo(GroupBaseInfo groupBaseInfo) throws Exception {
    if (groupBaseInfo != null) {
      groupBaseInfoDao.saveBaseInfo(groupBaseInfo);
    }
  }

  /**
   * 转换封装群组控制信息
   * 
   * @author lhd
   * @param groupPsn
   * @return
   */
  private GroupControl buildControl(GroupPsn groupPsn) {
    GroupControl groupControl = groupControlDao.getGroupControl(groupPsn.getGroupId());
    boolean flag = false;
    if (groupControl == null) {
      groupControl = new GroupControl();
      if (groupPsn.getGroupId() != null) {
        groupControl.setGroupId(groupPsn.getGroupId());
      }
    }
    // 是否支持讨论板[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsDiscuss())) {
      flag = true;
      groupControl.setIsDiscuss(groupPsn.getIsDiscuss());
    }
    // 项目组成员是否可以查看其他群组成员的科研项目和成果[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsGroupMemberView())) {
      flag = true;
      groupControl.setIsGroupMemberView(groupPsn.getIsGroupMemberView());
    }
    if (groupPsn.getIsIsisPrj() != null) {
      flag = true;
      groupControl.setIsIsisPrj(groupPsn.getIsIsisPrj());
    }
    // 是否支持教学课件[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsMaterialView())) {
      flag = true;
      groupControl.setIsMaterialView(groupPsn.getIsMaterialView());
    }
    // 是否允许普通成员发表话题[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsMemberPublish())) {
      flag = true;
      groupControl.setIsMemberPublish(groupPsn.getIsMemberPublish());
    }
    // 主页是否显示群组介绍[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsPageDescOpen())) {
      flag = true;
      groupControl.setIsPageDescOpen(groupPsn.getIsPageDescOpen());
    }
    // 主页是否显示文件[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsPageFileOpen())) {
      flag = true;
      groupControl.setIsPageFileOpen(groupPsn.getIsPageFileOpen());
    }
    // 主页是否显示教学课件[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsPageMaterialOpen())) {
      flag = true;
      groupControl.setIsPageMaterialOpen(groupPsn.getIsPageMaterialOpen());
    }
    // 主页是否显示群组成员[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsPageMemberOpen())) {
      flag = true;
      groupControl.setIsPageMemberOpen(groupPsn.getIsPageMemberOpen());
    }
    // 群组主页是否开放[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsPageOpen())) {
      flag = true;
      groupControl.setIsPageOpen(groupPsn.getIsPageOpen());
    }
    // 主页是否显示科研项目[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsPagePrjOpen())) {
      flag = true;
      groupControl.setIsPagePrjOpen(groupPsn.getIsPagePrjOpen());
    }
    // 主页是否显示科研成果[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsPagePubOpen())) {
      flag = true;
      groupControl.setIsPagePubOpen(groupPsn.getIsPagePubOpen());
    }
    // 主页是否显示科研文献[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsPageRefOpen())) {
      flag = true;
      groupControl.setIsPageRefOpen(groupPsn.getIsPageRefOpen());
    }
    // 主页是否显示作业[1=是,0=否]
    // if (StringUtils.isNotBlank(groupPsn.getIsPageWorkOpen())) {
    // flag = true;
    // groupControl.setIsPageWorkOpen(groupPsn.getIsPageWorkOpen());
    // }
    // 是否支持群组项目[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsPrjView())) {
      flag = true;
      groupControl.setIsPrjView(groupPsn.getIsPrjView());
    }
    // 是否支持群组成果[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsPubView())) {
      flag = true;
      groupControl.setIsPubView(groupPsn.getIsPubView());
    }
    // 是否支持群组参考文献[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsRefView())) {
      flag = true;
      groupControl.setIsRefView(groupPsn.getIsRefView());
    }
    // 是否支持文件共享[1=是,0=否]
    if (StringUtils.isNotBlank(groupPsn.getIsShareFile())) {
      flag = true;
      groupControl.setIsShareFile(groupPsn.getIsShareFile());
    }
    // 是否支持作业[1=是,0=否]
    // if (StringUtils.isNotBlank(groupPsn.getIsWorkView())) {
    // flag = true;
    // groupControl.setIsWorkView(groupPsn.getIsWorkView());
    // }
    // 群组项目权限类型[1=所有成员,0=管理员]
    if (StringUtils.isNotBlank(groupPsn.getPrjViewType())) {
      flag = true;
      groupControl.setPrjViewType(groupPsn.getPrjViewType());
    }
    // 群组成果权限类型[1=所有成员,0=管理员]
    if (StringUtils.isNotBlank(groupPsn.getPubViewType())) {
      flag = true;
      groupControl.setPubViewType(groupPsn.getPubViewType());
    }
    // 参考文献权限类型[1=所有成员,0=管理员]
    if (StringUtils.isNotBlank(groupPsn.getRefViewType())) {
      flag = true;
      groupControl.setRefViewType(groupPsn.getRefViewType());
    }
    // 文件共享权限类型[1=所有成员,0=管理员]
    if (StringUtils.isNotBlank(groupPsn.getShareFileType())) {
      flag = true;
      groupControl.setShareFileType(groupPsn.getShareFileType());
    }
    if (!flag) {
      return null;
    }
    return groupControl;
  }

  /**
   * 保存群组控制信息
   * 
   * @author lhd
   */
  @Override
  public void saveControl(GroupControl groupControl) throws Exception {
    groupControlDao.saveGroupControl(groupControl);
  }

  /**
   * 转换封装群组过滤信息.
   * 
   * @author lhd
   * @param groupPsn
   * @return
   */
  private GroupFilter buildFilter(GroupPsn groupPsn) {
    GroupFilter groupFilter = groupFilterDao.getGroupFilter(groupPsn.getGroupId());
    boolean flag = false;
    if (groupFilter == null) {
      groupFilter = new GroupFilter();
      if (groupPsn.getGroupId() != null) {
        groupFilter.setGroupId(groupPsn.getGroupId());
      }
    }
    // 群组认证码
    if (StringUtils.isNotBlank(groupPsn.getGroupCode())) {
      flag = true;
      groupFilter.setGroupCode(groupPsn.getGroupCode());
    }
    // 公开类型[O=开放,H=半开放,P=保密]
    if (StringUtils.isNotBlank(groupPsn.getOpenType())) {
      flag = true;
      groupFilter.setOpenType(groupPsn.getOpenType());
    }
    // 群组拥有者的psn_id
    if (groupPsn.getOwnerPsnId() != null) {
      flag = true;
      groupFilter.setOwnerPsnId(groupPsn.getOwnerPsnId());
    }
    // 群组状态[01=正常,99=删除]
    if (StringUtils.isNotBlank(groupPsn.getStatus())) {
      flag = true;
      groupFilter.setStatus(groupPsn.getStatus());
    }
    if (!flag) {
      return null;
    }
    return groupFilter;
  }

  /**
   * 保存群组基本检索过滤条件信息
   * 
   * @author lhd
   */
  @Override
  public void saveFilter(GroupFilter groupFilter) throws Exception {
    groupFilterDao.saveGroupFilter(groupFilter);
  }

  /**
   * 转换封装群组学科关键词信息.
   * 
   * @author lhd
   * @param groupPsn
   * @return
   */
  private GroupKeyDisc buildKeyDisc(GroupPsn groupPsn) {
    GroupKeyDisc keyDisc = groupKeyDiscDao.getGroupKeyDisc(groupPsn.getGroupId());
    boolean flag = false;
    if (keyDisc == null) {
      keyDisc = new GroupKeyDisc();
      if (groupPsn.getGroupId() != null) {
        keyDisc.setGroupId(groupPsn.getGroupId());
      }
    }
    // 学科代码1
    if (groupPsn.getDiscipline1() != null) {
      flag = true;
      keyDisc.setDiscipline1(groupPsn.getDiscipline1());
    }
    // 学科领域
    if (StringUtils.isNotBlank(groupPsn.getDisciplines())) {
      flag = true;
      keyDisc.setDisciplines(groupPsn.getDisciplines());
    }
    // 学科代码
    if (StringUtils.isNotBlank(groupPsn.getDiscCodes())) {
      flag = true;
      keyDisc.setDiscCodes(groupPsn.getDiscCodes());
    }
    // 英文关键字
    if (StringUtils.isNotBlank(groupPsn.getEnKeyWords())) {
      flag = true;
      keyDisc.setEnKeyWords(groupPsn.getEnKeyWords());
    }
    // 英文关键字1
    if (StringUtils.isNotBlank(groupPsn.getEnKeyWords1())) {
      flag = true;
      keyDisc.setEnKeyWords1(groupPsn.getEnKeyWords1());
    }
    // if (CollectionUtils.isNotEmpty(groupPsn.getEnKeyWordsList())) {
    // flag = true;
    // keyDisc.setEnKeyWordsList(groupPsn.getEnKeyWordsList());
    // }
    // 中文关键字
    if (StringUtils.isNotBlank(groupPsn.getKeyWords())) {
      flag = true;
      keyDisc.setKeyWords(groupPsn.getKeyWords());
    }
    // 关键字1
    if (StringUtils.isNotBlank(groupPsn.getKeyWords1())) {
      flag = true;
      keyDisc.setKeyWords1(groupPsn.getKeyWords1());
    }
    // if (CollectionUtils.isNotEmpty(groupPsn.getKewWordsList())) {
    // flag = true;
    // keyDisc.setKewWordsList(groupPsn.getKewWordsList());
    // }
    if (!flag) {
      return null;
    }
    return keyDisc;
  }

  /**
   * 保存群组学科关键词信息.
   * 
   * @author lhd
   */
  @Override
  public void saveKeyDisc(GroupKeyDisc groupKeyDisc) throws Exception {
    groupKeyDiscDao.saveGroupKeyDisc(groupKeyDisc);
  }

  /**
   * 转换封装群组统计信息.
   * 
   * @author lhd
   * @param groupPsn
   * @return
   */
  private GroupStatistics buildStatistics(GroupPsn groupPsn) {
    GroupStatistics statistics = groupStatisticsDao.getStatistics(groupPsn.getGroupId());
    if (statistics == null) {
      statistics = new GroupStatistics();
      if (groupPsn.getGroupId() != null && groupPsn.getGroupId().longValue() > 0) {
        statistics.setGroupId(groupPsn.getGroupId());
      }
    }
    // 群组文件数
    if (groupPsn.getSumFiles() != null && groupPsn.getSumFiles().intValue() > 0) {
      statistics.setSumFiles(groupPsn.getSumFiles());
    }
    // 群组文件数(文件夹未分类)
    if (groupPsn.getSumFilesNfolder() != null && groupPsn.getSumFilesNfolder().intValue() > 0) {
      statistics.setSumFilesNfolder(groupPsn.getSumFilesNfolder());
    }
    // 群组教学课件数
    if (groupPsn.getSumMaterials() != null && groupPsn.getSumMaterials().intValue() > 0) {
      statistics.setSumMaterials(groupPsn.getSumMaterials());
    }
    // 群组教学课件数(文件夹未分类)
    if (groupPsn.getSumMaterialsNfolder() != null && groupPsn.getSumMaterialsNfolder().intValue() > 0) {
      statistics.setSumMaterialsNfolder(groupPsn.getSumMaterialsNfolder());
    }
    // 群组成员数
    if (groupPsn.getSumMembers() != null && groupPsn.getSumMembers().intValue() > 0) {
      statistics.setSumMembers(groupPsn.getSumMembers());
    }
    // 群组项目数
    if (groupPsn.getSumPrjs() != null && groupPsn.getSumPrjs().intValue() > 0) {
      statistics.setSumPrjs(groupPsn.getSumPrjs());
    }
    // 群组项目数(文件夹未分类)
    if (groupPsn.getSumPrjsNfolder() != null && groupPsn.getSumPrjsNfolder().intValue() > 0) {
      statistics.setSumPrjsNfolder(groupPsn.getSumPrjsNfolder());
    }
    // 群组成果数
    if (groupPsn.getSumPubs() != null && groupPsn.getSumPubs().intValue() > 0) {
      statistics.setSumPubs(groupPsn.getSumPubs());
    }
    // 群组成果数(文件夹未分类)
    if (groupPsn.getSumPubsNfolder() != null && groupPsn.getSumPubsNfolder().intValue() > 0) {
      statistics.setSumPubsNfolder(groupPsn.getSumPubsNfolder());
    }
    // 群组文献数
    if (groupPsn.getSumRefs() != null && groupPsn.getSumRefs().intValue() > 0) {
      statistics.setSumRefs(groupPsn.getSumRefs());
    }
    // 群组文献数(文件夹未分类)
    if (groupPsn.getSumRefsNfolder() != null && groupPsn.getSumRefsNfolder().intValue() > 0) {
      statistics.setSumRefsNfolder(groupPsn.getSumRefsNfolder());
    }
    // 群组话题数
    if (groupPsn.getSumSubjects() != null && groupPsn.getSumSubjects().intValue() > 0) {
      statistics.setSumSubjects(groupPsn.getSumSubjects());
    }
    // 待确认群组成员数
    if (groupPsn.getSumToMembers() != null && groupPsn.getSumToMembers().intValue() > 0) {
      statistics.setSumToMembers(groupPsn.getSumToMembers());
    }
    // 群组访问统计
    statistics.setVisitCount(groupPsn.getVisitCount() != null ? groupPsn.getVisitCount() : 1);
    return statistics;
  }

  /**
   * 保存群组统计信息
   * 
   * @author lhd
   */
  @Override
  public void saveStatistics(GroupStatistics groupStat) throws Exception {
    groupStatisticsDao.saveGroupStatistics(groupStat);
  }

  /**
   * 转换群组项目扩展表信息
   * 
   * @author lhd
   * @param groupPsn
   * @return
   */
  private GroupPrjExtend buildGroupPrjExtend(GroupPsn groupPsn) {
    GroupPrjExtend groupPrjExtend = groupPrjExtendDao.getGroupPrjExtend(groupPsn.getGroupId());
    if (groupPrjExtend == null) {
      groupPrjExtend = new GroupPrjExtend();
      if (groupPsn.getGroupId() != null && groupPsn.getGroupId().longValue() > 0) {
        groupPrjExtend.setGroupId(groupPsn.getGroupId());
      }
    }
    // 资助机构
    if (StringUtils.isNotBlank(groupPsn.getFundingIns())) {
      groupPrjExtend.setFundingIns(groupPsn.getFundingIns());
    }
    // 资助类别
    if (StringUtils.isNotBlank(groupPsn.getFundingTypes())) {
      groupPrjExtend.setFundingTypes(groupPsn.getFundingTypes());
    }
    // 资助金额
    if (IrisNumberUtils.createBigDecimal(groupPsn.getAmount()) != null) {
      groupPrjExtend.setAmount(IrisNumberUtils.createBigDecimal(groupPsn.getAmount()));
    }
    // 币种
    if (StringUtils.isNotBlank(groupPsn.getCurrency())) {
      groupPrjExtend.setCurrency(groupPsn.getCurrency());
    }
    // 开始年份
    if (IrisNumberUtils.toInteger(groupPsn.getStartYear()) != null) {
      groupPrjExtend.setStartYear(IrisNumberUtils.toInteger(groupPsn.getStartYear()));
    }
    // 开始月份
    if (IrisNumberUtils.toInteger(groupPsn.getStartMonth()) != null) {
      groupPrjExtend.setStartMonth(IrisNumberUtils.toInteger(groupPsn.getStartMonth()));
    }
    // 开始日期
    if (IrisNumberUtils.toInteger(groupPsn.getStartDay()) != null) {
      groupPrjExtend.setStartDay(IrisNumberUtils.toInteger(groupPsn.getStartDay()));
    }
    // 结束年份
    if (IrisNumberUtils.toInteger(groupPsn.getEndYear()) != null) {
      groupPrjExtend.setEndYear(IrisNumberUtils.toInteger(groupPsn.getEndYear()));
    }
    // 结束月份
    if (IrisNumberUtils.toInteger(groupPsn.getEndMonth()) != null) {
      groupPrjExtend.setEndMonth(IrisNumberUtils.toInteger(groupPsn.getEndMonth()));
    }
    // 结束日期
    if (IrisNumberUtils.toInteger(groupPsn.getEndDay()) != null) {
      groupPrjExtend.setEndDay(IrisNumberUtils.toInteger(groupPsn.getEndDay()));
    }
    return groupPrjExtend;
  }

  /**
   * 保存群组项目扩展表信息
   * 
   * @author lhd
   * @param groupPrjExtend
   */
  private void saveGroupPrjExtend(GroupPrjExtend groupPrjExtend) throws Exception {
    groupPrjExtendDao.saveGroupPrjExtend(groupPrjExtend);
  }



}
