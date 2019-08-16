package com.smate.center.batch.util.pub;

import java.io.Serializable;

/**
 * 成果详情页面来源.
 * 
 * @author pwl
 * 
 */
public interface PublicationDetailFrom extends Serializable {

  /** 普通查看. */
  public final static int PUBLICATION_LIST = 1;

  /** 成果分享. */
  public final static int PUBLICATION_SHARE = 2;

  /** 全文请求. */
  public final static int FULLTEXT_REQUEST = 3;

  /** 全文推荐. */
  public final static int FULLTEXT_RCMD = 4;

}
