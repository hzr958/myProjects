package com.smate.center.task.service.rol.quartz;

import java.util.List;

import com.smate.center.task.model.pdwh.pub.PdwhPubSourceDb;
import com.smate.center.task.model.pdwh.quartz.PdwhPubXml;
import com.smate.center.task.model.rol.quartz.RolPubIdTmp;
import com.smate.center.task.model.rol.quartz.RolPubXml;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;

public interface HandlePubListService {

  List<RolPubIdTmp> getRolPubId(Integer size);

  Long getPdwhPubId(Long rolPubId);

  PdwhPubXml getPdwhPubXml(Long pdwhPubId);

  RolPubXml getRolPubXml(Long rolPubId);

  PdwhPubSourceDb getPdwhPubSourceDb(Long pdwhPubId);

  void fillPubList(PubXmlDocument rolXmldocument, PdwhPubSourceDb pdwhPubSourceDb);

  void saveRolPubXml(RolPubXml rolPubXml);

  void praseSourcePubList(PubXmlDocument rolXmldocument);

  void saveOptResult(RolPubIdTmp rolPubIdTmp, Integer status);

}
