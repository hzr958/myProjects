package com.smate.web.psn.service.autocomplete;

import java.util.List;
import java.util.Map;

import com.smate.core.base.consts.model.ConstKeyDisc;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.autocomplete.AutoCompleteForm;
import com.smate.web.psn.model.autocomplete.AcInsUnit;
import com.smate.web.psn.model.autocomplete.AcInstitution;

public interface AutoCompleteSnsService {

  /**
   * 自动补研究领域.
   * 
   * @param maxSize
   * @param startWith
   * @param discId
   * @return
   * @throws ServiceException
   */
  public String autoCompleteDiscipline(int maxSize, String startWith) throws Exception;

  /**
   * 自动填充部门
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public List<AcInsUnit> getAcInsUnit(AutoCompleteForm form) throws Exception;

  /**
   * 自动填充机构名称
   * 
   * @param searchKey
   * @param excludeIns
   * @param maxSize
   * @return
   * @throws Exception
   */
  public List<AcInstitution> getAcInstitution(String searchKey, String excludeIns, int size) throws Exception;

  /**
   * 自动填充学历
   * 
   * @param category
   * @param searchKey
   * @return
   * @throws Exception
   */
  public List<ConstDictionary> getAcDegree(String category, String searchKey) throws Exception;

  /**
   * 检索地区信息供自动填充用
   * 
   * @param searchKey 用户输入的字符
   * @param category 检索类型
   * @param size 每次检索出来数量
   * @return
   * @throws Exception
   */
  public List<Map<String, Object>> searchConstRegionInfo(String searchKey, String category, Integer size)
      throws Exception;

  /**
   * 获取自动填充的机构名称
   */
  public List<ConstRegion> getAcregion(String searchKey, String excludeIns, int size) throws Exception;

  /**
   * 从成果合作者或好友中提示人名
   */
  public List<Map<String, Object>> searchPsnCooperator(AutoCompleteForm form, int size) throws Exception;

  /**
   * 奖励类别自动提示列表
   */
  public List<Map<String, Object>> getAcAwardCategory(String searchKey, int maxsize) throws Exception;

  /**
   * 奖励等级列表
   */
  public List<Map<String, Object>> getAcAwardGrade(String searchKey, int maxsize) throws Exception;

  @Deprecated
  /**
   * 颁奖机构列表
   */
  public List<Map<String, Object>> getAcAwardIssueIns(String searchKey, int maxsize) throws Exception;

  /**
   * 会议名称自动提示
   */
  public List<Map<String, Object>> getAcConfName(String searchKey, int maxsize) throws Exception;

  /**
   * 会议组织者自动提示列表
   */
  public List<Map<String, Object>> getAcConfOrganizer(String searchKey, int maxsize) throws Exception;

  /**
   * 期刊列表
   */
  public List<Map<String, Object>> getAcJournal(String searchKey, int maxsize) throws Exception;

  /**
   * 发证单位自动提示列表
   */
  public List<Map<String, Object>> getAcPatentOrg(String searchKey, int maxsize) throws Exception;

  /**
   * 颁发单位自动提示列表
   */
  public List<Map<String, Object>> getAcThesisOrg(String searchKey, int maxsize) throws Exception;

  /**
   * 出版社自动提示列表
   */
  public List<Map<String, Object>> getAcPublisher(String searchKey, int maxsize) throws Exception;

  /**
   * 获取学科关键词列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public List<ConstKeyDisc> getConstKeyDiscs(String seachKey, Integer size) throws Exception;

  /**
   * 获取标题自动补全
   * 
   * @param searchKey
   * @param maxsize
   * @param type
   * @return
   */
  public List<Map<String, Object>> getAcTitle(String searchKey, String type);

  /**
   * 检索人员与成果
   * 
   * @param searchKey 检索字符串
   * @param maxsize
   * @param type 1.成果，2.专利，3.人员
   * @return
   */
  List<Map<String, Object>> getPdwhSearchSuggest(String searchKey, Integer type);

  /**
   * 获取自动填充的省份
   */
  public List<ConstRegion> getAcprovinces(String searchKey, int size) throws Exception;

}
