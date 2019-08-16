package com.smate.center.task.single.factory.pub;

import java.util.List;

import org.springframework.util.Assert;

import com.smate.center.task.model.sns.quartz.IValidator;
import com.smate.core.base.utils.exception.ValidatorNotFoundException;

/**
 * @author yamingd Xml校验者工厂,查找校验者
 */
public class XmlValidatorFactory {

  /**
   * 注册的校验者.
   */
  private List<IValidator> validators = null;

  /**
   * Spring注入Validators.
   * 
   * @param validators 校验者集合
   */
  public void setValidators(List<IValidator> validators) {
    Assert.notNull(validators);
    Assert.notEmpty(validators);
    this.validators = validators;
  }

  /**
   * 查找校验者.
   * 
   * @param forTmplForm 录入模板名称
   * @param forType 成果类型Id
   * @return IValidator
   * @throws ValidatorNotFoundException ValidatorNotFoundException
   */
  public IValidator getValidator(String forTmplForm, int forType) throws ValidatorNotFoundException {
    Assert.notNull(forTmplForm);
    IValidator result = null;
    for (int i = 0; i < validators.size(); i++) {
      IValidator item = validators.get(i);
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
