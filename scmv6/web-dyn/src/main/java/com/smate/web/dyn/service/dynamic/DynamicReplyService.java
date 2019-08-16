package com.smate.web.dyn.service.dynamic;

import java.util.List;

import com.smate.web.dyn.form.dynamic.DynReplayInfo;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.reply.DynamicReplyPsn;

/**
 * 动态评论服务接口
 * 
 * @author zk
 *
 */
public interface DynamicReplyService {
  // 没全文的默认图片
  public final static String DEFAULT_PUBFULLTEXT_IMAGE = "/resscmwebsns/images_v5/images2016/file_img.jpg";
  // 有全文的默认图片
  public final static String DEFAULT_PUBFULLTEXT_IMAGE1 = "/resscmwebsns/images_v5/images2016/file_img1.jpg";

  /**
   * 动态评论
   * 
   * @param form
   * @throws Exception
   */
  public void replyDyn(DynamicForm form) throws Exception;

  /**
   * 加载动态评论
   * 
   * @param form
   * @throws Exception
   */
  public void loadDynReply(DynamicForm form) throws Exception;

  /**
   * 加载动态评论
   * 
   * @param form
   * @throws Exception
   */
  public void loadSampleDynReply(DynamicForm form) throws Exception;

  /**
   * 用于IOS客户端调用获取动态回复信息
   * 
   * @param dynReplyList
   * @return
   * @throws Exception
   */
  public List<DynReplayInfo> rebuildShowDynReply(List<DynamicReplyPsn> dynReplyList) throws Exception;
}
