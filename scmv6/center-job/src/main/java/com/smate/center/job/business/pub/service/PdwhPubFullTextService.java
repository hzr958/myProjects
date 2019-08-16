package com.smate.center.job.business.pub.service;

import java.util.List;

public interface PdwhPubFullTextService {

	void delPdwhPubFulltextByPubId(Long pubId) throws Exception;

	void ConvertPubFulltextPdfToimage(Long pubId) throws Exception;

	void updateTaskStatus(Long pubId, int status, String string);

	List<Long> getbatchhandleIdList(long begin, long end, int size);

}
