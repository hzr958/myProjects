package com.smate.sie.center.task.pdwh.service;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.center.task.dao.SiePubListDao;
import com.smate.sie.center.task.model.PubList;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.center.task.pub.enums.PubLibraryEnum;
import com.smate.sie.core.base.utils.pub.dto.PubSituationDTO;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

@Service("pubListService")
@Transactional(rollbackFor = Exception.class)
public class PubListServiceImpl implements PubListService {

  private static Logger logger = LoggerFactory.getLogger(PubListServiceImpl.class);
  @Autowired
  private SiePubListDao siePubListDao;

  @Override
  public PubList getPubList(Long pubId) throws SysServiceException {

    return this.siePubListDao.get(pubId);
  }

  @Override
  public void savePubList(PubList pubList) throws SysServiceException {
    this.siePubListDao.save(pubList);
  }

  @Override
  public PubList elementConvertPubList(Element node) throws SysServiceException {
    if (node == null) {
      return null;
    }
    String listEi = node.attributeValue("list_ei");
    String listSci = node.attributeValue("list_sci");
    String listIstp = node.attributeValue("list_istp");
    String listSsci = node.attributeValue("list_ssci");
    String listPku = node.attributeValue("list_pku");
    String listCscd = node.attributeValue("list_cscd");
    PubList pubList = new PubList();
    pubList.setListEi(NumberUtils.toInt(listEi));
    pubList.setListIstp(NumberUtils.toInt(listIstp));
    pubList.setListSci(NumberUtils.toInt(listSci));
    pubList.setListSsci(NumberUtils.toInt(listSsci));
    pubList.setListPku(NumberUtils.toInt(listPku));
    pubList.setListCscd(NumberUtils.toInt(listCscd));
    return pubList;
  }

  @Override
  public void saveOrUpdatePubList(PubList argsPubList) throws SysServiceException {
    if (argsPubList.getPubId() == null) {
      throw new SysServiceException("成果ID不能为空");
    }
    PubList pubList = this.getPubList(argsPubList.getPubId());
    if (pubList == null) {
      this.siePubListDao.save(argsPubList);
    } else {
      pubList.setListEi(argsPubList.getListEi());
      pubList.setListSci(argsPubList.getListSci());
      pubList.setListIstp(argsPubList.getListIstp());
      pubList.setListSsci(argsPubList.getListSsci());
      this.siePubListDao.save(pubList);
    }
  }

  @Override
  public PubList wrapPubList(String[] pubLists, String[] pubListsSource) {
    if (pubLists == null || pubLists.length < 1) {
      return null;
    }
    PubList pubList = new PubList();

    for (String pubList1 : pubLists) {
      if ("SCI".equalsIgnoreCase(pubList1)) {
        pubList.setListSci(1);
      }
      if ("ISTP".equalsIgnoreCase(pubList1)) {
        pubList.setListIstp(1);
      }
      if ("SSCI".equalsIgnoreCase(pubList1)) {
        pubList.setListSsci(1);
      }
      if ("EI".equalsIgnoreCase(pubList1)) {
        pubList.setListEi(1);
      }
      if ("PKU".equalsIgnoreCase(pubList1)) {
        pubList.setListPku(1);
      }
      if ("CSCD".equalsIgnoreCase(pubList1)) {
        pubList.setListCscd(1);
      }
    }
    if (pubListsSource == null || pubListsSource.length < 1) {
      return pubList;
    }
    for (String pubList1 : pubListsSource) {
      if ("SCI".equalsIgnoreCase(pubList1)) {
        pubList.setListSciSource(1);
      }
      if ("ISTP".equalsIgnoreCase(pubList1)) {
        pubList.setListIstpSource(1);
      }
      if ("SSCI".equalsIgnoreCase(pubList1)) {
        pubList.setListSsciSource(1);
      }
      if ("EI".equalsIgnoreCase(pubList1)) {
        pubList.setListEiSource(1);
      }
      if ("PKU".equalsIgnoreCase(pubList1)) {
        pubList.setListPku(1);
      }
      if ("CSCD".equalsIgnoreCase(pubList1)) {
        pubList.setListCscd(1);
      }
    }
    return pubList;
  }

