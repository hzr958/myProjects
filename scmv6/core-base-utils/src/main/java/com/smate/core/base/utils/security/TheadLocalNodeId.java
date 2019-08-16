package com.smate.core.base.utils.security;

import java.util.List;

/**
 * 保存当前服务器的NodeId，当前用户的NODE ID.
 * 
 * @author liqinghua
 * 
 */
public class TheadLocalNodeId {
  private static List<Integer> nodeId;
  private static ThreadLocal<Integer> currentUserNodeId = new ThreadLocal<Integer>();
  private static ThreadLocal<Boolean> isUserFirst = new ThreadLocal<Boolean>();

  public static List<Integer> getNodeId() {
    return nodeId;
  }

  public static void setNodeId(List<Integer> nodeid) {
    nodeId = nodeid;
  }

  /**
   * 当前用户的节点ID.
   * 
   * @return
   */
  public static Integer getCurrentUserNodeId() {
    return currentUserNodeId.get();
  }

  public static void setCurrentUserNodeId(Integer curNodeId) {
    currentUserNodeId.set(curNodeId);
  }

  public static void setIsFirst(boolean isFirst) {
    isUserFirst.set(isFirst);
  }

  public static boolean getIsFirst() {
    return isUserFirst.get() == null ? false : isUserFirst.get();
  }


}
