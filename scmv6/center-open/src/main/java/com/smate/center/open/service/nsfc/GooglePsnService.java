package com.smate.center.open.service.nsfc;

import java.util.List;

import com.smate.center.open.model.nsfc.NsfcwsPerson;

/**
 * 
 * @zjh googleservice
 *
 */


public interface GooglePsnService {
  /**
   * 通过英文名获和单位名或邮箱获取人员信息.
   * 
   * @param psnName
   * @param insName
   * @param email
   * @return
   * @throws ServiceException
   */
  List<NsfcwsPerson> getNsfcwsPerson(String psnName, String insName, String email) throws Exception;

}
