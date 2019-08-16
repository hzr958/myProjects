package com.smate.sie.core.base.utils.random;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * 产生20位不重复的随机字符串
 * 
 * @author ztg
 *
 */
public class RandomStrUtil {

  private static LinkedHashSet<String> randomSets = new LinkedHashSet<String>();

  /**
   * 生成20位不重复随机数
   * 
   * @return
   * @throws Exception
   */
  public static String gainRandomStr() {
    synchronized (randomSets) {
      String resultStr = "";
      try {
        long currentTime = 0L;
        long lastTime = 0L;
        // 调用生成id方法
        LinkedList<String> ranDomList = getGuid();
        String ranDomResult = ranDomList.getLast();
        currentTime = Long.valueOf(ranDomList.getFirst());
        if (randomSets.contains(ranDomResult)) {
          ranDomList = getGuid();
          ranDomResult = ranDomList.getLast();
          currentTime = Long.valueOf(ranDomList.getFirst());
        } else if (randomSets.contains(ranDomResult)) {
          throw new Exception("出现重复字符串!");
        } else {
          if (randomSets.size() > 3000 && currentTime > lastTime) {
            randomSets.clear();
            randomSets.add(ranDomResult);
            resultStr = ranDomResult;
            lastTime = Long.valueOf(ranDomList.getFirst());
          } else {
            randomSets.add(ranDomResult);
            resultStr = ranDomResult;
            lastTime = Long.valueOf(ranDomList.getFirst());
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return resultStr;
    }
  }

  private static LinkedList<String> getGuid() {
    String prefixStr = generateWord();
    String info = System.currentTimeMillis() + "";
    LinkedList<String> randomStrList = new LinkedList<>();
    randomStrList.add(info);// 第一个值存 当前时间戳
    randomStrList.add(prefixStr + info); // 第二个值存 当前生成的随机字符串
    return randomStrList;
  }

  @SuppressWarnings("rawtypes")
  private static String generateWord() {
    String[] beforeShuffle = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
        "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    List list = Arrays.asList(beforeShuffle);
    Collections.shuffle(list); // 随机打散
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < list.size(); i++) {
      sb.append(list.get(i));
    }
    String afterShuffle = sb.toString();
    String result = afterShuffle.substring(2, 9);// 截取7位作为结果返回
    return result;
  }
}
