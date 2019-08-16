package com.smate.web.v8pub.service.searchimport;

import java.util.List;
import java.util.Set;

import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.vo.searchimport.ConstRefDbVO;
import com.smate.web.v8pub.vo.searchimport.PubImportVO;

/**
 * 检索导入成果接口
 * 
 * @author wsn
 * @date 2018年8月9日
 */
public interface PubImportService {

  /**
   * 初始化进入联邦检索页面所需信息
   * 
   * @param importVo
   * @throws ServiceException
   */
  void initSearchInfo(PubImportVO importVo) throws ServiceException;

  /**
   * 获取人员可查找文献库列表
   * 
   * @param dbType：查找的文献库类型（成果、项目...）
   * @param insIdList：人员单位ID
   * @return
   * @throws ServiceException
   */
  List<ConstRefDbVO> getSearchDBList(String dbType, List<Long> insIdList) throws ServiceException;

  /**
   * 将传入的xml转换成PendingImportPubVO对象
   * 
   * @param importVo
   * @throws ServiceException
   */
  void buildPendingImportPubByXml(PubImportVO importVo) throws ServiceException;

  /**
   * 重构待导入成果信息
   * 
   * @param importVo
   * @throws ServiceException
   */
  void initImportPubInfo(PubImportVO importVo) throws ServiceException;

  /**
   * 获取人员所有的单位信息
   * 
   * @param psnId
   * @return
   */
  List<WorkHistory> findPsnAllInsInfo(Long psnId);

  /**
   * 保存待导入成果
   * 
   * @param importVo
   * @throws ServiceException
   */
  void saveImportPub(PubImportVO importVo) throws ServiceException;

  /**
   * 查找好友推荐时需要排除掉的人员
   * 
   * @return
   * @throws ServiceException
   */
  List<Long> findAllRemovePsnId(Long psnId) throws ServiceException;

  /**
   * 成果查重
   * 
   * @param detail
   * @return
   * @throws ServiceException
   */
  String checkDuplicatePub(PubPdwhDetailDOM detail) throws ServiceException;

  /**
   * 根据输入机构名称获取别名
   * 
   * @param orgName 单位名称
   * @param dbCodes 文献库code
   * @return
   */
  String getOrgNameAlias(String orgName, String dbCodes);

  void buildPdwhPubInfo(PubInfo pubInfo, Long psnId);

  List<ConstPubType> getAllPubType();

  Set<PubSituationBean> buildSitationList(List<String> sitations, Long dupPubId);

  String updateSnsPub(Long dupPubId, Long psnId, Long pdwhPubId, Integer pubType, Set<PubSituationBean> situationList);

}
