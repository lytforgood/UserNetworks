package org.apache.hadoop.mapreduce.app.paixu;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * 
 * MapReduce的最小驱动配置
 * 
 */
public class MykeyMapReduce {
	/**
	 * Mapper Class
	 */
	static class MykeyMaper extends
			Mapper<LongWritable, Text, MyK2, LongWritable> {
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] split = value.toString().split("\t");
			MyK2 myK2 = new MyK2(Long.parseLong(split[0]),
					Long.parseLong(split[1]));
			context.write(myK2, new LongWritable(Long.parseLong(split[1])));
		};
	}

	/**
	 * Reducer Class
	 */
	static class MykeyReduce extends
			Reducer<MyK2, LongWritable, LongWritable, LongWritable> {
		protected void reduce(MyK2 myk2, Iterable<LongWritable> v2s,
				Context context) throws IOException, InterruptedException {
			context.write(new LongWritable(myk2.myk2), new LongWritable(
					myk2.myv2));
		};
	}

	/**
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		args = new String[] {
				"hdfs://hadoop-00:9000/home910/liyuting/input/paixu.txt",
				"hdfs://hadoop-00:9000/home910/liyuting/paixuoutput" };
		// conf
		Configuration conf = new Configuration();
		// create job
		Job job = new Job(conf, MykeyMapReduce.class.getSimpleName());
		// set job
		job.setJarByClass(MykeyMapReduce.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapperClass(MykeyMaper.class);
		job.setMapOutputKeyClass(MyK2.class);
		job.setMapOutputValueClass(LongWritable.class);
		job.setReducerClass(MykeyReduce.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(LongWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		// set input/output path
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		// submin job
		boolean isSuccess = job.waitForCompletion(true);
		// exit
		System.exit(isSuccess ? 0 : 1);
	}

}
