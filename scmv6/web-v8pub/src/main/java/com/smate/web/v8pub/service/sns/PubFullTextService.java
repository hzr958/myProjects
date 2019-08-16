package com.smate.web.v8pub.service.sns;

import java.util.List;
import java.util.Map;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.service.BaseService;
import com.smate.web.v8pub.vo.PubFulltextSimilarVO;

public interface PubFullTextService extends BaseService<Long, PubFullTextPO> {

  /**
   * 获取个人库成果全文的下载权限
   * 
   * @param pubId 成果id
   * @param fileId 文件id
   * @return 全文下载权限，0所有人可下载，1好友可下载，2自己可下载
   * @throws ServiceException
   */
  Integer getFullTextPermission(Long pubId, Long fileId, Long reqUserId) throws ServiceException;

  /**
   * 更新个人库成果全文的文件描述
   * 
   * @param pubId 成果id
   * @param fileId 文件id
   * @throws ServiceException
   */
  void updateFullTextFileDesc(Long pubId, Long fileId, String fileDesc) throws ServiceException;

  /**
   * 更新个人库成果全文的下载权限
   * 
   * @param pubId 成果id
   * @param fileId 文件id
   * @param permission
   * @throws ServiceException
   */
  void updateFullTextPermission(Long pubId, Long fileId, Integer permission) throws ServiceException;

  /**
   * 查找全文列表
   * 
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  public List<PubFullTextPO> findPubfulltextList(List<Long> pubIds) throws ServiceException;

  /**
   * 获取成果全文图片地址.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public String getFulltextImageUrl(Long pubId) throws ServiceException;

  /**
   * 查找其他类似全文数目
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Long getSimilarCount(Long pubId) throws ServiceException;

  /**
   * 查找pdwh其他类似全文数目
   * 
   * @param pdwhPubId
   * @return
   * @throws ServiceException
   */
  public Long getPdwhSimilarCount(Long pdwhPubId) throws ServiceException;

  /**
   * 查找相似全文信息
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  List<PubFulltextSimilarVO> getSimilarInfo(Long pubId) throws ServiceException;

  /**
   * 查找pdwh相似全文信息
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  List<PubFulltextSimilarVO> getPdwhSimilarInfo(Long pdwhPubId) throws ServiceException;

  /**
   * 获取全文认领的全文下载url
   * 
   * @param fulltextFileId
   * @param b
   * @return
   * @throws ServiceException
   */
  public String getRcmdFullTextDownloadUrl(Long fulltextFileId, boolean b) throws ServiceException;

  /**
   * 获取个人库成果全文的下载地址
   * 
   * @author ChuanjieHou
   * @param pubId 成果id
   * @param isShortUrl 是否获取短地址
   * @return 返回真实下载地址或者""
   */
  public String getSnsFullTextDownloadUrl(Long pubId, boolean isShortUrl);

  /**
   * 查找全文
   * 
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  public PubFullTextPO findPubfulltext(Long pubId) throws ServiceException;

  /**
   * 获取基准库成果全文下载地址
   * 
   * @author ChuanjieHou
   * @param pubId 成果id
   * @param isShortUrl 是否获取短地址
   * @return 返回真实下载地址或者""
   */
  public String getPdwhFullTextDownloadUrl(Long pubId, boolean isShortUrl);

  /**
   * 获取基准库成果全文图片地址
   */
  public String getPdwhFullTextUrl(Long pdwhPubId);

  /**
   * 上传全文 更新成果全文附件信息
   * 
   * @param pubId
   * @param fileId
   * @return
   * @throws ServiceException
   */
  public Map<String, String> uploadPubFulltext(Long pubId, Long fileId, Long psnId) throws ServiceException;

  /**
   * 通过id获取全文
   * 
   * @param fullTextId
   * @return
   * @throws ServiceException
   */
  public PubFullTextPO getPubFullTextById(Long fullTextId) throws ServiceException;

  /**
   * 通过pubId和fileId获取全文
   * 
   * @return
   * @throws ServiceException
   */
  public PubFullTextPO getPubFullText(Long pubId, Long fileId) throws ServiceException;

  /**
   * 获取最大的全文id
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  Long getMaxFulltextId(Long pubId) throws ServiceException;

  void deleteByPubId(Long pubId) throws ServiceException;;

  /**
   * 获取个人库成果全文的下载地址
   * 
   * @author ChuanjieHou
   * @param pubId 成果id
   * @param isShortUrl
   * @param currentPsnId 当前登录人员ID 是否获取短地址
   * @return 返回真实下载地址或者""
   */
  String getSnsFullTextDownloadUrl(Long pubId, Long currentPsnId, boolean isShortUrl);
}
