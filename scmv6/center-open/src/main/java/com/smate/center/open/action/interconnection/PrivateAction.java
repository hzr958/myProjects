package com.smate.center.open.action.interconnection;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.core.base.utils.string.JnlFormateUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


/**
 * 私密链接 只给来清楚 第3方系统 刷新成果的缓存限制用
 * 
 * @author tsz
 *
 */
public class PrivateAction extends ActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = -4513498781483065562L;

  private static final String PRIVATE_DEL_FLAG = "IOKJWBHYWE2";

  @Autowired
  private OpenCacheService openCacheService;

  private String openId;

  private String delFlag;

  private String groupCode;

  /*
   * 清理缓存
   */
  @Action("/open/interconnection/delcache")
  public String delCache() {

    Map<String, String> map = new HashMap<String, String>();
    try {
      if (PRIVATE_DEL_FLAG.equals(delFlag)) {
        openCacheService.remove(OpenConsts.UNION_REFRESH_PUB_CACHE, openId);
        openCacheService.remove(OpenConsts.UNION_REFRESH_GROUP_PUB_CACHE, groupCode);
        map.put("result", "sucess");
        map.put("MSG", "清理成功");
      } else {
        map.put("MSG", "错误的标记");
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("MSG", "清理出现错误");
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public String getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(String delFlag) {
    this.delFlag = delFlag;
  }

  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  public static void main(String[] args) {

    String jnameA ="Ecos - %26 A Review of Conservation  &amp;";
    jnameA = StringEscapeUtils.unescapeHtml(jnameA);
    jnameA = URLDecoder.decode(jnameA);
    String jnameB ="ecosA ReviewOFConservation";
    boolean b = matchJname(jnameA, jnameB);
    System.out.println(b);

  }

  private  static  boolean matchJname(String jnameA, String jnameB){
    if(StringUtils.isBlank(jnameA) || StringUtils.isBlank(jnameB)){
      return false ;
    }
    jnameA = JnlFormateUtils.getStrAlias(jnameA);
    jnameB = JnlFormateUtils.getStrAlias(jnameB);
    if(jnameA.contains(jnameB)){
      return true ;
    }
    if(jnameB.contains(jnameA)){
      return true ;
    }
    return false ;
  }
}
