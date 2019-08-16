package com.smate.center.data.subset.statistic;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SubsetStatisticMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
  private static IntWritable one = new IntWritable(1);

  @Override
  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    if (value == null || StringUtils.isEmpty(value.toString())) {
      return;
    }
    String[] keyString = value.toString().split("\t");
    if (keyString == null || keyString.length < 1) {
      return;
    }
    String subset = keyString[0];
    String cotf = keyString[1];
    Integer subsetSize = subset.split(";").length;
    context.write(new Text("Subset_Size==" + subsetSize + ";Cotf==ALL"), one);
    if (Integer.parseInt(cotf) > 1) {
      context.write(new Text("Subset_Size==" + subsetSize + ";Cotf==GT1"), one);
    }
    Text rsString = new Text("Subset_Size==" + subsetSize + ";Cotf==" + cotf);
    context.write(rsString, one);
  }

}
