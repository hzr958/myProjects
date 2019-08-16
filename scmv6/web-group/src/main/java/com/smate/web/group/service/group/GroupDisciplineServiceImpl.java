package com.smate.web.group.service.group;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.group.dao.group.DisciplineDao;
import com.smate.web.group.exception.GroupException;
import com.smate.web.group.model.group.pub.Discipline;

@Service("groupDisciplineService")
@Transactional(rollbackFor = Exception.class)
public class GroupDisciplineServiceImpl implements GroupDisciplineService {
  @Autowired
  private DisciplineDao disciplineDao;

  /**
   * 获取一级学科.
   * 
   * 
   * @throws ServiceException
   */
  @Override
  public List<Map<Object, Object>> getTopDisciplineList() throws GroupException {
    try {
      return disciplineDao.getTopDisciplineLists();
    } catch (Exception e) {
      throw new GroupException("获取一级学科失败", e);
    }
  }

  /**
   * 根据一级学科获取二级学科.
   * 
   * @param topCategoryId
   * @throws ServiceException
   */
  @Override
  public List<Discipline> getSecondDisciplineListById(Long topCategoryId) throws GroupException {
    try {
      return disciplineDao.getsecondDisciplineLists(topCategoryId);
    } catch (Exception e) {
      throw new GroupException("获取二级学科失败", e);
    }
  }

  /**
   * 根据二级学科获取一级学科.
   * 
   * @param topCategoryId
   * @throws ServiceException
   */
  @Override
  public Discipline getTopDisciplinetById(Long secondCategoryId) throws GroupException {
    try {
      return disciplineDao.getTopDisciplineById(secondCategoryId);
    } catch (Exception e) {
      throw new GroupException("获取二级学科失败", e);
    }
  }

  /**
   * 根据二级学科id获取二级学科名称.
   * 
   * @param topCategoryId
   * @throws ServiceException
   */
  @Override
  public Discipline getSecondDisciplinetById(Long secondCategoryId) throws GroupException {
    try {
      return disciplineDao.getSecondDisciplinetById(secondCategoryId);
    } catch (Exception e) {
      throw new GroupException("获取二级学科失败", e);
    }
  }

}
