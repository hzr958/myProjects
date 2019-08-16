package com.smate.web.v8pub.service.pdwh;

import java.util.Map;

import com.smate.web.v8pub.vo.PubListVO;

public interface PdwhSearchService {

  void getPapers(PubListVO pubListVO);

  Map<String, Object> getPaperLeftMenu(PubListVO pubListVO);

  void getPatents(PubListVO pubListVO);

  Map<String, Object> getPatentLeftMenu(PubListVO pubListVO);

}
