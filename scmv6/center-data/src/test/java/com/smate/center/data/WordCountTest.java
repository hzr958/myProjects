package com.smate.center.data;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

import com.smate.center.data.hadoop.WordCount;

public class WordCountTest {
    MapDriver<LongWritable, Text, Text, IntWritable> mapDriver;
    ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
    MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;
    List<Pair<Text, IntWritable>> outputRecords;
    static final IntWritable one = new IntWritable(1);

    @Before
    public void setUp() {
        WordCount.TokenizerMapper mapper = new WordCount.TokenizerMapper();
        WordCount.IntSumReducer reducer = new WordCount.IntSumReducer();
        mapDriver = MapDriver.newMapDriver(mapper);
        reduceDriver = ReduceDriver.newReduceDriver(reducer);
        mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
        outputRecords = new ArrayList<Pair<Text, IntWritable>>();
    }

    @Test
    public void testMapper() throws IOException, InterruptedException {
        Text value = new Text(
                "Quantum query complexity; Fixed-point computation; Local search;   Sperner's; lemma; Quantum adversary method");

        outputRecords.add(new Pair<Text, IntWritable>(new Text("Quantum query complexity"), one));
        outputRecords.add(new Pair<Text, IntWritable>(new Text("Fixed-point computation"), one));
        outputRecords.add(new Pair<Text, IntWritable>(new Text("Local search"), one));
        outputRecords.add(new Pair<Text, IntWritable>(new Text("Sperner's"), one));
        outputRecords.add(new Pair<Text, IntWritable>(new Text("lemma"), one));
        outputRecords.add(new Pair<Text, IntWritable>(new Text("Quantum adversary method"), one));
        mapDriver.withInput(new LongWritable(), value).withAllOutput(outputRecords).runTest();

        outputRecords.forEach(pair -> System.out.println(pair));
    }

    @Test
    public void testReducer() throws IOException {
        List<Pair<Text, List<IntWritable>>> inputList = new ArrayList<Pair<Text, List<IntWritable>>>();
        ArrayList<IntWritable> arrayList1 = new ArrayList<IntWritable>();
        ArrayList<IntWritable> arrayList2 = new ArrayList<IntWritable>();
        ArrayList<IntWritable> arrayList3 = new ArrayList<IntWritable>();
        arrayList1.add(one);
        arrayList1.add(one);
        arrayList1.add(one);
        arrayList2.add(one);
        arrayList3.add(one);
        arrayList3.add(one);
        Text text1 = new Text("Local search");
        Text text2 = new Text("Fixed-point computation");
        Text text3 = new Text("Quantum adversary method");
        inputList.add(new Pair<Text, List<IntWritable>>(text1, arrayList1));
        inputList.add(new Pair<Text, List<IntWritable>>(text2, arrayList2));
        inputList.add(new Pair<Text, List<IntWritable>>(text3, arrayList3));
        outputRecords.add(new Pair<Text, IntWritable>(text1, new IntWritable(3)));
        outputRecords.add(new Pair<Text, IntWritable>(text2, new IntWritable(1)));
        outputRecords.add(new Pair<Text, IntWritable>(text3, new IntWritable(2)));
        reduceDriver.withAll(inputList).withAllOutput(outputRecords).runTest();

        outputRecords.forEach(pair -> System.out.println(pair));
    }

    @Test
    public void testMapReducer() throws IOException {
        Text value = new Text(
                "Quantum query complexity; Fixed-point computation; Local search;   Sperner's; lemma; Quantum adversary method");

        outputRecords.add(new Pair<Text, IntWritable>(new Text("Fixed-point computation"), one));
        outputRecords.add(new Pair<Text, IntWritable>(new Text("Local search"), one));
        outputRecords.add(new Pair<Text, IntWritable>(new Text("Quantum adversary method"), one));
        outputRecords.add(new Pair<Text, IntWritable>(new Text("Quantum query complexity"), one));
        outputRecords.add(new Pair<Text, IntWritable>(new Text("lemma"), one));
        outputRecords.add(new Pair<Text, IntWritable>(new Text("Sperner's"), one));

        Collections.sort(outputRecords);

        mapReduceDriver.withInput(new Pair<LongWritable, Text>(new LongWritable(), value)).withAllOutput(outputRecords)
                .runTest();
        outputRecords.forEach(pair -> System.out.println(pair));
    }

    @Test
    public void testString() {
        System.out.println(String.valueOf(100000));
    }

    @Test
    public void testBigDecimal() {
        BigDecimal bigDecimal = new BigDecimal(0.59566);
        System.out.println(bigDecimal.setScale(4, BigDecimal.ROUND_HALF_DOWN).doubleValue());
        bigDecimal = new BigDecimal(0.59564);
        System.out.println(bigDecimal.setScale(4, BigDecimal.ROUND_HALF_DOWN).doubleValue());
    }
}
