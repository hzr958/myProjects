package com.smate.sie.core.base.utils.json.prj;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 项目业务传输对象(所有属性必须为对象 类型)
 * 
 * @author lijianming
 * @date 2019年6月3日
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectJsonDTO {

  public String prjHandlerName;// 调用处理器名字

  public Long prjId; // 项目ID号

  public String des3PrjId; // 加密的prjId
  public String des3PsnId; // 加密的psnId

  public Integer HCP = 0; // 高被引用文章 0未否，1为是
  public Integer HP = 0; // 热门文章 0未否，1为是
  public String OA = new String(); // Open Access

  public Long insId; // 项目所属机构id
  public String title;// 项目标题
  public String summary; // 摘要
  public String keywords = new String();// 关键字
  public String authorNames;// 作者名称列表，通过成果作者构造出来的
  public String disciplineCode; // 学科代码 英文分号分割
  public String disciplineName;
  public Integer isPublicCode = 1; // 1公开，0非公开
  public String isPublicName = "公开"; // 1公开，0非公开
  public Long prjFromId; // 来源Id
  public String prjFromName; // 来源名称
  public Long schemeId; // 类别Id
  public String schemeName; // 类别名称
  public String cascadeFromId; // 关联来源项目ID
  public String prjInternalNo; // 项目编号
  public String prjExterNo; // 批准号
  public String fundingYear; // 立项年度
  public String amount; // 项目金额
  public String statusCode; // 项目状态Id
  public String statusName; // 项目状态名称
  public String startDate; // 项目开始时间
  public String endDate; // 项目结束时间
  public Long fulltextId; // 全文id
  public String fulltextName; // 全文名称
  public String srcFulltextUrl; // 全文链接
  public String sourceDbCode;// 数据来源

  /**
   * 项目作者 字段有：insName 所在机构 name 姓名 firstAuthor 是否负责人
   */
  public JSONArray members;

  public List<PrjMemberDTO> memberList; // 用来保存authorNames拆分出来的作者信息

  /**
   * 项目附件 内涵字段： prjId 项目id fileId 文件id fileName 文件名 permission 文件 权限 0=所有人；2=仅本人
   */
  public JSONArray prjAttachments;

  public Integer dataFrom;// 数据来源：0表单新增，1xls导入，2标准文件导入，3联邦检索，4基准库指派，9脚本修复

  public boolean isEdit = false;// 是否页面编辑
  public boolean isImport = false;// 是否文件导入编辑
}
