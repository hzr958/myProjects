package com.smate.web.group.service.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.group.dao.group.GroupKeywordsDao;
import com.smate.web.group.dao.group.GroupPsnDao;
import com.smate.web.group.dao.group.invit.GroupInvitePsnDao;
import com.smate.web.group.exception.GroupException;
import com.smate.web.group.form.GroupPsnForm;
import com.smate.web.group.model.group.GroupKeywords;
import com.smate.web.group.model.group.GroupPsn;
import com.smate.web.group.service.group.consts.ConstDisciplineManage;

/**
 * 群组编辑业务逻辑实现类.
 * 
 * @author zjh.
 * 
 */
@Service("groupEditService")
@Transactional(rollbackOn = Exception.class)
public class GroupEditServiceImpl implements GroupEditService {
  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupPsnSearchService groupPsnSearchService;
  @Autowired
  private ConstDisciplineManage constDisciplineManage;
  @Autowired
  private GroupPsnEditService groupPsnEditService;
  @Autowired
  private SyncGroupService syncGroupService;
  @Autowired
  private GroupPsnDao groupPsnDao;
  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;
  @Autowired
  private GroupKeywordsDao groupKeywordsDao;
  @Autowired
  private GroupService groupService;
  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;
  @Autowired
  private BatchJobsService batchJobsService;

  /**
   * 更新编辑后的群组人员信息
   */
  @Override
  public void updateGroupPsn(GroupPsn groupPsn) throws GroupException {
    try {
      GroupPsn updateGroupPsn = groupPsnSearchService.getBuildGroupPsn(groupPsn.getGroupId());// groupPsnDao.findMyGroup(groupPsn.getGroupId());
      updateGroupPsn.setUpdateDate(new Date());
      updateGroupPsn.setGroupImgUrl(refreshRemoteAvatars(groupPsn.getGroupImgUrl()));
      if (groupPsn.getGroupDescription().length() > 4000) {
        updateGroupPsn.setGroupDescription(groupPsn.getGroupDescription().substring(0, 4000));
      } else {
        updateGroupPsn.setGroupDescription(groupPsn.getGroupDescription());
      }
      updateGroupPsn.setGroupDescription(groupPsn.getGroupDescription());
      if (groupPsn.getGroupName().length() > 200) {
        updateGroupPsn.setGroupName(groupPsn.getGroupName().substring(0, 200));
      } else {
        updateGroupPsn.setGroupName(groupPsn.getGroupName());
      }
      updateGroupPsn.setGroupCategory(groupPsn.getGroupCategory());
      updateGroupPsn.setGroupAnnouncement(groupPsn.getGroupAnnouncement());
      updateGroupPsn.setFundingTypes(groupPsn.getFundingTypes());
      updateGroupPsn.setGroupCode(groupPsn.getGroupCode());
      updateGroupPsn.setEmail(groupPsn.getEmail());
      updateGroupPsn.setTel(groupPsn.getTel());
      updateGroupPsn.setAddress(groupPsn.getAddress());
      updateGroupPsn.setIsGroupMemberView(groupPsn.getIsGroupMemberView());
      updateGroupPsn.setIsDiscuss(groupPsn.getIsDiscuss());
      updateGroupPsn.setIsShareFile(groupPsn.getIsShareFile());
      updateGroupPsn.setShareFileType(groupPsn.getShareFileType());
      updateGroupPsn.setIsPrjView(groupPsn.getIsPrjView());
      updateGroupPsn.setPrjViewType(groupPsn.getPrjViewType());
      updateGroupPsn.setIsPubView(groupPsn.getIsPubView());
      updateGroupPsn.setPubViewType(groupPsn.getPubViewType());
      updateGroupPsn.setIsRefView(groupPsn.getIsRefView());
      updateGroupPsn.setRefViewType(groupPsn.getRefViewType());
      updateGroupPsn.setIsMemberPublish(groupPsn.getIsMemberPublish());
      updateGroupPsn.setIsWorkView(groupPsn.getIsWorkView());
      updateGroupPsn.setIsMaterialView(groupPsn.getIsMaterialView());

      // 封装group_key_disc表参数_MJG_SCM-6000.
      updateGroupPsn.setDiscipline1(groupPsn.getDiscipline1());
      updateGroupPsn.setDisciplines(groupPsn.getDisciplines());// 增加本字段赋值，实现数据库记录的各字段内容同步_MaoJianGuo_2012_1015_JIRA问题SCM-860.
      updateGroupPsn.setDiscCodes(getDiscCodes(groupPsn.getDisciplines()));
      updateGroupPsn.setKeyWords1(groupPsn.getKeyWords1());
      updateGroupPsn.setKeyWords(groupPsn.getKeyWords());
      updateGroupPsn.setEnKeyWords1(groupPsn.getEnKeyWords1());
      updateGroupPsn.setEnKeyWords(groupPsn.getEnKeyWords());

      updateGroupPsn.setOpenType(groupPsn.getOpenType());
      groupPsnEditService.saveGroupPsn(updateGroupPsn);

      groupKeywordsDao.deleteGroupKeywordsByGId(updateGroupPsn.getGroupId());
      saveGroupRcmdKeyWords(updateGroupPsn);
      GroupPsnForm form = groupService.modelToForm(groupPsn);
      form.setOpenId("99999999");
      form.setPsnId(SecurityUtils.getCurrentUserId());
      syncGroupService.syncGroupInfo(form);

      // 保存更新群组所属成果的相关度与是否被标注
      this.saveGroupPubInfoReCalTask(groupPsn.getGroupId());

    } catch (Exception e) {
      LOGGER.error("保存群组出错", e);
      throw new GroupException(e);
    }

  }



