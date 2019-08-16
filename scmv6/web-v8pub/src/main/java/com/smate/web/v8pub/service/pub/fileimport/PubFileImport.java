package com.smate.web.v8pub.service.pub.fileimport;

import java.util.List;
import java.util.Map;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.vo.PendingImportPubVO;
import com.smate.web.v8pub.vo.importfile.ImportSaveVo;
import com.smate.web.v8pub.vo.importfile.ImportfileVo;

public interface PubFileImport {
  /**
   * 获取xmlid
   * 
   * @param publicationArticleType
   * @return
   * @throws ServiceException
   */
  public List<ConstPubType> getConstPubTypeAll() throws ServiceException;

  /**
   * 初始化和校验成果
   * 
   * @param publicationArticleType
   * @return
   * @throws ServiceException
   */
  public List<PendingImportPubVO> getInitImportPubVOList(List<PendingImportPubVO> pubList, ImportfileVo importfileVo)
      throws ServiceException;

  /**
   * 调用接口保存数据
   * 
   * @param importfileVo
   * @throws ServiceException
   */
  public Map<String, Integer> savePublist(ImportSaveVo importSaveVo) throws ServiceException;

}
