package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PubFolder;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果/文献数据库访问接口.
 * 
 * @author LY
 * 
 */
@Repository
public class PubFolderDaoImpl extends SnsHibernateDao<PubFolder, Long> implements PubFolderDao {

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.dao.folder.PubFolderDao#addPubFolder(com.iris.scm
   * .scmweb.model.folder.PubFolder)
   */
  @Override
  public Long addPubFolder(PubFolder pubFolder) {
    return (Long) super.getSession().save(pubFolder);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.dao.folder.PubFolderDao#getPubFolderByPsnId(java. lang.Integer)
   */
  @Override
  public List<PubFolder> getPubFolderByPsnId(Long psnId, Integer articleType) {
    return super.find("from PubFolder t where t.psnId=? and t.articleType=? and enabled=1 order by t.id",
        new Object[] {psnId, articleType});
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.dao.folder.PubFolderDao#removePubFolder(java.lang .Long)
   */
  @Override
  public void removePubFolder(Long id) {
    PubFolder pubFolder = getPubFolderById(id);
    super.getSession().delete(pubFolder);
  }

  @Override
  public void removePubFolder(PubFolder folder) {

    super.delete(folder);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.dao.folder.PubFolderDao#updatePubFolder(com.iris.
   * scm.scmweb.model.folder.PubFolder)
   */
  @Override
  public Long updatePubFolder(PubFolder pubFolder) {
    return (Long) super.getSession().save(pubFolder);
  }

  @Override
  public PubFolder getPubFolderById(Long id) {
    return super.findUnique("from PubFolder t where t.id=?", new Object[] {id});
  }

  @Override
  public PubFolder getFolderByName(String folderName, Long psnId, Integer articleType) {
    String hql = "from PubFolder t where t.name = ? and t.psnId=? and t.articleType=? and enabled=1";
    List<PubFolder> list = super.createQuery(hql, folderName, psnId, articleType).list();
    if (list.size() > 0)
      return list.get(0);
    else
      return null;
  }

  /**
   * 获取指定人员成果文件夹.
   */
  @Override
  public List<PubFolder> queryPubFolderByPsnId(Long psnId) {
    return super.find("from PubFolder t where t.psnId=?", new Object[] {psnId});
  }

}
