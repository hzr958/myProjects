package com.smate.center.batch.util.pub;

/**
 * 成果全文下载权限.
 * 
 * @author pwl
 * 
 */
public interface PubFulltextPermission {

  /** 所有人可下载. */
  public int all = 0;

  /** 好友可下载. */
  public int friend = 1;

  /** 自己可下载. */
  public int owner = 2;

}
