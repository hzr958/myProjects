package com.smate.center.batch.util.pub;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态中的一些常量定义.
 * 
 * @author chenxiangrong
 * 
 */
public class DynamicConstant {

  public static final int RES_TYPE_NORMAL = 0;// 普通动态
  public static final int RES_TYPE_PUB = 1;// 成果
  public static final int RES_TYPE_REF = 2;// 文献
  public static final int RES_TYPE_FILE = 3;// 文件
  public static final int RES_TYPE_PRJ = 4;// 项目
  public static final int RES_TYPE_COURSE = 5;// 课件
  public static final int RES_TYPE_PSNRESUME = 6;// 个人主页
  public static final int RES_TYPE_FRIEND = 7;// 好友
  public static final int RES_TYPE_GROUP = 8;// 群组
  public static final int RES_TYPE_RESUME = 9;// 简历
  public static final int RES_TYPE_HELP = 10;// 帮助中心
  public static final int RES_TYPE_FUND = 11;// 基金推荐
  public static final int RES_TYPE_KEY = 12;// 关键词投票
  public static final int RES_TYPE_JOURNAL = 13;// 期刊推荐
  public static final int RES_TYPE_HOTKEY = 14;// 热点关键词
  public static final int RES_TYPE_JOURNALVOTE = 15;// 期刊投票
  public static final int RES_TYPE_PROBLEM = 16;// 基金申请常见问题自测
  public static final int RES_TYPE_CITETOOL = 17;// 引文工具
  public static final int RES_TYPE_SREGION = 18;// 领域推荐
  public static final int RES_TYPE_WORK = 19;// 工作经历
  public static final int RES_TYPE_EDU = 20;// 教育经历
  public static final int RES_TYPE_RESEARCH = 21;// 研究领域
  public static final int RES_TYPE_PUB_PDWH = 22;// 基准库成果
  public static final int RES_TYPE_KEYWORD = 23;// 关键词
  public static final int RES_TYPE_JOURNALAWARD = 24;// 期刊赞

  // 分享统计，需要记录的类型（有拥有者的类型，基本需要记录）
  public static final Map<Integer, Integer> SHARE_TYPE_MAP = new HashMap<Integer, Integer>();
  static {
    SHARE_TYPE_MAP.put(RES_TYPE_NORMAL, RES_TYPE_NORMAL);
    SHARE_TYPE_MAP.put(RES_TYPE_PUB, RES_TYPE_PUB);
    SHARE_TYPE_MAP.put(RES_TYPE_REF, RES_TYPE_REF);
    SHARE_TYPE_MAP.put(RES_TYPE_FILE, RES_TYPE_FILE);
    SHARE_TYPE_MAP.put(RES_TYPE_PRJ, RES_TYPE_PRJ);
    SHARE_TYPE_MAP.put(RES_TYPE_COURSE, RES_TYPE_COURSE);
    SHARE_TYPE_MAP.put(RES_TYPE_PSNRESUME, RES_TYPE_PSNRESUME);
    SHARE_TYPE_MAP.put(RES_TYPE_GROUP, RES_TYPE_GROUP);
    SHARE_TYPE_MAP.put(RES_TYPE_RESUME, RES_TYPE_RESUME);
  }

