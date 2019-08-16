package com.smate.center.job.business.pub.service.impl;

import com.smate.center.job.business.pdwhpub.dao.*;
import com.smate.center.job.business.pdwhpub.model.*;
import com.smate.center.job.business.psn.dao.PersonPmNameDao;
import com.smate.center.job.business.psn.model.PersonPmName;
import com.smate.center.job.business.pub.service.PdwhPublicationService;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.core.base.utils.pubxml.ImportPubXmlDocument;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service("PdwhPublicationService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPublicationServiceImpl implements PdwhPublicationService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private PdwhInsAddrConstDao pdwhInsAddrConstDao;
	@Autowired
	private PdwhPubAddrDao pdwhPubaddrDao;
	@Autowired
	private PdwhPubAddrInsRecordDao pdwhPubAddrInsRecordDao;
	@Autowired
	private PdwhPublicationDao pdwhPublicationDao;
	@Autowired
	private PdwhPubXmlDao pdwhPubXmlDao;
	@Autowired
	private PersonPmNameDao personPmNameDao;
	@Autowired
	private PdwhPubAuthorSnsPsnRecordDao pdwhPubAuthorSnsPsnRecordDao;
	@Autowired
	private PdwhMatchTaskRecordDao pdwhMatchTaskRecordDao;
	@Autowired
	private EducationHistoryDao educationHistoryDao;
	@Autowired
	private WorkHistoryDao workHistoryDao;

	@Value("${extpdwh.custom.dicpath}")
	private String dicRootPath;
	private static String psndic = "psn/";
	private static String addrdic = "addr/";

	@Override
	public List<PdwhPublication> getPdwhPubIds(Long lastPubId, int batchSize) {
		return pdwhPublicationDao.getPdwhPubIds(lastPubId, batchSize);
	}

	@Override
	public List<Long> batchGetPdwhPubIds(int batchSize) {
		return pdwhMatchTaskRecordDao.batchGetNeededData(batchSize);
	}

	@Override
	public ImportPubXmlDocument getPubXmlDocById(Long pubId) {
		PdwhPubXml xml = pdwhPubXmlDao.getpdwhPubXmlPubId(pubId);
		String xmlData = xml.getXml();
		if (org.apache.commons.lang.StringUtils.isBlank(xmlData)) {
			return null;
		}
		ImportPubXmlDocument document = null;
		try {
			document = new ImportPubXmlDocument(xmlData);
		} catch (Exception e) {
			logger.error("从成果xml中解析信息出错，pub_id:" + pubId, e);
			return null;
		}
		return document;
	}

	/**
	 * 匹配地址
	 */
	@Override
	public void matchPubAddr(Long pubId, ImportPubXmlDocument xmlDocument) throws Exception {
		String organization = xmlDocument.getOrgnization();
		Integer dbId = pdwhPublicationDao.getDbIdById(pubId);

		// 改成果单位信息由xml读取（pdwhpubaddr表存在数据不全问题）
		Set<String> pubAddrSet = new HashSet<String>();
		if (dbId == 4 || dbId == 14 || dbId == 21 || dbId == 11) {
			// cnki || ei || cnkipat的成果用这个方法去解析
			pubAddrSet = XmlUtil.parseCnkiPubAddrs(organization);
		} else if (dbId == 8 || dbId == 15 || dbId == 16 || dbId == 17 || dbId == 10) {
			// isi的成果用这个方法去解析
			pubAddrSet = XmlUtil.parseIsiPubAddrs(organization);
		} else {
			// 其他没有定义的都暂时用isi的拆分
			pubAddrSet = XmlUtil.parseIsiPubAddrs(organization);
		}
		if (CollectionUtils.isEmpty(pubAddrSet)) {
			logger.error("基准库成果地址匹配任务PdwhAddrMacthInsTask,获取不到成果地址，跳过匹配，pub_id：" + pubId);
			return;
		}
		this.segmentPubOrg(pubId, pubAddrSet.toString().trim());

	}

	/**
	 * 对成果单位进行分词匹配,字符将会被替换后匹配
	 * 
	 * @author LIJUN
	 * @throws IOException
	 * @date 2018年3月26日
	 */
	@Override
	public void segmentPubOrg(Long pubId, String addrs) throws Exception {
		pdwhPubAddrInsRecordDao.deleteUnconfirmedRecord(pubId);
		Map<String, Set<String>> extractInsName = this.getExtractInsName(addrs);
		Set<String> addrlist = extractInsName.get("pdwh_ins_name");
		if (CollectionUtils.isEmpty(addrlist)) {
			return;
		}

		this.saveMatchedAddr(pubId, addrlist);

	}

	/**
	 * @Author LIJUN
	 * @Description //TODO 从词典提取单位
	 * @Date 10:49 2018/7/19
	 * @Param [str]
	 * @return java.util.Map<java.lang.String,java.util.Set<java.lang.String>>
	 **/
	public Map<String, Set<String>> getExtractInsName(String str) throws Exception {
		if (org.apache.commons.lang3.StringUtils.isEmpty(str)) {
			return null;
		}
		str = replaceChars(str);
		// 直接使用，在服务器启动时加载
		Result kwRs = DicAnalysis.parse(str);
		Set<String> ins = new TreeSet<String>();
		Map<String, Set<String>> mp = new HashMap<String, Set<String>>();
		for (Term t : kwRs.getTerms()) {
			if (t == null) {
				continue;
			}

			if ("pdwh_ins_name".equals(t.getNatureStr())) {
				if (org.apache.commons.lang3.StringUtils.isNotEmpty(t.getName())) {
					ins.add(resetChars(t.getName()));
				}
			}
		}
		if (ins.size() > 0) {
			mp.put("pdwh_ins_name", ins);
		}
		return mp;
	}

	/**
	 * 获取目录下最新的词典，根据名称排序，忽略tmp_开头的临时词典，不要包含其他文件名
	 * 
	 * @param dicpath
	 * @return
	 * @author LIJUN
	 * @date 2018年5月9日
	 */
	public String getNewestDic(String dicpath) {
		File psnFile = new File(dicRootPath + dicpath);
		List<Long> namelist = new ArrayList<>();
		String[] list = psnFile.list();// 获取词典目录下所有文件名
		for (String string : list) {
			if (!string.contains("tmp_") && string.endsWith(".dic")) {
				namelist.add(NumberUtils.toLong(string.replace(".dic", "")));
			}
		}
		String fileName = dicRootPath + dicpath + Collections.max(namelist) + ".dic";
		return fileName;
	}

	/**
	 * 替换常见字符
	 * 
	 * @param string
	 * @return
	 * @author LIJUN
	 * @date 2018年3月26日
	 */
	public String replaceChars(String string) {

		string = string.replace("，", ",").replace("（", "(").replace("）", ")").replace("。", ".");
		string = string.replace(" ", "空格").replace(",", "逗号").replace(".", "点符号").replace("(", "左括号")
				.replace(")", "右括号").replace("&", "和符号").replace("'", "撇号").replace("-", "杠符号").replace("《", "前书名号")
				.replace("》", "后书名号");
		return string;

	}

	/**
	 * 还原字符
	 * 
	 * @param string
	 * @return
	 * @author LIJUN
	 * @date 2018年3月26日
	 */
	public static String resetChars(String string) {
		string = string.replace("空格", " ").replace("逗号", ",").replace("点符号", ".").replace("左括号", "(")
				.replace("右括号", ")").replace("和符号", "&").replace("撇号", "'").replace("杠符号", "-").replace("前书名号", "《")
				.replace("后书名号", "》");
		return string;

	}

	@Override
	public void matchPubAuthor(Long pubId, ImportPubXmlDocument xmlDocument) throws Exception {
		List<PdwhPubAddrInsRecord> list = this.getPubAddrMatchedRecord(pubId);
		startMatchSnsPsn(pubId, list, xmlDocument);

	}

	/**
	 * 更新对应关系
	 * 
	 * @param pubId
	 * @param addrlist
	 * @author LIJUN
	 * @date 2018年3月20日
	 */
	public void saveMatchedAddr(Long pubId, Set<String> addrlist) {
		Set<PdwhPubAddrInsRecord> matchedAddrlist = new HashSet<>();// 临时保存匹配上的单位名，用于去除重复数据后保存

		for (String addr : addrlist) {
			Long addrHash = PubHashUtils.cleanPubAddrHash(addr);
			List<PdwhInsAddrConst> insInfo = pdwhInsAddrConstDao.getInsInfoByNameHash(addrHash);
			if (CollectionUtils.isEmpty(insInfo)) {
				logger.info("PdwhInsAddrConst地址常量表查询不到对应单位，请检查分词器单位字典数据是否完整！pubId:" + pubId + " addr:" + addr);
				continue;
			}
			Set<PdwhInsAddrConst> insMatchList = new HashSet<>();
			/**
			 * 去除重复的单位，防止单位常量表有重复数据
			 */
			List<Long> ids = new ArrayList<>();// 用于排除ins_id相同的单位
			for (PdwhInsAddrConst pdwhInsAddrConst : insInfo) {
				/**
				 * //重写了PdwhInsAddrConst equals 方法，ins_id 和ins_name_hash相同则是唯一记录
				 */
				if (!ids.contains(pdwhInsAddrConst.getInsId())) {
					insMatchList.add(pdwhInsAddrConst);
				}
			}
			/**
			 * 单位地址常量表存在一个单位名对应了几个ins_id的情况，都保存
			 */
			for (PdwhInsAddrConst pdwhInsAddrConst : insMatchList) {
				Long constId = pdwhInsAddrConst.getConstId();
				Long insId = pdwhInsAddrConst.getInsId();
				List<PdwhPubAddrInsRecord> list = pdwhPubAddrInsRecordDao.findRecByPubIdAndInsId(pubId, insId);

				if (CollectionUtils.isEmpty(list)) {
					// 记录不存在执行保存流程
					/**
					 * 单位匹配记录表，pub_id 和ins_id为唯一记录
					 */
					PdwhPubAddrInsRecord insRecord = new PdwhPubAddrInsRecord(constId, pubId, insId, addr, addrHash, 0,
							new Date());
					matchedAddrlist.add(insRecord);
				} else {// 更新时间
					if (list.size() > 1) {
						logger.error("PdwhPubAddrInsRecord中有重复数据，请确认！pubid:" + pubId);
					}
					pdwhPubAddrInsRecordDao.updateTime(constId);// 更新时间
				}

			}

		}
		if (CollectionUtils.isNotEmpty(matchedAddrlist)) {
			for (PdwhPubAddrInsRecord pdwhPubAddrInsRecord : matchedAddrlist) {
				try {
					pdwhPubAddrInsRecordDao.save(pdwhPubAddrInsRecord);
				} catch (Exception e) {
					logger.error("PdwhPubAddrInsRecord保存出错,pubId={},insId={},list={}", pdwhPubAddrInsRecord.getPubId(),
							pdwhPubAddrInsRecord.getInsId(), matchedAddrlist.toString(), e);
				}
			}
		}
		// SCM-18085
		pdwhPublicationDao.updatePubUpdateTime(pubId);
	}

	public List<PdwhPubAddrInsRecord> getPubAddrMatchedRecord(Long pubId) {

		return pdwhPubAddrInsRecordDao.getPubAddrInsRecordByPubId(pubId);

	}

	/**
	 * 作者匹配
	 * 
	 * @param record
	 * @throws Exception
	 * @author LIJUN
	 * @param list
	 * @date 2018年3月27日
	 */
	public void startMatchSnsPsn(Long pubId, List<PdwhPubAddrInsRecord> records, ImportPubXmlDocument xmlDocument)
			throws Exception {

		StringBuffer stringBuffer = new StringBuffer();
		String namespec = xmlDocument.getAuthorNameSpec();
		String name = xmlDocument.getAuthorNames();

		if (StringUtils.isBlank(name) && StringUtils.isBlank(namespec)) {
			logger.error("该成果没有获取到作者信息，pub_id:" + pubId);
			return;
		}
		if (StringUtils.isNotBlank(name)) {
			stringBuffer.append(name);
			stringBuffer.append(";");
		}
		if (StringUtils.isNotBlank(namespec)) {
			stringBuffer.append(namespec);
		}
		this.segmentStr(pubId, records, XmlUtil.replaceXMLAuthorChars(stringBuffer.toString()));

	}

	/**
	 * 分词匹配
	 * 
	 * @author LIJUN
	 * @throws IOException
	 * @date 2018年3月26日
	 */
	public void segmentStr(Long pubId, List<PdwhPubAddrInsRecord> records, String string) throws Exception {
		Map<String, Set<String>> extractUserName = this.getExtractUserName(string);
		Set<String> namelist = extractUserName.get("pdwh_psn_name");

		if (CollectionUtils.isEmpty(namelist)) {
			return;
		}
		pdwhPubAuthorSnsPsnRecordDao.deleteUnconfirmedRecord(pubId);// 先删除没有被确认的记录
		this.updateMatchedRecord(pubId, records, namelist);

	}

	/**
	 * @Author LIJUN
	 * @Description //TODO 从词典提取人名
	 * @Date 10:50 2018/7/19
	 * @Param [str]
	 * @return java.util.Map<java.lang.String,java.util.Set<java.lang.String>>
	 **/
	public Map<String, Set<String>> getExtractUserName(String str) throws Exception {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		// 直接使用，在服务器启动时加载
		Result kwRs = DicAnalysis.parse(str);
		Set<String> name = new TreeSet<String>();
		Map<String, Set<String>> mp = new HashMap<String, Set<String>>();
		for (Term t : kwRs.getTerms()) {
			if (t == null) {
				continue;
			}
			if ("pdwh_psn_name".equals(t.getNatureStr()) || "nr".equals(t.getNatureStr())) {
				if (StringUtils.isNotEmpty(t.getName())) {
					name.add(t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim());
				}
			}
		}
		if (name.size() > 0) {
			mp.put("pdwh_psn_name", name);
		}
		return mp;
	}

	/**
	 * 更新记录
	 * 
	 * @param record
	 * @param namelist
	 * @author LIJUN
	 * @date 2018年3月20日
	 */
	private void updateMatchedRecord(Long pubId, List<PdwhPubAddrInsRecord> records, Set<String> namelist) {
		for (String name : namelist) {
			// 更新被确认的人员记录时间
			List<PdwhPubAuthorSnsPsnRecord> list = pdwhPubAuthorSnsPsnRecordDao.findConfirmRecByPubIdAndName(pubId,
					name);// 查询该成果人员姓名是否被用户确认
			if (CollectionUtils.isEmpty(list)) {// 记录不存在执行保存流程
				for (PdwhPubAddrInsRecord record : records) {
					this.saveMatchedRecord(record, name);
				}
			} else {
				for (PdwhPubAuthorSnsPsnRecord psnRecord : list) {
					pdwhPubAuthorSnsPsnRecordDao.updateTime(psnRecord.getId());// 更新时间
				}

			}
		}

	}

	/**
	 * 保存人员匹配记录
	 * 
	 * @param pubId
	 * @param pbinsId
	 * @param insName
	 * @param matchName
	 * @author LIJUN
	 * @date 2018年3月20日
	 */
	public void saveMatchedRecord(PdwhPubAddrInsRecord record, String matchName) {
		Long pubId = record.getPubId();
		Long insId = record.getInsId();
		String insName = record.getInsName();
		Integer status = record.getStatus();// 成果地址状态，0默认，1被用户确认
		List<PersonPmName> psnlist = personPmNameDao.getPsnByNameAndInsId(matchName, insId);

		if (CollectionUtils.isEmpty(psnlist)) {
			logger.debug("根据人名获取人名常量表数据为空，请确认人名常量字典信息是否完整,pubId:" + pubId + "  name:" + matchName);
			return;
		}
		/**
		 * pdwhPubAuthorSnsPsnRecord 状态 0 成果匹配不到sns人员，暂不记录<br>
		 * 状态1.表示可信度低(仅人名匹配上)<br>
		 * 2.表示可信度中（人名和未确认的地址），<br>
		 * 3表示可信度最高（由用户成果认领确认或人名和确认的地址）。<br>
		 * （地址状态信息PDWH_PUB_ADDR_INS_REcord表状态描述）
		 **/
		Set<PdwhPubAuthorSnsPsnRecord> psnRecords = new HashSet<>();
		for (PersonPmName psn : psnlist) {
			// insId和人名匹配上
			PdwhPubAuthorSnsPsnRecord psnRecord = pdwhPubAuthorSnsPsnRecordDao.getPsnRecord(pubId, psn.getPsnId(),
					insId, matchName, psn.getType());
			if (psnRecord == null) {// 不存在匹配记录直接保存

				if (status == 1) {// 成果地址被确认
					PdwhPubAuthorSnsPsnRecord newRecord = new PdwhPubAuthorSnsPsnRecord(pubId, psn.getPsnId(),
							matchName, insId, insName, 3, new Date(), psn.getType());
					psnRecords.add(newRecord);
				} else {
					PdwhPubAuthorSnsPsnRecord newRecord = new PdwhPubAuthorSnsPsnRecord(pubId, psn.getPsnId(),
							matchName, insId, insName, 2, new Date(), psn.getType());
					psnRecords.add(newRecord);
				}

			}

		}
		for (PdwhPubAuthorSnsPsnRecord pdwhPubAuthorSnsPsnRecord : psnRecords) {
			try {
				pdwhPubAuthorSnsPsnRecordDao.saveWithNewTransaction(pdwhPubAuthorSnsPsnRecord);
			} catch (Exception e) {
				logger.error("PdwhPubAuthorSnsPsnRecord保存出错,recordinfo={}", psnRecords.toString(), e);
			}
		}
	}

	@Override
	public void updatePubMatchStatus(Long pubId, int status, String string) {
		PdwhMatchTaskRecord record = pdwhMatchTaskRecordDao.get(pubId);
		record.setMatchStatus(status);
		record.setUpdateTime(new Date());
		record.setErrMsg(string);
	}

}
