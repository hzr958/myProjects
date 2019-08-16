package com.smate.core.base.utils.exception;

/**
 * 图片信息异常类
 * 
 * @author houchuanjie
 * @date 2018年1月13日 上午11:14:15
 */
public class ImageInfoException extends Exception {
  private static final long serialVersionUID = -7728157494071475754L;

  public ImageInfoException() {
    super();
  }

  public ImageInfoException(String msg) {
    super(msg);
  }

  public ImageInfoException(Throwable e) {
    super(e);
  }
}
