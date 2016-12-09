package org.apache.hadoop.ipquery;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;



/**
 * 
 * MapReduce 模板 编写 wordcount +使用枚举自定义计数器
 * 
 */
public class SelectAllIP3 extends Configured implements Tool {

	/**
	 * Counter Class
	 */
	enum Counter {
		LINESKIP,
	}

	/**
	 * Mapper Class
	 */
	public static class SelectAllIPMapper extends
			Mapper<LongWritable, Text, Text, Text> {

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			super.setup(context);
		}

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			String regex = "([0-3][0-9]\\/[0-1][0-9]\\/[2][0][1][3-7])\\s*?" // 匹配开始日期
					+ "([0-2][0-9]\\:[0-6][0-9]\\:[0-6][0-9])\\s*?" // 开始时间
					+ "([0-3][0-9]\\/[0-1][0-9]\\/[2][0][1][3-7])\\s*?" // 结束日期
					+ "([0-2][0-9]\\:[0-6][0-9]\\:[0-6][0-9])\\s*?" // 结束时间
					+ "((\\s*?[0-9]+\\s*?\\.){3}\\s*?[0-9]+)\\s*?" // 源IP
					+ "((\\s*?[0-9]+\\s*?\\.){3}\\s*?[0-9]+)"; // 目标IP
			try {
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					String dstIP = matcher.group(7).replace(" ", "");
					if (DstIPContains.isDBContains(dstIP)) { // 查找源地址为X.X.X.X且目标地址为X.X.X.X

						String startDate = matcher.group(1);
						String startTime = matcher.group(2);
						String endTime = matcher.group(4);
						String srcIP = matcher.group(5).replace(" ", "");
						Text out = new Text(startDate + "\t" + startTime + "\t"
								+ endTime + " " + srcIP);
						context.write(new Text("db"), out); // 以源地址做key值,out为value
					}
					if (DstIPContains.isKXContains(dstIP)) { // 查找源地址为X.X.X.X且目标地址为X.X.X.X

						String startDate = matcher.group(1);
						String startTime = matcher.group(2);
						String endTime = matcher.group(4);
						String srcIP = matcher.group(5).replace(" ", "");
						Text out = new Text(startDate + "\t" + startTime + "\t"
								+ endTime + " " + srcIP);
						context.write(new Text("kx"), out); // 以源地址做key值,out为value
					}
					if (DstIPContains.isQZContains(dstIP)) { // 查找源地址为X.X.X.X且目标地址为X.X.X.X

						String startDate = matcher.group(1);
						String startTime = matcher.group(2);
						String endTime = matcher.group(4);
						String srcIP = matcher.group(5).replace(" ", "");
						Text out = new Text(startDate + "\t" + startTime + "\t"
								+ endTime + " " + srcIP);
						context.write(new Text("qz"), out); // 以源地址做key值,out为value
					}
					if (DstIPContains.isRRContains(dstIP)) { // 查找源地址为X.X.X.X且目标地址为X.X.X.X

						String startDate = matcher.group(1);
						String startTime = matcher.group(2);
						String endTime = matcher.group(4);
						String srcIP = matcher.group(5).replace(" ", "");
						Text out = new Text(startDate + "\t" + startTime + "\t"
								+ endTime + " " + srcIP);
						context.write(new Text("rr"), out); // 以源地址做key值,out为value
					}
					if (DstIPContains.isSinaWBContains(dstIP)) { // 查找源地址为X.X.X.X且目标地址为X.X.X.X

						String startDate = matcher.group(1);
						String startTime = matcher.group(2);
						String endTime = matcher.group(4);
						String srcIP = matcher.group(5).replace(" ", "");
						Text out = new Text(startDate + "\t" + startTime + "\t"
								+ endTime + " " + srcIP);
						context.write(new Text("sina"), out); // 以源地址做key值,out为value
					}
					if (DstIPContains.isTXWBContains(dstIP)) { // 查找源地址为X.X.X.X且目标地址为X.X.X.X

						String startDate = matcher.group(1);
						String startTime = matcher.group(2);
						String endTime = matcher.group(4);
						String srcIP = matcher.group(5).replace(" ", "");
						Text out = new Text(startDate + "\t" + startTime + "\t"
								+ endTime + " " + srcIP);
						context.write(new Text("txwb"), out); // 以源地址做key值,out为value
					}
					if (DstIPContains.isWXWBContains(dstIP)) { // 查找源地址为X.X.X.X且目标地址为X.X.X.X

						String startDate = matcher.group(1);
						String startTime = matcher.group(2);
						String endTime = matcher.group(4);
						String srcIP = matcher.group(5).replace(" ", "");
						Text out = new Text(startDate + "\t" + startTime + "\t"
								+ endTime + " " + srcIP);
						context.write(new Text("wxwb"), out); // 以源地址做key值,out为value
					}
					
				}
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				context.getCounter(Counter.LINESKIP).increment(1);
				return;
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
	public static class SelectAllIPReducer extends
			Reducer<Text, Text, NullWritable, Text> {
		private MultipleOutputs<NullWritable, Text> mos;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			mos = new MultipleOutputs<NullWritable, Text>(context);
		}

