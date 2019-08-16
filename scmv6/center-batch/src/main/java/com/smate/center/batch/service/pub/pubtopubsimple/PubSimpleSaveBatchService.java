package com.smate.center.batch.service.pub.pubtopubsimple;

import com.smate.core.base.utils.exception.BatchTaskException;

public interface PubSimpleSaveBatchService {


  void savePubEditBatch(Long pubId, Long version) throws BatchTaskException;

  void savePubAddBatch(Long pubId, Long version) throws BatchTaskException;

  void savePubImportBatch(Long pubId, Long version) throws BatchTaskException;

  void savePubImportBatch(Long pubId, Long version, Long groupId) throws BatchTaskException;

}
