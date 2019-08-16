package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果xml.
 * 
 * @author lichangwen
 * 
 */
@Repository
@SuppressWarnings("rawtypes")
public class PdwhPubXmlListDao extends PdwhHibernateDao {

  /**
   * 获取基准库cnki成果xml.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings({"unchecked"})
  public Map<String, Object> getCnkiPubXml(Long pubId) {
    String sql = "select t.pub_id,t.xml_data from cnki_pub_extend t where t.pub_id=?";
    List list = super.queryForList(sql, new Object[] {pubId});
    return CollectionUtils.isEmpty(list) ? null : (Map<String, Object>) list.get(0);
  }

  /**
   * 获取基准库cnipr成果xml.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings({"unchecked"})
  public Map<String, Object> getCniprPubXml(Long pubId) {
    String sql = "select t.pub_id,t.xml_data from cnipr_pub_extend t where t.pub_id=?";
    List list = super.queryForList(sql, new Object[] {pubId});
    return CollectionUtils.isEmpty(list) ? null : (Map<String, Object>) list.get(0);
  }

  /**
   * 获取基准库wangfang成果xml.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings({"unchecked"})
  public Map<String, Object> getWangFangPubXml(Long pubId) {
    String sql = "select t.pub_id,t.xml_data from wf_pub_extend t where t.pub_id=?";
    List list = super.queryForList(sql, new Object[] {pubId});
    return CollectionUtils.isEmpty(list) ? null : (Map<String, Object>) list.get(0);
  }

  /**
   * 获取基准库isi成果xml.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings({"unchecked"})
  public Map<String, Object> getIsiPubXml(Long pubId) {
    String sql = "select t.pub_id,t.xml_data from isi_pub_extend t where t.pub_id=?";
    List list = super.queryForList(sql, new Object[] {pubId});
    return CollectionUtils.isEmpty(list) ? null : (Map<String, Object>) list.get(0);
  }

  /**
   * 获取基准库ei成果xml.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings({"unchecked"})
  public Map<String, Object> getEiPubXml(Long pubId) {
    String sql = "select t.pub_id,t.xml_data from ei_pub_extend t where t.pub_id=?";
    List list = super.queryForList(sql, new Object[] {pubId});
    return CollectionUtils.isEmpty(list) ? null : (Map<String, Object>) list.get(0);
  }

  /**
   * 获取基准库scopus成果xml.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings({"unchecked"})
  public Map<String, Object> getScopusPubXml(Long pubId) {
    String sql = "select t.pub_id,t.xml_data from sps_pub_extend t where t.pub_id=?";
    List list = super.queryForList(sql, new Object[] {pubId});
    return CollectionUtils.isEmpty(list) ? null : (Map<String, Object>) list.get(0);
  }

  /**
   * 获取基准库cnkipat成果xml.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings({"unchecked"})
  public Map<String, Object> getCnkipatPubXml(Long pubId) {
    String sql = "select t.pub_id,t.xml_data from cnkipat_pub_extend t where t.pub_id=?";
    List list = super.queryForList(sql, new Object[] {pubId});
    return CollectionUtils.isEmpty(list) ? null : (Map<String, Object>) list.get(0);
  }

}
