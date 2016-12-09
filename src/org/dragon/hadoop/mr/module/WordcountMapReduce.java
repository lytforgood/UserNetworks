package org.dragon.hadoop.mr.module;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
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
 * MapReduce 模板 编写 wordcount +使用枚举自定义计数器
 * 
 */
public class WordcountMapReduce extends Configured implements Tool {

	/**
	 * Counter Class
	 */
	public static enum Counter {
		MAP_INPUT_KEYVALUES, MAP_OUTPUT_KEYVALUES, REDUCE_INPUT_KEYVALUES, REDUCE_OUTPUT_KEYVALUES
	}

	/**
	 * Mapper Class
	 */
	public static class WordcountMapper extends
			Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			super.setup(context);
		}

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			// 获取每行数据的之
			String lineValue = value.toString();
			// 进行分割 \t\n\r\f 空格,制表符\t,换行\n,回车符\r,换页\f
			StringTokenizer stringTokenizer = new StringTokenizer(lineValue);
			// 遍历
			while (stringTokenizer.hasMoreElements()) {
				// 获取每个值
				String wordValue = stringTokenizer.nextToken();
				// 设置map输出的key值
				word.set(wordValue);
				//map output counter +1
				context.getCounter(Counter.MAP_OUTPUT_KEYVALUES).increment(1L);
				
				// 上下文输出map的key和value
				context.write(word, one);
			}
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
	public static class WordcountReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {

		private IntWritable result = new IntWritable();

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			super.setup(context);
		}

		@Override
		protected void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			
			//reduce input counter +1
			context.getCounter(Counter.REDUCE_INPUT_KEYVALUES).increment(1L);
			// 用于累加
			int sum = 0;
			// 循环遍历 Interable
			for (IntWritable value : values) {
				// 累加
				sum += value.get();
			}
			// 设置总次数
			result.set(sum);
			
			//reduce output counter +1
			context.getCounter(Counter.REDUCE_OUTPUT_KEYVALUES).increment(1L);
			
			context.write(key, result);
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
		job.setMapperClass(WordcountMapper.class);
		// 3.5 set map output key/value class
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
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
		job.setReducerClass(WordcountReducer.class);
		// 3.12 set output format
		job.setOutputFormatClass(TextOutputFormat.class);
		// 3.13 job output key/value class
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
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
		args = new String[] {
				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/input/sss.data",
				// "hdfs://hadoop-00:9000/home910/liyuting/output/" };
				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/wordoutput2/" };
		// run mapreduce
		int status = ToolRunner.run(new WordcountMapReduce(), args);
		// 5 exit
		System.exit(status);
	}
}
