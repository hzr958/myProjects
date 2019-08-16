package com.smate.center.data.hbase.pubkeywords;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 将成果关键词的sha1值通过MapReduce计算后导入到Hbase表中
 * 
 * @author houchuanjie
 */
public class KeywordsMapReduceImporter extends Configured implements Tool {

  public static class KeywordsMapper extends Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable ONE = new IntWritable(1);
    private Text word = new Text();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      if (itr.hasMoreTokens()) {
        String idStr = itr.nextToken().trim();
        if (!NumberUtils.isNumber(idStr)) {
          return;
        }
      }
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken().trim());
        context.write(word, ONE);
      }
    }
  }

  public static class IntSumReducer extends TableReducer<Text, IntWritable, NullWritable> {

    @Override
    public void reduce(Text keyword, Iterable<IntWritable> values, Context context)
        throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      byte[] rowKey = RowKeyConverter.generateRowKey(keyword.toString());
      Put p = new Put(rowKey);
      p.addColumn(KeywordsQuery.COLUMN_FAMILY_INFO, KeywordsQuery.QUALIFIER_COUNT, Bytes.toBytes(String.valueOf(sum)));
      context.write(NullWritable.get(), p);
    }
  }

  @Override
  public int run(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Usage: KeywordsMapReduceImporter <input> <HTable>");
      return -1;
    }
    if (args[0].trim().isEmpty()) {
      System.err.println("the input path could not be empty.");
      return -1;
    }
    if (args[1].trim().isEmpty()) {
      System.err.println("the HTable name could not be empty.");
      return -1;
    }
    String path = args[0].trim();
    String tableName = args[1].trim();
    Configuration conf = HBaseConfiguration.create(getConf());
    conf.set(TableOutputFormat.OUTPUT_TABLE, TableName.valueOf(tableName).getNameWithNamespaceInclAsString());
    Job job = Job.getInstance(conf, getClass().getSimpleName());
    job.setJarByClass(getClass());

    job.setMapperClass(KeywordsMapper.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);

    job.setReducerClass(IntSumReducer.class);

    job.setOutputFormatClass(TableOutputFormat.class);

    FileInputFormat.addInputPath(job, new Path(path));

    return job.waitForCompletion(true) ? 0 : 1;
  }

  public static void main(String[] args) throws Exception {
    int exitCode = ToolRunner.run(HBaseConfiguration.create(), new KeywordsMapReduceImporter(), args);
    System.exit(exitCode);
  }

}
