package com.smate.center.job.business.pub.service;

import com.smate.center.job.business.pdwhpub.model.PdwhPublication;
import com.smate.core.base.utils.pubxml.ImportPubXmlDocument;

import java.io.IOException;
import java.util.List;

public interface PdwhPublicationService {

	List<PdwhPublication> getPdwhPubIds(Long lastPubId, int batchSize);

	List<Long> batchGetPdwhPubIds(int batchSize);

	void matchPubAddr(Long pubId,ImportPubXmlDocument xmlDocument) throws Exception;

	void matchPubAuthor(Long pubId,ImportPubXmlDocument xmlDocument) throws Exception;

	void updatePubMatchStatus(Long pubId, int i, String string);
	public ImportPubXmlDocument getPubXmlDocById(Long pubId);
	/**
	 * 对成果单位进行分词匹配,字符将会被替换后匹配
	 * 
	 * @param pubId
	 * @param addrs
	 * @throws IOException
	 * @author LIJUN
	 * @date 2018年5月23日
	 */
	void segmentPubOrg(Long pubId, String addrs) throws Exception;

}
