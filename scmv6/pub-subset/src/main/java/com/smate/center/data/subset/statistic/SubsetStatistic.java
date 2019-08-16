package com.smate.center.data.subset.statistic;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SubsetStatistic {

  public static void main(String[] args) throws Exception {
    Job job = Job.getInstance();
    job.setJarByClass(SubsetStatistic.class);
    job.setJobName("keyword subset statistic");
    job.setMapperClass(SubsetStatisticMapper.class);
    job.setReducerClass(SubsetStatisticReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    job.setNumReduceTasks(20);
    FileInputFormat.addInputPath(job, new Path("/output/subset/"));
    FileOutputFormat.setOutputPath(job, new Path("/output/subsetcount/"));
    System.exit(job.waitForCompletion(true) ? 0 : 1);

  }
}
