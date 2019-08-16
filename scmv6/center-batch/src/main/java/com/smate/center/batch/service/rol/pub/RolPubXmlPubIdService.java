package com.smate.center.batch.service.rol.pub;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.RolPubXmlPubId;

public interface RolPubXmlPubIdService {

  List<RolPubXmlPubId> gets(Long startPubId, Integer size) throws ServiceException;

  void handlePubXml(List<RolPubXmlPubId> pubIds) throws ServiceException;

}
