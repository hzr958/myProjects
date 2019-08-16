package com.smate.center.batch.util.pub;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 研究成果报告自定义类型名字
 * 
 * @author Scy
 * 
 */
public class ConstDefTypeName implements Serializable {

  private static final long serialVersionUID = 6927653491930816013L;

  public static Map<Integer, String> defTypeNameMap = new HashMap<Integer, String>();
  static {
    // 1:代表性论著 2:全部论著
    defTypeNameMap.put(1, "代表性论著");
    defTypeNameMap.put(2, "全部论著");
  }
}
