package com.smate.web.fund.dao.wechat;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.web.fund.model.common.ConstFundCategoryFile;

/**
 * 基金机构类别-附件dao
 * 
 * @author lhd
 * 
 */
@Repository
public class ConstFundCategoryFileDao extends RcmdHibernateDao<ConstFundCategoryFile, Long> {

  /**
   * 获取基金附件
   * 
   * @param categoryId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstFundCategoryFile> findFundFileByCategoryId(Long categoryId) {
    String hql = "from ConstFundCategoryFile t where t.categoryId=:categoryId";
    return super.createQuery(hql).setParameter("categoryId", categoryId).list();
  }

}
