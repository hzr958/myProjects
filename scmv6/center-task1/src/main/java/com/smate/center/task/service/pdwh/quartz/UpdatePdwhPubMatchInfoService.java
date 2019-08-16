package com.smate.center.task.service.pdwh.quartz;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;

import com.smate.center.task.psn.model.PersonPmName;
import com.smate.core.base.utils.model.security.Person;

public interface UpdatePdwhPubMatchInfoService {

  public List<Long> getNeedUpdateAddrConstId(Integer size);

  void startUpdateAddrConst(Long constId) throws Exception;

  List<Long> getNeedUpdatePsnConstId(Integer size);

  void updatePubAuthorSnsPsnRecord(Person person) throws Exception;

  public void updateStatus(Long constId, int type, int status);

  Person getPersonIns(Long psnId);

  void deleteUnconfirmedRecordByPsn(Long psnId) throws Exception;

  public void generalPsnPmName(Person person);

  public List<PersonPmName> getPsnPmName(Long personId);

  public List<Long> tmpGetNeedUpdatePsnConstId(Integer size);

  public void tmpGeneralPsnPmName(Long psnId);

  boolean matchPsnPubs(PersonPmName personPmName, List<Map<String, Object>> searchPsnPubs) throws SolrServerException;

  public void saveOtherTaskRecord(Long psnId);

}
