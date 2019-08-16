package com.smate.web.management.service.grp;


import com.smate.web.management.model.grp.GrpItemInfo;
import com.smate.web.management.model.grp.GrpManageForm;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 群组管理serv
 */
public interface GrpManageService {

  /**
   * 查询群组列表
   * @param form
   */
  public void  findGrpList(GrpManageForm form);



  /**
   *
   *  resutlMap.put("list", "");      项目列表
   *  resutlMap.put("warnmsg", "");   错误信息
   *  resutlMap.put("count", 0);      条数
   * @return
   */
  public Map<String ,Object> extractFileData(File file,String sourceType , String sourceFileFileName);


  /**
   * 构建待导入的群组信息
   * @param form
   * @param itemInfos
   */
  public  void buildPendingGrpInfos(GrpManageForm form ,List<GrpItemInfo> itemInfos) ;

  /**
   * 保存待导入的群组信息
   * @param form
   */
  public  void savePendingGrpInfos(GrpManageForm form) ;


  /**
   * 检索成果
   * @param form
   */
  public  void  findPdwhPub(GrpManageForm form) ;

  /**
   *
   * @param form
   */
  public void  importPdwhPubToGrp(GrpManageForm form) ;

}
