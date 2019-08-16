package com.smate.web.group.dao.grp.file;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.group.action.grp.form.GrpFileForm;
import com.smate.web.group.model.grp.file.GrpFile;

/**
 * 群组文件dao
 * 
 * @author AiJiangBin
 */

@Repository
public class GrpFileDao extends SnsHibernateDao<GrpFile, Long> {
  /**
   * 获取群组的文件-复制群组用
   *
   * @return
   */
  public List<GrpFile> getGrpFileList(Long grpId) {
    String hql =
        "select new GrpFile(t.fileName,t.filePath,t.fileType,t.fileSize,t.fileDesc,t.fileStatus,t.uploadDate,t.updateDate,t.uploadPsnId,t.archiveFileId,t.fileModuleType) from GrpFile t where t.fileStatus=0 and t.grpId=:grpId";
    return this.createQuery(hql).setParameter("grpId", grpId).list();
  }

  /**
   *
   * @param grpId
   * @param fileModuleType 0=文件 ， 1=作业 ， 2=课件
   * @return
   */
  public List<GrpFile> getGrpFileList(Long grpId, Integer fileModuleType) {
    String hql =
        "select new GrpFile(t.fileName,t.filePath,t.fileType,t.fileSize,t.fileDesc,t.fileStatus,t.uploadDate,t.updateDate,t.uploadPsnId,t.archiveFileId,t.fileModuleType) from GrpFile t where t.fileStatus=0   and t.fileModuleType=:fileModuleType  and t.grpId=:grpId";
    return this.createQuery(hql).setParameter("grpId", grpId).setParameter("fileModuleType", fileModuleType).list();
  }
  /**
   * 获取群组的文件-复制群组用
   *
   * @return
   */
  public List<GrpFile> getGrpFileList(Long grpId, Long psnId) {
    String hql =
        "select new GrpFile(t.fileName,t.filePath,t.fileType,t.fileSize,t.fileDesc,t.fileStatus,t.uploadDate,t.updateDate,t.uploadPsnId,t.archiveFileId,t.fileModuleType) "
            + "from GrpFile t where t.fileStatus=0 and exists (select gf.grpFileId from GrpFile gf where t.uploadPsnId=:psnId) and t.grpId=:grpId";
    return this.createQuery(hql).setParameter("grpId", grpId).setParameter("psnId", psnId).list();
  }

  /**
   *
   * @param grpId
   * @param fileModuleType 0=文件 ， 1=作业 ， 2=课件
   * @return
   */
  public List<GrpFile> getGrpFileList(Long grpId, Integer fileModuleType, Long psnId) {
    String hql =
        "select new GrpFile(t.fileName,t.filePath,t.fileType,t.fileSize,t.fileDesc,t.fileStatus,t.uploadDate,t.updateDate,t.uploadPsnId,t.archiveFileId,t.fileModuleType) "
            + "from GrpFile t where t.fileStatus=0 and exists (select gf.grpFileId from GrpFile gf where t.uploadPsnId=:psnId) and t.fileModuleType=:fileModuleType  and t.grpId=:grpId";
    return this.createQuery(hql).setParameter("grpId", grpId).setParameter("fileModuleType", fileModuleType)
        .setParameter("psnId", psnId).list();
  }

  /**
   * 查询群组文件列表
   *
   * @param grpFileForm
   */
  public void findGrpFile(GrpFileForm form) {
    checkAndInitPage(form);
    StringBuilder hql = new StringBuilder();
    List params = new ArrayList();
    hql.append("from GrpFile gf where gf.grpId=? and gf.fileStatus=0 ");
    params.add(form.getGrpId());
    if (StringUtils.isNotEmpty(form.getSearchKey())) {
      hql.append(" and (instr(upper(gf.fileName),?)>0) ");
      params.add(form.getSearchKey().toUpperCase().trim());
    }
    // 组员的文件
    if (form.getSearchGrpFileMemberId() != null && form.getSearchGrpFileMemberId() != 0L) {
      hql.append(" and   gf.uploadPsnId = ? ");
      params.add(form.getSearchGrpFileMemberId());
    }
    // 过滤文件类型
    if (form.getSearchFileType() != null && form.getSearchFileType().size() > 0) {
      if (form.getSearchFileType().size() == 1) { // 查找一种类型
        hql.append(" and   gf.fileModuleType  = ? ");
        params.add(form.getSearchFileType().get(0));
      } else if (form.getSearchFileType().size() == 2) {
        hql.append(" and   ( gf.fileModuleType  = ?   or gf.fileModuleType  = ?  ) ");
        params.add(form.getSearchFileType().get(0));
        params.add(form.getSearchFileType().get(1)); // 查找两种类型
      }
    }
    // 过滤 文件标签 , 会
    if (form.getGrpLabelIdList() != null && form.getGrpLabelIdList().size() > 0) {
      for (Long grpLabelId : form.getGrpLabelIdList()) {
        hql.append(
            " and   exists  (select  1 from  GrpFileLabel  gfl  where  gfl.status = 0 and  gfl.grpFileId = gf.grpFileId  and gfl.grpLabelId  = "
                + grpLabelId + "   )   ");
      }

    }


    hql.append(" order by gf.uploadDate " + form.getPage().getOrder() + "    ,  gf.grpFileId  desc  ");
    // 查询总页数
    Query queryCt = super.createQuery("select count(gf.grpFileId) " + hql.toString(), params.toArray());
    Long count = (Long) queryCt.uniqueResult();
    form.getPage().setTotalCount(count.intValue());
    // 查询数据实体
    String dataStr =
        " select new GrpFile(gf.grpId ,gf.grpFileId,gf.fileName,gf.filePath,gf.fileType,gf.fileDesc ,gf.uploadDate,gf.uploadPsnId,gf.archiveFileId , gf.fileModuleType) ";
    Query queryResult = super.createQuery(dataStr + hql.toString(), params.toArray());
    queryResult.setFirstResult(form.getPage().getFirst() - 1);
    queryResult.setMaxResults(form.getPage().getPageSize());
    form.getPage().setResult(queryResult.list());
  }

