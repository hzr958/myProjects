package com.smate.sie.web.application.service.validate;

/**
 * 科研验证调接口参数需要加密解密.
 * 
 * @author sjzhou
 *
 */
public interface KpiCodecService {

    public String encode(String token, String content);

    public String decode(String token, String content);
}
