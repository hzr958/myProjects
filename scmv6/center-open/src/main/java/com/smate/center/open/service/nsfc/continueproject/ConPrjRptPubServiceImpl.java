package com.smate.center.open.service.nsfc.continueproject;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.nsfc.continueproject.ConPrjRptPubDao;
import com.smate.center.open.model.consts.ConstPubType;
import com.smate.center.open.model.nsfc.continueproject.ConPrjRptPub;
import com.smate.center.open.service.consts.ConstPubTypeService;



/**
 * 延续报告成果服务实现
 * 
 * @author tsz
 *
 */
@Service("conPrjRptPubService")
@Transactional(rollbackFor = Exception.class)
public class ConPrjRptPubServiceImpl implements ConPrjRptPubService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConPrjRptPubDao conPrjRptPubDao;

  @Autowired
  private ConstPubTypeService constPubTypeService;

  /**
   * 获取成果
   */
  @Override
  public List<ConPrjRptPub> findRptPubs(Long psnId, Long nsfcRptId) throws Exception {
    try {
      List<ConPrjRptPub> list = this.conPrjRptPubDao.findRptPubs(psnId, nsfcRptId);
      if (CollectionUtils.isNotEmpty(list)) {
        for (ConPrjRptPub rptPub : list) {
          ConstPubType pubType = this.constPubTypeService.get(rptPub.getPubType());
          rptPub
              .setPubTypeName(StringUtils.isNotBlank(pubType.getZhName()) ? pubType.getZhName() : pubType.getEnName());
        }
      }
      return list;
    } catch (Exception e) {
      logger.error("查找延续项目报告成果出现错误，psnId=" + psnId + ",nsfcRptId=" + nsfcRptId, e);
      throw new Exception("查找延续项目报告成果出现错误，psnId=" + psnId + ",nsfcRptId=" + nsfcRptId, e);
    }
  }



}
