package com.smate.sie.center.task.pdwh.validator;

import java.util.List;

import org.springframework.util.Assert;

import com.smate.core.base.utils.exception.ValidatorNotFoundException;

/**
 * Xml校验者工厂,查找校验者
 * 
 * @author jszhou
 *
 */
public class PubXmlValidatorFactory {

  /** 注册的校验者. */
  private List<IPubValidator> pubValidators = null;

  /**
   * Spring注入Validators.
   * 
   * @param pubValidators 校验者集合
   */
  public void setValidators(List<IPubValidator> pubValidators) {
    Assert.notNull(pubValidators);
    Assert.notEmpty(pubValidators);
    this.pubValidators = pubValidators;
  }

  /**
   * 查找校验者.
   * 
   * @param forTmplForm 录入模板名称
   * @param forType 成果类型Id
   * @return IValidator
   * @throws ValidatorNotFoundException ValidatorNotFoundException
   */
  public IPubValidator getValidator(String forTmplForm, int forType) throws ValidatorNotFoundException {
    Assert.notNull(forTmplForm);
    IPubValidator result = null;
    for (int i = 0; i < pubValidators.size(); i++) {
      IPubValidator item = pubValidators.get(i);
      if (item.getForType() == forType && item.getForTmplForm().equalsIgnoreCase(forTmplForm)) {
        result = item;
        break;
      }
    }
    if (result == null) {
      throw new ValidatorNotFoundException(forTmplForm, forType);
    }

    return result;
  }

}
