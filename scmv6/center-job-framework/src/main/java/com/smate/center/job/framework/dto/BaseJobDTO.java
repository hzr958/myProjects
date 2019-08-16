package com.smate.center.job.framework.dto;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobStatusEnum.JobStatusEnumDeserializer;
import com.smate.center.job.common.enums.JobTypeEnum;
import com.smate.center.job.common.enums.JobTypeEnum.JobTypeEnumDeserializer;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringUtils;

/**
 * 离线任务和在线任务的数据传输模型公共父类，不能直接实例化使用，必须通过其子类实例化，此类只是用来充当泛型
 *
 * @author houchuanjie
 * @date 2018年4月24日 下午4:41:59
 */
@JsonTypeInfo(use = Id.NAME, property = "jobType", include = As.EXISTING_PROPERTY, visible = true)
@JsonSubTypes({@Type(value = OnlineJobDTO.class, name = "1"),
    @Type(value = OfflineJobDTO.class, name = "0")})
public abstract class BaseJobDTO implements Serializable {

  /**
   * 任务id
   */
  protected String id;


  /**
   * 任务名称
   */
  protected String jobName;
  /**
   * 任务优先级
   */
  protected Integer priority;
  /**
   * 任务状态
   */
  protected JobStatusEnum status;
  /**
   * 任务类型
   */
  protected JobTypeEnum jobType;
  /**
   * 任务参数
   */
  protected String dataMap;
  /**
   * 任务错误信息
   */
  protected String errMsg;
  /**
   * 开始执行时间
   */
  protected Date startTime;
  /**
   * 执行耗时，单位：ms
   */
  protected Long elapsedTime;

  /**
   * 创建时间
   */
  protected Date gmtCreate;

  /**
   * 修改时间
   */
  protected Date gmtModified;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  /**
   * @return status
   */
  public JobStatusEnum getStatus() {
    return status;
  }

  /**
   * @param status 要设置的 status
   */
  @JsonDeserialize(using = JobStatusEnumDeserializer.class)
  public void setStatus(JobStatusEnum status) {
    this.status = status;
  }

  public JobTypeEnum getJobType() {
    return jobType;
  }

  @JsonDeserialize(using = JobTypeEnumDeserializer.class)
  public void setJobType(JobTypeEnum jobType) {
    this.jobType = jobType;
  }

  /**
   * @return dataMap
   */
  public String getDataMap() {
    return dataMap;
  }

  /**
   * @param dataMap 要设置的 dataMap
   */
  public void setDataMap(String dataMap) {
    this.dataMap = dataMap;
  }

  /**
   * @return errMsg
   */
  public String getErrMsg() {
    return errMsg;
  }

  /**
   * @param errMsg 要设置的 errMsg
   */
  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }


  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Long getElapsedTime() {
    return elapsedTime;
  }

  public void setElapsedTime(Long elapsedTime) {
    this.elapsedTime = elapsedTime;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  @Override
  public int hashCode() {
    return 31 * this.id.hashCode() + 7 * this.jobName.hashCode();
  }

  /**
   * 比较两个对象是否相同，相同的条件：hashCode相同或者id相同且jobName相同
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o instanceof BaseJobDTO) {
      BaseJobDTO that = (BaseJobDTO) o;
      if (Objects.nonNull(this.id) && this.id.equals(that.getId()) && Objects.nonNull(this.jobName)
          && this.jobName.equals(that.getJobName())) {
        return true;
      }
    }

    return false;
  }

  @Override
  public String toString() {
    return JacksonUtils.jsonObjectSerializer(this);
  }

  /**
   * 自定义BaseJobDTO反序列化处理类
   */
  public static class BaseJobDTODeserializer extends JsonDeserializer<BaseJobDTO> {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public BaseJobDTO deserialize(JsonParser parser, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      if (StringUtils.isBlank(parser.getValueAsString())) {
        return null;
      }
      ObjectReader reader = new ObjectMapper().reader();
      ObjectNode obj = (ObjectNode) reader.readTree(parser);
      try {
        JobTypeEnum jobType = JobTypeEnum.parse(obj.get("jobType").asInt());
        switch (jobType) {
          case ONLINE:
            return reader.readValue(parser, OnlineJobDTO.class);
          case OFFLINE:
            return reader.readValue(parser, OfflineJobDTO.class);
          default:
        }
      } catch (Exception e) {
        logger.error("反序列化BaseJobDTO子类实例对象，转换jobType时出错！", e);
      }
      return reader.readValue(parser, BaseJobDTO.class);
    }
  }

  

}
