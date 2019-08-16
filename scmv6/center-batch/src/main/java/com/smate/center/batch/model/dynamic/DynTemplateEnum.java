package com.smate.center.batch.model.dynamic;

/**
 * 机构主页动态类型.
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
public enum DynTemplateEnum {
  DYN_INSPG_FILE_SHARE(101, "inspg_dyn_template_share_file"), // 新鲜事-文件
  DYN_INSPG_LINK_SHARE(102, "inspg_dyn_template_share_normal_link"), // 新鲜事&新鲜事-链接
  DYN_INSPG_IMG_UPLOAD(103, "inspg_dyn_template_photo_add"), // Photo新增
  DYN_INSPG_MEMBER_ADD(104, "inspg_dyn_template_member_add"), // Member新增
  DYN_INSPG_NEWS_ADD(105, "inspg_dyn_template_news_add");// News新增

  // 定义私有变量
  private int value;
  private String str;

  // 构造函数，枚举类型只能为私有
  private DynTemplateEnum(int value, String str) {

    this.value = value;
    this.str = str;

  }

  @Override
  public String toString() {

    return this.str;

  }

  public Integer toInt() {

    return Integer.valueOf(this.value);

  }
}
