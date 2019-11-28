package edu.gatech.cse6242;

/*import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;*/

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;



import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Q1 {

  public static class TokenizerMapper
  extends Mapper<Object, Text, Text, Text>{
    private Text word = new Text();
    private Text value= new Text();

    @Override

    public void map(Object key, Text value, Context context)
     throws IOException, InterruptedException {
StringTokenizer itr = new StringTokenizer(value.toString());
  String[] str_row = value.toString().split("\\s+");
          word.set(str_row[0]);
          value.set(str_row[1]+","+str_row[2]);
          context.write(word, value);


}
  }  //This is the end of TokenizerMapper

  public static class MyReducer
  extends Reducer<Text, Text, Text, Text>{

   private Text key = new Text();
   private Text value = new Text();
   @Override
   public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

String source = key.toString();

 int maxValue = Integer.MIN_VALUE;
 int target_itr= 0;



for (Text i : values){

String[] value_row = i.toString().split(",");


int target = Integer.parseInt(value_row[0]);
 int weight = Integer.parseInt(value_row[1]);

if( weight > maxValue)
{

    maxValue = weight; //iterate to find the largest value
    target_itr = target;


}


 else if ( weight == maxValue)
{
	if(target < target_itr)
  {
    target_itr = target;
  }

}
}





key.set(source);
value.set(target_itr +"," +maxValue);
context.write(key,value);








}




  } //This is the end of the reduce








  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Q1");
     job.setJarByClass(Q1.class);

        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(MyReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

    /* TODO: Needs to be implemented */

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
