package com.smate.center.data.subset;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class KeywordSubsetCotfMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
  private final IntWritable one = new IntWritable(1);

  @Override
  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    if (value == null || StringUtils.isEmpty(value.toString())) {
      return;
    }
    String subsetStr = value.toString();
    Integer start = subsetStr.indexOf("-!");
    String kwStr = StringUtils.trimToEmpty(subsetStr.substring(start + 2));// 关键词文本可能包含 discode,需要从文本里边判断
    if (StringUtils.isNotEmpty(kwStr)) {
      context.write(new Text(kwStr), one);
    }
  }
}
