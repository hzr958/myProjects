package com.smate.center.open.service.data.dealdirtydata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.group.OpenGroupUnionDao;
import com.smate.center.open.dao.union.his.OpenGroupUnionHisDao;
import com.smate.center.open.model.union.his.OpenGroupUnionHis;
import com.smate.center.open.service.data.ThirdDataTypeBase;

/**
 * 获取群组删除后 移动到历史关联表的记录 (通过token取数据 入口处openid使用公用的)
 * 
 * @author tsz
 *
 */
public class GetDelGroupUnionHisServiceImpl extends ThirdDataTypeBase {

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
    try {
      String token = paramet.get(OpenConsts.MAP_TOKEN).toString();
      // 最多一次返回50个数据
      List<OpenGroupUnionHis> list = openGroupUnionHisDao.getNeedDelUnionHisList(token, "2");
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
      if (list != null && list.size() > 0) {
        for (OpenGroupUnionHis grouphis : list) {
          try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("groupCode", grouphis.getGroupCode());
            dataList.add(data);
            // 重置状态
            saveDealGroupUnionHis(grouphis, 1);
          } catch (Exception e) {
            logger.error("获取历史openid新数据 的时候 处理群组数据出错 token:" + token + ",groupid:" + grouphis.getGroupId(), e);
            saveDealGroupUnionHis(grouphis, 9);// 处理异常
          }
        }
      }
      return successMap("scm-000 获取因群组删除 需要处理的数据成功", dataList);
    } catch (Exception e) {
      logger.error("获取历史openid新数据 的时候 处理群组数据出错 " + paramet.toString(), e);
      return errorMap("获取因群组删除 需要处理的数据失败", paramet, e.getMessage());
    }
  }

  private void saveDealGroupUnionHis(OpenGroupUnionHis his, Integer status) {
    his.setDealDate(new Date());
    his.setStatus(status);
    openGroupUnionHisDao.save(his);
  }
}
