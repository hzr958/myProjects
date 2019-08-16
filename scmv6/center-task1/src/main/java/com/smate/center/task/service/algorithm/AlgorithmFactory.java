package com.smate.center.task.service.algorithm;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

/**
 * 推荐算法基类.
 * 
 * @author lichangwen
 * 
 */
@Service("algorithmFactory")
public class AlgorithmFactory implements BeanFactoryAware {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 个人文献推荐服务接口类.
   */
  private final static String[] PSN_REF_INTERFACES = new String[] {"psnRefRequir", "psnRefPlus", "psnRefCommon"};

  /**
   * 相关文献/基金文献-服务接口类.
   */
  private final static String[] RELATED_REF_INTERFACES =
      new String[] {"relatedRefRequir", "relatedRefPlus", "relatedRefCommon"};

  /**
   * sns成果相关文献-服务接口类
   */
  private final static String[] PUB_RELATED = new String[] {"relatedRefRequir", "pubRelatedPlus", "pubRelatedCommon"};

  /**
   * sns成果读者推荐-服务接口类
   */
  private final static String[] PUB_READER = new String[] {"pubReadRequir", "pubReadPlus", "pubReadCommon"};

  /**
   * 个人期刊推荐服务接口类.
   */
  private final static String[] PSN_JNL_INTERFACES = new String[] {"psnJnlRequir", "psnJnlPlus", "psnJnlCommon"};

  /**
   * 论文期刊-推荐服务接口类.
   */
  private final static String[] REF_JNL_INTERFACES = new String[] {"refJnlRequir", "refJnlPlus", "refJnlCommon"};

  private BeanFactory beanFactory;
  private RequirService requirService;
  private PlusService plusService;
  private CommonService commonService;

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  /**
   * 个人文献推荐.
   * 
   * @param psnId
   * @throws Exception
   */
  public void runPsnRefRec(Long psnId) {
    try {
      requirService = (RequirService) beanFactory.getBean(PSN_REF_INTERFACES[0]);
      plusService = (PlusService) beanFactory.getBean(PSN_REF_INTERFACES[1]);
      commonService = (CommonService) beanFactory.getBean(PSN_REF_INTERFACES[2]);
      Algorithm alg = new Algorithm(requirService, plusService, commonService);
      alg.execute(psnId);
    } catch (Exception e) {
      logger.error("run 个人文献推荐出错，psnId：{}", psnId, e);
    }
  }

  /**
   * 相关文献/基金文献-推荐.
   * 
   * @param psnId
   * @throws Exception
   */
  public void runRelatedRefRec(Long psnId, List<String> kws) {
    try {
      requirService = (RequirService) beanFactory.getBean(RELATED_REF_INTERFACES[0]);
      plusService = (PlusService) beanFactory.getBean(RELATED_REF_INTERFACES[1]);
      commonService = (CommonService) beanFactory.getBean(RELATED_REF_INTERFACES[2]);
      Algorithm alg = new Algorithm(requirService, plusService, commonService);
      alg.execute(psnId, kws);
    } catch (Exception e) {
      logger.error("run 相关文献/基金文献-推荐出错，psnId：{}", psnId, e);
    }
  }

  /**
   * 个人期刊推荐.
   * 
   * @param psnId
   * @throws Exception
   */
  public void runPsnJnlRec(Long psnId) {
    try {
      requirService = (RequirService) beanFactory.getBean(PSN_JNL_INTERFACES[0]);
      plusService = (PlusService) beanFactory.getBean(PSN_JNL_INTERFACES[1]);
      commonService = (CommonService) beanFactory.getBean(PSN_JNL_INTERFACES[2]);
      Algorithm alg = new Algorithm(requirService, plusService, commonService);
      alg.execute(psnId);
    } catch (Exception e) {
      logger.error("run 个人期刊推荐出错，psnId：{}", psnId, e);
    }
  }

  /**
   * 论文期刊推荐.
   * 
   * @param psnId
   * @throws Exception
   */
  public void runRefJnlRec(Long psnId, List<String> kws) {
    try {
      requirService = (RequirService) beanFactory.getBean(REF_JNL_INTERFACES[0]);
      plusService = (PlusService) beanFactory.getBean(REF_JNL_INTERFACES[1]);
      commonService = (CommonService) beanFactory.getBean(REF_JNL_INTERFACES[2]);
      Algorithm alg = new Algorithm(requirService, plusService, commonService);
      alg.execute(psnId, kws);
    } catch (Exception e) {
      logger.error("run 论文期刊推荐出错，psnId：{}", psnId, e);
    }
  }

  /**
   * sns成果相关文献推荐.
   * 
   * @param psnId
   * @param pubId
   * @param kws
   */
  public void runPubRelated(Long psnId, Long pubId, List<String> kws) {
    try {
      requirService = (RequirService) beanFactory.getBean(PUB_RELATED[0]);
      plusService = (PlusService) beanFactory.getBean(PUB_RELATED[1]);
      commonService = (CommonService) beanFactory.getBean(PUB_RELATED[2]);
      Algorithm alg = new Algorithm(requirService, plusService, commonService);
      alg.executeByRelated(psnId, pubId, kws);
    } catch (Exception e) {
      logger.error("run sns成果相关文献推荐出错，pubId：{}", pubId, e);
    }
  }

  /**
   * sns成果读者献推荐.
   * 
   * @param psnId
   * @param pubId
   * @param kws
   */
  public void runPubReader(Long pubId, List<String> kws) {
    try {
      requirService = (RequirService) beanFactory.getBean(PUB_READER[0]);
      plusService = (PlusService) beanFactory.getBean(PUB_READER[1]);
      commonService = (CommonService) beanFactory.getBean(PUB_READER[2]);
      Algorithm alg = new Algorithm(requirService, plusService, commonService);
      alg.executeByReader(pubId, kws);
    } catch (Exception e) {
      logger.error("run sns成果读者推荐出错，pubId：{}", pubId, e);
    }
  }

}
