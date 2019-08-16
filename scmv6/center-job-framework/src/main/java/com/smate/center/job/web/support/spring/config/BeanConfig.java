package com.smate.center.job.web.support.spring.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring bean定义配置类（代替XML配置）
 *
 * @author houchuanjie
 * @date 2018/05/16 14:09
 */
@Configuration
public class BeanConfig {

  @Bean(name = "emptyValueObjectMapper")
  public ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.getSerializerProvider().setNullValueSerializer(getNull2EmptyValueSerializer());
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    objectMapper.setLocale(Locale.getDefault());
    objectMapper.setTimeZone(TimeZone.getDefault());
    return objectMapper;
  }

  @Bean(name = "null2EmptyValueSerializer")
  public JsonSerializer getNull2EmptyValueSerializer() {
    return new JsonSerializer() {
      @Override
      public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
          throws IOException, JsonProcessingException {
        gen.writeString("");
      }
    };
  }
}
