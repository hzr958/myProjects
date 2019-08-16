package com.smate.center.batch.util.pub;

import org.springframework.util.Assert;

import com.smate.center.batch.model.sns.pub.PubSimple;

/**
 * 
 * @author apache
 *
 */
public class PubSimpleUtils {

  /**
   * return true 被删除 return false 其他
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubSimple
   * @return
   */
  public static boolean checkStatus(PubSimple pubSimple) {
    Assert.notNull(pubSimple, "PubSimple不能为空");
    return pubSimple.getStatus() == 1;
  }

  /**
   * return true 老数据表为最新数据 return false 老数据表不是最新数据
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubSimple
   * @return
   */
  public static boolean checkSimpleStatus(PubSimple pubSimple) {
    Assert.notNull(pubSimple, "PubSimple不能为空");
    return pubSimple.getSimpleStatus() == 1;
  }

  /**
   * return true:执行过任务 return false:没有执行过任务
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubSimple
   * @return
   */
  public static boolean checkSimpleTask(PubSimple pubSimple) {
    Assert.notNull(pubSimple, "PubSimple不能为空");
    return pubSimple.getSimpleTask() == 1;
  }

}
