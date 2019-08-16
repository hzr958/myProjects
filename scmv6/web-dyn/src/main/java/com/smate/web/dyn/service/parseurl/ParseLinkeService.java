package com.smate.web.dyn.service.parseurl;

import java.io.IOException;
import java.util.Map;

/**
 * 解析url
 * 
 * @author zzx
 *
 */
public interface ParseLinkeService {
  /**
   * 通过url获取分享的title和image
   * 
   * @param shareUrl
   * @return
   * @throws IOException
   */
  public Map<String, String> resolveUrl(String shareUrl) throws IOException;
}
