package com.smate.core.base.psn.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.PsnFile;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 个人文件 dao
 * 
 * @author tsz
 *
 */
@Repository
public class PsnFileDao extends SnsHibernateDao<PsnFile, Long> {
  /**
   * 编辑文件描述
   */
  public void saveFileDesc(Long fileId, Long ownerPsnId, String fileDesc) {
    String hql =
        "update PsnFile t set t.fileDesc=:fileDesc,t.updateDate=:updateDate where t.ownerPsnId=:ownerPsnId and t.id=:fileId";
    this.createQuery(hql).setParameter("ownerPsnId", ownerPsnId).setParameter("fileId", fileId)
        .setParameter("updateDate", new Date()).setParameter("fileDesc", fileDesc).executeUpdate();
  }

  /**
   * 删除文件
   */
  public int delFile(Long fileId, Long ownerPsnId) {
    String hql =
        "update PsnFile t set t.status=1,t.updateDate=:updateDate where t.ownerPsnId=:ownerPsnId and t.id=:fileId";
    return this.createQuery(hql).setParameter("ownerPsnId", ownerPsnId).setParameter("fileId", fileId)
        .setParameter("updateDate", new Date()).executeUpdate();
  }

  public Map<String, Object> getFileListTypeCount(Long ownerPsnId, String searchKey) {
    Map<String, Object> map = new HashMap<String, Object>();
    StringBuilder sb = new StringBuilder();
    sb.append("select count(1) from PsnFile sf where sf.status=0 and sf.ownerPsnId=:ownerPsnId ");
    if (StringUtils.isNotBlank(searchKey)) {
      sb.append(" and instr(upper(sf.fileName),upper(:searchKey))>0  ");
    }
    String hql1 = " and sf.fileType='pdf' ";
    String hql2 = " and sf.fileType='doc' ";
    String hql3 = " and sf.fileType='xls' ";
    String hql7 = " and (sf.fileType<>'xls' and sf.fileType<>'doc' and sf.fileType<>'pdf') ";
    if (StringUtils.isNotBlank(searchKey)) {
      map.put("1", this.createQuery(sb.toString() + hql1).setParameter("ownerPsnId", ownerPsnId)
          .setParameter("searchKey", searchKey).uniqueResult());
      map.put("2", this.createQuery(sb.toString() + hql2).setParameter("ownerPsnId", ownerPsnId)
          .setParameter("searchKey", searchKey).uniqueResult());
      map.put("3", this.createQuery(sb.toString() + hql3).setParameter("ownerPsnId", ownerPsnId)
          .setParameter("searchKey", searchKey).uniqueResult());
      map.put("7", this.createQuery(sb.toString() + hql7).setParameter("ownerPsnId", ownerPsnId)
          .setParameter("searchKey", searchKey).uniqueResult());
    } else {
      map.put("1", this.createQuery(sb.toString() + hql1).setParameter("ownerPsnId", ownerPsnId).uniqueResult());
      map.put("2", this.createQuery(sb.toString() + hql2).setParameter("ownerPsnId", ownerPsnId).uniqueResult());
      map.put("3", this.createQuery(sb.toString() + hql3).setParameter("ownerPsnId", ownerPsnId).uniqueResult());
      map.put("7", this.createQuery(sb.toString() + hql7).setParameter("ownerPsnId", ownerPsnId).uniqueResult());
    }
    return map;
  }

