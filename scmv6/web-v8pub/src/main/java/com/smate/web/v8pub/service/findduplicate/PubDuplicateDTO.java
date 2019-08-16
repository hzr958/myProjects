package com.smate.web.v8pub.service.findduplicate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 成果查重传输对象
 * 
 * @author YJ
 *
 *         2018年8月18日
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PubDuplicateDTO {

  public Integer pubGener; // 成果大类别， PubGenreConstants

  public String des3PsnId; // 加密的psnId
  public String des3PubId; // 加密的pubId

  public Long psnId; // 解密处理的psnId
  public Long pubId; // 成果pubId，用于排除自己
  public Long groupId;

  /**
   * 界面需要传的值
   */
  // 个人库与基准库必传的值
  public String title; // 成果标题
  public Integer pubYear; // 成果年限
  public Integer pubType; // 成果类别
  // 基准库必传的值
  public String doi; // 成果doi
  public String sourceId; // 成果的sourceId
  public Integer srcDbId; // 成果所在文献库的dbID
  public String applicationNo; // 专利申请号
  public String publicationOpenNo; // 专利公开（公告）号
  public String standardNo; // 标准的标准号
  public String registerNo; // 软件著作权的登记号

  /**
   * 所需要构造的hash值
   */
  public String hashTPP; // title + pubType + pubYear组成的hash值
  public String hashTP; // title + pubType 组成的hash值
  public String hashT; // title 组成的hash值
  public Long hashDoi; // doi的hash值，与doiHash匹配
  public Long hashCleanDoi;// doi被清理符号的hash值
  public Long hashSourceId; // sourceId的hash值，与isiSourceHash和eiSourceIdHash匹配
  public Long hashApplicationNo; // 专利申请号的hash值
  public Long hashPublicationOpenNo; // 专利公开（公告）号的hash值
  public Long hashStandardNo; // 标准号的hash值
  public Long hashRegisterNo; // 软件著作权的登记号

}
