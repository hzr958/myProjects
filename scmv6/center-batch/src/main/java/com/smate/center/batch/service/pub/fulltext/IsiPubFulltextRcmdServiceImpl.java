package com.smate.center.batch.service.pub.fulltext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rcmd.pub.IsiPubFulltextRcmdDao;
import com.smate.center.batch.model.rcmd.pub.IsiPubFulltextRcmd;


/**
 * isi成果全文推荐 服务接口
 * 
 * @author tsz
 *
 */
@Service("isiPubFulltextRcmdService")
@Transactional(rollbackFor = Exception.class)
public class IsiPubFulltextRcmdServiceImpl implements IsiPubFulltextRcmdService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private IsiPubFulltextRcmdDao isiPubFulltextRcmdDao;

  @Override
  public void saveIsiPubFulltextRcmd(IsiPubFulltextRcmd obj) {
    isiPubFulltextRcmdDao.save(obj);
  }
}
