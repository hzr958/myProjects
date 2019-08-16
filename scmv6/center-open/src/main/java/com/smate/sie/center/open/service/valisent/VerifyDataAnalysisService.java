package com.smate.sie.center.open.service.valisent;

import java.util.List;
import java.util.Map;

import com.smate.sie.core.base.utils.model.validate.KpiValidateMain;

/**
 * 科研验证数据加工服务
 * 
 * @author hd
 *
 */
public interface VerifyDataAnalysisService {
  /**
   * 保存数据到科研验证主表
   * 
   * @param paramet
   * @return
   */
  public String saveMain(Map<String, Object> paramet);

  /**
   * 保存数据到科研验证详细信息表
   * 
   * @param content 数据
   * @param keyCode 内部唯一标识
   * @param uuid 主表主键
   * @param fromKeyCode 业务系统传的唯一标识
   * @return
   */
  public Map<String, Object> doDataAnalysis(Map<String, Object> content, String uuid, String fromKeyCode);

  /**
   * 拆分人员信息
   * 
   * @param psns
   * @param keyCode
   * @param uuid
   * @return
   */
  public String spiltPsnIntoDetail(List<Map<String, Object>> psns, String uuid);

  /**
   * 拆分项目成果信息
   * 
   * @param pubs
   * @param keyCode
   * @param uuid
   * @param participantNames
   */
  public void spiltPubIntoDetail(List<Map<String, Object>> pubs, String uuid, String participantNames,
      Boolean emptyPrjCode);

  /**
   * 拆分机构信息
   * 
   * @param org
   * @param keyCode
   * @param uuid
   */
  public void spiltOrgIntoDetail(List<Map<String, Object>> org, String uuid);

  /**
   * 数据校验
   * 
   * @param content
   * @param fromKeyCode
   * @return
   */
  public Map<String, Object> doDataValidate(Map<String, Object> content, String fromKeyCode);

  /**
   * 更新main、mian_data表，删除detail、log表相应数据
   * 
   * @param valiMain
   * @param title
   * @param dataFrom
   * @param priority
   * @param status
   * @param prpYear
   * @param orgName
   * @param keyCode
   * @param keyType
   * @param versionNo
   */
  public void doUpdate(KpiValidateMain valiMain, String title, String dataFrom, Integer priority, Integer status,
      String prpYear, String orgName, String keyCode, String keyType, String versionNo);

}
