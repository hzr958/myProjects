package com.smate.center.batch.service.application;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 推荐算法基类
 * 
 * @author lichangwen
 * 
 */
public class Algorithm {
  /**
   * 必要条件
   */
  private RequirService requirService;

  /**
   * 加分条件
   */
  private PlusService plusService;

  /**
   * 公用
   */
  private CommonService commonService;

  public Algorithm(RequirService requirService, PlusService plusService, CommonService commonService) {
    super();
    this.requirService = requirService;
    this.plusService = plusService;
    this.commonService = commonService;
  }

  /**
   * 根据个人信息后台任务推荐.
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void execute(Long psnId) throws ServiceException {
    List<?> list = requirService.matching(psnId, null);
    commonService.clear(psnId, list);
    if (CollectionUtils.isEmpty(list)) {
      return;
    }
    List<?> scoreList = plusService.complex(psnId, list, null);
    commonService.save(psnId, scoreList);
    commonService.degrees(psnId);
    commonService.product(psnId, list);
  }

  /**
   * 根据用户输入的信息即时推荐.
   * 
   * @param psnId
   * @param kwList
   * @throws ServiceException
   */
  public void execute(Long psnId, List<String> kwList) throws ServiceException {
    commonService.clear(psnId, null);
    List<?> list = requirService.matching(psnId, kwList);
    if (CollectionUtils.isEmpty(list)) {
      return;
    }
    List<?> scoreList = plusService.complex(psnId, list, kwList);
    commonService.save(psnId, scoreList);
    commonService.degrees(psnId);
    commonService.product(psnId, list);
  }

  /**
   * sns成果相关文献推荐.
   * 
   * @param psnId
   * @param pubId
   * @param kwList
   * @throws ServiceException
   */
  public void executeByRelated(Long psnId, Long pubId, List<String> kwList) throws ServiceException {
    commonService.clear(pubId, null);
    List<?> list = requirService.matching(psnId, kwList);
    if (CollectionUtils.isEmpty(list)) {
      return;
    }
    List<?> scoreList = plusService.complex(psnId, list, kwList);
    commonService.save(pubId, scoreList);
    commonService.degrees(pubId);
    commonService.product(pubId, list);
  }

  /**
   * sns成果读者推荐.
   * 
   * @param pubId
   * @param kwList
   * @throws ServiceException
   */
  public void executeByReader(Long pubId, List<String> kwList) throws ServiceException {
    commonService.clear(pubId, null);
    List<?> list = requirService.matching(pubId, kwList);
    if (CollectionUtils.isEmpty(list)) {
      return;
    }
    List<?> scoreList = plusService.complex(pubId, list, kwList);
    commonService.save(pubId, scoreList);
    commonService.degrees(pubId);
    commonService.product(pubId, list);
  }

  public void setRequirement(RequirService requirService) {
    this.requirService = requirService;
  }

  public void setPlusConditions(PlusService plusService) {
    this.plusService = plusService;
  }

  public void setAlgCommon(CommonService commonService) {
    this.commonService = commonService;
  }

}
