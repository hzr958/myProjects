package com.smate.center.task.service.sns.pub;

import java.util.List;

import com.smate.web.v8pub.dto.PubMemberDTO;

public interface PubAuthorMatchService {

  /**
   * 匹配完善成果members数据
   * 
   * @param authorNames
   * @param members
   * @return
   */
  List<PubMemberDTO> perfectMembers(String authorNames, List<PubMemberDTO> members);
}
