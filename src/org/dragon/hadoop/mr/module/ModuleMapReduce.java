package org.dragon.hadoop.mr.module;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
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
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 
 * MapReduce 模板
 * 
 */
public class ModuleMapReduce extends Configured implements Tool {

	/**
	 * Mapper Class
	 */
	public static class ModuleMapper extends
			Mapper<LongWritable, Text, LongWritable, Text> {

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			super.setup(context);
		}

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			super.map(key, value, context);
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			super.cleanup(context);
		}

	}

	/**
	 * Reducer Class
	 */
	public static class ModuleReducer extends
			Reducer<LongWritable, Text, LongWritable, Text> {

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			super.setup(context);
		}

		@Override
		protected void reduce(LongWritable key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
			super.reduce(key, values, context);
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			super.cleanup(context);
		}
	}

	/**
	 * Driver
	 */
	public int run(String[] args) throws Exception {
		// 1 conf
		Configuration conf = new Configuration();
		// 2 create job
		// Job job = new Job(conf, ModuleMapReduce.class.getSimpleName());
		Job job = this.parseInputAndOutput(this, conf, args);
		// 3 set job
		// 3.1 set run jar class
		// job.setJarByClass(ModuleReducer.class);
		// 3.2 set intputformat
		job.setInputFormatClass(TextInputFormat.class);
		// 3.3 set input path
		// FileInputFormat.addInputPath(job, new Path(args[0]));
		// 3.4 set mapper
		job.setMapperClass(Mapper.class);
		// 3.5 set map output key/value class
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);
		// 3.6 set partitioner class
		job.setPartitionerClass(HashPartitioner.class);
		// 3.7 set reduce number
		job.setNumReduceTasks(1);
		// 3.8 set sort comparator class
		// job.setSortComparatorClass(LongWritable.Comparator.class);
		// 3.9 set group comparator class
		// job.setGroupingComparatorClass(LongWritable.Comparator.class);
		// 3.10 set combiner class
		// job.setCombinerClass(null);
		// 3.11 set reducer class
		job.setReducerClass(Reducer.class);
		// 3.12 set output format
		job.setOutputFormatClass(TextOutputFormat.class);
		// 3.13 job output key/value class
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		// 3.14 set job output path
		// FileOutputFormat.setOutputPath(job, new Path(args[1]));
		// 4 submit job
		boolean isSuccess = job.waitForCompletion(true);
		// 5 exit
		// System.exit(isSuccess ? 0 : 1);
		return isSuccess ? 0 : 1;
	}

	public Job parseInputAndOutput(Tool tool, Configuration conf, String[] args)
			throws Exception {
		// validate
		if (args.length != 2) {
			System.err.printf("Usage:%s [genneric options]<input><output>\n",
					tool.getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return null;
		}
		// 2 create job
		Job job = new Job(conf, tool.getClass().getSimpleName());
		// 3.1 set run jar class
		job.setJarByClass(tool.getClass());
		// 3.3 set input path
		FileInputFormat.addInputPath(job, new Path(args[0]));
		// 3.14 set job output path
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job;
	}

	public static void main(String[] args) throws Exception {
		// run mapreduce
		int status = ToolRunner.run(new ModuleMapReduce(), args);
		// 5 exit
		System.exit(status);
	}
}