  public static final int DYNMSG_TEMPLATE_NORMALNEW = 1;// 普通发布新鲜事模板
  public static final int DYNMSG_TEMPLATE_EXTENDNEW = 2;// 扩展型发布新鲜事模板
  public static final int DYNMSG_TEMPLATE_ADDRES = 3;// 添加资源动态模板
  public static final int DYNMSG_TEMPLATE_EDITRESUME = 4;// 修改个人基本信息动态模板
  public static final int DYNMSG_TEMPLATE_ADDFRIEND = 5;// 添加好友动态模板
  public static final int DYNMSG_TEMPLATE_JOINGROUP = 6;// 加入群组动态模板
  public static final int DYNMSG_TEMPLATE_EDITGROUP = 7;// 修改群组动态模板
  public static final int DYNMSG_TEMPLATE_FORWARDRES = 8;// 转发资源动态模板
  public static final int DYNMSG_TEMPLATE_SHAREEXT = 9;// 资源分享动态模板
  public static final int DYNMSG_TEMPLATE_SHAREAPP = 10;// 应用分享动态模板
  public static final int DYNMSG_TEMPLATE_SHARERESUME = 11;// 个人简历分享动态模板
  public static final int DYNMSG_TEMPLATE_SHAREGROUP = 12;// 群组分享动态模板
  public static final int DYNMSG_TEMPLATE_AWARDRES = 13;// 资源赞动态模板
  public static final int DYNMSG_TEMPLATE_REPLYRES = 14;// 资源评论动态模板
  public static final int DYNMSG_TEMPLATE_REPLYFRIEND = 15;// 好友评论动态模板
  public static final int DYNMSG_TEMPLATE_EDITRESUME_COMPLEX = 16;// 综合修改个人基本信息动态模板
  public static final int DYNMSG_TEMPLATE_ADDFRIEND_COMPLEX = 17;// 综合添加好友动态模板
  public static final int DYNMSG_TEMPLATE_JOINGROUP_COMPLEX = 18;// 综合加入群组动态模板
  public static final int DYNMSG_TEMPLATE_SHAREEXT_COMPLEX = 19;// 综合资源分享动态模板
  public static final int DYNMSG_TEMPLATE_SHAREAPP_COMPLEX = 20;// 综合应用分享动态模板
  public static final int DYNMSG_TEMPLATE_SHARERESUME_COMPLEX = 21;// 综合个人简历分享动态模板
  public static final int DYNMSG_TEMPLATE_SHAREGROUP_COMPLEX = 22;// 综合群组分享动态模板
  public static final int DYNMSG_TEMPLATE_AWARDRES_COMPLEX = 23;// 综合资源赞动态模板
  public static final int DYNMSG_TEMPLATE_REPLYRES_COMPLEX = 24;// 综合资源评论动态模板
  public static final int DYNMSG_TEMPLATE_REPLYFRIEND_COMPLEX = 25;// 综合好友评论动态模板
  public static final int DYNMSG_TEMPLATE_EDITGROUP_COMPLEX = 26;// 综合修改群组动态模板
  public static final int DYNMSG_TEMPLATE_EDITWORKEXP = 27;// 修改个人工作经历
  public static final int DYNMSG_TEMPLATE_EDITEDUEXP = 28;// 修改个人教育经历
  public static final int DYNMSG_TEMPLATE_EDITRESEARCH = 29;// 修改个人研究领域
  public static final int DYNMSG_TEMPLATE_ENDORSERESEARCH = 30;// 认可研究领域
  public static final int DYNMSG_TEMPLATE_ENDORSERESEARCH_COMPLEX = 31;// 认可研究领域
  public static final int DYNMSG_TEMPLATE_UPLODFULLTEXT = 32;// 上传全文
  public static final int DYNMSG_TEMPLATE_SHAREEXT_PDWH = 33;// 分享基准库成果
  public static final int DYNMSG_TEMPLATE_SHAREEXT_PDWH_COMPLEX = 34;// 综合基准库成果分享动态模板
  public static final int DYNMSG_TEMPLATE_EDITKEYWORD = 35;// 修改个人关键词

  public static final int DYN_TYPE_ADD = 111;// 添加类型动态
  public static final int DYN_TYPE_EDIT = 222;// 修改类型动态
  public static final int DYN_TYPE_FORWARD = 333;// 转发类型动态
  public static final int DYN_TYPE_SHARE = 444;// 分享类型动态
  public static final int DYN_TYPE_AWARD = 555;// 赞类型动态
  public static final int DYN_TYPE_REPLY = 666;// 评论类型动态
  public static final int DYN_TYPE_ENDORSE = 777;// 认可
  public static final int DYN_TYPE_UPLOAD = 888;// 上传全文
  public static final int DYN_TYPE_SCM_RECOMD = 999;// 科研之友推荐类型动态

