package com.smate.web.v8pub.service.journal;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.journal.JournalPO;


/**
 * sns 冗余期刊Service
 * 
 * @author tsz
 * 
 */
public interface JournalService {


  /**
   * 根据id得到期刊
   * 
   * @param jid
   * @return
   * @throws ServiceException
   */
  JournalPO getById(Long jid) throws ServiceException;

  /**
   * 根据名字 查找单个期刊
   * 
   * @param jname
   * @param issn
   * @param psnId
   * @return
   * @throws ServiceException
   */
  JournalPO findJournalByNameIssn(String jname, String issn, Long psnId) throws ServiceException;

  /**
   * 保存普通期刊信息
   * 
   * @param journalPO
   * @throws ServiceException
   */
  void save(JournalPO journalPO) throws ServiceException;


  /**
   * 在成果录入过程中通过ajax添加期刊.
   * 
   * @param name
   * @param issn
   * @return
   * @throws ServiceException
   */
  String ajaxAddJournalByPubEnter(String name, String issn) throws ServiceException;

  /**
   * 查找影响因子
   * 
   * @param jid
   * @return
   */
  String findImpactFactorsByJid(Long jid);

}
