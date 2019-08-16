package com.smate.center.open.service.data.isis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * pdwh journal service
 * 
 * @author hp
 * @date 2015-10-23
 */
public interface PdwhJournalService {
  public Map<Long, String> getJournalRomeoColour(Set<Long> jidSet);

  public Map<Long, Long> getHxj(Set<Long> jidSet);

  public List<Long> getIsInJournalCategory(Set<Long> jidSet);
}
