package com.smate.center.data.subset;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class KeywordSubsetCotfReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int cotf = 0;
        for (IntWritable value : values) {
            cotf += value.get();
        }
        if (cotf > 1) {
            context.write(key, new IntWritable(cotf));
        }
    }
}
