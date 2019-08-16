package com.smate.web.psn.dao.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.form.FileMainForm;
import com.smate.web.psn.form.PsnFileInfo;

/**
 * 
 * 我的文件库Dao
 * 
 * @author zk
 *
 */
@Repository
public class StationFileDao extends SnsHibernateDao<StationFile, Long> {
  /**
   * 编辑文件描述
   */
  public void saveFileDesc(Long fileId, Long psnId, String fileDesc) {
    String hql = "update StationFile t set t.fileDesc=:fileDesc where t.psnId=:psnId and t.fileId=:fileId";
    this.createQuery(hql).setParameter("psnId", psnId).setParameter("fileId", fileId).setParameter("fileDesc", fileDesc)
        .executeUpdate();
  }

  /**
   * 删除文件
   */
  public void delFile(Long fileId, Long psnId) {
    String hql = "update StationFile t set t.fileStatus=1 where t.psnId=:psnId and t.fileId=:fileId";
    this.createQuery(hql).setParameter("psnId", psnId).setParameter("fileId", fileId).executeUpdate();
  }

  /**
   * 
   * @param psnId
   * @param groupId
   * @param pageSize
   * @param pageNo
   * @return
   */
  public void findFileForPsn(Long psnId, Page<StationFile> page, String searchKey) {
    String countHql = " select count(1) ";
    String selectHql =
        "select new StationFile(sf.fileId,sf.fileName  ,sf.filePath ,sf.fileType , sf.fileDesc  ,sf.uploadTime ,sf.archiveFileId) ";
    String hql = " from StationFile sf where sf.fileStatus=0 and sf.psnId=:psnId ";

    if (StringUtils.isNotBlank(searchKey)) {
      hql += "and     instr(upper(sf.fileName),  :searchKey)>0  ";
    }
    hql += "  order by sf.uploadTime desc    ,  sf.fileId desc ";

    Query query = super.createQuery(selectHql + hql).setParameter("psnId", psnId)
        .setFirstResult((page.getParamPageNo() - 1) * page.getPageSize()).setMaxResults(page.getPageSize());
    Query queryCount = super.createQuery(countHql + hql).setParameter("psnId", psnId);
    if (StringUtils.isNotBlank(searchKey)) {
      query.setParameter("searchKey", searchKey);
      queryCount.setParameter("searchKey", searchKey);
    }
    Object count = queryCount.uniqueResult();
    page.setTotalCount(NumberUtils.toInt(count.toString()));
    page.setResult(query.list());
  }

