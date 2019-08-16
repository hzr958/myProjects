package com.smate.center.data.nsfckw;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.smate.center.data.utils.XmlUtil;

public class NsfcKeywordsTfMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
  private static IntWritable one = new IntWritable(1);

  @Override
  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    if (value == null || StringUtils.isEmpty(value.toString())) {
      return;
    }
    // 每一行为一个成果或者项目的关键词，分隔符为英文分号";"
    String[] keyString = value.toString().split(";");
    if (keyString == null || keyString.length < 1) {
      return;
    }
    for (String kw : keyString) {
      try {
        if (StringUtils.isEmpty(kw)) {
          continue;
        }
        kw = XmlUtil.cToe(kw);
        kw = XmlUtil.getSimpleCleanAuthorName(kw);
        String newKw = StringUtils.trim(kw.toLowerCase());
        context.write(new Text(newKw), one);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
