package com.smate.center.batch.service.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.AcInstitution;

/**
 * 智能提示service.
 * 
 * @author liqinghua
 * 
 */
public interface AutoCompleteService extends Serializable {

  /**
   * 获取智能匹配奖励类别自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException
   */
  String getAcAwardCategory(String startWith, int size) throws ServiceException;

  /**
   * 获取智能匹配奖励等级列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException
   */
  String getAcAwardGrade(String startWith, int size) throws ServiceException;

  /**
   * 获取智能匹配颁奖机构列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException
   */
  String getAcAwardIssueIns(String startWith, int size) throws ServiceException;

  /**
   * 获取智能匹配城市自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException
   */
  String getAcCity(String startWith, int size) throws ServiceException;

  /**
   * 获取智能匹配城市自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @param prvId TODO
   * @return
   * @throws ServiceException
   */
  String getAcConstCity(String startWith, Long prvId, int size) throws ServiceException;

  /**
   * 获取智能匹配省份自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @param size
   * @return
   * @throws ServiceException
   */
  String getAcProvince(String startWith, int size) throws ServiceException;

  /**
   * 获取智能匹配会议名称自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException
   */
  String getAcConfName(String startWith, int size) throws ServiceException;

  /**
   * 获取智能匹配会议组织者自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException
   */
  String getAcConfOrganizer(String startWith, int size) throws ServiceException;

  /**
   * 获取智能匹配国别列表，只读size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException
   */
  String getAcCountryRegion(String startWith, int size) throws ServiceException;

  /**
   * 获取智能匹配单位列表，只读size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException
   */
  String getAcInstitution(String startWith, String excludes, int size) throws ServiceException;

  /**
   * 获取智能匹配单位列表，只读size条记录.
   * 
   * @param startWith
   * @param prvId TODO
   * @param ignorePriv TODO
   * @return
   * @throws ServiceException
   */
  String getStaAcInstitution(String startWith, Long prvId, Integer ignorePriv, int size) throws ServiceException;

  /**
   * 获取智能匹配单位列表，只读size条记录.
   * 
   * @param startWith
   * @param prvId
   * @param disId
   * @param ignorePriv
   * @param size
   * @return
   * @throws ServiceException
   */
  String getStaAcInstitution(String startWith, Long prvId, Long cyId, Long disId, Integer ignorePriv, int size)
      throws ServiceException;

  /**
   * 获取智能匹配发证单位自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException
   */
  String getAcPatentOrg(String startWith, int size) throws ServiceException;

  /**
   * 获取智能匹配颁发单位自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException
   */
  String getAcThesisOrg(String startWith, int size) throws ServiceException;

  /**
   * 获取智能匹配出版社自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException
   */
  String getAcPublisher(String startWith, int size) throws ServiceException;

  /**
   * 通过任务保存自动提示信息.
   * 
   * @param text
   * @param type
   * @throws ServiceException
   */
  void saveAcDataByTask(String text, String type) throws ServiceException;

  /**
   * 智能匹配单位列表，如果未输入任何数据，则查询本单位数据.
   * 
   * @param startWith
   * @param excludes
   * @param size
   * @return
   * @throws ServiceException
   */
  String getAcAuthorInstitution(String startWith, String excludes, int size) throws ServiceException;

  /**
   * 智能匹配学科关键字列表.
   * 
   * @param startWith
   * @param excludes
   * @param size
   * @return
   * @throws ServiceException
   */
  String getDiscKeyWords(String startWith, String excludes, int size) throws ServiceException;

  String getDiscKeyWordsEn(String startWith, Long keyId, int size) throws ServiceException;

  /**
   * 智能匹配职务列表.
   * 
   * @return
   */
  String getAcPosition(String startWith, int size) throws ServiceException;

  /**
   * 项目资助机构，查询指定智能匹配前 N条数据.
   * 
   * @param startStr
   * @param size
   * @param language TODO
   * @return
   * @throws ServiceException
   */
  String getAcPrjSchemeAgency(String startStr, int size, String language) throws ServiceException;

  /**
   * 项目资助类别，查询指定智能匹配前 N条数据.
   * 
   * @param startStr
   * @param size
   * @param language TODO
   * @return
   * @throws ServiceException
   */
  String getAcPrjScheme(String startStr, Long agencyId, int size, String language) throws ServiceException;

  /**
   * 获取单位信息.
   * 
   * @param name
   * @return
   * @throws ServiceException
   */
  AcInstitution getAcInstitutionByName(String name) throws ServiceException;

  /**
   * 关键词自动提示.
   * 
   * @param startWith
   * @param disId
   * @param size
   * @return
   * @throws ServiceException
   */
  String getAcDisciplineKey(String startWith, Long disId, int size) throws ServiceException;

  String getAcDisciplineKey(String startWith, Long disId, int viewType, int size) throws ServiceException;

  String getAcPsnDisciplineKey(String startWith, Long psnDisId, int size) throws ServiceException;

  /**
   * 输入自己填写的关键词提示相关学科领域.
   * 
   * @param startStr
   * @param size
   * @return
   * @throws ServiceException
   */
  String getAcMyDisKeyAutoDis(String startStr, int size) throws ServiceException;

  /**
   * 基金申请计划类别.
   * 
   * @param startStr
   * @param size
   * @return
   * @throws ServiceException
   */
  String getAcFundCategory(String startStr, int agencyFlag, int size) throws ServiceException;
}
