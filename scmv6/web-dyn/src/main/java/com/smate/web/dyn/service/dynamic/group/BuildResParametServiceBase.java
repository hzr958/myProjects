package com.smate.web.dyn.service.dynamic.group;

import java.util.Map;

import com.smate.web.dyn.exception.DynGroupException;
import com.smate.web.dyn.form.dynamic.group.GroupDynamicForm;

public abstract class BuildResParametServiceBase implements BuildResParametService {


  public abstract Boolean doVerify(GroupDynamicForm form);

  public abstract void doHandler(Map<String, Object> data, GroupDynamicForm form);

  @Override
  public void buildResParamet(Map<String, Object> data, GroupDynamicForm form) throws DynGroupException {
    if (doVerify(form)) {
      doHandler(data, form);
    }
  }

}
