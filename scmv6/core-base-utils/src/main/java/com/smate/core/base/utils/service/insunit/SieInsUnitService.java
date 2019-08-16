package com.smate.core.base.utils.service.insunit;

import java.util.List;

import com.smate.core.base.utils.model.rol.SieInsUnit;

/**
 * 
 * @author yxs
 * @descript 部门接口
 */
public interface SieInsUnitService {

  /**
   * 通过输入语言种类，用户角色自动补全部门名称.
   * 
   * @param startWith
   * @param maxSize
   * @param extraItem 提示列表额外添加的选项
   * @return 返回包含部门名称和部门id的JSON字符串
   */
  List<SieInsUnit> getAcInsUnit(Long insId) throws Exception;

  /**
   * 根据部门名获取部门Id.
   * 
   * @param unitName
   * @return
   * @throws ServiceException
   */
  Long getInsUnitId(String unitName, Long insId) throws Exception;

  public SieInsUnit getInsUnitById(Long id) throws Exception;

  /**
   * 获取所有部门
   * 
   * @return
   * @throws Exception
   */
  public List<SieInsUnit> getAllInsUnit() throws Exception;;
}
