package com.smate.center.open.service.common;

import java.util.List;

import com.smate.center.open.model.nsfc.NsfcwsPerson;
import com.smate.center.open.model.nsfc.NsfcwsPublication;
import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.model.security.Person;

/**
 * Iris业务系统与SNS交互时需要用到的公共辅助接口.
 * 
 * @author pwl
 * 
 */
public interface IrisCommonService {
  /**
   * 获取可以查询那些权限的成果.
   * 
   * @param psnGuidID
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Integer> getQueryPubPermission(String psnGuidID, Long psnId) throws Exception;

  /**
   * 获取关联用户的psn_id。
   * 
   * @param psnGuidID
   * @return
   * @throws ServiceException
   */
  Long getConnectedPsnId(String psnGuidID) throws Exception;

  String buildPubListXmlStr(List<Publication> publicationList) throws Exception;

  String buildGooglePubListXmlStr(List<NsfcwsPublication> publicationList) throws Exception;

  String buildGooglePsnListXmlStr(List<NsfcwsPerson> personList) throws Exception;

  /**
   * 重构邮件显示效果.
   * 
   * @param email
   * @return
   * @throws ServiceException
   */
  String buildEmail(String email) throws Exception;

  /**
   * 检查用户是否关联.
   * 
   * @param psnGuidID
   * @param psnId
   * @return
   * @throws ServiceException
   */
  int checkPsnConnected(String psnGuidID, Long psnId) throws Exception;

  String buildPsnListXmlStr(List<Person> personList) throws Exception;

}
