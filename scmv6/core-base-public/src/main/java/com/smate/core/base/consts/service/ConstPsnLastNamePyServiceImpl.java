package com.smate.core.base.consts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.psnname.ConstPsnLastNamePyDao;


@Transactional(rollbackFor = Exception.class)
@Service("ConstPsnLastNamePyService")
public class ConstPsnLastNamePyServiceImpl implements ConstPsnLastNamePyService {

  @Autowired
  private ConstPsnLastNamePyDao constPsnLastNamePyDao;

  @Override
  public List<String> findPinyinByZhWord(String zhWord) {
    return constPsnLastNamePyDao.findPinyinByZhWord(zhWord);
  }

  @Override
  public String findFirstPinyinByZhWord(String zhWord) {
    return constPsnLastNamePyDao.findFirstPinyinByZhWord(zhWord);
  }

}
