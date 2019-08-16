package com.smate.core.base.consts.dao.psnname;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.consts.model.psnname.ConstPsnLastNamePy;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人名姓氏多音字常量表DAO
 * 
 * @author SYL
 *
 */
@Repository
public class ConstPsnLastNamePyDao extends SnsHibernateDao<ConstPsnLastNamePy, Long> implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * 根据中文姓氏查找对应的读音拼音
   * 
   * @param zhWord
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> findPinyinByZhWord(String zhWord) {
    String hql = "select pinyinWord from ConstPsnLastNamePy where zhWord=?";
    return super.createQuery(hql, zhWord).list();
  }

  /**
   * 根据中文姓氏查找对应的读音拼音，按id升序（获取第一个）
   * 
   * @param zhWord
   * @return
   */
  public String findFirstPinyinByZhWord(String zhWord) {
    String hql = "select pinyinWord from ConstPsnLastNamePy where zhWord=? order by id asc";
    List<?> list = super.createQuery(hql, zhWord).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return (String) list.get(0);
    } else {
      return null;
    }
  }

}
