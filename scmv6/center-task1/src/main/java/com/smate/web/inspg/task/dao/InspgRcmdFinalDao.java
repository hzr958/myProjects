package com.smate.web.inspg.task.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.exception.RcmdTaskException;
import com.smate.core.base.utils.model.Page;
import com.smate.web.inspg.task.model.InspgRcmdFinal;

/**
 * 机构主页-发现机构主页，最终推荐表Dao
 * 
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Repository
public class InspgRcmdFinalDao extends SnsHibernateDao<InspgRcmdFinal, Long> {

  /**
   * 删除psnId的推荐数据
   * 
   * @param psnId
   * 
   */
  public void deleteByPsnId(Long psnId) throws RcmdTaskException {
    String hql = "delete from InspgRcmdFinal t where t.psnId = ?";
    super.createQuery(hql, psnId).executeUpdate();
  }

  /**
   * 删除一条推荐数据
   * 
   * @param psnId,inspgId
   * 
   */
  public void deleteByInspgIdAndPsnId(Long inspgId, Long psnId) throws RcmdTaskException {
    String hql = "delete from InspgRcmdFinal t where t.psnId = ? and t.inspgId = ?";
    super.createQuery(hql, psnId, inspgId).executeUpdate();

  }

  /**
   * 得到psnId所有的推荐数据
   * 
   * @param psnId
   * 
   */
  @SuppressWarnings("unchecked")
  public List<InspgRcmdFinal> getInspgRcmdList(Long psnId) throws RcmdTaskException {
    String hql = "from InspgRcmdFinal t where t.psnId = ?";
    return super.createQuery(hql, psnId).list();

  }

  /**
   * 保存推荐数据
   * 
   * @param psnId,inspgId
   * 
   */
  public void saveInspgRcmd(InspgRcmdFinal inspgRcmdFinal) throws RcmdTaskException {

    super.save(inspgRcmdFinal);

  }

}
