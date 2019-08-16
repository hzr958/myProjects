package com.smate.web.psn.model.resume;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 
 * @author liqinghua
 * 
 */
public class ConfigItem implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1732471381191570274L;
  private Integer isShow = 0;
  private Integer isShowOld = -1;
  private Integer isAll = 0;
  private List<ConfigElement> configElements;
  private Integer seqNum = 1;

  public ConfigItem() {
    super();
  }

  public ConfigItem(Integer isShow) {
    super();
    this.isShow = isShow;
  }

  public ConfigItem(Integer isShow, Integer isAll, Integer seqNum) {
    super();
    this.isShow = isShow;
    this.isAll = isAll;
    this.seqNum = seqNum;
  }

  public ConfigItem(Integer isShow, Integer isAll) {
    super();
    this.isShow = isShow;
    this.isAll = isAll;
  }

  public ConfigItem(Integer isShow, Integer isAll, List<ConfigElement> configElements, Integer seqNum) {
    super();
    this.isShow = isShow;
    this.isAll = isAll;
    this.configElements = configElements;
    this.seqNum = seqNum;
  }

  @SuppressWarnings("unchecked")
  public static void main(String[] args) {
    List<ConfigElement> resumeEelements = new ArrayList<ConfigElement>();
    resumeEelements.add(new ConfigElement("111", 1));
    resumeEelements.add(new ConfigElement("222", 2));
    resumeEelements.add(new ConfigElement());
    ConfigItem item = new ConfigItem(1, 1, resumeEelements, 1);

    String str = JSONObject.fromObject(item).toString();
    JSONObject jobj = JSONObject.fromObject(str);
    Map<String, Class> m = new HashMap<String, Class>();
    m.put("configElements", ConfigElement.class);
    ConfigItem item2 = (ConfigItem) JSONObject.toBean(jobj, ConfigItem.class, m);
    System.out.println(item2.getConfigElements().get(0).getElemCode());
  }

  public Integer getIsShow() {
    return isShow;
  }

  public void setIsShow(Integer isShow) {
    this.isShow = isShow;
  }

  public Integer getIsAll() {
    return isAll;
  }

  public void setIsAll(Integer isAll) {
    this.isAll = isAll;
  }

  public List<ConfigElement> getConfigElements() {
    return configElements;
  }

  public void setConfigElements(List<ConfigElement> resumeElements) {
    this.configElements = resumeElements;
  }

  public Integer getSeqNum() {
    return seqNum;
  }

  public void setSeqNum(Integer seqNum) {
    this.seqNum = seqNum;
  }

  public Integer getIsShowOld() {
    return isShowOld;
  }

  public void setIsShowOld(Integer isShowOld) {
    this.isShowOld = isShowOld;
  }

}
