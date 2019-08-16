package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.sie.center.task.model.SiePdwhInsAddrConst;
import com.smate.sie.center.task.model.SiePdwhInsAddrConstRefresh;

/***
 * 获取SIE库中的单位别名表中的数据信息
 * 
 * @author 叶星源
 * @Date 20180717
 */
public interface SiePdwhInsAddrConstService {

  /***
   * 获取需要刷新的数据
   */
  List<SiePdwhInsAddrConst> getNeedRefresh(int size);

  /***
   * 更新单位别名表和单位别名临时表的操作
   */
  void refreshInsName(SiePdwhInsAddrConstRefresh pdwhInsAddrConstRefresh);

  /***
   * 更新临时导入数据表
   */
  void saveStRefresh(SiePdwhInsAddrConstRefresh pdwhInsAddrConstRefresh);

  /***
   * 根据参数获取需要入库的临时表中的数据
   * 
   * @param batchSize
   */
  List<SiePdwhInsAddrConstRefresh> loadNeedDealSizeData(int batchSize);

}
