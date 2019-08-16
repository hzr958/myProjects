package com.smate.center.data.subset;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class KeywordSubsetCotf {

    public static void main(String[] args) throws Exception {
        Job job = Job.getInstance();
        job.setJarByClass(KeywordSubsetCotf.class);
        job.setJobName("Keyword subset cotf");
        job.setMapperClass(KeywordSubsetCotfMapper.class);
        job.setReducerClass(KeywordSubsetCotfReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setNumReduceTasks(1);
        /*
         * Job job1 = Job.getInstance(); job1.setJarByClass(KeywordSubsetCotf.class);
         * job1.setJobName("keyword subset statistic"); job1.setMapperClass(KeywordSubsetCotfMapper.class);
         * job1.setReducerClass(KeywordSubsetCotfReducer.class); job1.setOutputKeyClass(IntWritable.class);
         * job1.setOutputValueClass(IntWritable.class);
         */

        FileInputFormat.addInputPath(job, new Path("/input/test/subset/"));
        FileOutputFormat.setOutputPath(job, new Path("/output/test/subset/"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