  @SuppressWarnings("unchecked")
  public List<PsnFile> getFileListForPsn(Page page, Long ownerPsnId, String searchKey, Integer fileTypeNum) {
    String countHql = "select count(1) ";
    StringBuilder sb = new StringBuilder();
    sb.append(" from PsnFile sf where sf.status=0 and sf.ownerPsnId=:ownerPsnId ");
    if (StringUtils.isNotBlank(searchKey)) {
      searchKey = StringEscapeUtils.unescapeHtml4(searchKey);
      sb.append(" and instr(upper(sf.fileName),upper(:searchKey))>0  ");
    }
    if (fileTypeNum != null && fileTypeNum != 0) {
      switch (fileTypeNum) {
        case 1:
          sb.append(" and sf.fileType='pdf' ");
          break;
        case 2:
          sb.append(" and sf.fileType='doc' ");
          break;
        case 3:
          sb.append(" and sf.fileType='xls' ");
          break;
        case 7:
          sb.append(" and (sf.fileType<>'xls' and sf.fileType<>'doc' and sf.fileType<>'pdf') ");
          break;
        default:
          break;
      }
    }
    sb.append("  order by sf.updateDate desc,sf.id desc  ");
    if (StringUtils.isNotBlank(searchKey)) {
      page.setTotalCount((Long) this.createQuery(countHql + sb.toString()).setParameter("ownerPsnId", ownerPsnId)
          .setParameter("searchKey", searchKey).uniqueResult());
      return this.createQuery(sb.toString()).setParameter("ownerPsnId", ownerPsnId).setParameter("searchKey", searchKey)
          .setFirstResult((page.getParamPageNo() - 1) * page.getPageSize()).setMaxResults(page.getPageSize()).list();
    } else {
      page.setTotalCount(
          (Long) this.createQuery(countHql + sb.toString()).setParameter("ownerPsnId", ownerPsnId).uniqueResult());
      return this.createQuery(sb.toString()).setParameter("ownerPsnId", ownerPsnId)
          .setFirstResult((page.getParamPageNo() - 1) * page.getPageSize()).setMaxResults(page.getPageSize()).list();
    }
  }

  public List<Map<Object, Object>> getFileList(Page page, Long ownerPsnId) {
    String countHql = "select count(1) from PsnFile sf where sf.status=0 and sf.ownerPsnId=:ownerPsnId";
    String hql =
        " select sf.fileName as fileName,sf.fileDesc as fileDesc,sf.id as id, sf.archiveFileId as archiveFileId, sf.fileType as fileType,sf.uploadDate as uploadDate,af.fileSize as fileSize from PsnFile sf, ArchiveFile af where sf.status=0 and sf.archiveFileId=af.fileId and sf.ownerPsnId=:ownerPsnId order by sf.updateDate desc,sf.id desc";
    page.setTotalCount((Long) this.createQuery(countHql).setParameter("ownerPsnId", ownerPsnId).uniqueResult());
    return this.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
        .setParameter("ownerPsnId", ownerPsnId).setFirstResult((page.getPageNo() - 1) * page.getPageSize())
        .setMaxResults(page.getPageSize()).list();
  }

  /**
   * 查询自己的文件psnId，archiveFileId
   * 
   * @return
   */
  public PsnFile findPsnFileByPsnIdArchiveId(Long ownerPsnId, Long archiveFileId) {
    String hql = " from   PsnFile  f  where f.ownerPsnId=:ownerPsnId  and  f.archiveFileId=:archiveFileId ";
    List list = this.createQuery(hql).setParameter("ownerPsnId", ownerPsnId)
        .setParameter("archiveFileId", archiveFileId).list();
    if (list != null && list.size() > 0) {
      return (PsnFile) list.get(0);
    }
    return null;
  }

  /**
   * 根据fileId查询
   */
  public List<Map<Object, Object>> getFileListByFileId(List<Long> fileId) {
    String hql =
        " select sf.fileName as fileName,sf.fileDesc as fileDesc,sf.id as id,sf.fileType as fileType,sf.uploadDate as uploadDate,af.fileSize as fileSize from PsnFile sf, ArchiveFile af where sf.status=0 and sf.archiveFileId=af.fileId and sf.id in(:fileId)  order by sf.updateDate desc,sf.id desc";
    return this.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
        .setParameterList("fileId", fileId).list();
  }

  /**
   * 检查当前需要删除的文件是否存在并且状态正常
   */
  public PsnFile checkCurrFileIsExist(Long psnId, Long fileId) {
    String HQL = "from PsnFile p where p.ownerPsnId=:psnId and p.id=:fileId and p.status = 0";
    return (PsnFile) getSession().createQuery(HQL).setParameter("psnId", psnId).setParameter("fileId", fileId)
        .uniqueResult();
  }
}
