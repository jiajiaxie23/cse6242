package edu.gatech.cse6242;

/*import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;*/
import java.io.IOException;

import org.apache.hadoop.util.*;


import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;



import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Q4 {

public static class TokenizerMapper
extends Mapper <Object, Text,IntWritable, IntWritable>{

  private IntWritable value1 = new IntWritable();
  private  IntWritable value2 =  new IntWritable();
  private final static IntWritable one = new IntWritable(1);
private final static IntWritable neg_one = new IntWritable(-1);
  @Override

  public void map(Object key, Text values, Context context)
  throws IOException, InterruptedException {
//StringTokenizer itr = new StringTokenizer(value.toString());

 String[] str = values.toString().split("\t");

 //int inted = str[0];
          value1.set(Integer.parseInt(str[0]));
          value2.set(Integer.parseInt(str[1]));

       

          
            
           context.write(value1,one);
         
          context.write(value2, neg_one);
         





        


  }}


  public static class MyfirstReducer
  extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable>{

   //private IntWritable key = new IntWritable();
   private  IntWritable value= new IntWritable();
  // private Text keys = new Text();
   @Override
   public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

//int source = Integer.parseInt(key.toString());

 int difference=0;

//String source_cou=null;
  for(IntWritable i:values)
  {
    difference += i.get();
  }

value.set(difference);
//keys.set(key);
context.write(key,value);



}}



   public static class SecondMapper
extends Mapper <Object, Text,IntWritable, IntWritable>{

  //private Text word = new Text();
  private IntWritable value1 =  new IntWritable();
private IntWritable value2 =  new IntWritable();
  @Override

  public void map(Object key, Text values, Context context)
  throws IOException, InterruptedException {
//StringTokenizer itr = new StringTokenizer(value.toString());
String[] inted = values.toString().split("\t");
int inted1= Integer.parseInt(inted[0]);
int inted2= Integer.parseInt(inted[1]);
value1.set(inted1);
value2.set(inted2);
 
          context.write(value2,value1);


  }}







 public static class MysecondReducer
  extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable>{

   //private Text key = new IntWritable();
   private IntWritable value = new IntWritable();
   @Override
   public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

     int counter=0;

     for(IntWritable i: values){

     
     
      counter+=1;
     }



    value.set(counter);
    context.write(key,value);


   }}





  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();

Path out = new Path(args[1]);


    Job job1 = Job.getInstance(conf, "first");
job1.setJarByClass(Q4.class);
    /* TODO: Needs to be implemented */


        job1.setMapperClass(TokenizerMapper.class);
        job1.setReducerClass(MyfirstReducer.class);
       // job1.setCombinerClass(MyfirstReducer.class);
        job1.setMapOutputKeyClass(IntWritable.class);
        job1.setMapOutputValueClass(IntWritable.class);
        job1.setOutputKeyClass(IntWritable.class);
        job1.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job1, new Path(args[0]));
    FileOutputFormat.setOutputPath(job1, new Path(out, "out1"));
    //System.exit(job1.waitForCompletion(true) ? 0 : 1);
job1.waitForCompletion(true);

    Job job2 = Job.getInstance(conf, "second");
job2.setJarByClass(Q4.class);
    /* TODO: Needs to be implemented */


        job2.setMapperClass(SecondMapper.class);
        job2.setReducerClass(MysecondReducer.class);
      //  job2.setCombinerClass(MysecondReducer.class);
         job2.setMapOutputKeyClass(IntWritable.class);
        job2.setMapOutputValueClass(IntWritable.class);

        job2.setOutputKeyClass(IntWritable.class);
        job2.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job2, new Path(out, "out1"));
    FileOutputFormat.setOutputPath(job2, new Path(out, "out2"));
    System.exit(job2.waitForCompletion(true) ? 0 : 1); 







  }
}
