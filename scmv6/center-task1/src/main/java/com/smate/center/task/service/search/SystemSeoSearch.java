package com.smate.center.task.service.search;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.model.pdwh.quartz.PdwhIndexPublication;
import com.smate.center.task.model.pub.seo.PubIndexFirstLevel;
import com.smate.center.task.model.pub.seo.PubIndexSecondLevel;
import com.smate.center.task.model.search.PubSearchForm;
import com.smate.core.base.utils.model.Page;

/**
 * 提供seo数据的分析
 * 
 * @author tsz
 * 
 */
public interface SystemSeoSearch extends Serializable {

  /**
   * 重新生成成果分层数据
   * 
   * @param code 成果首字母A-Z,中文other
   * @param secondGroup 所属分组
   * @param iPubList 成果数据集
   * @param num
   * @param onlyOne 是否只有一组数据
   */
  void pubFilter(String code, int secondGroup, List<PdwhIndexPublication> iPubList, int num, boolean onlyOne);

  /**
   * 根据成果首字母获取第一层分组数据
   * 
   * @param code 成果首字母A-Z,中文other
   * @return 第一层分组数据集
   */
  public List<PubIndexFirstLevel> getPubByCode(String code);

  /**
   * 模糊查询
   * 
   * @param code
   * @return
   */
  public List<PubIndexFirstLevel> getPubByCodeAndLabel(String code);

  Page<PubIndexSecondLevel> getPubByLabel(String code, Integer label, Page<PubIndexSecondLevel> page);

  /**
   * 查询成果
   * 
   * @param form
   * @return TODO
   */
  public PubSearchForm findPubByName(PubSearchForm form);

  void delDir();

}
