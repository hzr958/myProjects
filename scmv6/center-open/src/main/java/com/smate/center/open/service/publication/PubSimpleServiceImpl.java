package com.smate.center.open.service.publication;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.publication.PubSimpleDao;
import com.smate.center.open.model.publication.PubSimple;

/**
 * 
 * @author lxz
 *
 */
@Service("pubSimpleService")
@Transactional(rollbackFor = Exception.class)
public class PubSimpleServiceImpl implements PubSimpleService {

  @Autowired
  private PubSimpleDao pubSimpleDao;

  @Override
  public PubSimple checkPubNewest(Long pubId, Date importDate) throws Exception {

    return pubSimpleDao.checkPubNewest(pubId, importDate);
  }

}
