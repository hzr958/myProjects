package com.smate.web.v8pub.service.match;

import java.util.List;

import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.exception.ServiceException;

public interface PubAuthorMatchService {

  /**
   * 拆分authorNames匹配当前人
   * 
   * @param authorNames 传入的作者名
   * @param psnId 当前人的psnId
   * @return 1:匹配成功 0:匹配失败
   * @throws ServiceException
   */
  Integer isMatch(String authorNames, Long psnId) throws ServiceException;

  /**
   * authorNames拆分匹配
   * 
   * @param authorNames
   * @param psnId
   * @return
   * @throws ServiceException
   */
  void authorMatch(List<PubMemberDTO> memberList, Long psnId) throws ServiceException;

  /**
   * 1.循环authors的数据，<br/>
   * 先取authors的作者名与members的作者名进行全称匹配，匹配成功，则进行信息补充，且标记此author数据为已匹配<br/>
   * 去除标记为已匹配的authors数据<br/>
   * 2.再循环authors数据，<br/>
   * 取authors的作者名与members的作者名进行可简称匹配，匹配成功，记录次数，成功匹配次数大于1，不进行数据填充<br/>
   * （简称匹配，另增加条件，作者名必须是包含关系才进行简称匹配）<br/>
   * 
   * @param authorNames
   * @param xmlAuthors
   * @return
   */
  List<PubMemberDTO> perfectMembers(String authorNames, List<PubMemberDTO> xmlAuthors);
}
