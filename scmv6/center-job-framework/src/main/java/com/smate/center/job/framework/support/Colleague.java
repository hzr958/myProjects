package com.smate.center.job.framework.support;

/**
 * 同事
 *
 * @author Created by hcj
 * @date 2018/07/19 15:08
 */
public interface Colleague<T> {

  boolean execute(T msg);
}
