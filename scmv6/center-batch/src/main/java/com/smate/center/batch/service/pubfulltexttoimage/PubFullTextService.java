package com.smate.center.batch.service.pubfulltexttoimage;

import java.io.FileNotFoundException;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PdwhPubFulltextImageRefresh;
import com.smate.center.batch.model.sns.pub.PubFulltext;
import com.smate.center.batch.model.sns.pub.PubFulltextImageRefresh;
import com.smate.core.base.utils.exception.BatchTaskException;

public interface PubFullTextService {

  /**
   * 保存成果全文转换成图片的刷新记录.
   * 
   * @param pubId
   * @param fulltextFileId
   * @param fulltextNode
   * @param fulltextFileExt
   * @throws ServiceException
   */
  public void savePubFulltextImageRefresh(Long pubId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt)
      throws BatchTaskException;

  /**
   * 保存刷新记录.
   * 
   * @param pubFulltextImageRefresh
   * @throws ServiceException
   */
  public void savePubFulltextImageRefresh(PubFulltextImageRefresh pubFulltextImageRefresh) throws BatchTaskException;

  /**
   * 通过id删除刷新记录.
   * 
   * @param id
   * @throws ServiceException
   */
  public void delPubFulltextImageRefresh(Long id) throws BatchTaskException;

  /**
   * 批量获取需要刷新的数据.
   * 
   * @param startId
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  public List<PubFulltextImageRefresh> getNeedRefreshData(int maxSize) throws BatchTaskException;

  /**
   * 转换图片并保存图片信息.
   * 
   * @param pubFulltextImageRefresh
   * @throws InterruptedException
   * @throws ServiceException
   */
  public void refreshData(PubFulltextImageRefresh pubFulltextImageRefresh) throws Exception, FileNotFoundException;

  /**
   * 保存成果全文信息.
   * 
   * @param pubId
   * @param fulltextFileId
   * @param fulltextNode
   * @param fulltextFileExt
   * @throws ServiceException
   */
  public void savePubFulltext(Long pubId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt)
      throws BatchTaskException;

  /**
   * 获取成果全文信息.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public PubFulltext getPubFulltextByPubId(Long pubId) throws BatchTaskException;

  /**
   * 获取成果全文图片地址.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public String getFulltextImageUrl(Long pubId) throws BatchTaskException;

  /**
   * 删除全文信息，同时也会删除全文附件所转换的图片.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void deletePubFulltextByPubId(Long pubId) throws BatchTaskException;

  /**
   * 处理成果全文.
   * 
   * @param pubId
   * @param fulltextFileId
   * @param fulltextNode
   * @param fulltextFileExt
   * @throws ServiceException
   */
  public void dealPubFulltext(Long pubId, Long psnId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt,
      int permission, Long groupId) throws ServiceException;

  /**
   * 通过id查询需要更新的数据.
   * 
   * @param startId
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  public PubFulltextImageRefresh getNeedRefreshDataById(Long id) throws BatchTaskException;

  void updatePubFulltext(PubFulltext ptext) throws ServiceException;

  public void sendFtRequestReplyMail(Long pubId);

  public boolean isOtherTypeFt(Long pubId);

  /**
   * 获取基准库全文图片待处理数据
   * 
   * @param pubId
   * @return
   */
  public PdwhPubFulltextImageRefresh getNeedRefreshPdwhDataById(Long pubId);

  /**
   * 转换图片并保存图片信息.
   * 
   * @param pubFulltextImageRefresh
   */
  public void convertPdftoImg(PdwhPubFulltextImageRefresh pdwhImage) throws Exception;

  /**
   * 保存pdwh刷新记录.
   * 
   * @param pubFulltextImageRefresh
   * @throws ServiceException
   * @throws Exception
   */
  public void savePdwhPubFulltextImageRefresh(PdwhPubFulltextImageRefresh pdwhImage) throws BatchTaskException;

  /**
   * 删除pdwh pub image刷新纪录
   * 
   * @param pubId
   * @throws BatchTaskException
   */
  public void delPdwhPubFulltextImageRefresh(Long pubId) throws BatchTaskException;

  /**
   * 更新全文的图片地址
   * 
   * @author houchuanjie
   * @date 2018年1月11日 下午4:07:27
   * @param pubId
   * @param destRelativePath 缩略图相对地址
   * @param index pdf文件第几页转换的图片
   */
  public void updateSnsPubFulltextImage(Long pubId, String destRelativePath, int index) throws BatchTaskException;

  /**
   * 删除全文图片
   * 
   * @param pubId
   * @throws BatchTaskException
   * @author LIJUN
   * @date 2018年4月10日
   */
  public void deleteSnsPubFulltextImageByPubId(Long pubId) throws BatchTaskException;
}