  @Override
  public String convertPubListToString(PubList pubList) {
    if (pubList == null) {
      return null;
    }
    StringBuilder retStr = new StringBuilder();
    if (pubList.getListEi() != null && pubList.getListEi() == 1) {
      retStr.append(",EI");
    }
    if (pubList.getListSci() != null && pubList.getListSci() == 1) {
      retStr.append(",SCI");
    }
    if (pubList.getListIstp() != null && pubList.getListIstp() == 1) {
      retStr.append(",ISTP");
    }
    if (pubList.getListSsci() != null && pubList.getListSsci() == 1) {
      retStr.append(",SSCI");
    }
    if (pubList.getListCscd() != null && pubList.getListCscd() == 1) {
      retStr.append(",CSCD");
    }
    if (pubList.getListPku() != null && pubList.getListPku() == 1) {
      retStr.append(",PKU");
    }
    return retStr.length() > 0 ? retStr.substring(1) : retStr.toString();
  }

  @Override
  public String convertPubListSourceToString(PubList pubList) {
    if (pubList == null) {
      return null;
    }
    StringBuilder retStr = new StringBuilder();
    if (pubList.getListEiSource() != null && pubList.getListEiSource() == 1) {
      retStr.append(",EI");
    }
    if (pubList.getListSciSource() != null && pubList.getListSciSource() == 1) {
      retStr.append(",SCI");
    }
    if (pubList.getListIstpSource() != null && pubList.getListIstpSource() == 1) {
      retStr.append(",ISTP");
    }
    if (pubList.getListSsciSource() != null && pubList.getListSsciSource() == 1) {
      retStr.append(",SSCI");
    }
    if (pubList.getListCscd() != null && pubList.getListCscd() == 1) {
      retStr.append(",CSCD");
    }
    if (pubList.getListPku() != null && pubList.getListPku() == 1) {
      retStr.append(",PKU");
    }
    return retStr.length() > 0 ? retStr.substring(1) : retStr.toString();
  }


  @Override
  public PubList praseSourcePubList(PubXmlDocument doc) {
    // 保存期刊文章的收录情况
    if (doc.existsNode(PubXmlConstants.PUB_LIST_XPATH)) {
      PubList pubList = siePubListDao.get(doc.getPubId());
      if (pubList == null) {
        pubList = new PubList();
        pubList.setPubId(doc.getPubId());
      }
      String listEI = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei");
      if (StringUtils.isNotBlank(listEI)) {
        listEI = resetPubList(listEI);
        pubList.setListEi(NumberUtils.toInt(listEI));
        pubList.setListEiSource(NumberUtils.toInt(listEI));
      } else {
        pubList.setListEiSource(0);
        pubList.setListEi(0);
      }
      String listSCI = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci");
      if (StringUtils.isNotBlank(listSCI)) {
        listSCI = resetPubList(listSCI);
        pubList.setListSci(NumberUtils.toInt(listSCI));
        pubList.setListSciSource(NumberUtils.toInt(listSCI));
      } else {
        pubList.setListSciSource(0);
        pubList.setListSci(0);
      }
      String listISTP = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp");
      if (StringUtils.isNotBlank(listISTP)) {
        listISTP = resetPubList(listISTP);
        pubList.setListIstp(NumberUtils.toInt(listISTP));
        pubList.setListIstpSource(NumberUtils.toInt(listISTP));
      } else {
        pubList.setListIstpSource(0);
        pubList.setListIstp(0);
      }
      String listSSCI = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci");
      if (StringUtils.isNotBlank(listSSCI)) {
        listSSCI = resetPubList(listSSCI);
        pubList.setListSsci(NumberUtils.toInt(listISTP));
        pubList.setListSsciSource(NumberUtils.toInt(listISTP));
      } else {
        pubList.setListSsciSource(0);
        pubList.setListSsci(0);
      }
      String listCscd = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_cscd");
      if (StringUtils.isNotBlank(listCscd)) {
        listCscd = resetPubList(listCscd);
        pubList.setListCscd(NumberUtils.toInt(listCscd));
        pubList.setListCscdSource(NumberUtils.toInt(listCscd));
      } else {
        pubList.setListCscd(0);
        pubList.setListCscdSource(0);
      }
      String listPku = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_pku");
      if (StringUtils.isNotBlank(listPku)) {
        listPku = resetPubList(listPku);
        pubList.setListPku(NumberUtils.toInt(listPku));
        pubList.setListPkuSource(NumberUtils.toInt(listPku));
      } else {
        pubList.setListPku(0);
        pubList.setListPkuSource(0);
      }
      String listCssci = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_cssci");
      if (StringUtils.isNotBlank(listCssci)) {
        listCssci = resetPubList(listCssci);
        pubList.setListCssci(NumberUtils.toInt(listCssci));
        pubList.setListCssciSource(NumberUtils.toInt(listCssci));
      } else {
        pubList.setListCssci(0);
        pubList.setListCssciSource(0);
      }
      String other = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_other");
      if (StringUtils.isNotBlank(other)) {
        other = resetPubList(other);
        pubList.setListOther(NumberUtils.toInt(other));
      } else {
        pubList.setListOther(0);
      }
      siePubListDao.save(pubList);
      return pubList;
    }
    return null;
  }

