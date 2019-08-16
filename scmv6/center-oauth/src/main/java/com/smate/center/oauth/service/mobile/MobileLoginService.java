package com.smate.center.oauth.service.mobile;

import java.util.Map;

import com.smate.center.oauth.exception.OauthException;
import com.smate.core.base.utils.model.shorturl.OpenShortUrl;

/**
 * 移动端登录服务接口
 *
 * @author wsn
 * @createTime 2017年6月6日 下午6:31:58
 *
 */
public interface MobileLoginService {

  /*
   * 尝试通过wxOpenId获取AID
   */
  public Map<String, Object> buildAIDByWxOpenId(String wxOpenId) throws OauthException;

  /*
   * 尝试通过微信code获取AID
   */
  public Map<String, Object> buildAIDByWxCode(String code) throws OauthException;

  /*
   * 尝试通过wxUnionId获取AID
   */
  public Map<String, Object> buildAIDByWxUnionId(String wxUnionId) throws OauthException;

  /**
   * 取消绑定
   */
  public void cancelBlind(Long psnId, String wxOpenId) throws OauthException;

  /**
   * 从微信端公众号菜单进入科研之友时,通过微信返回的code构建跳转url
   * 
   * @return
   * @throws OauthException
   */
  public String buildTargetUrlInWechat(String wxCode) throws OauthException;

  public OpenShortUrl getOpenShortUrlByShortUrl(String substring);
}
