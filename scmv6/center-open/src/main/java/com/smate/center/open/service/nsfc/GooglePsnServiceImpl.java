package com.smate.center.open.service.nsfc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.institution.InstitutionDao;
import com.smate.center.open.dao.nsfc.NsfcwsPersonDao;
import com.smate.center.open.model.institution.Institution;
import com.smate.center.open.model.nsfc.NsfcwsPerson;
import com.smate.core.base.utils.string.JnlFormateUtils;

/**
 * 
 * @zjh GooglePsnServiceImpl
 *
 */
@Service(value = "googlePsnService")
@Transactional(rollbackFor = Exception.class)
public class GooglePsnServiceImpl implements GooglePsnService {
  @Autowired
  private ConstSurNameService constSurNameService;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private NsfcwsPersonDao nsfcwsPersonDao;
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public List<NsfcwsPerson> getNsfcwsPerson(String psnName, String insName, String email) throws Exception {
    return nsfcwsPersonDao.queryNsfcwsPerson(psnName, insName, email);
  }
}
