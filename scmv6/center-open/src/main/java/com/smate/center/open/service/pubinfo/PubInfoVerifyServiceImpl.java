package com.smate.center.open.service.pubinfo;

import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.PubGenreConstants;
import com.smate.center.open.dao.data.isis.BaseJournalDao;
import com.smate.center.open.dao.pdwh.jnl.BaseJournalTitleDao;
import com.smate.center.open.dao.psn.PersonPmNameDao;
import com.smate.center.open.dao.publication.PubVerifyLogDao;
import com.smate.center.open.model.pdwh.jnl.BaseJournal2;
import com.smate.center.open.model.pdwh.jnl.BaseJournalTitleTo;
import com.smate.center.open.model.publication.PubVerifyLog;
import com.smate.center.open.service.data.pub.verify.*;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.Computeclass;
import com.smate.core.base.utils.string.JnlFormateUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.url.RestUtils;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts2.util.URLDecoderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 成果信息验证实现类
 *
 * @author aijiangbin
 * @create 2018-11-12 16:42
 **/

@Service("pubInfoVerifyService")
@Transactional(rollbackFor = Exception.class)
public class PubInfoVerifyServiceImpl implements PubInfoVerifyService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  public static final  String  dot_split = "(,)|(\\s+and\\s+)";
  public static final  String  sem_split = "(;)|(\\s+and\\s+)";
  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Resource
  private BaseJournalDao baseJournalDao;
  @Resource
  private BaseJournalTitleDao baseJournalTitleDao;
  @Resource
  private PersonDao personDao;
  @Resource
  private PersonPmNameDao personPmNameDao;
  @Resource
  private PubVerifyLogDao pubVerifyLogDao;


  /**
   * 名字统一 转小写匹配 去除特殊字符空格
   *
   * @param name
   * @param compareName
   * @return
   */
  @Override
  public boolean compareNames(String name, String compareName) {
    name = XmlUtil.getCleanAuthorName4(name);
    compareName = XmlUtil.getCleanAuthorName4(compareName);
    if (StringUtils.isBlank(name) || StringUtils.isBlank(compareName)) {
      return false;
    }
    Boolean isNameCh = ServiceUtil.isChineseStr(name);
    Boolean isCompareNameCh = ServiceUtil.isChineseStr(compareName);
    if (isNameCh && isCompareNameCh) {
      // 全是中文,
      return name.equals(compareName);
    } else if (!isNameCh && !isCompareNameCh) {
      // 全是英文
      Boolean b = compareNameEn(name, compareName);
      return b;
    } else if (isNameCh && !isCompareNameCh) {
      // 中文转英文，在使用英文匹配
      Boolean fullnames = getChEnNameBoolean(name, compareName);
      if (fullnames != null)
        return fullnames;
    } else {
      // 中文转英文，在使用英文匹配
      Boolean fullnames = getChEnNameBoolean(compareName, name);
      if (fullnames != null)
        return fullnames;
    }
    return false;
  }


  /**
   * 全是英文名的比较 拆分英文 wuyong ,成果是wu yong 成功 CHUNHUA ZHAO 和 Zhao, R-C-H 成果
   *
   * @param ename
   * @param otherEname
   * @return
   */
  public boolean compareNameEn(String ename, String otherEname) {
    // 算出 ename 的各种组合？
    String enameNotEmp = ename.replaceAll("\\s+", "");
    String otherEnameNotEmp = otherEname.replaceAll("\\s+", "");
    if (enameNotEmp.equalsIgnoreCase(otherEnameNotEmp)) {
      return true;
    }
    Set<String> enameList = getEameGroup(ename);
    Set<String> otherEnameList = getEameGroup(otherEname);
    for (String name1 : enameList) {
      for (String name2 : otherEnameList) {
        if (name1.equalsIgnoreCase(name2)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 得到英文名的组合例如： //CaiGuo Yu Yu CaiGuo //CaiGuo Y Yu C CHUNHUA ZHAO 和 Zhao, R-C-H 特例
   *
   * @param ename
   * @return
   */
  public Set<String> getEameGroup(String ename) {
    Set<String> enameList = new HashSet<>();
    String[] split = ename.split(" ");
    if (split.length == 2) {
      // CaiGuo Yu
      enameList.add(split[0] + " " + split[1]);// CaiGuo Yu
      enameList.add(split[0] + " " + split[1].substring(0, 1));// CaiGuo Y
      enameList.add(split[0] + " " + "r" + split[1].substring(0, 1));// CaiGuo Y
      enameList.add(split[1] + " " + split[0]);// Yu CaiGuo
      enameList.add(split[1] + " " + "r" + split[0].substring(0, 1));// Yu C
    } else {
      enameList.add(ename);
    }
    return enameList;
  }

  @Override
  public Map doVerfiyPaper(PaperInfo paperInfo,String participantNames) {
    // 匹配成果
    Map<String, Object> resultMap = null;
    //多条结果集
    Map<Integer,Map> scoreMap  = new HashMap<>();
    List<Long> dupPubIds = getDupPubIds(paperInfo ,0);
    List<Long> dupPubIdsForTitle = new ArrayList<>();
    if(StringUtils.isNotBlank(paperInfo.getDoi())){
      dupPubIdsForTitle = getDupPubIds(paperInfo ,1);
      if(CollectionUtils.isNotEmpty(dupPubIdsForTitle)) dupPubIds.addAll(dupPubIdsForTitle);
    }
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      for (Long pubId : dupPubIds) {
        PubDetailVO pubDetailVO = getPdwhPubInfo(pubId);
        //在比较时直接过滤掉，不用查询的论文
        pubDetailVO = excludePub(pubDetailVO);
        PaperItemMsg type = new PaperItemMsg();
        PaperItemMsg itemMsg = new PaperItemMsg();
        Boolean jnameMatchFlag = true;// 期刊匹配标志
        if (pubDetailVO != null) {

          // 第-1步， 验证成果类型， 专利的直接忽略
          boolean patentPub = isPatentPub(paperInfo, pubDetailVO, itemMsg, type);
          if(patentPub){
            if(resultMap == null){
              resultMap = buildPatentPubResult(paperInfo, pubDetailVO, type, itemMsg);
            }
            continue;
          }
          // 第0步：验证title
          verifyTitle(paperInfo, pubDetailVO,itemMsg, type);
          //0.1 title 不匹配时， 拿标题匹配成果，
          // a)标题匹配到了成果，则继续原来逻辑。
          // b)未匹配到成果，返回未找不到记录   临时方案  只拿一条条
          if(!type.getTitle().equals(PubInfoVerifyConstant.SUCCESS.toString()) && StringUtils.isNotBlank(paperInfo.getTitle())
              && StringUtils.isNotBlank(paperInfo.getDoi())){
            List<Long> tempDupPubIds = dupPubIdsForTitle;
            if(CollectionUtils.isNotEmpty(tempDupPubIds)){
              pubDetailVO = getPdwhPubInfo(tempDupPubIds.get(0));
            }
            type.setTitle("");
            itemMsg.setTitle("");
            if(CollectionUtils.isEmpty(tempDupPubIds) || pubDetailVO == null){
              pubDetailVO = null ;
              resultMap = buildNotFindPdwhPubResult(paperInfo, pubDetailVO, type, itemMsg);
              continue;
            }else{
              verifyTitle(paperInfo, pubDetailVO,itemMsg, type);
            }
          }
          //处理作者信息
          pubDetailVO.setAuthorNames( PubDetailVoUtil.dealAuthorNames(pubDetailVO.getAuthorNames()) );
          participantNames = PubDetailVoUtil.dealAuthorNames(participantNames );

          // 第一步：验证doi
          verifyDoi(paperInfo, pubDetailVO ,itemMsg, type);

          // 第二步：验证期刊名，没找到返回不确定
          verifyJournalName(paperInfo, pubDetailVO ,itemMsg, type);
          // 第三步：验证作者
          verifyAuthorNames(paperInfo, pubDetailVO ,itemMsg, type,participantNames);

          // 第四步：验证发表年份
          verifyPublishYear(paperInfo, pubDetailVO ,itemMsg, type);

          // 第五步：验证：资助基金号
          verifyFundInfo(paperInfo, pubDetailVO,itemMsg, type);

          if (isMatchSuccess(type,paperInfo)) {
            // 完全匹配到成果
            resultMap = PubInfoVerifyServiceImpl.buildResultMap(PubInfoVerifyConstant.SUCCESS , paperInfo.getKeyCode());
            addPubInfo(resultMap, pubDetailVO, type);
            resultMap.put(PubInfoVerifyConstant.ITEM_MSG, itemMsg);
            break;
          } else {
            if(type.getTitle().equals("2") || type.getDoi().equals("2")
                || type.getFundingInfo().equals("2") || type.getPublishYear().equals("2")
                || type.getAuthorNames().equals("2") ){
              resultMap = PubInfoVerifyServiceImpl.buildResultMap(PubInfoVerifyConstant.UNMATCH, paperInfo.getKeyCode());
            }else{
              //验证期刊名，没找到返回不确定
              resultMap = PubInfoVerifyServiceImpl.buildResultMap(PubInfoVerifyConstant.NOT_SURE, paperInfo.getKeyCode());
            }
            addPubInfo(resultMap, pubDetailVO, type);
            resultMap.put(PubInfoVerifyConstant.ITEM_MSG, itemMsg);
            //计算匹配结果分数
            caculateScoreMap(resultMap, scoreMap, type);
          }
        }
      }
    }
    if(resultMap == null || resultMap.size() == 0){
      resultMap = PubInfoVerifyServiceImpl.buildResultMap(PubInfoVerifyConstant.NOT_SURE, paperInfo.getKeyCode());
      PaperItemMsg itemMsg = new PaperItemMsg();
      itemMsg.setOtherErrorMsg(OpenMsgCodeConsts.SCM_2051);
      addPubInfo(resultMap, null, null);
      resultMap.put(PubInfoVerifyConstant.ITEM_MSG, itemMsg);
    }
    //存在重复成果，并且没有匹配到
    if(scoreMap.size()>0 && Integer.parseInt(resultMap.get("itemStatus").toString()) != PubInfoVerifyConstant.SUCCESS){
      Set<Integer> scoreList = scoreMap.keySet();
      Iterator<Integer> iterator = scoreList.iterator();
      int maxScore = 0;
      while(iterator.hasNext()){
        Integer next = iterator.next();
        if(next>maxScore) maxScore = next;
      }
      if(scoreMap.get(maxScore) != null ){
        resultMap = scoreMap.get(maxScore);
      }
    }
    return resultMap;
  }

  private PubDetailVO  excludePub(PubDetailVO pubDetailVO){
    if(pubDetailVO == null) return null ;
    if(pubDetailVO.getSrcDbId() != null && pubDetailVO.getSrcDbId() == 36) return  null ;
    return pubDetailVO ;
  }

  /**
   * 没有查到基准库成果的结果
   * @param paperInfo
   * @param pubDetailVO
   * @param type
   * @param itemMsg
   * @return
   */
  private Map<String, Object> buildNotFindPdwhPubResult(PaperInfo paperInfo, PubDetailVO pubDetailVO, PaperItemMsg type,
      PaperItemMsg itemMsg) {
    Map<String, Object> resultMap;// 查询成果为空， 出现了异常，或者错误的成id
    resultMap = PubInfoVerifyServiceImpl.buildResultMap(PubInfoVerifyConstant.NOT_SURE, paperInfo.getKeyCode());
    addPubInfo(resultMap, pubDetailVO, type);
    itemMsg.setOtherErrorMsg(OpenMsgCodeConsts.SCM_2051);
    resultMap.put(PubInfoVerifyConstant.ITEM_MSG, itemMsg);
    return resultMap;
  }

  /**
   * 构建专利成果结果
   * @param paperInfo
   * @param pubDetailVO
   * @param type
   * @param itemMsg
   * @return
   */
  private Map<String, Object> buildPatentPubResult(PaperInfo paperInfo, PubDetailVO pubDetailVO, PaperItemMsg type,
      PaperItemMsg itemMsg) {
    Map<String, Object> resultMap;//
    resultMap = PubInfoVerifyServiceImpl.buildResultMap(PubInfoVerifyConstant.NOT_SURE, paperInfo.getKeyCode());
    addPubInfo(resultMap, pubDetailVO, type);
    itemMsg.setOtherErrorMsg(OpenMsgCodeConsts.SCM_2071);
    resultMap.put(PubInfoVerifyConstant.ITEM_MSG, itemMsg);
    return resultMap;
  }
  private void caculateScoreMap(Map<String, Object> resultMap, Map<Integer, Map> scoreMap, PaperItemMsg type) {
    int score = 0;
    if(type.getTitle().equals("1")){
      score ++;
    }
    if(type.getDoi().equals("1")){
      score ++;
    }
    if(type.getFundingInfo().equals("1")){
      score ++;
    }
    if(type.getPublishYear().equals("1")){
      score ++;
    }
    if(type.getAuthorNames().equals("1")){
      score ++;
    }
    if(type.getJournalName().equals("1")){
      score ++;
    }
    scoreMap.put(score,resultMap);
  }

  /**
   *
   * @param type
   * @param paperInfo
   * @return
   */
  public boolean isMatchSuccess(PaperItemMsg type , PaperInfo paperInfo){
    if(!type.getTitle().equals("1"))return false ;
    if(!type.getAuthorNames().equals("1"))return false ;
    if(!type.getDoi().equals("1") && paperInfo.getDoiCompare())return false ;
    if(!type.getFundingInfo().equals("1") && paperInfo.getFundingInfoCompare())return false ;
    if(!type.getPublishYear().equals("1") && paperInfo.getPublishYearCompare())return false ;
    if(!type.getJournalName().equals("1") && paperInfo.getJournalNameCompare())return false ;

    return true;
  }

  /**
   * "correlationData":"{"title":"1234","doi":"xxx","journalName":"xxx",
   * "authorNames":"xxx","publishYear":"xxx","fundingInfo":"xxx"}
   *
   * @param resMap
   * @param pub
   */
  public void addPubInfo(Map<String, Object> resMap, PubDetailVO pub, PaperItemMsg type) {
    Map<String, Object> correlationData = new HashMap<>();
    if (pub != null) {
      correlationData.put("title", pub.getTitle());
      correlationData.put("authorNames", pub.getAuthorNamesBak());
      correlationData.put("publishYear", pub.getPublishYear());
      correlationData.put("fundingInfo", pub.getFundInfo());
      correlationData.put("doi", pub.getDoi());
      StringBuffer sb = new StringBuffer();
      if (pub.getPubType() == 4) {
        JournalInfoDTO pubTypeInfo = (JournalInfoDTO) pub.getPubTypeInfo();
        if (pubTypeInfo.getJid() != null) {
          BaseJournal2 baseJournal = baseJournalDao.getBaseJournal(pubTypeInfo.getJid());
          if (baseJournal != null&&(StringUtils.isNotBlank(baseJournal.getTitleEn()) || StringUtils.isNotBlank(baseJournal.getTitleXx()))) {
            sb.append(
                StringUtils.isNotBlank(baseJournal.getTitleEn()) ? baseJournal.getTitleEn() : baseJournal.getTitleXx());
          }
        } else if (StringUtils.isNotBlank(pubTypeInfo.getName())) {
          sb.append(pubTypeInfo.getName());
        }
      }
      correlationData.put("journalName", sb.toString());
      resMap.put("correlationData", correlationData);
      resMap.put("type", type);
    }
  }

  /**
   * 验证title
   *
   * @param paperInfo
   * @param pubDetailVO
   * @return
   */
  public  void   verifyTitle(PaperInfo paperInfo, PubDetailVO pubDetailVO , PaperItemMsg itemMsg ,PaperItemMsg type) {


    String title = PubHashUtils.cleanTitle(paperInfo.getTitle());
    String cTitle = PubHashUtils.cleanTitle(pubDetailVO.getTitle());
    if(StringUtils.isBlank(title) || StringUtils.isBlank(cTitle)){
      itemMsg.setTitle(OpenMsgCodeConsts.SCM_2063);
      type.setTitle(PubInfoVerifyConstant.NOT_SURE.toString());
      return;
    }

    if ( title.equals(cTitle)) {
      type.setTitle(PubInfoVerifyConstant.SUCCESS.toString());
    }else{
      //在判断相似度
      double result = Computeclass.SimilarDegree(title, cTitle);
      if(result >= 0.8){
        type.setTitle(PubInfoVerifyConstant.SUCCESS.toString());
      }else{
        itemMsg.setTitle(OpenMsgCodeConsts.SCM_2046);
        type.setTitle(PubInfoVerifyConstant.UNMATCH.toString());
      }

    }

  }

  /**
   * 验证 专利
   *
   * @param paperInfo
   * @param pubDetailVO
   * @return
   */
  public  boolean   isPatentPub(PaperInfo paperInfo, PubDetailVO pubDetailVO , PaperItemMsg itemMsg ,PaperItemMsg type) {
    if(pubDetailVO.getPubType() != null && pubDetailVO.getPubType() == 5){
      return true ;
    }
    return false ;
  }

  /**
   * 验证doi
   *
   * @param paperInfo
   * @param pubDetailVO
   * @return
   */
  public void verifyDoi(PaperInfo paperInfo, PubDetailVO pubDetailVO,PaperItemMsg itemMsg ,PaperItemMsg type) {
    //不匹配信息
    if(!paperInfo.getDoiCompare()) return ;
    if(StringUtils.isBlank(paperInfo.getDoi()) || StringUtils.isBlank(pubDetailVO.getDoi())){
      itemMsg.setDoi(OpenMsgCodeConsts.SCM_2059);
      type.setDoi(PubInfoVerifyConstant.NOT_SURE.toString());
      return ;
    }
    String  pubDoi = cleanDoi(pubDetailVO.getDoi()) ;
    String paperDoi = cleanDoi(paperInfo.getDoi()) ;
    if (pubDoi.contains(paperDoi) || paperDoi.contains(pubDoi)) {
      type.setDoi(PubInfoVerifyConstant.SUCCESS.toString());
    }else{
      itemMsg.setDoi(OpenMsgCodeConsts.SCM_2047);
      type.setDoi(PubInfoVerifyConstant.UNMATCH.toString());
    }
  }

  /**
   * 获取干净的doi .hash  和hash查重相关
   *
   * @param doi
   * @return Long
   */
  public static String cleanDoiHash(String doi) {
    if (StringUtils.isBlank(doi))
      return "";
    doi = XmlUtil.getTrimBlankLower(doi);
    String doiUrl = "doi.org/";
    int urlIdx = doi.indexOf(doiUrl);
    if (urlIdx > -1) {
      doi = doi.substring(urlIdx);
      doi = doi.replace(doiUrl, "");
    }
    doi = doi.replaceAll("\\/", "");
    String doiUrl2 = "doi:";
    if (doi.startsWith(doiUrl2)) {
      doi = doi.replace(doiUrl2, "");
    }
    return doi;
  }

  /**
   * 获取干净的doi . 比较时用
   *
   * @param doi
   * @return Long
   */
  public static String cleanDoi(String doi) {
    if (StringUtils.isBlank(doi))
      return "";
    doi = XmlUtil.getTrimBlankLower(doi);
    String doiUrl = "doi.org/";
    int urlIdx = doi.indexOf(doiUrl);
    if (urlIdx > -1) {
      doi = doi.substring(urlIdx);
      doi = doi.replace(doiUrl, "");
    }
    doi = doi.replaceAll("\\/", "");
    String doiUrl2 = "doi";
    if (doi.startsWith(doiUrl2)) {
      doi = doi.replace(doiUrl2, "");
    }
    //只保留字母和特殊字符
    //先反转义  &nbsp;
    doi = org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(doi);
    doi = doi.replaceAll("[^a-z0-9]","");
    return doi;
  }

  /**
   * 验证期刊
   *
   * @param paperInfo
   * @param pubDetailVO
   * @return
   */
  public void verifyJournalName(PaperInfo paperInfo, PubDetailVO pubDetailVO
      ,PaperItemMsg itemMsg ,PaperItemMsg type) {

    //不匹配信息
    if(!paperInfo.getJournalNameCompare()) return ;
    String journalName = "";
    if(StringUtils.isBlank(paperInfo.getJournalName()) || pubDetailVO.getPubType() != 4 ){
      itemMsg.setJournalName(OpenMsgCodeConsts.SCM_2060);
      type.setJournalName(PubInfoVerifyConstant.NOT_SURE.toString());
      return ;
    }
    if(pubDetailVO.getPubType() == 4 ){
      JournalInfoDTO pubTypeInfo = (JournalInfoDTO) pubDetailVO.getPubTypeInfo();
      journalName = pubTypeInfo.getName();
      if (pubTypeInfo == null ||( StringUtils.isBlank(pubTypeInfo.getName()) && pubTypeInfo.getJid() == null)) {
        itemMsg.setJournalName(OpenMsgCodeConsts.SCM_2060);
        type.setJournalName(PubInfoVerifyConstant.NOT_SURE.toString());
        return ;
      }
    }
    // 先匹配 jid
    Boolean flag = matchPdwhJournal(paperInfo.getJournalName() , pubDetailVO.getJid());
    flag = flag? flag : matchJname(paperInfo.getJournalName(),journalName) ;
    if(flag){
      type.setJournalName(PubInfoVerifyConstant.SUCCESS.toString());
      return ;
    }else{
      itemMsg.setJournalName(OpenMsgCodeConsts.SCM_2048);
      type.setJournalName(PubInfoVerifyConstant.NOT_SURE.toString());
    }



  }

  public static  String unescapeStr(String s){
    s = StringEscapeUtils.unescapeHtml(s);
    try {
      s = URLDecoderUtil.decode(s, Charset.defaultCharset().toString());
    }catch (Exception e){
      //异常吃掉
      System.out.println(e.toString());
    }
    return s;
  }

  private Boolean matchPdwhJournal(String jname , Long jid){
    if(jid == null) return false ;
    BaseJournal2 baseJournal = baseJournalDao.getBaseJournal(jid);
    boolean flag = false ;
    if(baseJournal !=null){
      String titleEn = baseJournal.getTitleEn();
      String titleXx = baseJournal.getTitleXx();
      flag = matchJname(jname ,titleEn);
      if(flag) return  flag ;
      flag = matchJname(jname ,titleXx);
      if(flag) return  flag ;
    }
    List<BaseJournalTitleTo> lists = baseJournalTitleDao.findByJnlId(jid);
    if(CollectionUtils.isNotEmpty(lists)){
      for(BaseJournalTitleTo titleTo : lists){
        flag = matchJname(jname ,titleTo.getTitleXx());
        if(flag) return  flag ;
        flag = matchJname(jname ,titleTo.getTitleXxAlias());
        if(flag) return  flag ;
        flag = matchJname(jname ,titleTo.getTitleAbbr());
        if(flag) return  flag ;
        flag = matchJname(jname ,titleTo.getTitleAbbrAlias());
        if(flag) return  flag ;
        flag = matchJname(jname ,titleTo.getTitleEn());
        if(flag) return  flag ;
        flag = matchJname(jname ,titleTo.getTitleEnAlias());
        if(flag) return  flag ;
      }
    }

    return false ;
  }
  private  boolean matchJname(String jnameA, String jnameB){
    if(StringUtils.isBlank(jnameA) || StringUtils.isBlank(jnameB)){
      return false ;
    }
    jnameA = unescapeStr(jnameA);
    jnameB = unescapeStr(jnameB);
    jnameA = JnlFormateUtils.getStrAlias(jnameA);
    jnameB = JnlFormateUtils.getStrAlias(jnameB);
    if(jnameA.contains(jnameB)){
      return true ;
    }
    if(jnameB.contains(jnameA)){
      return true ;
    }
    return false ;
  }

  /**
   * 验证期刊
   *
   * @param paperInfo
   * @param pubDetailVO
   * @return
   */
  public void verifyPublishYear(PaperInfo paperInfo, PubDetailVO pubDetailVO
      ,PaperItemMsg itemMsg ,PaperItemMsg type) {

    //不匹配信息
    if(!paperInfo.getPublishYearCompare()) return ;
    if(StringUtils.isBlank(paperInfo.getPublishYear()) ||  pubDetailVO.getPublishYear() == null){
      itemMsg.setPublishYear(OpenMsgCodeConsts.SCM_2061);
      type.setPublishYear(PubInfoVerifyConstant.NOT_SURE.toString());
      return ;
    }

    if( paperInfo.getPublishYear().equals(pubDetailVO.getPublishYear().toString())){
      type.setPublishYear(PubInfoVerifyConstant.SUCCESS.toString());
    }else{
      type.setPublishYear(PubInfoVerifyConstant.UNMATCH.toString());
      itemMsg.setPublishYear(OpenMsgCodeConsts.SCM_2049);
    }

  }


  /**
   * 名字长度大于5
   * @param paperInfo
   * @param pubDetailVO
   * @param participantNames
   * @return
   */
  public boolean  nameGtLengt5(PaperInfo paperInfo, PubDetailVO pubDetailVO,String participantNames){

    String[] names = participantNames.split(sem_split);
    names = PubInfoVerifyUtil.dealBlankAuthorsNames(names);
    for(String name : names){
      Boolean isNameCh = ServiceUtil.isChineseStr(name);
      if(isNameCh && name.length() >5){
        return true ;
      }
    }

    String[] names2 = pubDetailVO.getAuthorNames().split(sem_split);
    names2 = PubInfoVerifyUtil.dealBlankAuthorsNames(names2);
    for(String name : names2){
      Boolean isNameCh = ServiceUtil.isChineseStr(name);
      if(isNameCh && name.length() >5){
        return true ;
      }
    }

    String[] names3 = {};
    names3 = paperInfo.getAuthorNames().split(sem_split);
    names3 = PubInfoVerifyUtil.dealBlankAuthorsNames(names3);
    for(String name : names3){
      Boolean isNameCh = ServiceUtil.isChineseStr(name);
      if(isNameCh && name.length() >5){
        return true ;
      }
    }

    return  false ;
  }

  /**
   * 验证作者
   *
   * @param paperInfo
   * @param pubDetailVO
   * @return
   */
  private void verifyAuthorNames(PaperInfo paperInfo, PubDetailVO pubDetailVO
      ,PaperItemMsg itemMsg ,PaperItemMsg type,String participantNames) {

    if(StringUtils.isBlank(paperInfo.getAuthorNames()) || StringUtils.isBlank(pubDetailVO.getAuthorNames())){
      itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2064);
      type.setAuthorNames(PubInfoVerifyConstant.NOT_SURE.toString());
      return ;
    }
    //不成功还要判断名字是否过长
    boolean lengt5 = nameGtLengt5(paperInfo, pubDetailVO, participantNames);
    // 判断 成果中是否包含项目成员其中一个
    boolean containNames = authorNamesContainNames(paperInfo.getAuthorNames(), participantNames);
    if(!containNames){
      if(lengt5){
        itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2072);
        type.setAuthorNames(PubInfoVerifyConstant.NOT_SURE.toString());
        return;
      }
      itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2058);
      type.setAuthorNames(PubInfoVerifyConstant.NOT_SURE.toString());
      return ;
    }
    // 判断  基准库是否包含项目成员其中一个
    containNames = authorNamesContainNames(pubDetailVO.getAuthorNames(), participantNames);
    if(!containNames){
      if(lengt5){
        itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2072);
        type.setAuthorNames(PubInfoVerifyConstant.NOT_SURE.toString());
        return;
      }
      itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2070);
      type.setAuthorNames(PubInfoVerifyConstant.UNMATCH.toString());
      return ;
    }

    String[] authorNames = paperInfo.getAuthorNames().split(sem_split);
    authorNames = PubInfoVerifyUtil.dealBlankAuthorsNames(authorNames);
    String[] matchAuthorNames = pubDetailVO.getAuthorNames().split(sem_split);
    matchAuthorNames = PubInfoVerifyUtil.dealBlankAuthorsNames(matchAuthorNames);
    //以参与人为标准， 循环参与人， 报告作者其次
    String[] participantNamesArr = participantNames.split(sem_split);
    boolean matchFlag = true ;
    boolean indexBefore = false ;
    StringBuffer  errorPartname =  new StringBuffer();
    for(String partname : participantNamesArr){
      List<Integer> authorNameIndexs = findAuthorNameIndexs(authorNames, partname);
      if(authorNameIndexs.size() == 0) continue;
      List<Integer> matchAuthorNameIndexs = findAuthorNameIndexs(matchAuthorNames, partname);
      boolean exist = CollectionUtils.containsAny(authorNameIndexs, matchAuthorNameIndexs);
      if(exist){
        continue;
      }else{
        matchFlag = false ;
        errorPartname.append(","+partname);
        type.setAuthorNames(PubInfoVerifyConstant.UNMATCH.toString());
        if(matchAuthorNameIndexs.size()>0){
          int  mainIdx  = matchAuthorNameIndexs.get(0);
          int  cmpIdx = authorNameIndexs.get(0);
          if(cmpIdx < mainIdx){// 小于基准库的 表示提前了
            indexBefore = true ;
          }
        }
      }
    }
    //匹配成功
    if(matchFlag){
      type.setAuthorNames(PubInfoVerifyConstant.SUCCESS.toString());
      // 检查名字个数
      checkNameCount(authorNames,matchAuthorNames,itemMsg);
      return ;
    }
    itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2069+"names="+errorPartname.substring(1,errorPartname.length()));
    // 这里肯定是名序错误的
    if(indexBefore){
      itemMsg.setAuthorNames(itemMsg.getAuthorNames()+";"+OpenMsgCodeConsts.SCM_2073);
    }else{
      itemMsg.setAuthorNames(itemMsg.getAuthorNames()+";"+OpenMsgCodeConsts.SCM_2074);
    }
    // 检查名字个数
    checkNameCount(authorNames,matchAuthorNames,itemMsg);
    if(lengt5){
      itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2072);
      type.setAuthorNames(PubInfoVerifyConstant.NOT_SURE.toString());
    }
  }
  public void checkNameCount(String authorNames[] ,String matchAuthorNames[] ,PaperItemMsg itemMsg){
    if(authorNames.length == matchAuthorNames.length) return ;
    if (authorNames.length < matchAuthorNames.length) {
      //type.setAuthorNames(PubInfoVerifyConstant.UNMATCH.toString());
      if(StringUtils.isNotBlank(itemMsg.getAuthorNames())){
        itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2065+";"+itemMsg.getAuthorNames());
      }else{
        itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2065);
      }
    }else if(authorNames.length > matchAuthorNames.length){
      // type.setAuthorNames(PubInfoVerifyConstant.UNMATCH.toString());
      if(StringUtils.isNotBlank(itemMsg.getAuthorNames())){
        itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2066+";"+itemMsg.getAuthorNames());
      }else{
        itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2066);
      }
    }
  }
  /**
   * 查询查询参与人作者的顺序
   * @param authorNames
   * @param partName
   * @return
   */
  public List<Integer> findAuthorNameIndexs(String authorNames[] , String partName){
    List<Integer> list = new ArrayList();
    int index = 1 ;
    for(String authorName : authorNames){
      boolean b = PubInfoVerifyUtil.compareNames(authorName, partName);
      if(b){
        list.add(index);
      }
      index++;
    }
    return list ;
  }

  /**
   * 验证基金
   *
   * @param paperInfo
   * @param pubDetailVO
   * @return
   */
  public void verifyFundInfo(PaperInfo paperInfo, PubDetailVO pubDetailVO
      ,PaperItemMsg itemMsg ,PaperItemMsg type) {
    //不匹配信息
    if(!paperInfo.getFundingInfoCompare()) return ;
    if(StringUtils.isBlank(paperInfo.getFundingInfo()) || StringUtils.isBlank(pubDetailVO.getFundInfo())){
      itemMsg.setFundingInfo(OpenMsgCodeConsts.SCM_2062);
      type.setFundingInfo(PubInfoVerifyConstant.NOT_SURE.toString());
      return;
    }
    if( pubDetailVO.getFundInfo().contains(paperInfo.getFundingInfo())){
      type.setFundingInfo(PubInfoVerifyConstant.SUCCESS.toString());
    }else{
      type.setFundingInfo(PubInfoVerifyConstant.UNMATCH.toString());
      itemMsg.setFundingInfo(OpenMsgCodeConsts.SCM_2050);
    }
  }

  /**
   * 构建结果
   *
   * @param itemStatus
   * @param keyCode
   * @return
   */
  public static Map<String, Object> buildResultMap(Integer itemStatus, String keyCode) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    resultMap.put("itemStatus", itemStatus);
    resultMap.put("keyCode", keyCode);
    return resultMap;
  }

  /**
   * 构建结果
   *
   * @param itemStatus
   * @param keyCode
   * @return
   */
  public static Map<String, Object> buildResultMap(Integer itemStatus, String keyCode, PsnPubInfo pubInfo,
      PsnPubItemMsg type, String itemMsg) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    resultMap.put("itemStatus", itemStatus);
    resultMap.put("keyCode", keyCode);
    resultMap.put("type", type);
    // resultMap.put("itemMsg", itemMsg);
    resultMap.put("correlationData", pubInfo);
    return resultMap;
  }


  /**
   * 匹配中英文名 布尔值 新增特例：Zhao RCH R可以是任何字符
   *
   * @param zhName
   * @param enName
   * @return
   */
  private Boolean getChEnNameBoolean(String zhName, String enName) {
    String nameA = "";
    String[] split = enName.split(" ");
    {
      if (split.length == 2 && split[1].length() > 1) {
        nameA = split[0] + " " + split[1].substring(1, split[1].length());
      }
    }
    enName.substring(1, enName.length());
    Map<String, Set<String>> map = ServiceUtil.generatePsnName(zhName.replaceAll("\\d+", ""));
    if (map == null || map.isEmpty()) {
      return false;
    }
    Set<String> fullnames = map.get("fullname");
    if (CollectionUtils.isNotEmpty(fullnames)) {
      if (fullnames.contains(enName)) {
        return true;
      }
      // 新增特例 Chen Ming 应支持与Chen _ming
      if (StringUtils.isNotBlank(nameA)) {
        if (fullnames.contains(nameA)) {
          return true;
        }
      }
    }
    Set<String> initNames = map.get("initname");
    if (CollectionUtils.isNotEmpty(initNames)) {
      if (initNames.contains(enName)) {
        return true;
      }
      // 新增特例：Zhao RCH R可有可无
      if (StringUtils.isNotBlank(nameA)) {
        if (initNames.contains(nameA)) {
          return true;
        }
      }
    }
    Set<String> prefixNames = map.get("prefixname");
    if (CollectionUtils.isNotEmpty(prefixNames)) {
      if (prefixNames.contains(enName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 作者参与人比较
   * @param authorNames
   * @param participantNames
   * @return
   */
  public boolean authorNamesContainNames(String authorNames, String participantNames) {
    if (StringUtils.isBlank(participantNames) || StringUtils.isBlank(authorNames)) {
      return false;
    }
    String[] authorsNamesArr = authorNames.split(sem_split);
    String[] participantNamesArr = participantNames.split(sem_split);
    for (String authorsName : authorsNamesArr) {
      for (String names : participantNamesArr) {
        boolean b = PubInfoVerifyUtil.compareNames(authorsName, names);
        if (b) {
          return b;
        }
      }
    }
    return false;
  }

  /**
   * 查找参与人名字的在作者中的顺序顺序
   * @param authorsNames
   * @param participantNames
   * @return
   */
  public List<Map<String ,List<Integer>>>  findNameIndex(String authorsNames, String participantNames) {
    List<Map<String,List<Integer>>>  resultList = new ArrayList<>();
    Map<String ,List<Integer>> partNamesMap = new HashMap<>(); //参与人，
    Map<String ,List<Integer>> noPartNamesMap = new HashMap<>();//非参与人
    resultList.add(partNamesMap);
    resultList.add(noPartNamesMap);
    String[] authorsNamesArr = authorsNames.split(sem_split);
    authorsNamesArr = PubInfoVerifyUtil.dealBlankAuthorsNames(authorsNamesArr);
    String[] participantNamesArr = participantNames.split(sem_split);
    participantNamesArr = PubInfoVerifyUtil.dealBlankAuthorsNames(participantNamesArr);
    for (String name : participantNamesArr) {
      for (int i=0 ; i<authorsNamesArr.length ; i++ ) {
        boolean b = PubInfoVerifyUtil.compareNames(name, authorsNamesArr[i]);
        if (b) {
          addNameIndex(partNamesMap, authorsNamesArr[i], i);
        }
      }
    }
    // 非参与人 排序
    for (int i=0 ; i<authorsNamesArr.length ; i++ ) {
      if (partNamesMap.get(authorsNamesArr[i]) == null) {
        addNameIndex(noPartNamesMap, authorsNamesArr[i], i);
      }
    }
    return resultList ;
  }

  private void addNameIndex(Map<String, List<Integer>> partNamesMap, String key, int i) {
    //有重名的，直接在重名的后面添加名序
    for(String   name : partNamesMap.keySet()){
      if( PubInfoVerifyUtil.compareNames(name, key) ){
        partNamesMap.get(name).add(i);
        partNamesMap.put(key,partNamesMap.get(name));
        return ;
      }
    }

    List<Integer>   intList = partNamesMap.get(key);
    if(intList == null){
      intList =  new ArrayList<>();
    }
    intList.add(i);
    partNamesMap.put(key,intList);
  }

  /**
   * 查找参与人名字的在作者中的顺序顺序
   * @param authorsNames
   * @param participantNames
   * @return
   */
  public List<Map<String ,List<Integer>>>  findPdwhNameIndex(String authorsNames, String participantNames) {
    List<Map<String,List<Integer>>>  resultList = new ArrayList<>();
    Map<String ,List<Integer>> partNamesMap = new HashMap<>(); //参与人，
    Map<String ,List<Integer>> noPartNamesMap = new HashMap<>();//非参与人
    resultList.add(partNamesMap);
    resultList.add(noPartNamesMap);
    String[] authorsNamesArr = authorsNames.split(sem_split);
    authorsNamesArr = PubInfoVerifyUtil.dealBlankAuthorsNames(authorsNamesArr);
    String[] participantNamesArr = participantNames.split(sem_split);
    participantNamesArr = PubInfoVerifyUtil.dealBlankAuthorsNames(participantNamesArr);
    for (String name : participantNamesArr) {
      for (int i=0 ; i<authorsNamesArr.length ; i++ ) {
        boolean b = PubInfoVerifyUtil.compareNames(name, authorsNamesArr[i]);
        if (b) {
          addNameIndex(partNamesMap, authorsNamesArr[i], i);
        }
      }
    }
    // 非参与人 排序
    for (int i=0 ; i<authorsNamesArr.length ; i++ ) {
      if (partNamesMap.get(authorsNamesArr[i]) == null) {
        addNameIndex(noPartNamesMap, authorsNamesArr[i], i);
      }
    }
    return resultList ;
  }

  /**
   * 1=顺序一致
   * 2=参与人顺序不一致
   * 3=不包含参与人
   * @param nameIndex
   * @param cNameIdx
   * @return
   */
  public  int  validNameIndex(Map<String ,List<Integer>> nameIndex , Map<String ,List<Integer>> cNameIdx){
    int result  = 3 ;
    boolean falg = true ;
    if(nameIndex.size() == 0 ||cNameIdx.size() == 0){
      return 3 ;
    }
    Set<String> keyList = nameIndex.keySet();
    for(String key :keyList){
      String key2 = key;
      if ( cNameIdx.get(key2) == null ){
        //可能是中文和英文； 英文和英文没找到
        Set<String> nameSet = cNameIdx.keySet();
        for(String name : nameSet){
          boolean b = PubInfoVerifyUtil.compareNames(name, key);
          if(b){
            key2 = name;
          }
        }
      }

      if ( cNameIdx.get(key2) == null ){
        result = 3 ;
        falg = false ;
      } else {
        List<Integer> nameIdxs = nameIndex.get(key);
        List<Integer> cNameIdxs = cNameIdx.get(key2);
        //两边只要存在 一个名序对的上 就ok
        boolean equal = false;
        for(int i : nameIdxs){
          if(cNameIdxs.contains(i)){
            equal = true ;
            break;
          }
        }
        if(!equal){
          result = 2 ;
          falg = false ;
        }
      }
    }
    if(falg) result  = 1 ;
    return result ;
  }

  /**
   * 验证非参与人的 姓名
   * 10==相同
   * 20 == 不相同
   * @param nameIndex
   * @param cNameIdx
   * @return
   */
  public  int  validNoPartNameIndex(Map<String ,List<Integer>> nameIndex , Map<String ,List<Integer>> cNameIdx){
    int result  = 10 ;
    if(nameIndex == null || nameIndex.size() == 0 || cNameIdx == null || cNameIdx.size() == 0){
      return result ;
    }
    Set<String> keyList = nameIndex.keySet();
    for(String key :keyList){
      String key2 = key;
      if ( cNameIdx.get(key2) == null ){
        //可能是中文和英文； 英文和英文没找到
        Set<String> nameSet = cNameIdx.keySet();
        for(String name : nameSet){
          boolean b = PubInfoVerifyUtil.compareNames(name, key);
          if(b){
            key2 = name;
          }
        }
      }
      List<Integer> nameIdxs = nameIndex.get(key);
      List<Integer> cNameIdxs = cNameIdx.get(key2);
      //两边只要存在 一个名序对的上 就ok
      if(CollectionUtils.isNotEmpty(cNameIdxs)){
        boolean equal = false;
        for(int i : nameIdxs){
          if(cNameIdxs.contains(i)){
            equal = true ;
            break;
          }
        }
        if(!equal){
          result = 20  ;
        }
      }else{
        result = 20  ;
      }
    }
    return result ;
  }

  /**
   * 1=顺序一致 ,参与人和非参与人验证成功
   * 2=参与人名序正确，非参与人名序错误
   * 3= 参与人名序错误
   * 4= 基准库成果不包含参与人
   * @param paperInfo
   * @param pubDetailVO
   * @param participantNames
   * @return
   */
  public int   compareNameIndex(PaperInfo paperInfo , PubDetailVO pubDetailVO , String participantNames){
    List<Map<String, List<Integer>>> nameIndexs = findNameIndex(paperInfo.getAuthorNames(), participantNames);
    List<Map<String, List<Integer>>> cNameIndexs = findPdwhNameIndex(pubDetailVO.getAuthorNames(), participantNames);
    // 1= 相同 ； 2 不相同  3 不包含参与人
    int  partResult =  validNameIndex(nameIndexs.get(0) ,cNameIndexs.get(0));
    // 10非参与人相同   ； 20= 非参与人不相同
    int  nopartResult =  validNoPartNameIndex(nameIndexs.get(1) ,cNameIndexs.get(1));
    int status = 0 ;
    switch (partResult + nopartResult){
      case 11://1+10
        status = 1 ;break;
      case 21://1+20
        status = 2 ;break;
      case 12://2+10 ; 2+20
      case 22:
        status = 3 ;break;
      default:
        status = 4 ;
    }

    return  status ;
  }



  /**
   * 获取重复成果ids，已测试正常 title doi
   *flag == 0 正常
   * flag == 1 表示只拿标题匹配
   *
   * @return
   */
  public List<Long> getDupPubIds(PaperInfo paperInfo , Integer flag) {

    String title = paperInfo.getTitle();
    String DOI = paperInfo.getDoi();
    List<Long> dupPubIds = new ArrayList<>();
    Map<String, Object> parmaMap = new HashMap<>();
    parmaMap.put("pubGener", PubGenreConstants.VERIFY_PDWH_SNS_PUB);
    parmaMap.put("title", title);
    if(flag == 1){
    }else{
      DOI = cleanDoiHash(DOI);
      parmaMap.put("doi", DOI);
    }
    // parmaMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    String SERVER_URL = domainscm + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    Map<String, Object> remoteInfo = (Map<String, Object>) RestUtils.getRemoteInfo(parmaMap, SERVER_URL, restTemplate);
    if ("SUCCESS".equalsIgnoreCase(remoteInfo.get("status").toString())) {
      Object msgList = remoteInfo.get("msgList");
      if (msgList != null) {
        String[] split = msgList.toString().split(",");
        if (split != null && split.length > 0) {
          for (String pubId : split) {
            if (org.apache.commons.lang.StringUtils.isNotBlank(pubId) && NumberUtils.isCreatable(pubId)) {
              dupPubIds.add(NumberUtils.toLong(pubId));
            }
          }
        }
      }
    }
    return dupPubIds;
  }

  public PubDetailVO getPdwhPubInfo(Long pubId) {
    String SERVER_URL = domainscm + com.smate.center.open.consts.V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
    Map<String, Object> map = new HashMap<>();
    map.put(com.smate.center.open.consts.V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubId.toString()));
    map.put("serviceType", com.smate.center.open.consts.V8pubQueryPubConst.OPEN_VERIFY_PDWH_PUB);
    Map<String, Object> pubInfoMap = (Map<String, Object>) RestUtils.getRemoteInfo(map, SERVER_URL, restTemplate);
    if (pubInfoMap != null) {
      PubDetailVO pubDetailVO = null;
      try {
        pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubInfoMap));
      } catch (Exception e) {
        logger.error("查询基准库成果异常 pubId=" + pubId, e);
      }
      return pubDetailVO;
    }
    return null;
  }

  @Override
  public int findPubAuthorPosition(String authorNames, Set<String> psnNameList) {
    if (StringUtils.isBlank(authorNames) || CollectionUtils.isEmpty(psnNameList)) {
      return -1;
    }
    for (String psnName : psnNameList) {
      String[] split = authorNames.split(sem_split);
      split = PubInfoVerifyUtil.dealBlankAuthorsNames(split);
      for (int i = 0; i < split.length; i++) {
        boolean b = PubInfoVerifyUtil.compareNames(split[i], psnName);
        if (b) {
          return i;
        }
      }
    }
    return -1;
  }
  @Override
  public int findPdwhPubAuthorPosition(String authorNames, Set<String> psnNameList) {
    if (StringUtils.isBlank(authorNames) || CollectionUtils.isEmpty(psnNameList)) {
      return -1;
    }
    for (String psnName : psnNameList) {
      String[] split = authorNames.split(sem_split);
      split = PubInfoVerifyUtil.dealBlankAuthorsNames(split);
      for (int i = 0; i < split.length; i++) {
        boolean b = PubInfoVerifyUtil.compareNames(split[i], psnName);
        if (b) {
          return i;
        }
      }
    }
    return -1;
  }

  @Override
  public Map<String, Object> doMatchPdwhPubAuthor(PaperInfo paperInfo, int psnPubAuthorIdx, Set<String> nameList) {
    List<Long> dupPubIds = getDupPubIds(paperInfo , 0);
    Map<String, Object> resultMap = null;
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      //记录分数  0=最低； 1其次
      Map<Integer,Map> scoreMap = new HashMap<>();
      for (Long pubId : dupPubIds) {
        resultMap = null;
        PubDetailVO pubDetailVO = getPdwhPubInfo(pubId);
        PsnPubItemMsg type = new PsnPubItemMsg();
        PaperItemMsg itemMsg = new PaperItemMsg();
        PsnPubInfo psnPubInfo = new PsnPubInfo();
        if (pubDetailVO != null) {
          //处理作者信息
          pubDetailVO.setAuthorNames( PubDetailVoUtil.dealAuthorNames(pubDetailVO.getAuthorNames()) );
          psnPubInfo.setTitle(pubDetailVO.getTitle());
          psnPubInfo.setAuthorNames(pubDetailVO.getAuthorNames());
          type.setTitle(PubInfoVerifyConstant.SUCCESS.toString());
          if (StringUtils.isBlank(pubDetailVO.getAuthorNames())) {
            continue;
          }
          int position = findPdwhPubAuthorPosition(pubDetailVO.getAuthorNames(), nameList);
          if (position == -1) {
            // 作者没找到
            type.setAuthorNames(PubInfoVerifyConstant.UNMATCH.toString());
            itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2045);
            resultMap = PubInfoVerifyServiceImpl.buildResultMap(PubInfoVerifyConstant.UNMATCH, paperInfo.getKeyCode(),
                psnPubInfo, type, OpenMsgCodeConsts.SCM_2045);
            resultMap.put(PubInfoVerifyConstant.ITEM_MSG, itemMsg);
            scoreMap.put(0,resultMap);
            continue;
          }
          if (psnPubAuthorIdx == position) {
            type.setAuthorNames(PubInfoVerifyConstant.SUCCESS.toString());
            resultMap = PubInfoVerifyServiceImpl.buildResultMap(PubInfoVerifyConstant.SUCCESS, paperInfo.getKeyCode(),
                psnPubInfo, type, OpenMsgCodeConsts.SCM_2043);
            break;
          }else{
            type.setAuthorNames(PubInfoVerifyConstant.UNMATCH.toString());
            itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2067);
            resultMap = PubInfoVerifyServiceImpl.buildResultMap(PubInfoVerifyConstant.UNMATCH, paperInfo.getKeyCode(),
                psnPubInfo, type, OpenMsgCodeConsts.SCM_2067);
            resultMap.put(PubInfoVerifyConstant.ITEM_MSG, itemMsg);
            scoreMap.put(1,resultMap);
          }
        }
      }
      if(scoreMap.size()>0){
        if(resultMap == null || (resultMap !=null && !resultMap.get("itemStatus").equals(PubInfoVerifyConstant.SUCCESS))){
          resultMap = scoreMap.get(1) != null ? scoreMap.get(1) : scoreMap.get(0);
        }
      }
    }
    if (resultMap == null) {
      PaperItemMsg itemMsg = new PaperItemMsg();
      itemMsg.setOtherErrorMsg(OpenMsgCodeConsts.SCM_2051);
      resultMap = PubInfoVerifyServiceImpl.buildResultMap(PubInfoVerifyConstant.NOT_SURE, paperInfo.getKeyCode());
      resultMap.put(PubInfoVerifyConstant.ITEM_MSG, itemMsg);
    }
    return resultMap;
  }

  @Override
  public Set<String> findPersonByVerifyPsnInfo(VerifyPsnInfo verifyPsnInfo) {
    List<Person> list =
        personDao.findListByVerifyPsnInfo(verifyPsnInfo.getName(), verifyPsnInfo.getEmail(), verifyPsnInfo.getPhone());
    Set<String> names = new HashSet<>();
    if (CollectionUtils.isNotEmpty(list)) {
      for (Person p : list) {
        names.add(p.getName());
        if(StringUtils.isNotBlank(p.getEname())){
          names.add(p.getEname());
        }
        if(StringUtils.isNotBlank(p.getFirstName())&&StringUtils.isNotBlank(p.getLastName())){
          names.add(p.getFirstName()+" "+p.getLastName());
        }
      }
    }
    return names;
  }

  @Override
  public void savePubVerifyLog(Map data,PaperInfo paperInfo,Integer serviceType) {
    PubVerifyLog log = new PubVerifyLog();
    log.setTitle(paperInfo.getTitle());
    log.setItemStatus(NumberUtils.parseInt(data.get("itemStatus").toString()));
    log.setItem(JacksonUtils.jsonObjectSerializer(paperInfo));
    log.setItemMsg(data.get("itemMsg") != null ?JacksonUtils.jsonObjectSerializer(data.get("itemMsg")) : "");
    log.setCorrelationData(data.get("correlationData") != null ? JacksonUtils.jsonObjectSerializer(data.get("correlationData")) : "");
    log.setType(data.get("type") != null ? JacksonUtils.jsonObjectSerializer(data.get("type")) : "");
    log.setGmtCreate(new Date());
    log.setServiceType(serviceType);
    log.setParticipantNames(paperInfo.getParticipantNames());
    pubVerifyLogDao.save(log);
  }
}
