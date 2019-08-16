package com.smate.center.open.service.data.pub.insid;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.consts.ConstPubTypeDao;
import com.smate.center.open.model.consts.ConstPubType;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.web.v8pub.dto.PubSituationDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2018-04-13 公开的获取个人所有的成果显示信息，
 * 
 * @author aijiangbin
 *
 */

@Transactional(rollbackFor = Exception.class)
public class PublicObtainShowPubInfoServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstPubTypeDao constPubTypeDao;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    // 具体业务
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Long psnId = NumberUtils.toLong(paramet.get("psnId").toString());
    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPsnId(psnId);
    pubQueryDTO.setServiceType(V8pubQueryPubConst.QUERY_PUB_SITUATION_LIST);
    Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
    int count = (int) result.get("totalCount");
    Map<String, Object> temp1 = new HashMap<String, Object>();
    temp1.put("count", count);
    dataList.add(temp1);
    List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("resultList");
    if (resultList != null && resultList.size() > 0) {
      if (resultList != null && resultList.size() > 0) {
        for (Map<String, Object> pubMap : resultList) {
          try {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
            Map<String, Object> map = new HashMap<>();
            map.put(V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubMap.get("pubId").toString()));
            map.put("serviceType", V8pubQueryPubConst.QUERY_OPEN_SNS_PUB);
            Map<String, Object> pubInfoMap = (Map<String, Object>) getRemotePubInfo(map, SERVER_URL);
            if (pubInfoMap != null) {
              PubDetailVO pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubInfoMap));
              dataMap = parseXmlToMap(pubDetailVO);
            }
            dataMap.put("pubShortUrl", pubMap.get("pubIndexUrl"));
            dataList.add(dataMap);
          } catch (Exception e) {
            logger.error("构建成果map数据出错 pubId=" + pubMap.get("pubId"), e);
          }
        }
      }
    }
    return successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }

  /**
   * authors zh_title en_title zh_keywords en_keywords doi doi_url list_info zh_source en_source
   * publish_year full_text_img_url product_mark pubShortUrl 成果短地址 【新增】
   * 
   * @param pubDetailVO
   * @return
   * @throws Exception
   */
  private Map<String, Object> parseXmlToMap(PubDetailVO pubDetailVO) throws Exception {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if (pubDetailVO != null) {
      Long pubId = pubDetailVO.getPubId();

      dataMap.put("authors", pubDetailVO.getAuthorName());
      dataMap.put("zh_title", pubDetailVO.getTitle());
      dataMap.put("en_title", pubDetailVO.getTitle());
      // 关键词
      dataMap.put("zh_keywords", IrisStringUtils.subMaxLengthString(pubDetailVO.getKeywords(), 200));
      dataMap.put("en_keywords", IrisStringUtils.subMaxLengthString(pubDetailVO.getKeywords(), 200));
      // doi
      String doi = pubDetailVO.getDoi();
      dataMap.put("doi", doi);
      dataMap.put("doi_url", StringUtils.isNotBlank(doi) ? "http://dx.doi.org/" + doi : "");
      // list_info
      fillListInfo(pubDetailVO, dataMap);
      dataMap.put("zh_source", pubDetailVO.getBriefDesc());
      dataMap.put("en_source", pubDetailVO.getBriefDesc());
      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(pubDetailVO.getPublishDate()));

      if (pubDetailVO.getFullText() != null) {
        dataMap.put("full_text_img_url", pubDetailVO.getFullText().getThumbnailPath());
      } else {
        dataMap.put("full_text_img_url", "");
      }
      ConstPubType pubType = constPubTypeDao.get(pubDetailVO.getPubType());
      dataMap.put("pub_type", pubType != null ? pubType.getZhName() : "");
      dataMap.put("product_mark", pubDetailVO.getFundInfo());
    }
    return dataMap;
  }

  /**
   * 得到成果收录情况
   * 
   * @return
   */
  public void fillListInfo(PubDetailVO pubDetailVO, Map<String, Object> dataMap) {
    List<PubSituationDTO> sitations = pubDetailVO.getSituations();
    String listInfoListStr = "";
    if (sitations != null && sitations.size() > 0) {
      for (PubSituationDTO sitation : sitations) {
        if (sitation.isSitStatus()) {
          listInfoListStr += sitation.getLibraryName() + ",";
        }
      }
      if (StringUtils.isNotBlank(listInfoListStr)) {
        listInfoListStr = listInfoListStr.substring(0, listInfoListStr.length() - 1).toLowerCase();
      }
    }
    dataMap.put("list_info", listInfoListStr);
  }

}
