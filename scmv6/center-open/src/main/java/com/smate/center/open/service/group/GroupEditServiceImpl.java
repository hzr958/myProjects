package com.smate.center.open.service.group;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.group.GroupMemberContants;
import com.smate.center.open.dao.group.GroupBaseinfoDao;
import com.smate.center.open.dao.group.GroupInvitePsnDao;
import com.smate.center.open.dao.group.GroupKeywordsDao;
import com.smate.center.open.exception.OpenCreateGroupException;
import com.smate.center.open.model.group.GroupBaseInfo;
import com.smate.center.open.model.group.GroupInvitePsn;
import com.smate.center.open.model.group.GroupKeywords;
import com.smate.center.open.model.group.GroupPsn;
import com.smate.center.open.service.group.consts.ConstDisciplineManage;
import com.smate.center.open.service.group.member.GroupMemberService;
import com.smate.center.open.service.group.psn.GroupPsnEditService;
import com.smate.center.open.service.group.psn.GroupPsnSearchService;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.common.HtmlUtils;

/**
 * 群组编辑业务逻辑实现类
 * 
 * @author lhd
 *
 */
@Service("groupEditService")
@Transactional(rollbackFor = Exception.class)
public class GroupEditServiceImpl implements GroupEditService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupPsnSearchService groupPsnSearchService;
  // @Resource(name = "snsSrvServiceLocator")
  // private SnsSrvServiceLocator snsSrvServiceLocator;
  @Autowired
  private ConstDisciplineManage constDisciplineManage;
  @Autowired
  private GroupBaseinfoDao groupBaseinfoDao;
  @Autowired
  private GroupPsnEditService groupPsnEditService;
  @Autowired
  private GroupKeywordsDao groupKeywordsDao;
  @Autowired
  private GroupMemberService groupMemberService;
  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;


  /**
   * 保存群组基本信息
   * 
   * @author lhd
   */
  @Override
  public void saveGroupPsnInfo(GroupPsn groupPsn, Long psnId, Long openId) throws Exception {
    GroupBaseInfo baseInfo = groupPsnSearchService.getBaseInfo(groupPsn.getGroupId());
    if (baseInfo == null) {
      // 创建群组
      groupPsn.setOwnerPsnId(psnId);
      groupPsn.setCreateDate(new Date());
      groupPsn.setUpdateDate(new Date());
      // groupPsn.setGroupImgUrl(refreshRemoteAvatars(groupPsn.getGroupImgUrl()));

      if (openId != 99999999) {
        groupPsn.setDiscCodes(getDiscCodes(groupPsn.getDisciplines()));
      }
      Long groupNo = groupPsn.getGroupNo();
      if (groupNo == null) {
        groupNo = groupBaseinfoDao.creatGroupNo();
        groupPsn.setGroupNo(groupNo);
      }
      if (StringUtils.isNotBlank(groupPsn.getGroupDescription())) {
        // 截取简介
        String shortBrief =
            HtmlUtils.replaceBlank(HtmlUtils.Html2Text(groupPsn.getGroupDescription()).replaceAll(" {1,}", "&nbsp;"))
                .replaceAll("&nbsp;", " ");
        int end = shortBrief.length() > 200 ? 200 : shortBrief.length();
        groupPsn.setGroupDescription(shortBrief.substring(0, end));
      }
      groupPsnEditService.saveGroupPsn(groupPsn, openId);
    }
  }

  // /**
  // * 刷新群组头像
  // */
  // @Override
  // public String refreshRemoteAvatars(String avator) throws Exception {
  // try {
  // // 获取群组头像
  // if (StringUtils.isNotBlank(avator) && !ServiceConstants.DEFAULT_GROUPIMG.equals(avator)) {
  // if (avator.startsWith("http://")) {
  // return avator;
  // }
  //// return snsSrvServiceLocator.getSnsWebDomain() + avator;
  // } else {
  // return ServiceConstants.DEFAULT_GROUPIMG;
  // }
  // } catch (Exception e) {
  // logger.error("读取图像路径出错", e);
  // return ServiceConstants.DEFAULT_GROUPIMG;
  // }
  // return null;
  // }

  private String getDiscCodesWithName(String disciplines) {

    if (disciplines == null) {
      return null;
    }
    String[] ary = disciplines.split(",");
    if (ary.length == 0) {
      return disciplines;
    }
    String discCodes = "";
    for (String s : ary) {
      if ("".equals(s)) {
        discCodes += ",";
      } else {
        try {
          discCodes += constDisciplineManage.findDisciplineById(Long.parseLong(s)).getDiscCode() + ",";
        } catch (Exception e) {
          logger.error("disciplines转换为discCode出错");
          throw new OpenCreateGroupException("disciplines转换为discCode出错", e);
        }
      }
    }
    return discCodes;
  }

  // disciplines转换为discCodes
  private String getDiscCodes(String disciplines) {
    if (disciplines == null) {
      return null;
    }
    String[] ary = disciplines.split(",");
    if (ary.length == 0) {
      return disciplines;
    }
    String discCodes = "";
    for (String s : ary) {
      if ("".equals(s)) {
        discCodes += ",";
      } else {
        try {
          discCodes += constDisciplineManage.findDisciplineById(Long.parseLong(s)).getDiscCode() + ",";
        } catch (Exception e) {
          logger.error("disciplines转换为discCode出错");
          throw new OpenCreateGroupException("disciplines转换为discCode出错", e);
        }
      }
    }
    return discCodes;
  }

  /**
   * 保存群组关键字
   * 
   * @author lhd
   */
  @Override
  public void saveGroupKeywordsInfo(GroupPsn groupPsn) throws Exception {
    String keyWords1 = null;
    List<String> arr = new ArrayList<String>();
    if (groupPsn != null) {
      keyWords1 = groupPsn.getKeyWords1();
    }
    // SCM-10578 当群组名中有多个;号时会被当做多个关键词显示出来
    arr.add(groupPsn.getGroupName());
    String[] arry1 = null;
    if (keyWords1 != null && keyWords1.length() > 0) {
      arry1 = keyWords1.split(";");
      if (arry1 != null) {
        for (String word : arry1) {
          arr.add(word);
        }

      }
    }
    if (arr != null) {
      GroupKeywords rk = null;
      for (String word : arr) {
        word = word.trim();
        if (!"".equals(word)) {
          rk = new GroupKeywords();
          rk.setGroupId(groupPsn.getGroupId());
          rk.setKeyword(word);
          rk.setKeyHash(HashUtils.getStrHashCode(word));
          // 关键词太长 ，会报错
          rk.setKeyword(truncateString(rk.getKeyword(), 200));
          groupKeywordsDao.save(rk);
        }
      }
    }
  }

  /**
   * 保存人员与群众的邀请关系
   * 
   * @author lhd
   */
  @Override
  public void saveGroupInvitePsnInfo(GroupPsn groupPsn, Long psnId) throws Exception {
    GroupInvitePsn groupInvitePsn = new GroupInvitePsn();
    groupInvitePsn.setPsnId(psnId);
    groupInvitePsn.setCreatePsnId(psnId);
    groupInvitePsn.setCreateDate(new Date());
    groupInvitePsn.setGroupRole(GroupMemberContants.GROUP_ROLE_CREATOR);// 创建人
    groupInvitePsn.setGroupId(groupPsn.getGroupId());
    groupMemberService.buildGroupInvitePsn(groupInvitePsn);
    groupInvitePsnDao.save(groupInvitePsn);
  }

  /**
   * 截取字符
   * 
   * @param source
   * @param length
   * @return
   */
  public String truncateString(String source, Integer length) {
    if (StringUtils.isNotBlank(source) && length > 0 && source.length() > length) {
      return source.substring(0, length);
    }
    return source;
  }

}
