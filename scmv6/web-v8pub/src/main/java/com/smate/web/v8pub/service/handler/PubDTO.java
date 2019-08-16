package com.smate.web.v8pub.service.handler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.dto.PubMemberDTO;

/**
 * 成果业务传输对象(所有属性必须为对象 类型)
 *
 * @author tsz
 *
 * @date 2018年6月7日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PubDTO {

  public String pubHandlerName;// 调用处理器名字

  public Long pubId; // 不区分基准库和sns库的成果id，全部统一用pubId
  public Long grpId;
  public Long psnId;
  public Long ownerPsnId;
  public String des3OwnerPsnId; // 群组成果导入中，成果拥有者psnId
  public Long pdwhPubId; // 此基准库id只用于个人与基准库成果关系表中的基准库成果Id字段
  public String des3PubId; // 加密的pubId
  public String des3GrpId; // 加密的grpId
  public String des3PsnId; // 加密的psnId
  public String des3PdwhPubId; // 加密的pdwhPubId

  public Integer HCP = 0; // 高被引用文章 0未否，1为是
  public Integer HP = 0; // 热门文章 0未否，1为是
  public String OA = new String(); // Open Access

  public Integer isPubConfirm; // 是否成果认领 1为认领

  public Integer pubGenre; // 成果大类别 1：个人成果 2：群组成果 3：基准库导入至sns库的成果
  public Integer isEdit; // 成果是否编辑（标示用户是否手动修改保存过成果）
  public Integer permission; // 成果权限字段 7表示默认公开 4表示私人
  public Long insId; // 成果所属机构id
  public String title;// 成果标题
  public String publishDate;// 发表日期
  public Integer publishYear;
  public Integer publishMonth;
  public Integer publishDay;
  public Long countryId = 0L;
  public String fundInfo;
  public Integer citations;// 引用次数

  public Integer updateMark;// 更新标记

  public String doi;
  public String summary;
  public String keywords = new String();// 关键字
  public String srcFulltextUrl;
  public Integer pubType;// 成果类型
  public PubSnsRecordFromEnum recordFrom;// 记录来源

  public String authorNames;// 作者名称列表，通过成果作者构造出来的
  public String briefDesc = new String();// 简短描述 指定规则计算出来的
  public String organization;
  public Long fulltextId; // 保存成果全文之后产生的
  public String des3fulltextId; // 加密的全文id
  // 群组与个人关系表字段
  public Integer labeled; // 是否标注 0成果未标注；1成果已标注； 保存之前查询得到的
  public Integer relevance; // 相关度 相关度：成果关键词与群组关键词匹配数 保存之前计算出来的

  public Integer srcDbId; // 基准库当前被收录机构的dbId
  public String sourceId;
  public String sourceUrl;

  public Integer isProjectPub; // 项目成果1 项目文献0

  /**
   * 成果全文 内含字段： pubId 成果id fileId 文件id fileName 文件名 permission 文件 权限 0=所有人；2=仅本人
   */
  public JSONObject fullText;
  public JSONObject pubTypeInfo;

  /**
   * 成果作者 字段有：email 作者邮箱 insName 所在机构 name 姓名 firstAuthor 是否第一作者 owner 是否本人 communicable 是否通讯作者
   */
  public JSONArray members;

  public List<PubMemberDTO> memberList; // 用来保存authorNames拆分出来的作者信息

  public String authorNamesAbbr; // 传入的作者简称

  /**
   * 收录情况 内涵字段： libraryName 收录机构名 sitStatus 收录状态 0:未收录 ，1:收录 sitOriginStatus 原始收录状态 0:未收录 ，1:收录 srcUrl
   * 来源URL srcDbId 来源dbid srcId 来源唯一标识
   */
  public JSONArray situations;
  /**
   * 成果附件 内涵字段： pubId 成果id fileId 文件id fileName 文件名 permission 文件 权限 0=所有人；2=仅本人
   */
  public JSONArray accessorys;
  /**
   * 科技领域 内含字段： scienceAreaId 科技领域id scienceAreaName 科技领域名
   */
  public JSONArray scienceAreas;
  /**
   * 行业 内含字段：industryId
   */
  public JSONArray industrys;

  public Integer citedType;// 引用更新类型 手动更新1，后台更新0
  public String citedUrl; // 引用url

  public String pubIndexUrl; // 成果短地址
  public String pubLongIndexUrl; // 成果32位短地址

  public PubTypeInfoBean pubTypeInfoBean; // 成果类型bean组件

  /**
   * 个人库成果查重
   */
  public String hashTPP = new String();
  public String hashTP = new String();
  public String detailsHash = new String();
  public String detailsJson;

  public String hashDoi = new String();
  public String hashCleanDoi = new String();
  public String hashCnkiDoi = new String();
  public String hashCleanCnkiDoi = new String();
  public String hashEiSourceId = new String();
  public String hashIsiSourceId = new String();
  public String hashApplicationNo = new String();
  public String hashPublicationOpenNo = new String();
  public String hashTitle = new String();
  public String hashRegisterNo = new String();
  public String hashStandardNo = new String();

  public Long baseJournalId; // 匹配上的基础期刊Id

  public Set<String> emailList = new HashSet<>(); // 存储成果作者邮件信息

  public Integer bakDbId; // 低优先级的dbId
  public Integer bakCitations; // 低优先级的citation

  public Long pubSourceFileId = 0L; // 成果来源文件id
  public String historyDetailsJson;// 成果历史json
  public Integer awardCount;// 点赞数
  public Integer commentCount;// 评论数
  public Integer shareCount;// 分享数

}