		@Override
		protected void reduce(Text key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
			if ("rr".equals(key.toString())) {
				for (Text t : values)
					mos.write("rr", null, t);

			} else if (key.toString().equals("db")) {
				for (Text t : values)
					mos.write("db", null, t);

			} else if (key.toString().equals("kx")) {
				for (Text t : values)
					mos.write("kx", null, t);

			} else if (key.toString().equals("qz")) {
				for (Text t : values)
					mos.write("qz", null, t);

			} else if (key.toString().equals("rr")) {
				for (Text t : values)
					mos.write("rr", null, t);

			} else if (key.toString().equals("sina")) {
				for (Text t : values)
					mos.write("sina", null, t);

			} else if (key.toString().equals("txwb")) {
				for (Text t : values)
					mos.write("txwb", null, t);

			} else if (key.toString().equals("wxwb")) {
				for (Text t : values)
					mos.write("wxwb", null, t);

			} else {
				System.out.println("NOT FOUND");
			}
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			mos.close();
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
		//job.setInputFormatClass(TextInputFormat.class);
		// 3.3 set input path
		// FileInputFormat.addInputPath(job, new Path(args[0]));
		// 3.4 set mapper
		job.setMapperClass(SelectAllIPMapper.class);
		// 3.5 set map output key/value class
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		// 3.6 set partitioner class
		//job.setPartitionerClass(HashPartitioner.class);
		// 3.7 set reduce number
		//job.setNumReduceTasks(1);
		// 3.8 set sort comparator class
		// job.setSortComparatorClass(LongWritable.Comparator.class);
		// 3.9 set group comparator class
		// job.setGroupingComparatorClass(LongWritable.Comparator.class);
		// 3.10 set combiner class
		// job.setCombinerClass(null);
		// 3.11 set reducer class
		job.setReducerClass(SelectAllIPReducer.class);
		// 3.12 set output format
		MultipleOutputs.addNamedOutput(job, "db", TextOutputFormat.class,
				NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, "kx", TextOutputFormat.class,
				NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, "qz", TextOutputFormat.class,
				NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, "rr", TextOutputFormat.class,
				NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, "sina", TextOutputFormat.class,
				NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, "txwb", TextOutputFormat.class,
				NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, "wxwb", TextOutputFormat.class,
				NullWritable.class, Text.class);
		//job.setOutputFormatClass(TextOutputFormat.class);
		// 3.13 job output key/value class
		job.setOutputKeyClass(NullWritable.class);
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
//		args = new String[] {
//				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/inputip/",
//				// "hdfs://hadoop-00:9000/home910/liyuting/output/" };
//				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/ipoutput2/" };
		// run mapreduce
		int status = ToolRunner.run(new SelectAllIP3(), args);
		// 5 exit
		System.exit(status);
	}
}
