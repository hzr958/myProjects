package com.smate.core.base.file.service;

import com.smate.core.base.file.enums.FileTypeEnum;

/**
 * 获取文件下载地址服务类接口，用于获取各种文件类型的下载地址
 * 
 * @author houchuanjie
 * @date 2017-11-27
 *
 */
public interface FileDownloadUrlService {

  /**
   * 下载链接的KEY
   */
  static final String DOWNLOAD_KEY = "_f";

  /**
   * 文件下载缓存
   */
  final String DOWNLOAD_CACHE_MARK = "DOWNLOAD_CACHE_MARK";

  /**
   * 获取站内文件下载地址（带下载权限）<br>
   * 各文件类型需要传的id对应如下：<br>
   * 
   * <pre>
   * FileTypeEnum.SNS_FULLTEXT	pub_fulltext.pub_id
   * FileTypeEnum.PDWH_FULLTEXT 	pdwh_fulltext_file.pub_id
   * FileTypeEnum.PSN		v_psn_file.id
   * FileTypeEnum.GROUP 		v_grp_file.grp_file_id
   * </pre>
   *
   * @author houchuanjie
   * @date 2017年11月28日 下午1:59:33
   * @param fileType
   * @param fileId
   * @return 下载地址url
   */
  String getDownloadUrl(FileTypeEnum fileType, Long id);

  String getDownloadUrl(FileTypeEnum fileType, Long id, Long pubId);

  /**
   * 获取站外文件下载短地址（不带下载权限） 各文件类型需要传的id对应如下：<br>
   * 
   * <pre>
   * FileTypeEnum.SNS_FULLTEXT	pub_fulltext.pub_id
   * FileTypeEnum.PDWH_FULLTEXT 	pdwh_fulltext_file.pub_id
   * FileTypeEnum.PSN		v_psn_file.id
   * FileTypeEnum.GROUP 		v_grp_file.grp_file_id
   * </pre>
   * 
   * @author houchuanjie
   * @date 2017年11月28日 下午2:00:06
   * @param fileType
   * @param fileId
   * @return
   */
  String getShortDownloadUrl(FileTypeEnum fileType, Long id);

  /**
   * 获取全文附件地址
   * 
   * @param fileType
   * @param id
   * @return
   */
  String getDownloadAttachmentUrl(FileTypeEnum fileType, Long id, Long pubId);

  /**
   * 获取批量打包下载地址
   * 
   * @author houchuanjie
   * @date 2018年3月7日 下午5:03:48
   * @param type
   * @param des3Ids
   * @return
   */
  String getZipDownloadUrl(FileTypeEnum type, String des3Ids);

  String getShortDownloadUrl(FileTypeEnum snsFulltext, Long fileId, Long pubId);
}
