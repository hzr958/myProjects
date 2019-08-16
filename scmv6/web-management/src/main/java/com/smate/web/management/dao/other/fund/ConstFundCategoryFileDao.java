package com.smate.web.management.dao.other.fund;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.web.management.model.other.fund.ConstFundCategoryFile;

/**
 * @author cwli
 * 
 */
@Repository
public class ConstFundCategoryFileDao extends HibernateDao<ConstFundCategoryFile, Long> {

  public void deleteFundFileByCategoryId(Long categoryId) {
    String hql = "delete from ConstFundCategoryFile t where t.categoryId=?";
    super.createQuery(hql, categoryId).executeUpdate();
  }

  public List<ConstFundCategoryFile> findFundFileByCategoryId(Long categoryId) {
    String hql = "from ConstFundCategoryFile t where t.categoryId=?";
    return super.find(hql, categoryId);
  }

  public ConstFundCategoryFile getConstFundCategoryFile(Long categoryId, String filePath) {
    String hql = "from ConstFundCategoryFile t where t.categoryId=? and t.filePath=?";
    return findUnique(hql, categoryId, filePath);
  }

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.RCMD;
  }
}
