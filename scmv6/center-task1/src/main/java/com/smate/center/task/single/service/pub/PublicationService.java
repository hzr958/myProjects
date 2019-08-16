package com.smate.center.task.single.service.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.pub.PubInfoTmp;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.center.task.model.sns.quartz.PublicationForm;

/**
 * 成果SERVICE接口.
 * 
 * @author liqinghua
 * 
 */
public interface PublicationService extends Serializable {


  PublicationForm getPublication(Long pubId) throws ServiceException;

  PublicationForm getPublication(PublicationForm form) throws ServiceException;

  Long getTotalPubsByPsnId(Long psnId);

  /**
   * 人员推荐-查询人员所有成果，字段限成果查重字段.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Publication> findPubByPnsId(Long psnId) throws ServiceException;

  /**
   * 人员推荐-根据成果查找相同成果、文献人员.
   * 
   * @param pub
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> matchPubPsnIds(Publication pub) throws ServiceException;

  /**
   * 查找tmPsnId的成果合作者中是否likeName的人员.
   * 
   * @param tmPsnId
   * @return
   * @throws ServiceException
   */
  int pubMatchName(Long tmPsnId, String zhName, String likeName) throws ServiceException;

  List<Publication> getPubByPsnId() throws ServiceException;

  void savePubInfoTmp(PubInfoTmp pubInfo) throws ServiceException;

  List<Map> getLastMonthPsnPubs(Integer size) throws ServiceException;

  List<Map> getLastMonthPsnCitedTimes(Integer size);

}
