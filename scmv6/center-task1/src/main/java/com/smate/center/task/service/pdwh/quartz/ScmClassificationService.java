package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.model.pdwh.quartz.PdwhPubForClassification;
import com.smate.center.task.model.pdwh.quartz.PdwhPubForClassificationNsfc;
import com.smate.center.task.model.sns.pub.NsfcPrjForClassification;
import com.smate.center.task.model.sns.pub.ScmUserForClassification;

public interface ScmClassificationService {

  /**
   * 对pdwh中的isi成果进行分类，映射至scm分类；分类结果：1.成果分类完成；2.成果分类完成但是未映射至scm分类；3.成果分类出错；7. 成果的期刊jnlid为空；9.成果为空
   * 
   * @param PdwhPubForClassification
   * @throws Exception
   */
  public Integer IsiCategoryToScm(PdwhPubForClassification pub) throws Exception;

  /**
   * 对pdwh中的cnki成果进行分类，映射至scm分类；分类结果：1.成果分类完成；2.成果分类完成但是未映射至scm分类；3.成果分类出错；7. 成果的期刊jnlid为空；9.成果为空
   * 
   * @param PdwhPubForClassification
   * @throws Exception
   */
  public Integer CnkiCategoryToScm(PdwhPubForClassification pub) throws Exception;

  /**
   * 用于对应的项目群组分类与推荐 对sns中的nsfc项目进行分类，映射至scm分类；分类结果：1.成果分类完成；2.成果分类完成但是未映射至scm分类；3.成果分类出错；7.
   * 成果的期刊jnlid为空；9.成果为空
   * 
   * @param PdwhPubForClassification
   * @throws Exception
   */
  public Integer classifyNsfcProjectToScm(NsfcPrjForClassification prj) throws Exception;

  public void ScmPsnClassification();

  public List<PdwhPubForClassification> getIsiPdwhPubs();

  public List<PdwhPubForClassification> getCnkiPdwhPubs();

  public void updateClassificationStatus(PdwhPubForClassification pub, Integer status);

  public void updateNsfcPrjClassificationStatus(NsfcPrjForClassification prj, Integer status);

  public List<NsfcPrjForClassification> getNsfcPrjs();

  public List<ScmUserForClassification> getScmUserForClassification();

  public void updateScmUserForClassificationStatus(ScmUserForClassification user, Integer status);

  public Integer psnClassifyBasedOnNsfcInfo(ScmUserForClassification psn) throws Exception;

  Integer IsiCategoryToNsfc(PdwhPubForClassificationNsfc pub);

  Integer CnkiCategoryToNsfc(PdwhPubForClassificationNsfc pub) throws Exception;

  List<PdwhPubForClassificationNsfc> getIsiPdwhPubsNsfc();

  List<PdwhPubForClassificationNsfc> getCnkiPdwhPubsNsfc();

  void updateClassificationStatusNsfc(PdwhPubForClassificationNsfc pub, Integer status);

}
