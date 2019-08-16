package com.smate.web.dyn.service.dynamic.group;

import java.util.Map;

import com.smate.web.dyn.exception.DynGroupException;
import com.smate.web.dyn.form.dynamic.group.GroupDynamicForm;

/**
 * 构建资源参数
 * 
 * @author tsz
 *
 */
public interface BuildResParametService {

  public void buildResParamet(Map<String, Object> data, GroupDynamicForm form) throws DynGroupException;
}
