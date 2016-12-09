package org.apache.hadoop.mapreduce.app.paixu;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
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
public class Mykey2MapReduce {
	/**
	 * Mapper Class
	 */
	static class Mykey2Maper extends Mapper<LongWritable, Text, MyKt, Text> {
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] split = value.toString().split("\t");
			System.out.println(split[1].trim().substring(9,split[1].trim().length()));
			MyKt MyKt = new MyKt(split[0], split[1].trim().substring(9,split[1].trim().length()));
			context.write(MyKt, value);
		};
	}

	/**
	 * Reducer Class
	 */
	static class Mykey2Reduce extends Reducer<MyKt, Text, Text, NullWritable> {
		protected void reduce(MyKt myk2, Iterable<Text> v2s, Context context)
				throws IOException, InterruptedException {
			for (Text text : v2s) {
				context.write(new Text(text), NullWritable.get());
			}

		};
	}

	/**
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		args = new String[] {
				"hdfs://hadoop-00:9000/home910/liyuting/input/paixu2.txt",
				"hdfs://hadoop-00:9000/home910/liyuting/paixuoutput" };
		// conf
		Configuration conf = new Configuration();
		// create job
		Job job = new Job(conf, Mykey2MapReduce.class.getSimpleName());
		// set job
		job.setJarByClass(Mykey2MapReduce.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapperClass(Mykey2Maper.class);
		job.setMapOutputKeyClass(MyKt.class);
		job.setMapOutputValueClass(Text.class);
		job.setNumReduceTasks(1);
		job.setReducerClass(Mykey2Reduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
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
