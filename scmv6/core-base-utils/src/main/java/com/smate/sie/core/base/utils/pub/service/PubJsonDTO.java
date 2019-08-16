package com.smate.sie.core.base.utils.pub.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smate.sie.core.base.utils.pub.dom.PubTypeInfoBean;
import com.smate.sie.core.base.utils.pub.dto.PubMemberDTO;

/**
 * 成果业务传输对象(所有属性必须为对象 类型)
 * 
 * @author ZSJ
 *
 * @date 2019年2月11日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PubJsonDTO {

  public String pubHandlerName;// 调用处理器名字

  public Long pubId;

  public String des3PubId; // 加密的pubId
  public String des3PsnId; // 加密的psnId

  public Integer HCP = 0; // 高被引用文章 0未否，1为是
  public Integer HP = 0; // 热门文章 0未否，1为是
  public String OA = new String(); // Open Access

  public Long insId; // 成果所属机构id
  public String title;// 成果标题
  public String publishDate;// 发表日期
  public String publishYear;
  public String publishMonth;
  public String publishDay;
  public String fundInfo;
  public Integer citations;// 引用数
  public String citationsUpdateTime;// 引用数更新时间

  public String doi;
  public String summary;
  public String keywords = new String();// 关键字
  public String srcFulltextUrl;
  public Long fulltextId; // 全文id
  public String fulltextName; // 全文名称
  public Integer pubTypeCode;// 成果类型
  public String pubTypeName;// 成果类型
  public String disciplineCode; // 学科代码 英文分号分割
  public String disciplineName;
  public Integer isPublicCode = 1; // 1公开，0非公开
  public String isPublicName = "公开"; // 1公开，0非公开
  public Integer dataFrom;// 数据来源：0表单新增，1xls导入，2标准文件导入，3联邦检索，4基准库指派，9脚本修复

  public String authorNames;// 作者名称列表，通过成果作者构造出来的
  public String briefDesc = new String();// 成果来源
  public String organization;
  public String des3fulltextId; // 加密的全文id

  public Integer srcDbId; // 基准库当前被收录机构的dbId
  public String sourceId;
  public String sourceUrl;
  public boolean isEdit = false;// 是否页面编辑
  public boolean isImport = false;// 是否文件导入编辑
  /**
   * 成果全文 内含字段： pubId 成果id fileId 文件id fileName
   * 
   */
  public JSONObject fullText;
  public JSONObject pubTypeInfo;

  /**
   * 成果作者 字段有：email 作者邮箱 insName 所在机构 name 姓名 firstAuthor 是否第一作者 communicable 是否通讯作者
   */
  public JSONArray members;

  public List<PubMemberDTO> memberList; // 用来保存authorNames拆分出来的作者信息

  /**
   * 收录情况 内涵字段： libraryName 收录机构名 sitStatus 收录状态 0:未收录 ，1:收录 sitOriginStatus 原始收录状态 0:未收录 ，1:收录 srcUrl
   * 来源URL srcDbId 来源dbid srcId 来源唯一标识
   */
  public JSONArray situations;
  /**
   * 成果附件 内涵字段： pubId 成果id fileId 文件id fileName 文件名 permission 文件 权限 0=所有人；2=仅本人
   */
  public JSONArray pubAttachments;

  public String citedUrl; // 引用url

  public PubTypeInfoBean pubTypeInfoBean; // 成果类型bean组件

  public String lastUpdateTime; // 最后成果或专利的更新时间
}
