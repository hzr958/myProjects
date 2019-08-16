package com.smate.web.dyn.service.dynamic.group;

/**
 * 群组动态 类型于 模版的对应枚举
 * 
 * @author tsz
 *
 */
public enum GroupDynamicTemplateEnum {

  SHARE("group_dyn_A3_template_share"), // 分享 普通分享
  SHAREPUB("group_dyn_A3_template_sharepub"), // 分享 成果
  ADDPUB("group_dyn_A2_template_addpub"), // 新增资源 成果
  ADDFILE("group_dyn_A2_template_addfile"), // 新增资源 文件
  PUBLISHDYN("group_dyn_A1_template_new"), // 发布新动态

  // 新群组动态模版
  GRP_SHARE("grp_dyn_A3_template_share"), // 分享 普通分享
  GRP_SHAREPUB("grp_dyn_A3_template_sharepub"), // 分享 成果
  GRP_SHAREFILE("grp_dyn_A3_template_sharefile"), // 分享文件
  GRP_ADDPUB("grp_dyn_A2_template_addpub"), // 新增资源 成果
  GRP_ADDFILE("grp_dyn_A2_template_addfile"), // 新增资源 文件
  GRP_ADDCOURSE("grp_dyn_A2_template_addcourse"), // 新增资源 课件
  GRP_ADDWORK("grp_dyn_A2_template_addwork"), // 新增资源 文件 作业
  GRP_PUBLISHDYN("grp_dyn_A1_template_new"), // 发布新动态
  GRP_SHAREFUND("group_dyn_A3_template_sharefund"), // 分享 基金
  GRP_SHAREAGENCY("group_dyn_A3_template_shareagency"), // 分享 资助机构
  GRP_SHAREPDWHPUB("grp_dyn_A3_template_sharepdwhpub"), // 分享基准库成果
  GRP_SHAREPRJ("grp_dyn_A3_template_shareprj"), // 分享项目
  GRP_SHARENEWS("group_dyn_A3_template_sharenews"); // 分享 新闻

  // 定义私有变量
  private String module;

  // 构造函数，枚举类型只能为私有
  private GroupDynamicTemplateEnum(String module) {
    this.module = module;
  }

  public String toString() { // 定义一个实例成员函数
    return module;
  }

}
