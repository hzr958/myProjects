package com.smate.center.task.service.sns.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhFullTextFileDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.quartz.PdwhFullTextFile;


@Service("pdwhFullTextService")
@Transactional(rollbackFor = Exception.class)
public class PdwhFullTextServiceImpl implements PdwhFullTextService {
  @Autowired
  private PdwhFullTextFileDao pdwhFullTextFiledao;

  @Override
  public void saveFullTextFile(PdwhFullTextFile fulltextfile) throws ServiceException {

    pdwhFullTextFiledao.save(fulltextfile);

  }



}
