package com.smate.batchtask.test.service;

import java.util.List;
import java.util.Map;

import com.smate.batchtask.test.model.TaskMessageList;
import com.smate.core.base.utils.exception.BatchTaskException;

public interface TaskMarkerService {
	
	//根据status的获取taskMsgId数
	public Long getTaskMsgIdCountByStatus(Integer status) throws BatchTaskException;
	
	
	//获取指定数量的不同权重为的taskMsgList
	public List<TaskMessageList> getTaskMsgIdListByWeightAndNum(Integer num, String weight) throws BatchTaskException;
	
	//获取不同权重为的taskMsgList
	public List<TaskMessageList> getTaskMsgIdListByWeight(String weight) throws BatchTaskException;
	
	//获取不同权重为的taskMsgList
	public Long getTaskMsgIdListCountByWeight(String weight) throws BatchTaskException;
	
	//重新分配各权重任务数
	public List<TaskMessageList> arrangeTask() throws BatchTaskException;	
	
	//更新任务
	public void updateTaskStatus(TaskMessageList itemList) throws BatchTaskException;
	
}