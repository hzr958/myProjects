package com.smate.web.prj.exception;

import com.smate.core.base.exception.NotExistException;

/**
 * 项目资源不存在的异常
 * 
 * @author houchuanjie
 * @date 2018年3月19日 下午5:14:34
 */
public class ProjectNotExistException extends NotExistException {
  private static final long serialVersionUID = -6892917828551110526L;

  public ProjectNotExistException() {
    super("您要操作的项目资源不存在！");
  }
}
