package com.smate.web.psn.model.psncnf;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.psn.model.psncnf.PsnConfigBrief;
import com.smate.core.base.psn.model.psncnf.PsnConfigContact;
import com.smate.core.base.psn.model.psncnf.PsnConfigEdu;
import com.smate.core.base.psn.model.psncnf.PsnConfigExpertise;
import com.smate.core.base.psn.model.psncnf.PsnConfigList;
import com.smate.core.base.psn.model.psncnf.PsnConfigMoudle;
import com.smate.core.base.psn.model.psncnf.PsnConfigPosition;
import com.smate.core.base.psn.model.psncnf.PsnConfigPrj;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.model.psncnf.PsnConfigTaught;
import com.smate.core.base.psn.model.psncnf.PsnConfigWork;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 个人配置：封装.
 * 
 * @author zhuangyanming
 * 
 */
public class PsnCnfBuild implements PsnCnfBase, Serializable {
  private static final long serialVersionUID = -4071265408238524231L;
  private PsnConfig cnf;
  private PsnConfigMoudle cnfMoudle;
  private PsnConfigBrief cnfBrief;
  private PsnConfigPosition cnfPosition;
  private PsnConfigContact cnfContact;
  private Map<Long, PsnConfigEdu> cnfEdu;
  private PsnConfigExpertise cnfExpertise;
  private PsnConfigList cnfList;
  private Map<Long, PsnConfigPrj> cnfPrj;
  private Map<Long, PsnConfigPub> cnfPub;
  private PsnConfigTaught cnfTaught;
  private Map<Long, PsnConfigWork> cnfWork;
  private final Long psnId;

  public PsnCnfBuild(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return psnId
   */
  @JsonIgnore
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @return cnfId
   */
  @Override
  @JsonIgnore
  public Long getCnfId() {
    if (cnf != null) {
      return cnf.getCnfId();
    } else {
      return 0L;
    }
  }

  public String toJSON() {
    return JacksonUtils.jsonObjectSerializer(this);
  }

  /**
   * @return cnf
   */
  @JsonIgnore
  public PsnConfig getCnf() {
    return cnf;
  }

  /**
   * @return cnfMoudle
   */
  public PsnConfigMoudle getCnfMoudle() {
    return cnfMoudle;
  }

  /**
   * @return cnfBrief
   */
  public PsnConfigBrief getCnfBrief() {
    return cnfBrief;
  }

  /**
   * @return cnfContact
   */
  public PsnConfigContact getCnfContact() {
    return cnfContact;
  }

  /**
   * @return cnfEdu
   */
  public Map<Long, PsnConfigEdu> getCnfEdu() {
    return cnfEdu;
  }

  /**
   * @return cnfExpertise
   */
  public PsnConfigExpertise getCnfExpertise() {
    return cnfExpertise;
  }

  /**
   * @return cnfList
   */
  @JsonIgnore
  public PsnConfigList getCnfList() {
    return cnfList;
  }

  /**
   * @return cnfPrj
   */
  public Map<Long, PsnConfigPrj> getCnfPrj() {
    return cnfPrj;
  }

  /**
   * @return cnfPub
   */
  public Map<Long, PsnConfigPub> getCnfPub() {
    return cnfPub;
  }

  /**
   * @return cnfTaught
   */
  public PsnConfigTaught getCnfTaught() {
    return cnfTaught;
  }

  /**
   * @return cnfWork
   */
  public Map<Long, PsnConfigWork> getCnfWork() {
    return cnfWork;
  }

  /**
   * @param cnf 要设置的 cnf
   */
  public void setCnf(PsnConfig cnf) {
    this.cnf = cnf;
  }

  /**
   * @param cnfMoudle 要设置的 cnfMoudle
   */
  public void setCnfMoudle(PsnConfigMoudle cnfMoudle) {
    this.cnfMoudle = cnfMoudle;
  }

  /**
   * @param cnfBrief 要设置的 cnfBrief
   */
  public void setCnfBrief(PsnConfigBrief cnfBrief) {
    this.cnfBrief = cnfBrief;
  }

  /**
   * @param cnfContact 要设置的 cnfContact
   */
  public void setCnfContact(PsnConfigContact cnfContact) {
    this.cnfContact = cnfContact;
  }

  /**
   * @param cnfEdu 要设置的 cnfEdu
   */
  public void setCnfEdu(Map<Long, PsnConfigEdu> cnfEdu) {
    this.cnfEdu = cnfEdu;
  }

  /**
   * @param cnfExpertise 要设置的 cnfExpertise
   */
  public void setCnfExpertise(PsnConfigExpertise cnfExpertise) {
    this.cnfExpertise = cnfExpertise;
  }

  /**
   * @param cnfList 要设置的 cnfList
   */
  public void setCnfList(PsnConfigList cnfList) {
    this.cnfList = cnfList;
  }

  /**
   * @param cnfPrj 要设置的 cnfPrj
   */
  public void setCnfPrj(Map<Long, PsnConfigPrj> cnfPrj) {
    this.cnfPrj = cnfPrj;
  }

  /**
   * @param cnfPub 要设置的 cnfPub
   */
  public void setCnfPub(Map<Long, PsnConfigPub> cnfPub) {
    this.cnfPub = cnfPub;
  }

  /**
   * @param cnfTaught 要设置的 cnfTaught
   */
  public void setCnfTaught(PsnConfigTaught cnfTaught) {
    this.cnfTaught = cnfTaught;
  }

  /**
   * @param cnfWork 要设置的 cnfWork
   */
  public void setCnfWork(Map<Long, PsnConfigWork> cnfWork) {
    this.cnfWork = cnfWork;
  }

  /**
   * @return cnfPosition
   */
  public PsnConfigPosition getCnfPosition() {
    return cnfPosition;
  }

  /**
   * @param cnfPosition 要设置的 cnfPosition
   */
  public void setCnfPosition(PsnConfigPosition cnfPosition) {
    this.cnfPosition = cnfPosition;
  }

}
