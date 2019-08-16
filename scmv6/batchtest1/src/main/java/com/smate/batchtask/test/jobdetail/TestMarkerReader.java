package com.smate.batchtask.test.jobdetail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.batchtask.test.model.TaskMessageList;
import com.smate.batchtask.test.service.TaskMarkerService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class TestMarkerReader implements ItemReader<List<TaskMessageList>>{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	//标记200个符合要求的任务
	@Autowired
	TaskMarkerService taskMarkerService;
	
	@Override
	public List<TaskMessageList> read() throws BatchTaskException,
			UnexpectedInputException, ParseException,
			NonTransientResourceException {
		List<TaskMessageList> taskList = new ArrayList<TaskMessageList>();
		//获得列表中待处理的msg数量,如果仍然有未处理完的任务，就不继续进行
		Long totalCount = taskMarkerService.getTaskMsgIdCountByStatus(1);
		if(totalCount > 0){
			return null;
		}
		
		//获得列表中status为0的msg数量，如果不超过200条，就全取出放入list中
		Long count0 = taskMarkerService.getTaskMsgIdCountByStatus(0);
		if(count0 > 0 && count0 <= 200){
			taskList = taskMarkerService.getTaskMsgIdListByWeight("all");
			return taskList;
		}
		
		//按权重比例获取任务
		taskList = taskMarkerService.arrangeTask();
		if(CollectionUtils.isEmpty(taskList)){
			logger.debug("===========================================Batch-Test  T表中无需要处理的数据!============");
			return null;
		}else{
			return taskList;
		}
	}
	
}