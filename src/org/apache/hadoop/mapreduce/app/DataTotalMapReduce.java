package org.apache.hadoop.mapreduce.app;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
/**
 * 
 *手机流量分析
 */
public class DataTotalMapReduce {
	// Mapper Class
	static class DataTotalMapper extends
			Mapper<LongWritable, Text, Text, DataWritable> {
		
		private Text mapOutputKey =new Text();
		private DataWritable dataWritable =new DataWritable();
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String lineValue = value.toString();
			//split
			String[] strs=lineValue.split("\t");
			//get data
			String phoneNum = strs[1];
			int upPackNum = Integer.valueOf(strs[5]);
			int downPackNum = Integer.valueOf(strs[6]);
			int upPayLoad = Integer.valueOf(strs[7]);
			int downPayLoad = Integer.valueOf(strs[8]);
			//set map output key / value
			mapOutputKey.set(phoneNum);
			dataWritable.set(upPackNum, upPayLoad, downPackNum, downPayLoad);
			//set map output
			context.write(mapOutputKey, dataWritable);
			
		};
	}

	// Reducer Class
	static class DataTotalReducer extends
			Reducer<Text, DataWritable, Text, DataWritable> {
		private DataWritable dataWritable =new DataWritable();
		
		public void reduce(Text key, Iterable<DataWritable> values,
				Context context) throws IOException, InterruptedException {
			int upPackNum = 0;
			int downPackNum = 0;
			int upPayLoad = 0;
			int downPayLoad = 0;
			//iterator
			for (DataWritable data : values) {
				upPackNum+=data.getUpPackNum();
				downPackNum+=data.getDownPackNum();
				upPayLoad+=data.getUpPayLoad();
				downPayLoad+=data.getDownPayLoad();
			}
			//set dataWritable
			dataWritable.set(upPackNum, upPayLoad, downPackNum, downPayLoad);
			//set reduce/job output
			context.write(key, dataWritable);
			
		};
	}
	// Driver Class
	public int run(String[] args) throws Exception{
		//get conf
		Configuration conf = new Configuration();
		//creat job
		Job job = new Job(conf, DataTotalMapReduce.class.getSimpleName());
		//set job
		job.setJarByClass(DataTotalMapReduce.class);
		//1)input
		Path intputDir = new Path(args[0]);
		FileInputFormat.addInputPath(job, intputDir);
		//2)map
		job.setMapperClass(DataTotalMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(DataWritable.class);
		//3)reduce
		job.setReducerClass(DataTotalReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DataWritable.class);
		//4)output
		Path outputDir =new Path(args[1]);
		FileOutputFormat.setOutputPath(job, outputDir);
		//sumbit job
		boolean isSuccess =job.waitForCompletion(true);
		//return status
		return isSuccess ? 0 : 1;
	}
	//run mapreduce
	public static void main(String[] args) throws Exception {
		//set args
		args = new String[]{
				//input path
				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/input/",
				//output path
				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/output3/"
		};
		//run job
		int status = new DataTotalMapReduce().run(args);
		//exit
		System.exit(status);
	}
	
}
