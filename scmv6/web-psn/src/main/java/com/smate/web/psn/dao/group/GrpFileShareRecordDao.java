package com.smate.web.psn.dao.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.file.PsnFileShareRecord;
import com.smate.web.psn.model.grp.GrpFile;
import com.smate.web.psn.model.grp.GrpFileShareRecord;

/**
 * 群组文件dao
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class GrpFileShareRecordDao extends SnsHibernateDao<GrpFileShareRecord, Long> {

  public Page<GrpFileShareRecord> queryGrpFileShareByPage(Long resSendId, Long resReveiverId, Long baseId)
      throws DaoException {
    String hql = "from GrpFileShareRecord t where t.sharerId=? and t.reveiverId=? and t.status=0 and t.shareBaseId=?";
    Page<GrpFileShareRecord> page = new Page<GrpFileShareRecord>();

    List<GrpFileShareRecord> list =
        super.createQuery(hql + " order by t.id", new Object[] {resSendId, resReveiverId, baseId}).list();
    page.setResult(list);

    return page;
  }

  public List<GrpFileShareRecord> queryGrpFileShareList(Long resSendId, Long resReveiverId, Long baseId)
      throws DaoException {
    String hql = "from GrpFileShareRecord t where t.sharerId=? and t.reveiverId=? and t.status=0 and t.shareBaseId=?";

    List<GrpFileShareRecord> list =
        super.createQuery(hql + " order by t.id", new Object[] {resSendId, resReveiverId, baseId}).list();
    return list;
  }

  public GrpFile findGrpFileById(Long grpFileId) {
    String hql = "from  GrpFile  g where g.grpFileId =:grpFileId and  g.fileStatus = 0  ";
    List list = this.createQuery(hql).setParameter("grpFileId", grpFileId).list();
    if (list != null && list.size() > 0) {
      return (GrpFile) list.get(0);
    }
    return null;
  }

}
