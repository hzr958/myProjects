package com.smate.web.group.service.group.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.group.dao.group.psn.FriendDao;
import com.smate.web.group.dao.group.pub.ConstPubTypeDao;
import com.smate.web.group.dao.group.pub.GroupFolderDao;
import com.smate.web.group.dao.group.pub.GroupPubsDao;
import com.smate.web.group.dao.group.pub.PubFulltextDao;
import com.smate.web.group.exception.GroupNotExistException;
import com.smate.web.group.exception.GroupParameterException;
import com.smate.web.group.form.GroupPsnForm;
import com.smate.web.group.model.group.pub.ConstPubType;
import com.smate.web.group.model.group.pub.GroupFolder;
import com.smate.web.group.model.group.pub.GroupPubs;
import com.smate.web.group.model.group.pub.PubFulltext;
import com.smate.web.group.service.group.GroupOptService;
import com.smate.web.group.service.group.GroupService;
import com.smate.web.group.service.group.invite.GroupInvitePsnService;


/**
 * 群组成果服务实现类
 * 
 * @author tsz
 *
 */
@Service("groupPublicationService")
@Transactional(rollbackFor = Exception.class)
public class GroupPublicationServiceImpl implements GroupPublicationService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${domainscm}")
  private String domainscm;

  @Autowired
  private GroupService groupService;
  @Autowired
  private GroupInvitePsnService groupInvitePsnService;
  @Autowired
  private GroupPubsDao groupPubsDao;
  @Autowired
  private GroupFolderDao groupFolderDao;
  @Autowired
  private ConstPubTypeDao constPubTypeDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private PubFulltextDao pubFulltextDao;

  @Autowired
  private GroupOptService groupOptService;


  /**
   * 构建群组成果页面数据接口
   */
  @Override
  public void show(GroupPsnForm form) {
    Locale locale = LocaleContextHolder.getLocale();
    form.setLocale(locale);
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    form.setPsnId(currentPsnId);
    // 判断参数
    this.checkParameter(form);
    // 判断群组是否存在
    boolean b = groupService.checkGroup(form.getGroupId());
    if (!b) {
      logger.error("访问的群组不存在" + form.getGroupId());
      throw new GroupNotExistException("访问的群组不存在" + form.getGroupId());
    }
    // 判断是否由邮件连接发出来的请求
    this.checkIsMailReq(form);
    // 获取群组的详细信息
    groupService.getGroupPsn(form);
    // 判断群组是否开放
    groupInvitePsnService.checkGroupIsOpenForPsn(form);

    // 构建群组成果页面左边栏数据
    this.buildGroupPubLeftData(form);

    // 获取 群组成果列表数据
    this.getGroupPubListData(form);

    // 解决http://jira.oa.irissz.com/browse/SCM-8443 中文标题是中文标点分割 问题
    if (form.getPage().getResult() != null && form.getPage().getResult().size() > 0) {
      for (int i = 0; i < form.getPage().getResult().size(); i++)
        buildBriefDesc(form.getPage().getResult().get(i), locale);
    }
  }

  /*
   * 解决：中文标题，逗号问题
   */
  private void buildBriefDesc(GroupPubs item, Locale locale) {
    String title = "";
    if ("zh".equalsIgnoreCase(locale.getLanguage())) {
      title = item.getZhTitle();
      if (StringUtils.isBlank(title)) {
        title = item.getEnTitle();
      }
    } else if ("en".equalsIgnoreCase(locale.getLanguage())) {
      title = item.getEnTitle();
      if (StringUtils.isBlank(title)) {
        title = item.getZhTitle();
      }
    } else {
      title = item.getZhTitle();
    }
    if (StringUtils.isNotBlank(item.getAuthorNames())) {
      item.setAuthorNamesForShow(XmlUtil.formateSymbol(title, item.getAuthorNames()).replace("； ", "；"));
    }
    String briefDesc = item.getBriefDesc();
    if (StringUtils.isNotBlank(briefDesc)) {
      briefDesc = XmlUtil.formateSymbol(title, briefDesc);
      if (briefDesc.length() > 500) {
        item.setBriefDesc(briefDesc.substring(0, 400));
      }
    }

  }

  /**
   * 判断 显示群组成果页面的必要参数是否正常 关键参数缺失 抛出异常
   * 
   * @param form
   */
  private void checkParameter(GroupPsnForm form) {
    if (form.getGroupId() == null) {
      throw new GroupParameterException("参数错误");
    }
  }

  /**
   * 判断是否由 邮件连接 发出的请求 如果是 标记参数
   * 
   * @param form
   */
  private void checkIsMailReq(GroupPsnForm form) {
    // 如果是响应邮件链接请求，则增加是否允许上传成果的处理逻辑_MJG_2013-03-07_SCM-1864.
    String isMailReq = form.getIsMailReq();
    if (StringUtils.isNotBlank(isMailReq) && "true".equalsIgnoreCase(isMailReq)) {
      String isPubView = form.getGroupPsn().getIsPubView();
      if (StringUtils.isNotBlank(isPubView) && "1".equals(isPubView)) {// 支持群组成果[1=是,0=否].
        String isAllowUpload = ("1".equals(form.getGroupPsn().getPubViewType())) ? "false" : "true";// 群组成果上传权限[false=所有成员,true=管理员]
        form.setIsAllowUpload(isAllowUpload);
      }
    }

  }

  /**
   * 构建群组成果页面左边栏数据
   * 
   * @param form
   */
  private void buildGroupPubLeftData(GroupPsnForm form) {
    // 成果标签
    List<GroupFolder> groupFolderList = this.findGroupFolderList(form.getGroupId(), "P");
    form.setGroupFolderList(groupFolderList);
    List<Map<String, String>> groupFolderListMap = this.findJsonGroupFolderList(groupFolderList);
    // Struts2Utils.getRequest().setAttribute("groupFolderListJson",
    // JacksonUtils.jsonListSerializer(groupFolderListMap));
    form.setGroupFolderListMap(groupFolderListMap);

    // 成果類別
    List<ConstPubType> pubTypes = this.getAll();
    for (ConstPubType pubType : pubTypes) {
      Integer count = this.countGroupPubs(form.getGroupId(), pubType.getId());
      pubType.setCount(count);
    }
    form.setPubTypes(pubTypes);

    // 左側菜單成果收錄類別
    List<Map<String, Object>> recordList = this.findPubRecordMap(form.getGroupId());
    form.setRecordList(recordList);

    // 左侧菜单栏发表年份

    Map<String, Object> yearsMap = this.queryPubYearsMap(form.getGroupId());
    form.setYearsMap(yearsMap);


  }

  // 左边栏成果文件夹
  @Override
  public List<GroupFolder> findGroupFolderList(Long groupId, String folderType) {
    List<GroupFolder> groupFolderList = null;
    try {
      groupFolderList = groupFolderDao.findGroupFolderList(groupId, folderType);

    } catch (Exception e) {
      logger.error("查询群组文件夹出错", e);
    }
    return groupFolderList;

  }

  @Override
  public List<Map<String, String>> findJsonGroupFolderList(List<GroupFolder> groupFolderList) {
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    for (GroupFolder groupFolder : groupFolderList) {
      Map<String, String> map = new HashMap<String, String>();
      map.put("folderName", groupFolder.getFolderName());
      map.put("folderType", groupFolder.getFolderType());
      map.put("groupFolderId", ObjectUtils.toString(groupFolder.getGroupFolderId()));
      list.add(map);
    }
    return list;
  }

  // 成果左侧栏成果类型
  @Override
  public List<ConstPubType> getAll() {
    return constPubTypeDao.getAllTypes(LocaleContextHolder.getLocale());

  }

  @Override
  public Integer countGroupPubs(Long groupId, Integer pubType) {

    return groupPubsDao.sumGroupPubsByType(groupId, pubType);

  }

  // 成果左側菜單收錄類別

  @Override
  public List<Map<String, Object>> findPubRecordMap(Long groupId) {
    List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

    List<String> pubLists = new ArrayList<String>();
    pubLists.add("ei");
    pubLists.add("sci");
    pubLists.add("istp");
    pubLists.add("ssci");
    for (String pubList : pubLists) {
      Map<String, Object> map = new HashMap<String, Object>();
      int count = this.groupPubsDao.getPubListNum(pubList, groupId);
      map.put("code", pubList);
      map.put("count", count);
      listMap.add(map);
    }
    return listMap;

  }

  // 左侧菜单栏发表年份

  @Override
  public Map<String, Object> queryPubYearsMap(Long groupId) {

    return this.groupPubsDao.sumGroupPubByYear(groupId);

  }



  /**
   * 获取 群组成果列表数据
   * 
   * @param form
   */
  private void getGroupPubListData(GroupPsnForm form) {
    groupPubsDao.findGroupPubsList(form);
    // Page<Publication> page2 = covertGroupPubsList(form.getPage(), 1);
    pubIsShowComment(form.getPage());
  }


  /**
   * 将群组成果列表转化成publication列表,lqh.
   * 
   * @param page
   * @param populateType populateType填充HtmlAbstract的类型 ，0为主页浏览等特殊用户的.
   * @return
   * @throws ServiceException
   */


  public void pubIsShowComment(Page<GroupPubs> page) {// 群组成果
    try {
      for (int i = 0; i < page.getResult().size(); i++) {
        /*
         * if (page.getResult().get(i).getFulltextExt()!=null) { page.getResult
         * ().get(i).setFileTypeIcoUrl(ArchiveFileUtil.getFileTypeIco ("",
         * page.getResult().get(i).getFulltextExt(),LocaleContextHolder .getLocale() )); }
         */
        Long psnId = page.getResult().get(i).getOwnerPsnId();// 成果拥有者id
        int isFriend = this.isPsnFirends(psnId, true);// 判断是否是好友
        if (psnId.equals(SecurityUtils.getCurrentUserId())) {// 本人则显示"评论"的链接
          page.getResult().get(i).setGroupPubsIsShowComment(1);
        } else if (isFriend == 1) {
          page.getResult().get(i).setGroupPubsIsShowComment(1);
        }
      }

    } catch (Exception e) {
      logger.error("查询群组成果出错", e);

    }
  }

  public int isPsnFirends(Long psnId, boolean isLocale) {
    Long userId = SecurityUtils.getCurrentUserId();
    boolean res = false;
    res = this.isPsnFirend(userId, psnId);
    return res ? 1 : 0;
  }


  public boolean isPsnFirend(Long curPsnId, Long psnId) {

    Long count = friendDao.isFriend(curPsnId, psnId);
    if (count != null && count > 0)
      return true;
    else
      return false;

  }


  @Override
  public void ajaxGroupMembers(GroupPsnForm form) throws GroupNotExistException {
    // 判断群组是否存在
    boolean groupExist = this.groupService.checkGroup(form.getGroupId());
    if (!groupExist) {
      throw new GroupNotExistException("群组不存在---groupId=" + form.getGroupId());
    } else {
      this.groupInvitePsnService.checkGroupIsOpenForPsn(form);
      this.groupInvitePsnService.findGroupInvitePsnForLeft(form);
    }
  }

  @Override
  public void showGroup(GroupPsnForm form) {


    form.setNodeId(1);
    Locale locale = LocaleContextHolder.getLocale();
    form.setLocale(locale);
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    form.setPsnId(currentPsnId);
    // 获取权限
    form.setCurrentPsnGroupRoleStatus(groupOptService.getRelationWithGroup(form.getPsnId(), form.getGroupId()));
    // 判断参数
    this.checkParameter(form);
    // 判断群组是否存在
    boolean b = groupService.checkGroup(form.getGroupId());
    if (!b) {
      logger.error("访问的群组不存在" + form.getGroupId());
      throw new GroupNotExistException("访问的群组不存在" + form.getGroupId());
    }
    // 判断是否由邮件连接发出来的请求
    this.checkIsMailReq(form);
    // 获取群组的详细信息
    groupService.getGroupPsn(form);
    // 判断群组是否开放
    groupInvitePsnService.checkGroupIsOpenForPsn(form);

    // 构建群组成果页面左边栏数据 (没有见到用该方法产生的数据)
    // this.buildGroupPubLeftData(form);

    // 获取 群组成果列表数据
    groupPubsDao.findGroupPubsListRemould(form);
    // (里面的循环 与外面的循环重复 调整到外面来)
    // pubIsShowComment(form.getPage());
    List<GroupPubs> resutlGroupPubs = form.getPage().getResult();
    // 解决http://jira.oa.irissz.com/browse/SCM-8443 中文标题是中文标点分割 问题
    if (resutlGroupPubs != null && resutlGroupPubs.size() > 0) {
      for (int i = 0; i < resutlGroupPubs.size(); i++) {
        buildBriefDesc(resutlGroupPubs.get(i), locale);
        // 获取全文图片
        warpFullTextImageUrl(resutlGroupPubs.get(i));

        Long psnId = resutlGroupPubs.get(i).getOwnerPsnId();// 成果拥有者id
        int isFriend = this.isPsnFirends(psnId, true);// 判断是否是好友
        if (psnId.equals(SecurityUtils.getCurrentUserId())) {// 本人则显示"评论"的链接
          resutlGroupPubs.get(i).setGroupPubsIsShowComment(1);
        } else if (isFriend == 1) {
          resutlGroupPubs.get(i).setGroupPubsIsShowComment(1);
        }

      }
    }

  }


  /**
   * 获取成果全文图片(有默认图片地址)
   * 
   * @param pub
   */
  private void warpFullTextImageUrl1(GroupPubs pub) {
    String domain = domainscm + "/";
    String fullTextImageUrl = "";
    PubFulltext pubFulltext = pubFulltextDao.get(pub.getPubId());
    if (pubFulltext != null) {
      // 有全文缩略图就有全文缩略图
      if (StringUtils.isNotBlank(pubFulltext.getFulltextImagePath())) {
        fullTextImageUrl = domain + pubFulltext.getFulltextImagePath();
        pub.setFileTypeIcoUrl(fullTextImageUrl);
      } else {
        pub.setFileTypeIcoUrl("/resscmwebsns/images_v5/images2016/file_img1.jpg");
      }
    } else {
      pub.setFileTypeIcoUrl("/resscmwebsns/images_v5/images2016/file_img.jpg");
    }


  }

  /**
   * 获取成果全文图片
   * 
   * @param pub
   */
  private void warpFullTextImageUrl(GroupPubs pub) {
    if (pub.getFulltextFileid() == null) {
      return;
    }
    String domain = domainscm + "/";
    String fullTextImageUrl = "";
    PubFulltext pubFulltext = pubFulltextDao.get(pub.getPubId());
    if (pubFulltext != null) {
      // 有全文缩略图就有全文缩略图
      if (StringUtils.isNotBlank(pubFulltext.getFulltextImagePath())) {
        fullTextImageUrl = domain + pubFulltext.getFulltextImagePath();
        pub.setFileTypeIcoUrl(fullTextImageUrl);
      }
    }


  }

  /**
   * 获取群组成果
   */
  @Override
  public void showGroupPubForDyn(GroupPsnForm form) throws Exception {
    form.setNodeId(1);
    Locale locale = LocaleContextHolder.getLocale();
    form.setLocale(locale);
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    form.setPsnId(currentPsnId);
    // 判断群组是否存在
    boolean b = groupService.checkGroup(form.getGroupId());
    if (!b) {
      logger.error("访问的群组不存在" + form.getGroupId());
      throw new GroupNotExistException("访问的群组不存在" + form.getGroupId());
    }
    // 获取 群组成果列表数据
    List<GroupPubs> resutlGroupPubs = groupPubsDao.getGroupPubListForDyn(form);
    form.getPage().setResult(resutlGroupPubs);;
    if (resutlGroupPubs != null && resutlGroupPubs.size() > 0) {
      for (int i = 0; i < resutlGroupPubs.size(); i++) {
        buildBriefDesc(resutlGroupPubs.get(i), locale);
        wrapGroupPubTitle(resutlGroupPubs.get(i));
        // 获取全文图片
        warpFullTextImageUrl1(resutlGroupPubs.get(i));
      }
    }

  }

  // jsp 页面显示的是 zhTitle
  private void wrapGroupPubTitle(GroupPubs groupPub) {
    String language = LocaleContextHolder.getLocale().toString();
    if ("zh_CN".equalsIgnoreCase(language)) {
      if (StringUtils.isBlank(groupPub.getZhTitle())) {
        groupPub.setZhTitle(groupPub.getEnTitle());
      }
    } else {
      if (StringUtils.isNotBlank(groupPub.getEnTitle())) {
        groupPub.setZhTitle(groupPub.getEnTitle());
      }
    }
  }

  @Override
  public void ajaxGroupPubMembers(GroupPsnForm form) {
    // 判断群组是否存在
    boolean groupExist = this.groupService.checkGroup(form.getGroupId());
    if (!groupExist) {
      throw new GroupNotExistException("群组不存在---groupId=" + form.getGroupId());
    } else {
      // 获取权限
      form.setCurrentPsnGroupRoleStatus(groupOptService.getRelationWithGroup(form.getPsnId(), form.getGroupId()));
      this.groupInvitePsnService.checkGroupIsOpenForPsn(form);
      this.groupInvitePsnService.findGroupPubInvitePsnForLeft(form);
    }

  }

  @Override
  public List<GroupPubs> getGroupPubsList(Long groupId) {
    return this.groupPubsDao.getGroupPubList(groupId);
  }

  @Override
  public void setRelevance(GroupPsnForm form) {
    if (CollectionUtils.isEmpty(form.getPage().getResult())) {
      return;
    }

    // 转化rel，样式与后台数据转换
    for (int i = 0; i < form.getPage().getResult().size(); i++) {
      Integer rel = form.getPage().getResult().get(i).getRelevance();
      // 转化，界面上样式与后台relevance值刚好相反
      // 成果设置最小相关度为1
      if (rel >= 3) {
        form.getPage().getResult().get(i).setRelevance(1);
      } else if (rel == 2) {
        form.getPage().getResult().get(i).setRelevance(2);
      } else if (rel == 1) {
        form.getPage().getResult().get(i).setRelevance(3);
      } else {
        form.getPage().getResult().get(i).setRelevance(4);
      }
    }
  }


}
