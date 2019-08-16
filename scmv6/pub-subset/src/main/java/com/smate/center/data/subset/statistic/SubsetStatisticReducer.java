package com.smate.center.data.subset.statistic;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SubsetStatisticReducer extends Reducer<Text, IntWritable, Text, LongWritable> {

  @Override
  public void reduce(Text keyString, Iterable<IntWritable> value, Context context)
      throws IOException, InterruptedException {
    Long counts = 0L;
    for (IntWritable one : value) {
      counts = counts + one.get();
    }
    context.write(keyString, new LongWritable(counts));
  }

}
