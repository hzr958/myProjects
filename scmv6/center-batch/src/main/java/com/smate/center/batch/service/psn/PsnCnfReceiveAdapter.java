package com.smate.center.batch.service.psn;

/**
 * 个人权限消息（成果、项目、工作经历和教育经历）转换接口
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnCnfReceiveAdapter {

  PsnCnfReceive get(String type);
}
