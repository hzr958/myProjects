package com.smate.web.group.dao.group;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.group.form.GroupInfoForm;
import com.smate.web.group.model.group.GroupFile;

/**
 * 群组文件.
 * 
 * @author zk
 * 
 */
@Repository
public class GroupFileDao extends SnsHibernateDao<GroupFile, Long> {

  /**
   * 获取群组文件
   * 
   * @param form
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void findGroupFile(GroupInfoForm form) {
    checkAndInitPage(form);
    StringBuilder hql = new StringBuilder();
    List params = new ArrayList();
    hql.append("from GroupFile gf where gf.groupId=? and gf.fileStatus=0");
    params.add(form.getGroupId());
    if (StringUtils.isNotEmpty(form.getSearchKey())) {
      hql.append(" and (instr(upper(gf.fileName),?)>0) ");
      params.add(form.getSearchKey().toUpperCase().trim());
    }
    hql.append(" order by gf.uploadTime " + form.getPage().getOrder());
    // 查询总页数
    Query queryCt = super.createQuery("select count(gf.groupFileId) " + hql.toString(), params.toArray());
    Long count = (Long) queryCt.uniqueResult();
    form.getPage().setTotalCount(count.intValue());
    // 查询数据实体
    String dataStr =
        " select new GroupFile(gf.groupFileId,gf.fileName,gf.fileSize,gf.uploadTime,gf.psnId,gf.fileId,gf.filePath,gf.fileDesc,gf.fileType) ";
    Query queryResult = super.createQuery(dataStr + hql.toString(), params.toArray());
    queryResult.setFirstResult(form.getPage().getFirst() - 1);
    queryResult.setMaxResults(form.getPage().getPageSize());
    form.getPage().setResult(queryResult.list());
  }

  // 检查并初始化Page
  @SuppressWarnings("rawtypes")
  private void checkAndInitPage(GroupInfoForm form) {
    if (form.getPage() == null) {
      Page page = new Page(10);
      page.setOrder("desc");
      form.setPage(page);
    } else if (form.getPage().getOrder() == "") {
      form.getPage().setOrder("desc");
    }
  }

  /**
   * 查找用户文件对象
   * 
   * @param fileId
   * @return StationFile
   */
  public StationFile findStationFileBysnsFileId(Long fileId) {
    String hql =
        "select new StationFile(sf.fileId,sf.psnId,sf.fileName,sf.filePath,sf.fileType,sf.fileSize,sf.fileDesc,sf.archiveFileId) from StationFile sf where sf.fileId=:fileId";
    return (StationFile) super.createQuery(hql).setParameter("fileId", fileId).uniqueResult();
  }

  /**
   * 检索群组文件是不是psnid的
   * 
   * @param groupId
   * @param psnId
   * @return
   */
  public Boolean checkHasSave(Long fileId, Long groupId, Long psnId) {
    String hql =
        "select gf.groupFileId from GroupFile gf where gf.fileId=:fileId and gf.groupId=:groupId and gf.psnId=:psnId and gf.fileStatus=0";
    Object obj = super.createQuery(hql).setParameter("fileId", fileId).setParameter("psnId", psnId)
        .setParameter("groupId", groupId).uniqueResult();
    if (obj != null) {
      return true;
    }
    return false;
  }

  public Integer findGroupFileCount(Long groupId) {
    String hql = "select gf.fileId  from  GroupFile gf  where gf.fileStatus = 0  and gf.groupId=:groupId  ";
    List<Long> list = super.createQuery(hql).setParameter("groupId", groupId).list();
    if (list != null) {
      return list.size();
    }
    return 0;
  }


  /**
   * 检索群组文件是不是psnid的
   * 
   * @param groupId
   * @param psnId
   * @return
   */
  public Boolean checkGroupFileIsMe(Long groupFileId, Long groupId, Long psnId) {
    String hql =
        "select gf.groupFileId from GroupFile gf where gf.groupFileId=:groupFileId and gf.groupId=:groupId and gf.psnId=:psnId  and gf.fileStatus=0";
    Object obj = super.createQuery(hql).setParameter("groupFileId", groupFileId).setParameter("psnId", psnId)
        .setParameter("groupId", groupId).uniqueResult();
    if (obj != null) {
      return true;
    }
    return false;
  }

  /**
   * 删除群组文件
   * 
   * @param groupFileId
   * @param groupId
   * @return
   */
  public int deleteGroupFile(Long groupFileId, Long groupId) {
    String hql = "update GroupFile gf set gf.fileStatus=1 where gf.groupFileId=:groupFileId and gf.groupId=:groupId";
    return super.createQuery(hql).setParameter("groupFileId", groupFileId).setParameter("groupId", groupId)
        .executeUpdate();
  }

  public void updateGroupFile(String fileDesc, Long groupFileId, Long psnId) {
    String hql = "update GroupFile gf set gf.fileDesc=:fileDesc where gf.groupFileId=:groupFileId and gf.psnId=:psnId";
    super.createQuery(hql).setParameter("fileDesc", fileDesc).setParameter("groupFileId", groupFileId)
        .setParameter("psnId", psnId).executeUpdate();
  }

  public GroupFile findGroupFileByArchiveFileId(Long groupId, Long psnId, Long fileId) {
    String hql = "from GroupFile gf  where gf.archiveFileId =:fileId  and gf.groupId =:groupId  and gf.psnId=:psnId ";
    List<GroupFile> list = this.createQuery(hql).setParameter("fileId", fileId).setParameter("groupId", groupId)
        .setParameter("psnId", psnId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }

    return null;

  }
}
