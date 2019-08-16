package com.smate.web.psn.service.profile.psnListView;

import java.util.List;

import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 人员列表接口
 *
 * @author wsn
 *
 */
public interface PsnListViewService {

  /**
   * 人员列表显示处理总入口
   * 
   * @param paremeterData
   * @return
   */
  public void getPsnListViewInfo(PsnListViewForm form) throws ServiceException;
}
