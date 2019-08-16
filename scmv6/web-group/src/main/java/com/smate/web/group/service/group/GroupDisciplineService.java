package com.smate.web.group.service.group;

import java.util.List;
import java.util.Map;

import com.smate.web.group.exception.GroupException;
import com.smate.web.group.model.group.pub.Discipline;

public interface GroupDisciplineService {
  /**
   * 获取一级学科.
   * 
   * 
   * @throws GroupException
   */
  List<Map<Object, Object>> getTopDisciplineList() throws GroupException;


  /**
   * 根据一级学科获取二级学科.
   * 
   * @param topCategoryId
   * @throws GroupException
   */
  List<Discipline> getSecondDisciplineListById(Long topCategoryId) throws GroupException;

  /**
   * 根据一级学科获取二级学科.
   * 
   * @param secondCategoryId
   * @throws GroupException
   */
  Discipline getTopDisciplinetById(Long secondCategoryId) throws GroupException;

  Discipline getSecondDisciplinetById(Long secondCategoryId) throws GroupException;

}
