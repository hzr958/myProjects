package com.smate.core.base.utils.string;

import java.util.HashMap;
import java.util.Map;

import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 构造map工具.
 * 
 * @author liqinghua
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MapBuilder {

  private Map map = new HashMap();

  private MapBuilder() {

  }

  public static MapBuilder getInstance() {
    return new MapBuilder();
  }

  /**
   * put object to map.
   * 
   * @param key
   * @param value
   * @return
   */
  public MapBuilder put(Object key, Object value) {
    map.put(key, value);
    return this;
  }

  public Object get(Object key) {
    return map.get(key);
  }

  public Object remove(Object key) {
    return map.remove(key);
  }

  /**
   * get map object.
   * 
   * @return
   */
  public Map getMap() {
    return this.map;
  }

  /**
   * get map json string.
   * 
   * @return
   */
  public String getJson() {
    return JacksonUtils.jsonObjectSerializer(map);
  }

  /**
   * clear map content.
   * 
   * @return
   */
  public MapBuilder clear() {
    map.clear();
    return this;
  }
}
