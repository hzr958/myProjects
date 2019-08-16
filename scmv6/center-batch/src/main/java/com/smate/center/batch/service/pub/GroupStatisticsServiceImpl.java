package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.psn.RolPsnInsDao;
import com.smate.center.batch.dao.rol.pub.GroupCooperationStatisticsDao;
import com.smate.center.batch.dao.rol.pub.GroupPsnRelationDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.center.batch.model.rol.pub.GroupCooperationStatistics;
import com.smate.center.batch.model.rol.pub.GroupPsnRelation;
import com.smate.center.batch.service.pub.mq.SnsSyncGroupStatisticsMessage;

/**
 * 合作分析：群组统计信息service.
 * 
 * @author zyx
 * 
 */
@Service("groupStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class GroupStatisticsServiceImpl implements GroupStatisticsService {

  @Autowired
  private GroupCooperationStatisticsDao statisticsDao;
  @Autowired
  private GroupPsnRelationDao relationDao;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;

  /**
   * 合作分析：添加或更新群组统计信息.
   */
  // 此方法适用群组的各种操作：添加群组、删除群组、添加群组成员、删除群组成员等等
  @Override
  public void handleAddOrUpdateSync(SnsSyncGroupStatisticsMessage g) throws ServiceException {

    try {
      GroupCooperationStatistics st = statisticsDao.findByGroupId(g.getGroupId());

      if (st == null) {
        st = new GroupCooperationStatistics();
        st.setGroupId(g.getGroupId());
      }

      st.setNodeId(g.getNodeId());
      st.setGroupName(g.getGroupName());
      st.setGroupCategory(g.getGroupCategory());
      st.setCreateDate(g.getCreateDate());
      st.setSumMemebers(g.getSumMemebers());
      st.setSumActivity(g.getSumActivity());
      st.setSumBiz(g.getSumBiz());
      st.setVisitCount(g.getVisitCount());

      statisticsDao.save(st);

      // 合作分析群组成员关系记录
      List<GroupPsnRelation> list = relationDao.findGroupPsnRelationList(g.getGroupId());
      // 保留合作分析存在的记录
      List<GroupPsnRelation> existList = new ArrayList<GroupPsnRelation>();

      // 清理合作分析旧群组成员数据
      // 群组成员发生变化
      for (GroupPsnRelation relation : list) {
        existList.add(relation);// 临时记录数据
        // 单位和部门已经不存在
        boolean isUnitNotExist = true;
        List<RolPsnIns> rList = rolPsnInsDao.findRolPsnInsList(relation.getPsnId());
        if (rList.size() > 0) {
          // 检查人员单位关系与人员群组关系是否一致
          for (RolPsnIns rolPsnIns : rList) {
            Long psnId = rolPsnIns.getPsnId();
            Long groupId = relation.getGroupId();
            Integer nodeId = relation.getNodeId();
            Long insId = rolPsnIns.getPk().getInsId();
            Long unitId = rolPsnIns.getUnitId();
            Long superUnitId = rolPsnIns.getSuperUnitId();

            if (relation.equals(new GroupPsnRelation(psnId, groupId, nodeId, insId, unitId, superUnitId))) {
              isUnitNotExist = false;
              break;
            }
          }
        } else {// 没有单位也保留群组和人员的关系
          Long psnId = relation.getPsnId();
          Long groupId = relation.getGroupId();
          Integer nodeId = relation.getNodeId();
          Long insId = NOTEXIST_INSID;
          Long unitId = null;
          Long superUnitId = null;
          if (relation.equals(new GroupPsnRelation(psnId, groupId, nodeId, insId, unitId, superUnitId))) {
            isUnitNotExist = false;
          }
        }

        // 群组成员已经不存在
        boolean isMemberNotExist = true;
        // 检查人员群组关系是否发生变化
        for (Long psnId : g.getMemberPsnIdList()) {
          if (psnId.equals(relation.getPsnId())) {
            isMemberNotExist = false;
            break;
          }
        }

        // 单位或人员不在群组中，都属于脏数据，需要清除
        if (isUnitNotExist || isMemberNotExist) {
          existList.remove(relation);// 移除要删除的数据
          relationDao.delete(relation);
        }
      }

      // 新增合作分析群组成员数据
      for (Long checkPsnId : g.getMemberPsnIdList()) {
        List<RolPsnIns> rList = rolPsnInsDao.findRolPsnInsList(checkPsnId);

        if (rList.size() > 0) {
          // 检查人员单位关系与人员群组关系是否一致
          for (RolPsnIns rolPsnIns : rList) {
            Long psnId = rolPsnIns.getPsnId();
            Long groupId = g.getGroupId();
            Integer nodeId = g.getNodeId();
            Long insId = rolPsnIns.getPk().getInsId();
            Long unitId = rolPsnIns.getUnitId();
            Long superUnitId = rolPsnIns.getSuperUnitId();
            // 不存在记录，则新增
            if (!existList.contains(new GroupPsnRelation(psnId, groupId, nodeId, insId, unitId, superUnitId))) {
              // 新增
              GroupPsnRelation relation = relationDao.findGroupPsnRelation(groupId, psnId, insId);
              if (relation == null) {// 解决唯一索引冲突问题
                relation = new GroupPsnRelation();
              }
              relation.setPsnId(checkPsnId);
              relation.setGroupId(groupId);
              relation.setNodeId(nodeId);
              relation.setInsId(insId);
              relation.setUnitId(unitId);
              relation.setSuperUnitId(superUnitId);
              relationDao.save(relation);
            }
          }
        } else {
          Long psnId = checkPsnId;
          Long groupId = g.getGroupId();
          Integer nodeId = g.getNodeId();
          Long insId = NOTEXIST_INSID;
          Long unitId = null;
          Long superUnitId = null;
          // 不存在记录，则新增
          if (!existList.contains(new GroupPsnRelation(psnId, groupId, nodeId, insId, unitId, superUnitId))) {
            // 新增
            GroupPsnRelation relation = relationDao.findGroupPsnRelation(groupId, psnId, insId);
            if (relation == null) {// 解决唯一索引冲突问题
              relation = new GroupPsnRelation();
            }
            relation.setPsnId(checkPsnId);
            relation.setGroupId(groupId);
            relation.setNodeId(nodeId);
            relation.setInsId(insId);
            relation.setUnitId(unitId);
            relation.setSuperUnitId(superUnitId);
            relationDao.save(relation);
          }
        }

      }
    } catch (DaoException e) {
      throw new ServiceException("合作分析：添加或更新群组统计信息出错", e);
    }
  }

  /**
   * 按群组ID删除合作分析数据.
   */
  @Override
  public void handleDelSync(Long groupId) throws ServiceException {
    try {

      // 删除合作分析统计
      GroupCooperationStatistics st = statisticsDao.findByGroupId(groupId);
      if (st != null) {
        statisticsDao.delete(st);
      }

      // 删除合作分析群组成员关系记录
      relationDao.delAllRelationByGroupId(groupId);

    } catch (DaoException e) {
      throw new ServiceException("合作分析：删除群组统计信息出错", e);
    }
  }


}
