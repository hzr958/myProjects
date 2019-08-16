package com.smate.core.base.utils.security;

/**
 * 远程调用使用.
 * 
 * @author yamingd
 * 
 */
public class TheadLocalPsnId {
  private static ThreadLocal<Long> psnIdSlot = new ThreadLocal<Long>();

  public static long getPsnId() {
    try {
      return psnIdSlot.get();
    } catch (Exception e) {
      return 0L;
    }

  }

  public static void setPsnId(long psnId) {
    psnIdSlot.set(psnId);
  }
}
