package com.smate.web.v8pub.service.fileimport.extract;

import java.util.ArrayList;
import java.util.List;

import com.smate.web.v8pub.vo.PendingImportPubVO;

/**
 * 成果保存数据表单
 * 
 * @author aijiangbin
 * @date 2018年7月30日
 */
public class PubSaveDataForm {

  List<PendingImportPubVO> list = new ArrayList<>();

  public List<PendingImportPubVO> getList() {
    return list;
  }

  public void setList(List<PendingImportPubVO> list) {
    this.list = list;
  }



}
