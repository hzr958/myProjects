package com.smate.center.batch.connector.enums;

/**
 * 服务码,与open配置保持统一
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @param job
 */
public enum BatchOpenCodeEnum {

  INPUT_FULLTEXT_TASK("a0t6c3ne"), // 导入 isi成果 全文
  THIRD_PSN_BASE_INFO("965eb72c"), // 个人基本信息服务
  THIRD_WECHAT_PSN_MSG("d0e80eb2"), // 开放数据 保存 个人微信消息
  THIRD_WECHAT_PUBLIC_MSG("b7303c9f"), // 开放数据 保存 公共微信消息
  THIRD_PUSH_PROJECT_INFO("80e6e33e"), // 项目接收
  WECHAT_ACCESS_TOKEN("7c630c84"), // 获取 微信 交互授权码
  // 成果相关任务码
  SIMPLE_PUB_ADD_SAVE("87eff869"), // 成果新增保存任务码
  SIMPLE_PUB_EDIT_SAVE("ba59abbe"), // 成果编辑保存任务码
  SIMPLE_PUB_DELETE("ee747f8b"), // 成果删除任务码
  SIMPLE_PUB_IMPORT("dxvd63av"), // 成果检索导入
  SIMPLE_PUB_CONFIRM("87ef0869"), // 成果认领

  SIMPLE_PUB_LOW_SAVE("87eff870"), // 成果新增or编辑低优先级任务的任务码
  SIMPLE_PUB_LOW_DELETE("ee747f8c"), // 成果删除低优先级任务的任务码

  PUB_ASSIGN_FOR_ROL("67pub8a9"), // 为ROL从为导入基准库的成果进行成果指派操作
  PUB_ASSIGN_FOR_ROL1("67pub8b1"), // 具体处理pub算分和指派

  SIMPLE_PSN_REGISTER("cetvceq6"), // 人员注册处理任务

  REGISTER_PDWH_PUB_MATCH("regpuba1"), // 新注册用户PDWH匹配成果
  REGISTER_RCMD_SYN_PSN_INFO("reginfo1"), // 注册时冗余用户信息至RCMD
  REGISTER_INVITATION_HANDLE("reginfo2"), // 注册时处理friendInvitation ||
  // groupInvitation
  REGISTER_COMPLETENESS_REFRESH("reginfo3"), // 更新注册人员信息完整度，查找或创建个人主页url
  REGISTER_PSN_HTML("reginfo4"), // 人员注册时，psnHtml列表需要处理的数据
  AddPsnIndex("addindx1"), // 老系统人员注册是，为psn添加index这样在首页就能够搜索到这个人

  SYSNC_GROUP_INFO("syncgro1"), // 同步群组信息到群组成员
  SYSNC_GROUP_PSN_TO_SNS("syncgro2"), // a、同步群组信息
  SYSNC_GROUP_INVITE_PSN_TO_SNS("syncgro3"), // b、同步群组成员（传递GroupPsn对象和invite_psn_id）
  SYSNC_FOR_ALL_GROUP_UPDATE_TO_ROL("syncgro4"), // c、群组信息和成员有变动，同步到ROL(合作分析)(传递GroupPsn对象)
  SYSNC_GROUP_STATISTICS("syncgro5"), // d、更新人员统计表群组数
  SYSNC_RCMD_SYNC_INFO("syncgro6"), // e、群组信息和成员变动，同步到推荐服务

  GROUP_PUB_RECALCULATE("gruppub1"), // 群组信息改变后，更新群组成果信息：1计算群组成果与群组的关键词相同数（相关性），2成果重新标注（项目群组批准号与成果基金资助信息）
  PSN_AVATARS_SYNC("avatars1"), // 同步人员头像信息,姓名
  ATEENTION_DYNAMIC("attn2dyn"), // 同步关注好友动态
  REFRESH_PSN_SOLR_INFO("oaftiz2b"), // 更新人员solr信息
  PRODUCE_THUMBNAIL_IMAGE_STRATEGY("abcdefgh"), // 生成缩略图任务strategy码："abcdefgh"
  REFRESH_PSN_CONFIG_INFO("rpcxit26"), // 更新人员配置信息
  MATCH_PDWHPUB_ADRR_AUTHOR("matpdpba"), // 基准库成果地址作者匹配
  SAVE_PUB_FROM_CROSSREF("crossref"); // crossref数据保存至基准库

  private String value;

  private BatchOpenCodeEnum(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }

}
