package com.smate.center.searcher.restful;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smate.center.searcher.entity.SolrPersonEntity;
import com.smate.center.searcher.service.SolrService;
import com.smate.center.searcher.util.SolrPage;


/**
 * 检索restful控制器
 * 
 * @author hp
 *
 */
@RestController
public class SearchController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SolrService<SolrPersonEntity, String> solrService;
	
	private SolrPage<SolrPersonEntity> p = new SolrPage<SolrPersonEntity>(1, 100);// 分页
	
	@RequestMapping("/personsearch")	
	public List<Long> personSearch(@RequestBody Map<String,Object> mailData) {
		List<Long> psnIdList = new ArrayList<Long>();
		 
		try {
			p = solrService.findPerson(mailData, p);
			List<SolrPersonEntity> psnList = p.getResult().getContent();
			for(SolrPersonEntity psn:psnList){
				if(NumberUtils.isDigits(psn.getNs_psn_id())){
					psnIdList.add(Long.valueOf(psn.getNs_psn_id()));
				}				
			}
		} catch (Exception e) {
			logger.error("检索restful检索数据时时出错",e);
			return null;
		}
		logger.info("检索restful检索数据成功");
		return psnIdList;
	}
	 
}
