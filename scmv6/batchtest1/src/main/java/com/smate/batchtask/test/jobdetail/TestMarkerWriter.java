package com.smate.batchtask.test.jobdetail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.batchtask.test.model.TaskMessageList;
import com.smate.batchtask.test.service.TaskMarkerService;

public class TestMarkerWriter implements ItemWriter<List<TaskMessageList>>{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	TaskMarkerService taskMarkerService;
	
	@Override
	public void write(List<? extends List<TaskMessageList>> itemsGet) throws Exception {
		List<TaskMessageList> items = new ArrayList<TaskMessageList>();
		items = itemsGet.get(0);
		//如果为空则返回
		if(CollectionUtils.isEmpty(items)){
			logger.debug("===========================================Batch-Test  没有数据!============");
		}
		//更新任务状态为1
		for(Integer i=0; i<items.size();i++){
			TaskMessageList item = items.get(i);
			item.setStatus(1);
			taskMarkerService.updateTaskStatus(item);
			
		}
		
	}
	
		
}