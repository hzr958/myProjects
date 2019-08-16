package com.smate.center.open.service.data.dealdirtydata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.bpo.psn.merge.PersonMergeDao;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.dao.group.OpenGroupUnionDao;
import com.smate.center.open.dao.union.his.OpenGroupUnionHisDao;
import com.smate.center.open.dao.union.his.OpenUserUnionHisDao;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.bpo.psn.merge.PersonMerge;
import com.smate.center.open.model.union.his.OpenGroupUnionHis;
import com.smate.center.open.model.union.his.OpenUserUnionHis;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.login.ThirdLoginService;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;

/**
 * 获取人员合并后 合并记录数据 (关联历史表记录数据) (通过token取数据 入口处openid使用公用的)
 * 
 * @author tsz
 *
 */
public class GetPersonMergeDataServiceImpl extends ThirdDataTypeBase {


  @Autowired
  private OpenUserUnionHisDao openUserUnionHisDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private PersonMergeDao personMergeDao;
  @Autowired
  private ThirdLoginService thirdLoginService;
  @Autowired
  private OpenGroupUnionDao openGroupUnionDao;
  @Autowired
  private OpenGroupUnionHisDao openGroupUnionHisDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    // 获取合并记录数据 并拼接到最新的openid
    try {
      String token = paramet.get(OpenConsts.MAP_TOKEN).toString();
      // 需要限制 最多给多少个 最多给到50个newopenid
      List<OpenUserUnionHis> hisList = openUserUnionHisDao.getAllNeedDealList(token);
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
      if (hisList != null && hisList.size() > 0) {
        for (OpenUserUnionHis his : hisList) {
          try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("oldOpenId", his.getOpenId());
            PersonMerge personMerge = personMergeDao.getPersonMerge(his.getPsnId());
            Long newOpenId = getNewOpenId(token, personMerge.getSavePsnId());
            data.put("newOpenId", newOpenId);
            // 处理人员合并后 群组的关联记录问题问题 (如果有关联记录 需要更新对应的openid)
            dealGroupUnionHis(token, his.getOpenId(), personMerge.getSavePsnId(), newOpenId);
            dataList.add(data);
            // 标记为已经处理
            saveDealUserUnionHis(his, 1);
          } catch (OpenException e) {
            logger.error("获取被合并后的openid出错 oldOpenId=" + his.getOpenId(), e);
            saveDealUserUnionHis(his, 9);// 处理异常
          }
        }
      }
      return successMap("scm-000  获取历史openid新数据成功,请处理相关关联记录", dataList);
    } catch (Exception e) {
      logger.error("获取历史openid新数据 失败 " + paramet.toString(), e);
      return errorMap("获取历史openid新数据 失败! ", paramet, e.toString());
    }
  }

  private void saveDealUserUnionHis(OpenUserUnionHis his, Integer status) {
    his.setDealDate(new Date());
    his.setStatus(status);
    openUserUnionHisDao.save(his);
  }

  /**
   * 处理群组关联数据 (人员合并后 移动到历史表的数据)
   * 
   * @param token
   * @param oldOpenId
   * @param newPsnId
   * @param newOpenId
   */
  private void dealGroupUnionHis(String token, Long oldOpenId, Long newPsnId, Long newOpenId) {
    try {
      List<OpenGroupUnionHis> list = openGroupUnionHisDao.getNeedReUnionHisList(oldOpenId, token, "1");
      if (list != null && list.size() > 0) {
        for (OpenGroupUnionHis grouphis : list) {
          try { // 保存新的关联记录 暂时不保存新的关联关系 只清理关联记录
            /*
             * OpenGroupUnion ogu = new OpenGroupUnion(); ogu.setCreateTime(new Date());
             * ogu.setGroupCode(grouphis.getGroupCode()); ogu.setGroupId(grouphis.getGroupId());
             * ogu.setToken(token); ogu.setOwnerOpenId(newOpenId); ogu.setOwnerPsnId(newPsnId);
             * openGroupUnionDao.save(ogu);
             */
            // 重置状态
            saveDealGroupUnionHis(grouphis, 1);
          } catch (Exception e) {
            logger.error("获取历史openid新数据 的时候 处理群组数据出错 token:" + token + " ,oldOpenId:" + oldOpenId + ",groupid:"
                + grouphis.getGroupId(), e);
            saveDealGroupUnionHis(grouphis, 9);// 处理异常
          }
        }
      }
    } catch (Exception e) {
      logger.error("获取历史openid新数据 的时候 处理群组数据出错 token:" + token + " ,oldOpenId:" + oldOpenId, e);
      throw new OpenException("获取历史openid新数据 的时候 处理群组数据出错 token:" + token + " ,oldOpenId:" + oldOpenId, e);
    }
  }

  private void saveDealGroupUnionHis(OpenGroupUnionHis his, Integer status) {
    his.setDealDate(new Date());
    his.setStatus(status);
    openGroupUnionHisDao.save(his);
  }

  /**
   * 获取保留人openid
   * 
   * @param token
   * @param personMerge
   * @return
   */
  private Long getNewOpenId(String token, Long newPsnId) {
    try {
      Long newOpenId = openUserUnionDao.getOpenIdByPsnId(newPsnId);
      if (newOpenId != null) {// 保留人 有关联记录
        // 保留人 有关联记录 但是没有与当前系统关联
        OpenUserUnion opUU = openUserUnionDao.getOpenUserUnion(newOpenId, token);
        // 保存关联记录
        if (opUU == null) {
          opUU = new OpenUserUnion();
          opUU.setOpenId(newOpenId);
          opUU.setPsnId(newPsnId);
          opUU.setToken(token);
          opUU.setCreateDate(new Date());
          opUU.setCreateType(OpenConsts.OPENID_CREATE_TYPE_6);
          openUserUnionDao.saveOpenUserUnion(opUU);
        }
      } else {// 保留人没有openid
        newOpenId = thirdLoginService.getOpenId(token, newPsnId, OpenConsts.OPENID_CREATE_TYPE_6);
      }
      return newOpenId;
    } catch (Exception e) {
      logger.error("获取历史openid新数据 的时候  获取新openid出错 token=" + token + ",newPsnId=" + newPsnId, e);
      throw new OpenException("获取历史openid新数据 的时候  获取新openid出错 token=" + token + ",newPsnId=" + newPsnId, e);
    }
  }


}