  public Map<String, Object> getFileListTypeCount(FileMainForm form) {
    Map<String, Object> map = new HashMap<String, Object>();
    StringBuilder sb = new StringBuilder();
    sb.append("select count(1) from StationFile sf where sf.fileStatus=0 and sf.psnId=:psnId ");
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      sb.append(" and instr(upper(sf.fileName),upper(:searchKey))>0  ");
    }
    String hql1 = " and sf.fileType='pdf' ";
    String hql2 = " and sf.fileType='doc' ";
    String hql3 = " and sf.fileType='xls' ";
    String hql7 = " and (sf.fileType<>'xls' and sf.fileType<>'doc' and sf.fileType<>'pdf') ";
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      map.put("1", this.createQuery(sb.toString() + hql1).setParameter("psnId", form.getPsnId())
          .setParameter("searchKey", form.getSearchKey()).uniqueResult());
      map.put("2", this.createQuery(sb.toString() + hql2).setParameter("psnId", form.getPsnId())
          .setParameter("searchKey", form.getSearchKey()).uniqueResult());
      map.put("3", this.createQuery(sb.toString() + hql3).setParameter("psnId", form.getPsnId())
          .setParameter("searchKey", form.getSearchKey()).uniqueResult());
      map.put("7", this.createQuery(sb.toString() + hql7).setParameter("psnId", form.getPsnId())
          .setParameter("searchKey", form.getSearchKey()).uniqueResult());
    } else {
      map.put("1", this.createQuery(sb.toString() + hql1).setParameter("psnId", form.getPsnId()).uniqueResult());
      map.put("2", this.createQuery(sb.toString() + hql2).setParameter("psnId", form.getPsnId()).uniqueResult());
      map.put("3", this.createQuery(sb.toString() + hql3).setParameter("psnId", form.getPsnId()).uniqueResult());
      map.put("7", this.createQuery(sb.toString() + hql7).setParameter("psnId", form.getPsnId()).uniqueResult());
    }
    return map;
  }

  @SuppressWarnings("unchecked")
  public List<StationFile> getFileListForPsn(FileMainForm form) {
    Page<PsnFileInfo> page = form.getPage();
    String countHql = "select count(1) ";
    String hql =
        "select new StationFile(sf.fileId,sf.fileName ,sf.filePath ,sf.fileType , sf.fileDesc  ,sf.uploadTime ,sf.archiveFileId) ";
    StringBuilder sb = new StringBuilder();
    sb.append(" from StationFile sf where sf.fileStatus=0 and sf.psnId=:psnId ");
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      sb.append(" and instr(upper(sf.fileName),upper(:searchKey))>0  ");
    }
    if (form.getFileTypeNum() != null && form.getFileTypeNum() != 0) {
      switch (form.getFileTypeNum()) {
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
    sb.append("  order by sf.uploadTime desc,sf.fileId desc  ");
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      page.setTotalCount((Long) this.createQuery(countHql + sb.toString()).setParameter("psnId", form.getPsnId())
          .setParameter("searchKey", form.getSearchKey()).uniqueResult());
      return this.createQuery(hql + sb.toString()).setParameter("psnId", form.getPsnId())
          .setParameter("searchKey", form.getSearchKey()).setFirstResult(page.getFirst() - 1)
          .setMaxResults(page.getPageSize()).list();
    } else {
      page.setTotalCount(
          (Long) this.createQuery(countHql + sb.toString()).setParameter("psnId", form.getPsnId()).uniqueResult());
      return this.createQuery(hql + sb.toString()).setParameter("psnId", form.getPsnId())
          .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    }
  }

  @SuppressWarnings("unchecked")
  public List<StationFile> getFileListForPsn(Page page, Long psnId, String searchKey, Integer fileTypeNum) {
    String countHql = "select count(1) ";
    String hql =
        "select new StationFile(sf.fileId,sf.fileName ,sf.filePath ,sf.fileType , sf.fileDesc  ,sf.uploadTime ,sf.archiveFileId) ";
    StringBuilder sb = new StringBuilder();
    sb.append(" from StationFile sf where sf.fileStatus=0 and sf.psnId=:psnId ");
    if (StringUtils.isNotBlank(searchKey)) {
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
    sb.append("  order by sf.uploadTime desc,sf.fileId desc  ");
    if (StringUtils.isNotBlank(searchKey)) {
      page.setTotalCount((Long) this.createQuery(countHql + sb.toString()).setParameter("psnId", psnId)
          .setParameter("searchKey", searchKey).uniqueResult());
      return this.createQuery(hql + sb.toString()).setParameter("psnId", psnId).setParameter("searchKey", searchKey)
          .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    } else {
      page.setTotalCount((Long) this.createQuery(countHql + sb.toString()).setParameter("psnId", psnId).uniqueResult());
      return this.createQuery(hql + sb.toString()).setParameter("psnId", psnId).setFirstResult(page.getFirst() - 1)
          .setMaxResults(page.getPageSize()).list();
    }
  }

  /**
   * 在群组模块获取我的文件夹
   * 
   * @param psnId
   * @param groupId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<StationFile> findFileForGroup(Long psnId, Long groupId, Integer pageSize, Integer pageNo) {
    String hql =
        "select new StationFile(sf.fileId,sf.fileName)  from StationFile sf where sf.fileStatus=0 and sf.psnId=:psnId and not exists(select gf.groupFileId from GroupFile gf where gf.groupId=:groupId and gf.fileId = sf.fileId and gf.fileStatus=0 ) order by sf.fileId desc";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("groupId", groupId)
        .setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
  }

  /**
   * 在群组模块获取我的文件夹 grp_file 表
   * 
   * @param psnId
   * @param grpId
   * @param pageSize
   * @param pageNo
   * @return
   */
  @SuppressWarnings("unchecked")
  public void findFileForGrp(Long psnId, Long grpId, Page<StationFile> page, String searchKey) {
    String countHql = " select count(1) ";
    String selectHql =
        "select new StationFile(sf.fileId,sf.fileName  ,sf.filePath ,sf.fileType , sf.fileDesc  ,sf.uploadTime ,sf.archiveFileId) ";
    String hql = " from StationFile sf" + " where sf.fileStatus=0 and sf.psnId=:psnId ";

    if (StringUtils.isNotBlank(searchKey)) {
      hql += "and     instr(upper(sf.fileName),  :searchKey)>0  ";
    }
    hql += "  order by sf.uploadTime desc    ,  sf.fileId desc ";

    Query query = super.createQuery(selectHql + hql).setParameter("psnId", psnId)
        .setFirstResult((page.getParamPageNo() - 1) * page.getPageSize()).setMaxResults(page.getPageSize());
    Query queryCount = super.createQuery(countHql + hql).setParameter("psnId", psnId);
    if (StringUtils.isNotBlank(searchKey)) {
      query.setParameter("searchKey", searchKey);
      queryCount.setParameter("searchKey", searchKey);
    }
    Object count = queryCount.uniqueResult();
    page.setTotalCount(NumberUtils.toInt(count.toString()));
    page.setResult(query.list());

  }

  /**
   * 我的文件是否存在群组
   * 
   * @return
   */
  public Boolean existsGrpFile(Long grpId, Long archiveFileId, Long psnId) {
    String hql =
        "select gf.grpFileId from GrpFile gf where gf.grpId=:grpId and gf.archiveFileId =:archiveFileId  and gf.uploadPsnId =:uploadPsnId and gf.fileStatus=0";
    List<Long> list = this.createQuery(hql).setParameter("grpId", grpId).setParameter("archiveFileId", archiveFileId)
        .setParameter("uploadPsnId", psnId).list();
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }

  /**
   * 查询自己的文件psnId，archiveFileId
   * 
   * @return
   */
  public StationFile findStationFileByPsnIdArchiveId(Long psnId, Long archiveFileId) {
    String hql = " from   StationFile  sf  where sf.psnId=:psnId  and  sf.archiveFileId=:archiveFileId ";
    List list = this.createQuery(hql).setParameter("psnId", psnId).setParameter("archiveFileId", archiveFileId).list();
    if (list != null && list.size() > 0) {
      return (StationFile) list.get(0);
    }
    return null;
  }

}
