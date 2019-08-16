package com.smate.web.v8pub.service.sns;

import java.util.HashMap;

import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.web.v8pub.exception.ServiceException;

/**
 * 成果编辑接口
 * 
 * @author Administrator
 *
 */
public interface PubEnterEditService {
  /**
   * 根据pubJson或成果id获取成果信息
   * 
   * @param json
   * @return
   * @throws ServiceException
   */
  public PubDetailVO getBuilPubDetailVO(String pubJson, String des3PubId, String des3GrpId, Integer changType)
      throws ServiceException;

  /**
   * 构建一些页面的来源成果类型等字段
   * 
   * @param pub
   */
  public void builParamsPubDetailVo(PubDetailVO pub) throws ServiceException;

  /**
   * 编辑进入的时候初始化一些信息
   * 
   * @return
   * @throws ServiceException
   */
  public PubDetailVO initPubDetailVO() throws ServiceException;

  /**
   * 调用接口保存成果json
   * 
   * @throws ServiceException
   */
  public String saveOrUpdatePubJson(String pubJson) throws ServiceException;

  /**
   * json转PubDetailVO对象
   * 
   * @param pubJson
   * @return
   */
  public PubDetailVO getPubDetailVoJson(String pubJson);

  /**
   * 判断当前人是否是成果的作者
   * 
   * @param pubId
   * @return
   */
  public boolean getIsCurrentUserPub(Long pubId, Long grpId);

  public PubDetailVO getBuilPubDetailVOByPdwh(String pdwhPubId, String des3GrpId, String des3GrpId2);

  /**
   * 解析pdf文件，获取基准库成果的pubId
   * 
   * @param fileId
   * @return
   */
  public HashMap<String, Object> resolverPDF(Long fileId) throws ServiceException;

  /**
   * 构建成果全文信息
   * 
   * @param pdwhPubShow
   * @param des3FileId
   */
  public void buildPubFulltext(PubDetailVO<?> pdwhPubShow, String des3FileId);

}
