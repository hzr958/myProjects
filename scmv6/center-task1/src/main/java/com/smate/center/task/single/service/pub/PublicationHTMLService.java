package com.smate.center.task.single.service.pub;



/**
 * 生成成果HTML内容服务<用模版构建的方式生成HTML以便解除后台逻辑代码和前台JS事件的绑定>_SCM-5988.
 * 
 * @author mjg
 * 
 */
public interface PublicationHTMLService {

  /**
   * 构建无全文的成果显示内容.
   * 
   * @param form
   * @return
   */
  String buildPubShowCellNoFull(DynRecommendThesisForm pubForm) throws Exception;
}
