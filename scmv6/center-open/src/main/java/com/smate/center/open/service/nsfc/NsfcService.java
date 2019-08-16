package com.smate.center.open.service.nsfc;

import java.util.List;

import com.smate.center.open.model.nsfc.NsfcExpertPub;
import com.smate.center.open.model.nsfc.NsfcSyncProject;
import com.smate.center.open.model.nsfc.SyncProposalModel;
import com.smate.center.open.model.nsfc.project.NsfcPrjRptPub;
import com.smate.center.open.model.nsfc.project.NsfcProject;
import com.smate.center.open.model.nsfc.project.NsfcReschProject;
import com.smate.center.open.model.proposal.NsfcPrpPub;
import com.smate.core.base.utils.model.cas.security.SysRolUser;



/**
 * 成果在线服务
 * 
 * @author tsz
 *
 */
public interface NsfcService {


  /**
   * 同步申请书
   * 
   * @param model
   */
  public void updateSnsProposal(SyncProposalModel model) throws Exception;

  /**
   * 获取申请书成果，以XML的格式的字符串返回
   * 
   * @param prpModel
   * @throws ServiceException
   * 
   * 
   */
  public String buildPrpPubsXml(SyncProposalModel model) throws Exception;

  /**
   * 查询论著列表
   * 
   * @param model
   * @return
   * @throws Exception
   */
  List<NsfcPrpPub> loadSnsPrpPubs(SyncProposalModel model) throws Exception;

  /**
   * 通过rolId 查询 personID
   * 
   * @param sysRolUser
   * @return
   * @throws Exception
   */
  Long getSyncRolPerson(SysRolUser sysRolUser) throws Exception;

  public NsfcReschProject saveNsfcReschSyncProject(NsfcSyncProject nsfcSyncProject) throws Exception;

  public NsfcProject syncNsfcProject(NsfcSyncProject nsfcSyncProject) throws Exception;

  /**
   * 构建评议专家成果
   * 
   * @param model
   * @return
   * @throws ServiceException
   */
  String buildExpertPubsXml(SyncProposalModel model) throws Exception;


  /**
   * 加载评议专家成果
   * 
   * @param model
   * @return
   * @throws ServiceException
   */
  List<NsfcExpertPub> loadSnsExpertPubs(SyncProposalModel model) throws Exception;

  /**
   * 填充进展/结题报告成果额外属性.
   * 
   * @param pubs
   * @return
   * @throws ServiceException
   */
  List<NsfcPrjRptPub> fillPFPubsAddtlProps(List<NsfcPrjRptPub> pubs) throws Exception;



}
