package com.smate.web.dyn.service.dynamic;

import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.dynamic.DynamicAwardPsn;

/**
 * 动态赞服务接口
 * 
 * @author zk
 *
 */
public interface DynamicAwardService {

  /**
   * 赞动作 ， 点击赞
   * 
   * @param paramJson
   * @return
   * @throws Exception
   */
  public String addAward(DynamicForm form) throws DynException;

  /**
   * 获取赞人员列表
   * 
   * @param dynId
   * @param maxSize
   * @return
   * @throws DynException
   */
  public List<DynamicAwardPsn> getAwardPsnList(Long dynId, Integer maxSize) throws DynException;

  /**
   * 获取赞人员列表
   * 
   * @param resIdResTypes
   * 
   * @return
   * @throws DynException
   */
  public Map<String, Boolean> getAwardPsnHasAward(DynamicForm form) throws DynException;

  /**
   * 根据动态Id获取该动态/资源是否被赞
   * 
   * @param psnId
   * @param dynId
   * @return
   */
  public boolean getPsnHasAward(Long psnId, Long dynId) throws DynException;

}
