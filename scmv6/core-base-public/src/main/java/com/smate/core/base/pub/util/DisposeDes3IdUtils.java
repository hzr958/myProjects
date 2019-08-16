package com.smate.core.base.pub.util;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import org.codehaus.plexus.util.StringUtils;

public class DisposeDes3IdUtils {

  /**
   * 处理id与加密des3Id 1.id有直接返回 2.des3Id为空，返回空id 3.id无，des3Id有，进行解密
   * 
   * @param id
   * @param des3Id
   * @return
   */
  public static Long disposeDes3Id(Long id, String des3Id) {
    if (!NumberUtils.isNullOrZero(id)) {
      return id;
    }
    if (StringUtils.isEmpty(des3Id)) {
      return null;
    }
    try {
        id = Long.valueOf(Des3Utils.decodeFromDes3(des3Id));
    } catch (Exception e) {
        id = null;
    }
    return id;
  }
  
}
