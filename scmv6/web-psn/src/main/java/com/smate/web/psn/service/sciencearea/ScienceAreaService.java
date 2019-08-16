package com.smate.web.psn.service.sciencearea;

import java.util.List;

import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.keyword.PsnScienceArea;
import com.smate.web.psn.model.keyword.PsnScienceAreaInfo;
import com.smate.web.psn.model.profile.PsnScienceAreaForm;

/**
 * 科技领域服务接口
 *
 * @author wsn
 * @createTime 2017年3月14日 下午6:29:46
 *
 */
public interface ScienceAreaService {

  /**
   * 获取人员科技领域列表
   * 
   * @param psnId
   * @return
   */
  List<PsnScienceArea> findPsnScienceAreaList(Long psnId, Integer status);

  /**
   * 保存人员科技邻域
   * 
   * @param psnId
   * @param ids
   * @return
   */
  String savePsnScienceArea(Long psnId, String ids);

  /**
   * 认同科技领域
   * 
   * @param psnId
   * @param scienceAreaId
   * @param friendId
   * @return
   */
  Integer saveIdentificationScienceArea(Long psnId, Integer scienceAreaId, Long friendId, Integer idenSum);

  /**
   * 构建科技领域信息
   * 
   * @param form
   * @return
   */
  PersonProfileForm buildPsnScienceAreaInfo(PersonProfileForm form);

  /**
   * 查找人员科技领域信息
   * 
   * @param form
   * @return
   */
  PersonProfileForm findPsnScienceArea(PersonProfileForm form);

  /**
   * 获取个人研究领域
   * 
   * @param form
   * @throws Exception
   */
  void myScienceArea(PsnListViewForm form) throws Exception;

  /**
   * 
   * 获取领域认同人员
   */
  List<Person> getIdentificationPerson(Long psnId, Integer areaId) throws Exception;

  /**
   * 获取研究领域信息
   */
  List<PsnScienceAreaForm> getPsnScienceAreaFormList(Long psnId);

  /**
   * 获取人员科技领域列表,返回操作类List
   * 
   * @param psnId
   * @return
   */
  List<PsnScienceAreaInfo> findPsnScienceAreaInfo(Long psnId, Integer status);

  List<PsnScienceArea> findPsnScienceAreaById(PersonProfileForm form) throws Exception;

  /**
   * 人员设置的科技领域id
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  List<Integer> findPsnAreaId(Long psnId) throws Exception;
}
