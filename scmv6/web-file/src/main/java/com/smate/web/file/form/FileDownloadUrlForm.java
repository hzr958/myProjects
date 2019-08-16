package com.smate.web.file.form;

import org.apache.commons.lang.math.NumberUtils;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.StringUtils;

/**
 * 获取下载链接的请求表单参数
 * 
 * @author houchuanjie
 * @date 2017年12月4日 上午9:58:55
 */
public class FileDownloadUrlForm {
  /**
   * 加密的id
   */
  private String des3Id;
  /**
   * 未加密的原始id
   */
  private Long id;
  /**
   * 多个加密的id串，“,”分隔
   */
  private String des3Ids;
  /**
   * 是否是短地址
   */
  private boolean shortUrl = false;
  /**
   * 文件类型
   */
  private Integer type;

  /**
   * 加密的id。各文件类型不同，需要传的id也不同，对应如下：<br>
   * 
   * <pre>
   * type     枚举类型                    id
   *  0   FileTypeEnum.PSN            v_psn_file.id
   *  1   FileTypeEnum.GROUP          v_grp_file.grp_file_id
   *  2   FileTypeEnum.SNS_FULLTEXT   pub_fulltext.pub_id
   *  3   FileTypeEnum.PDWH_FULLTEXT  pdwh_fulltext_file.pub_id
   * </pre>
   * 
   * @return des3Id
   */
  public String getDes3Id() {
    return des3Id;
  }

  /**
   * 加密的id。各文件类型不同，需要传的id也不同，对应如下：<br>
   * 
   * <pre>
   * type     枚举类型                    id
   *  0   FileTypeEnum.PSN            v_psn_file.id
   *  1   FileTypeEnum.GROUP          v_grp_file.grp_file_id
   *  2   FileTypeEnum.SNS_FULLTEXT   pub_fulltext.pub_id
   *  3   FileTypeEnum.PDWH_FULLTEXT  pdwh_fulltext_file.pub_id
   * </pre>
   * 
   * @param des3Id 要设置的 des3Id
   */
  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  /**
   * id。各文件类型不同，需要传的id也不同，对应如下：<br>
   * 
   * <pre>
   * type     枚举类型                    id
   *  0   FileTypeEnum.PSN            v_psn_file.id
   *  1   FileTypeEnum.GROUP          v_grp_file.grp_file_id
   *  2   FileTypeEnum.SNS_FULLTEXT   pub_fulltext.pub_id
   *  3   FileTypeEnum.PDWH_FULLTEXT  pdwh_fulltext_file.pub_id
   * </pre>
   * 
   * @return id
   */
  public Long getId() {
    if (StringUtils.isNotBlank(des3Id)) {
      id = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3Id));
    }
    return id;
  }

  /**
   * id。各文件类型不同，需要传的id也不同，对应如下：<br>
   * 
   * <pre>
   * type     枚举类型                    id
   *  0   FileTypeEnum.PSN            v_psn_file.id
   *  1   FileTypeEnum.GROUP          v_grp_file.grp_file_id
   *  2   FileTypeEnum.SNS_FULLTEXT   pub_fulltext.pub_id
   *  3   FileTypeEnum.PDWH_FULLTEXT  pdwh_fulltext_file.pub_id
   * </pre>
   * 
   * @param id 要设置的 id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * 是否是短地址
   * 
   * @return shortUrl
   */
  public boolean isShortUrl() {
    return shortUrl;
  }

  /**
   * 是否是短地址
   * 
   * @param shortUrl true 或 false
   */
  public void setShortUrl(boolean shortUrl) {
    this.shortUrl = shortUrl;
  }

  /**
   * 文件类型。<br>
   * 
   * <pre>
   * type     枚举类型                    id
   *  0   FileTypeEnum.PSN            v_psn_file.id
   *  1   FileTypeEnum.GROUP          v_grp_file.grp_file_id
   *  2   FileTypeEnum.SNS_FULLTEXT   pub_fulltext.pub_id
   *  3   FileTypeEnum.PDWH_FULLTEXT  pdwh_fulltext_file.pub_id
   * </pre>
   * 
   * @return type
   */
  public FileTypeEnum getType() {
    return FileTypeEnum.valueOf(type);
  }

  /**
   * 文件类型。<br>
   * 
   * <pre>
   * type     枚举类型                    id
   *  0   FileTypeEnum.PSN            v_psn_file.id
   *  1   FileTypeEnum.GROUP          v_grp_file.grp_file_id
   *  2   FileTypeEnum.SNS_FULLTEXT   pub_fulltext.pub_id
   *  3   FileTypeEnum.PDWH_FULLTEXT  pdwh_fulltext_file.pub_id
   * </pre>
   * 
   * @param type 要设置的 type
   */
  public void setType(Integer type) {
    this.type = type;
  }

  public boolean validate() {
    boolean flag = getId() != null || StringUtils.isNotBlank(des3Ids);
    try {
      // 获取文件类型时，会进行相应转换，转换失败的话抛出异常
      getType();
    } catch (Exception e) {
      flag = false;
    }
    return flag;
  }

  /**
   * @return des3Ids
   */
  public String getDes3Ids() {
    return des3Ids;
  }

  /**
   * @param des3Ids 要设置的 des3Ids
   */
  public void setDes3Ids(String des3Ids) {
    this.des3Ids = des3Ids;
  }
}