  private void checkAndInitPage(GrpFileForm form) {
    if (form.getPage() == null) {
      Page page = new Page(10);
      page.setOrder("desc");
      form.setPage(page);
    } else if (form.getPage().getOrder() == "") {
      form.getPage().setOrder("desc");
    }
  }

  /**
   * 检查群组文件是否存在
   *
   * @param grpFileId
   * @return
   */
  public Boolean checkGrpFileExit(Long grpFileId) {
    String hql = "select gf.grpFileId   from GrpFile gf where gf.grpFileId =:grpFileId  and  gf.fileStatus=0";
    Object obj = this.createQuery(hql).setParameter("grpFileId", grpFileId).uniqueResult();
    if (obj != null) {
      return true;
    }
    return false;
  }

  /**
   * 编辑文件描述
   *
   * @param grpFileId
   * @param fileDesc
   */
  public void editGrpFileDesc(Long grpFileId, Long grpId, String fileDesc) {
    String hql =
        " update  GrpFile  gf  set gf.fileDesc =:fileDesc  , gf.updateDate =:updateDate    where gf.grpFileId =:grpFileId  and gf.grpId=:grpId and gf.fileStatus=0 ";
    this.createQuery(hql).setParameter("fileDesc", fileDesc).setParameter("updateDate", new Date())
        .setParameter("grpId", grpId).setParameter("grpFileId", grpFileId).executeUpdate();

  }

  /**
   * 更新群组文件类型课件作业
   *
   * @param grpFileId
   * @param grpId
   * @param fileType
   */
  public int updateGrpFileType(Long grpFileId, Long grpId, Integer fileType) {
    String hql =
        " update  GrpFile  gf  set gf.fileModuleType =:fileType  , gf.updateDate =:updateDate    where gf.grpId =:grpId  and gf.grpFileId =:grpFileId and gf.fileStatus=0 ";
    return this.createQuery(hql).setParameter("fileType", fileType).setParameter("updateDate", new Date())
        .setParameter("grpFileId", grpFileId).setParameter("grpId", grpId).executeUpdate();
  }

  /**
   * 群组文件， 成员
   *
   * @param grpId
   * @return
   */
  public List<Object[]> findGrpFileMember(Long grpId, Integer fileModuleType) {
    String hql =
        "select  gf.uploadPsnId  , count(1)  from  GrpFile gf where gf.grpId =:grpId and gf.fileStatus =0   and gf.fileModuleType =:fileModuleType group by gf.uploadPsnId  order by count(1) desc  ";
    Object obj =
        this.createQuery(hql).setParameter("grpId", grpId).setParameter("fileModuleType", fileModuleType).list();
    if (obj != null) {
      return (List<Object[]>) obj;
    }
    return null;
  }

  /**
   * 判断是不是我的文件
   *
   * @param grpId
   * @param psnId
   * @param grpFileId
   * @return
   */
  public boolean checkIsMyGrpFile(Long grpId, Long psnId, Long grpFileId) {
    String hql =
        " select gf.grpFileId from  GrpFile gf where gf.grpId =:grpId  and  gf.uploadPsnId=:psnId and gf.grpFileId=:grpFileId  and gf.fileStatus=0";
    Object ojb = this.createQuery(hql).setParameter("grpId", grpId).setParameter("psnId", psnId)
        .setParameter("grpFileId", grpFileId).uniqueResult();
    if (ojb != null) {
      return true;
    }
    return false;
  }

  /**
   * 删除文件，更新文件
   *
   * @param grpFileId
   * @param grpId
   */
  public void deleteGrpFile(Long grpFileId, Long grpId) {
    String hql =
        " update  GrpFile  gf  set gf.fileStatus =1  , gf.updateDate =:updateDate    where gf.grpFileId =:grpFileId  and gf.grpId=:grpId";
    this.createQuery(hql).setParameter("updateDate", new Date()).setParameter("grpId", grpId)
        .setParameter("grpFileId", grpFileId).executeUpdate();

  }

  /**
   * 计算群组文件数量
   *
   * @param grpId
   * @param fileModuleType
   * @return
   */
  public Long countGrpFile(Long grpId, Integer fileModuleType) {
    String hql =
        " select  count (1) from GrpFile  gf  where   gf.fileStatus = 0  and  gf.fileModuleType =:fileModuleType  and gf.grpId=:grpId  ";
    Long count = (Long) this.createQuery(hql).setParameter("fileModuleType", fileModuleType)
        .setParameter("grpId", grpId).uniqueResult();
    return count;
  }

}
