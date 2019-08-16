package com.smate.center.open.service.snscode;


/**
 * IRIS业务系统关联验证码接口.
 * 
 * @author pwl
 * 
 */
public interface IrisSnsCodeService {

  public void saveIrisSnsCode(String guid, Long psnId, String code) throws Exception;

}
