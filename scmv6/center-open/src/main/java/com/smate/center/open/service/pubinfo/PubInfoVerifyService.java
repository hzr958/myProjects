package com.smate.center.open.service.pubinfo;

import com.smate.center.open.service.data.pub.verify.PaperInfo;
import com.smate.center.open.service.data.pub.verify.PaperItemMsg;
import com.smate.center.open.service.data.pub.verify.VerifyPsnInfo;
import com.smate.core.base.pub.vo.PubDetailVO;

import java.util.Map;
import java.util.Set;

/**
 * 成果信息验证
 * 
 * @author aijiangbin
 * @create 2018-11-12 16:42
 **/

public interface PubInfoVerifyService {


  /**
   * 比较名字
   * 
   * @param name
   * @param otherName
   * @return
   */
  public boolean compareNames(String name, String otherName);

  /**
   * 论文验证
   */
  public Map doVerfiyPaper(PaperInfo paperInfo,String participantNames);

  /**
   * 查找作者的位置 0为启始值; -1没找到
   * 
   * @param authorNames 成果的作者信息
   * @param nameList 人员列表信息
   * @return
   */
  public int findPubAuthorPosition(String authorNames, Set<String> nameList);

  /**
   * 匹配基准库成果作者
   * 
   * @param paperInfo
   * @param psnPubAuthorIdx
   * @param nameList 人员信息名字组合
   * @return
   */
  public Map<String, Object> doMatchPdwhPubAuthor(PaperInfo paperInfo, int psnPubAuthorIdx, Set<String> nameList);

  /**
   * 匹配科研之友的人员信息 2018-11-13
   * 
   * @param verifyPsnInfo
   * @return
   */
  public Set<String> findPersonByVerifyPsnInfo(VerifyPsnInfo verifyPsnInfo);

  /**
   * 添加匹配成果的信息
   * 
   * @param resMap
   * @param pub
   */
  public void addPubInfo(Map<String, Object> resMap, PubDetailVO pub, PaperItemMsg itemMsg);

  /**
   * 保存成果验证日志
   */
  public void savePubVerifyLog(Map data,PaperInfo paperInfo,Integer serviceType);

  public int findPdwhPubAuthorPosition(String authorNames, Set<String> psnNameList);
}