  public static final String DYN_SETTING_ADDFRIEND = "vAddFrd";// 添加好友动态
  public static final String DYN_SETTING_AWARD = "award";// 赞动态
  public static final String DYN_SETTING_EDITRESUME = "vUProfile";// 修改个人信息
  public static final String DYN_SETTING_ADDPUB = "add_pub";// 添加成果
  public static final String DYN_SETTING_ADDREF = "vMyLiter";// 添加文献
  public static final String DYN_SETTING_ADDPRJ = "add_prj";// 添加项目
  public static final String DYN_SETTING_JOINGROUP = "vJoinGroupDyn";// 加入群组
  public static final String DYN_SETTING_SHARE = "share";// 分享动态
  public static final String DYN_SETTING_REPLY = "vSendEva";// 评论动态
  public static final String DYN_SETTING_REPLYFRIEND = "vSendEva";// 评论好友.

  public static final int PSN_RESUME_BASE = 0;// 基本信息
  public static final int PSN_RESUME_CONTACT = 1;// 联系信息
  public static final int PSN_RESUME_EDU = 2;// 教育经历
  public static final int PSN_RESUME_WORK = 3;// 工作经历
  public static final int PSN_RESUME_DISC = 4;// 研究领域
  public static final int PSN_RESUME_PROFILE = 5;// 个人简介

  public static final int GROUP_EDITTYPE_BRIEF = 0;// 群组简介
  public static final int GROUP_EDITTYPE_BASE = 1;// 群组设置
  public static final int GROUP_EDITTYPE_PAGE = 2;// 群组主页
  public static final int GROUP_EDITTYPE_NOTICE = 3;// 群组公告
  public static final int GROUP_EDITTYPE_GOAL = 4;// 课程目标

  public static final int DYN_PERMISSION_ALL = 0;// 所有人可见
  public static final int DYN_PERMISSION_FRIEND = 1;// 好友可见
  public static final int DYN_PERMISSION_ME = 2;// 仅本人可见

  public static final int DYN_VISIBLE_TRUE = 0;// 可见
  public static final int DYN_VISIBLE_FALSE = 1;// 不可见

  public static final int DYN_RELATION_ME = 0;// 本人
  public static final int DYN_RELATION_FRIEND = 1;// 好友
  public static final int DYN_RELATION_ATTENTION = 2;// 关注
  public static final int DYN_RELATION_FANDA = 3;// 好友及关注
  public static final int DYN_RELATION_NO = 4;// 没有直接关系

  public static final int QUERYDYN_TYPE_PSNALL = 0;// 个人中心所有动态
  public static final int QUERYDYN_TYPE_PSNME = 1;// 个人中心与我有关动态
  public static final int QUERYDYN_TYPE_PSNNEW = 2;// 个人中心新鲜事动态
  public static final int QUERYDYN_TYPE_PSNFRIEND = 3;// 个人中心好友动态
  public static final int QUERYDYN_TYPE_GROUPALL = 4;// 群组所有动态
  public static final int QUERYDYN_TYPE_GROUPNEW = 5;// 群组新鲜事动态
  public static final int QUERYDYN_TYPE_PSNUSER = 6;// 加载某人的动态

  // 以下为动态缓存的名称和KEY值.
  public static final String CACHE_DYN_RECOMMEND_KEY = "cache_rec_dyn_key";// SCM-推荐动态
  public static final String CACHE_DYN_RECOMMEND_NAME = "cache_rec_dyn";// SCM-推荐动态

  public static final String CACHE_DYN_PSN_KEY = "cache_dyn_key";// SCM-个人动态.
  public static final String CACHE_DYN_PSN_NAME = "cache_psn_dynamic";// SCM-个人动态.

  public static final String DYN_CONTENT_CACHE_NAME = "dyn_content_cache_name";// 动态内容cache
  // name.
  public static final String DYN_CONTENT_CACHE_KEY = "dyn_content_cache_key";// 动态内容cache
  // key.
}
