package com.smate.web.v8pub.service.sns;
/**
 * 成果修改服务实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.sns.PubModifyDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubModifyPO;

@Service
@Transactional(rollbackFor = Exception.class)
public class PubModifyServiceImpl implements PubModifyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubModifyDAO pubModifyDAO;

  @Override
  public void addPubModifyRecord(Long pubId, Long psnId, String desc) throws ServiceException {
    try {
      PubModifyPO po = new PubModifyPO();
      po.setPubId(pubId);
      po.setModifyPsnId(psnId);
      po.setModifyDesc(desc);
      po.setGmtCreate(new Date());
      pubModifyDAO.save(po);
    } catch (Exception e) {
      logger.error("成果修改服务：添加成果修改记录异常,pubId" + pubId, e);
      throw new ServiceException(e);
    }

  }

}
