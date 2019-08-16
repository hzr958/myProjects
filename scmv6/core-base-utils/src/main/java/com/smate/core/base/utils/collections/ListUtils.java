package com.smate.core.base.utils.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * List集合工具类
 * 
 * @author houchuanjie
 * @date 2018/05/11 09:52
 */
public class ListUtils {

  /**
   * List分割，按照指定大小进行分割，返回分割后的List数组
   * 
   * @param list 原始list
   * @param splitSize 分割大小
   * @param <E> 任意对象类型
   * @return 返回分割后的List数组，如果{@code list = null}，返回{@code null}
   */
  public static final <E> List<E>[] split(final List<E> list, int splitSize) {
    if (list == null) {
      return null;
    }
    int total = list.size();
    final List<E> clonedList = new ArrayList<>(list);
    int cnt = total / splitSize;
    if (total % splitSize > 0) {
      cnt++;
    }
    List[] lists = new List[cnt];
    for (int i = 0; i < cnt; i++) {
      int from = i * splitSize;
      int to = (i + 1) * splitSize;
      to = to > total ? total : to;
      lists[i] = new ArrayList(clonedList.subList(from, to));
    }
    return lists;
  }
}
