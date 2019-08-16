package com.smate.sie.center.task.service.codec;

/**
 * task调用open接口前的加密解密
 * 
 * @author ztg
 * 
 */
public interface TaskCodecService {

  public String encode(String token, String content);

  public String decode(String token, String content);
}
