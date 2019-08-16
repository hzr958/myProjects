package com.smate.web.v8pub.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 其他成果类型信息
 * 
 * @author houchuanjie
 * @date 2018/05/31 14:09
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler", "OtherInfoDTO"},
    ignoreUnknown = true)
public class OtherInfoDTO extends PubTypeInfoDTO implements Serializable {

  private static final long serialVersionUID = 3971572749417723547L;

}