  /**
   * 增加保存到群组关键词表(2014-11-20新增独立表 GroupKeywords 与群组推荐服务器表对应,用于推送)
   * 
   * @param groupPsn
   */
  private void saveGroupRcmdKeyWords(GroupPsn groupPsn) throws GroupException {
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
        if (!word.equals("")) {
          rk = new GroupKeywords();
          rk.setGroupId(groupPsn.getGroupId());
          rk.setKeyword(word);
          rk.setKeyHash(new Long(HashUtils.getStrHashCode(word)));
          groupKeywordsDao.save(rk);
        }
      }
    }

  }

  // disciplines转换为discCodes
  private String getDiscCodes(String disciplines) {
    if (disciplines == null)
      return null;
    String[] ary = disciplines.split(",");
    if (ary.length == 0)
      return disciplines;
    String discCodes = "";
    for (String s : ary) {
      if ("".equals(s)) {
        discCodes += ",";
      } else {
        try {
          if (constDisciplineManage.findDisciplineById(Long.parseLong(s)) != null) {
            discCodes += constDisciplineManage.findDisciplineById(Long.parseLong(s)).getDiscCode() + ",";
          }
        } catch (Exception e) {
          LOGGER.error("disciplines转换为discCode出错", e);
        }
      }
    }
    return discCodes;
  }

  @Override
  public String refreshRemoteAvatars(String avator) {

    try {

      // 获取群组头像
      if (StringUtils.isNotEmpty(avator) && !ServiceConstants.DEFAULT_GROUPIMG.equals(avator)) {
        if (avator.startsWith("http://")) {
          return avator;
        }
        return "" + avator;// TheadLocalDomain.getDomain()
      } else {
        return ServiceConstants.DEFAULT_GROUPIMG;
      }

    } catch (Exception e) {
      LOGGER.error("读取图像路径出错", e);
      return ServiceConstants.DEFAULT_GROUPIMG;
    }
  }

  public void saveGroupPubInfoReCalTask(Long groupId) {
    String jobContext = "{\"msg_id\":" + groupId + "}";
    BatchJobs job = batchJobsFactory.getBatchJob1(jobContext, "B", BatchOpenCodeEnum.GROUP_PUB_RECALCULATE);
    this.batchJobsService.saveJob(job);
  }
}
