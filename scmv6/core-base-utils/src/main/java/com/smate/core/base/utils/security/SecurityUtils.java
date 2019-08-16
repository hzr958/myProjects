package com.smate.core.base.utils.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.model.UserInfo;

/**
 * Spring Security的工具类.
 * 
 * 
 */
public class SecurityUtils {

  private SecurityUtils() {}

  /**
   * 取得当前用户的登录的UserId ,如果当前用户未登录则返回0.
   */
  public static Long getCurrentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Long psnId = 0L;
    if (auth == null) {
      // 远程调用
      psnId = TheadLocalPsnId.getPsnId();
      if (psnId == null) {
        psnId = 0L;
      }
      return psnId;
    }
    if (auth == null || auth.getName().equalsIgnoreCase("anonymousUser")) {
      // 远程调用
      psnId = TheadLocalPsnId.getPsnId();
      if (psnId == null) {
        psnId = 0L;
      }
    } else {
      if (auth.getName() != null && auth.getName() != "") {
        psnId = new Long(auth.getName());
      } else {
        OauthsAuthenticationToken auth2 = (OauthsAuthenticationToken) auth;
        if (auth2 != null) {
          UserInfo ud = (UserInfo) auth2.getUserDetails();
          psnId = new Long(ud.getUsername());
        }
      }

    }

    return psnId;
  }

  /**
   * 获得用户当前的节点id，对应存储的位置为cas端的sys_user表node_id. 请使用常量类 ServiceConstants.SCHOLAR_NODE_ID_1
   * 
   * @return 节点位置.
   */
  @Deprecated
  public static int getCurrentUserNodeId() {
    // Authentication auth =
    // SecurityContextHolder.getContext().getAuthentication();
    // Integer nodeId = null;
    // if (auth == null) {
    // nodeId = TheadLocalNodeId.getCurrentUserNodeId();
    // } else if (auth.getName().equalsIgnoreCase("anonymousUser")) {
    // nodeId = 0;
    // } else {
    // UserInfo user = (UserInfo) auth.getPrincipal();
    // nodeId = user.getNodeId();
    // }
    // return nodeId == null ? 0 : nodeId;
    return ServiceConstants.SCHOLAR_NODE_ID_1;

  }

  /**
   * 获得用户当前的角色id，部分程序要求区分登录时选择的角色.. 请使用常量类 ServiceConstants.SCHOLAR_NODE_ID_1
   * 
   * @return 节点位置.
   */
  @Deprecated
  public static int getCurrentUserRoleId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Integer roleId = null;
    if (auth == null) {
      roleId = TheadLocalRoleId.getRoleId();
    } else if (auth.getName().equalsIgnoreCase("anonymousUser")) {
      roleId = 0;
    } else {
      UserInfo user = (UserInfo) auth.getPrincipal();
      roleId = user.getRoleId();
    }
    return roleId == null ? 0 : roleId;

  }

  /**
   * 获得用户当前的部门id.
   * 
   * @return
   */
  public static Long getCurrentUnitId() {

    Long unitId = TheadLocalUnitId.getUnitId();

    return unitId == null ? 0L : unitId;
  }

  /**
   * 判断用户是否是未在本程序等级的临时授权.
   * 
   * @return
   */
  public static boolean isAuthority() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getAuthorities().size() > 0) {
      Iterator<GrantedAuthority> iter = (Iterator<GrantedAuthority>) auth.getAuthorities().iterator();
      while (iter.hasNext()) {
        GrantedAuthority ga = iter.next();
        if ("A_AUTHORITY".equals(ga.getAuthority())) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 取得当前用户的登录的InsId ,如果当前用户未登录则返回0.
   */
  public static Long getCurrentInsId() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      return null;
    }
    Object userDetail = auth.getPrincipal();
    return ((UserInfo) userDetail).getInsId();
  }

  /**
   * 获取当前站点的所有节点.
   * 
   * @return
   */
  @Deprecated
  public static List<Integer> getCurrentAllNodeId() {
    List<Integer> nodes = new ArrayList<Integer>();
    nodes.add(ServiceConstants.SCHOLAR_NODE_ID_1);
    return nodes;
  }

  /**
   * 获取当前服务器的模块ID.
   * 
   * @return
   */
  public static Long getCurrentMdId() {
    return TheadLocalModuleId.getMdId();
  }

  /**
   * 获取当前服务器的Web服务ID.
   * 
   * @return
   */
  public static Long getCurrentWsId() {
    return TheadLocalWebServerId.getWsId();
  }

  /**
   * 获取当前服务器的URL Id.
   * 
   * @return
   */
  public static Long getCurrentUrlId() {
    return TheadLocalUrlId.getUrlId();
  }

  /**
   * sessionid.
   * 
   * @return
   */
  public static String getSessionId() {
    return TheadLocalSessionId.getSessionId();
  }
}
