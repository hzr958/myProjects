package com.smate.center.task.v8pub.enums;

import java.util.EnumSet;
import org.springframework.util.StringUtils;

/**
 * 成果收录库枚举
 * 
 * @author houchuanjie
 * @date 2018/05/30 17:38
 */
public enum PubLibraryEnum {
  SCIE("SCIE"), SSCI("SSCI"), EI("EI"), ISTP("ISTP"), CSSCI("CSSCI"), PKU("PKU"), OTHER("OTHER");

  public String desc;

  private PubLibraryEnum(String desc) {
    this.desc = desc;
  }

  /**
   * 将数值转换为PubThesisDegreeEnum，如果给定数值无法匹配枚举中的任意一个，则返回{ {@link PubThesisDegreeEnum#OTHER}},
   * 如果给数值为null或0，则返回null
   * 
   * @param val
   * @return
   */
  public static PubLibraryEnum parse(String val) {
    if (StringUtils.isEmpty(val)) {
      return OTHER;
    }
    if (val.equalsIgnoreCase("SCI")) {
      return SCIE;
    }
    EnumSet<PubLibraryEnum> enumSet = EnumSet.allOf(PubLibraryEnum.class);
    return enumSet.stream().filter(e -> e.desc.equalsIgnoreCase(val)).findFirst().orElse(OTHER);
  }



}
