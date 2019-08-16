package com.smate.center.batch.service.rol.pub;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubHtml;
import com.smate.center.batch.model.rol.pub.PubHtmlRefresh;

public interface PubHtmlService {

  void savePubHtml(PubHtml pubHtml) throws ServiceException;

  void updatePubHtmlRefresh(Long pubId, Integer tempCode) throws ServiceException;

  List<PubHtmlRefresh> findNeedRefresh(Integer tempCode, int size) throws ServiceException;

  PubHtml findPubHtml(Long pubId, Integer tempCode) throws ServiceException;

  List<PubHtmlRefresh> findNeedRefresh(int size) throws ServiceException;

}