  private String resetPubList(String pubList) {
    String result = "0";
    if (StringUtils.isNotBlank(pubList) && !NumberUtils.isNumber(pubList)) {
      if ("是".equals(pubList))
        result = "1";
      if ("否".equals(pubList))
        result = "0";
    } else if (StringUtils.isNotBlank(pubList) && NumberUtils.isNumber(pubList)) {
      return pubList;
    }
    return result;
  }

  @Override
  public List<PubList> getAllPubList() {
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public PubList prasePubList(PubJsonDTO pubJson) {
    PubList situation = siePubListDao.get(pubJson.pubId);
    if (situation == null) {
      situation = new PubList();
      situation.setPubId(pubJson.pubId);
    }
    if (pubJson.situations != null && pubJson.situations.size() > 0) {
      List<PubSituationDTO> situationList =
          JacksonUtils.jsonToCollection(pubJson.situations.toJSONString(), List.class, PubSituationDTO.class);
      for (PubSituationDTO pubSituationDTO : situationList) {
        String libraryName = pubSituationDTO.getLibraryName();
        if (StringUtils.isBlank(libraryName)) {
          continue;
        } else {
          if (libraryName.equals(PubLibraryEnum.EI.desc)) {
            situation.setListEi(pubSituationDTO.isSitStatus() ? 1 : 0);
            situation.setListEiSource(pubSituationDTO.isSitStatus() ? 1 : 0);
          } else if (libraryName.equals(PubLibraryEnum.SCIE.desc) || libraryName.equals("SCI")) {
            situation.setListSci(pubSituationDTO.isSitStatus() ? 1 : 0);
            situation.setListSciSource(pubSituationDTO.isSitStatus() ? 1 : 0);
          } else if (libraryName.equals(PubLibraryEnum.ISTP.desc)) {
            situation.setListIstp(pubSituationDTO.isSitStatus() ? 1 : 0);
            situation.setListIstpSource(pubSituationDTO.isSitStatus() ? 1 : 0);
          } else if (libraryName.equals(PubLibraryEnum.SSCI.desc)) {
            situation.setListSsci(pubSituationDTO.isSitStatus() ? 1 : 0);
            situation.setListSsciSource(pubSituationDTO.isSitStatus() ? 1 : 0);
          } else if (libraryName.equals(PubLibraryEnum.CSSCI.desc)) {
            situation.setListCssci(pubSituationDTO.isSitStatus() ? 1 : 0);
            situation.setListCssciSource(pubSituationDTO.isSitStatus() ? 1 : 0);
          } else if (libraryName.equals(PubLibraryEnum.PKU.desc)) {
            situation.setListPku(pubSituationDTO.isSitStatus() ? 1 : 0);
            situation.setListPkuSource(pubSituationDTO.isSitStatus() ? 1 : 0);
          } else if (libraryName.equals(PubLibraryEnum.CSCD.desc)) {
            situation.setListCscd(pubSituationDTO.isSitStatus() ? 1 : 0);
            situation.setListCscdSource(pubSituationDTO.isSitStatus() ? 1 : 0);
          } else if (libraryName.equals(PubLibraryEnum.OTHER.desc)) {
            situation.setListOther(pubSituationDTO.isSitStatus() ? 1 : 0);
          }
        }
      }
      siePubListDao.save(situation);
      return situation;
    } else {
      // 若表单新收集的数据没有收录，则说明最新数据是没有收录的，故删除库中的记录。
      this.siePubListDao.deletePubList(pubJson.pubId);
    }
    return null;
  }

}
