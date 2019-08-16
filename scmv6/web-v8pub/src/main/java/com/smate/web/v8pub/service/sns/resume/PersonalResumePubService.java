package com.smate.web.v8pub.service.sns.resume;

import java.util.List;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.exception.PubException;
import com.smate.web.v8pub.vo.PersonalResumePubVO;

/**
 * 个人简历-----成果服务
 * 
 * @author wsn
 *
 */
public interface PersonalResumePubService {

  /**
   * 是否是简历拥有者
   * 
   * @return
   * @throws PubException
   */
  public boolean isOwnerOfCV(Long cvId, Long psnId) throws Exception;

  /**
   * 查找简历模块中已选中的成果
   * 
   * @param cvId
   * @param psnId
   * @param moduleId
   * @return
   * @throws PubException
   */
  public List<PubInfo> findPsnCVSelectedPub(Long cvId, Long psnId, Integer moduleId) throws Exception;

  /**
   * 获取简历成果
   * 
   * @param form
   */
  public void buildPsnResumePubInfo(PersonalResumePubVO form) throws Exception;

  /**
   * 检查成果是否属于当前作者
   * 
   * @param form
   * @return
   */
  public boolean checkPubInfo(PersonalResumePubVO form) throws Exception;

  /**
   * 保存简历模块
   * 
   * @param form
   * @throws PsnException
   */
  public void savePsnResumeModule(PersonalResumePubVO form) throws Exception;
}
