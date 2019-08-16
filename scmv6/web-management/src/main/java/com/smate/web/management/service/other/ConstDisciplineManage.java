package com.smate.web.management.service.other;

import java.util.List;
import java.util.Locale;

import com.smate.core.base.consts.model.ConstDiscipline;
import com.smate.core.base.exception.ServiceException;
import com.smate.web.management.model.other.fund.ConstDisciplineKey;

/**
 * 
 * @author liqinghua
 * 
 */
public interface ConstDisciplineManage {

  /**
   * 获取所有学科列表.
   * 
   * @return
   * @throws ServiceException
   */
  List<ConstDiscipline> findAll() throws ServiceException;

  /**
   * 获取学科下拉树JSON，一级.
   * 
   * @return
   * @throws ServiceException
   */
  /*
   * String getDiscTreeJson(Locale... locales) throws ServiceException;
   *//**
      * 获取学科下拉树JSON，二级,三级...，需要通过id获取.
      * 
      * @return
      * @throws ServiceException
      */
  /*
   * String getDiscTreeJsonBySub(Long id, Locale... locales) throws ServiceException;
   */

  /**
   * 接收个人专长学科同步.
   * 
   * @param list
   * @throws ServiceException
   */
  void pullConstDisciplineSyn(List<ConstDiscipline> list) throws ServiceException;

  /**
   * 关键字匹配关键字列表.
   * 
   * @param keyWords
   * @return
   * @throws ServiceException
   */
  List<ConstDisciplineKey> findAutoCdKey(String keyWords, String exclude, int size) throws ServiceException;

  /**
   * 获取关键字.
   * 
   * @param keyWords
   * @return
   * @throws ServiceException
   */
  ConstDisciplineKey findCdKeyByKeyWords(String keyWords) throws ServiceException;

  /**
   * 获取学科领域名称.
   * 
   * @param id
   * @param locale
   * @return
   * @throws ServiceException
   */
  String getDisciplineName(Long id, Locale locale) throws ServiceException;

  /**
   * 通过CODE获取ID.
   * 
   * @param code
   * @return
   * @throws ServiceException
   */
  Long getIdByCode(String code) throws ServiceException;

  /**
   * 学科代码修改同步处理.
   * 
   * @param disId
   * @param name
   * @param zhOrEn
   * @throws ServiceException
   */
  void syncUpdateDiscipline(Long disId, String name, int zhOrEn) throws ServiceException;

  /**
   * 根据superId获取它所有的子节点Id
   * 
   * @param superId
   * @return
   * @throws ServiceException
   */
  List<Long> getAllChild(Long superId) throws ServiceException;

  /**
   * 根据学科id得到相关的学科
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  ConstDiscipline findDisciplineById(Long id) throws ServiceException;

  /**
   * 通过discCode获取ID.
   * 
   * @param discCode
   * @return
   * @throws ServiceException
   */
  Long getIdByDiscCode(String discCode) throws ServiceException;

  /**
   * @param id
   * @return
   * @throws ServiceException
   */
  String getDisCodeById(Long id) throws ServiceException;

  /**
   * 通过学科代码查询学科领域JSON数据.
   * 
   * @param discCode
   * @return
   * @throws ServiceException
   */
  String findDiscJsonData(String discCode) throws ServiceException;

  /**
   * bpo通过学科代码查询学科领域JSON数据.
   * 
   * @param discCode
   * @return
   * @throws ServiceException
   */
  String bpoFindDiscJsonData(String discCode) throws ServiceException;

  /**
   * 获取一组学科代码信息
   */
  List<ConstDiscipline> findConstDiscByList(List<Long> discIdList) throws ServiceException;
}
