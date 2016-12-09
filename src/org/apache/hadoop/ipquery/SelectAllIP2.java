package org.apache.hadoop.ipquery;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
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

public abstract class SelectAllIP2 extends Configured implements Tool {

	enum Counter {
		LINESKIP,
	}

	public static class Map extends Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, Context context)
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
						context.write(new Text("ds"), out); // 以源地址做key值,out为value
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
	}

	public static class Reduce extends Reducer<Text, Text, NullWritable, Text> {
		private MultipleOutputs<NullWritable, Text> mos;

		protected void setup(Context context) throws IOException,
				InterruptedException {
			mos = new MultipleOutputs<NullWritable, Text>(context);

		}

		protected void reduce(Text key, Iterable<Text> value, Context context)
				throws IOException, InterruptedException {
			/*
			 * NullWritable nullWritable = null; String keys = key.toString();
			 * System.out.println(keys); for (Text text : value) {
			 * 
			 * if (key.equals("ds")) { mos.write("ds", nullWritable, text);
			 * 
			 * 
			 * }else if (key.equals("kx")) { mos.write("kx", nullWritable,
			 * text);
			 * 
			 * } else if (key.equals("qz")) { mos.write("qz", nullWritable,
			 * text);
			 * 
			 * } else if (key.equals("rr")) { mos.write("rr", nullWritable,
			 * text);
			 * 
			 * } else if (key.equals("sina")) { mos.write("sina", nullWritable,
			 * text);
			 * 
			 * } else if (key.equals("txwb")) { mos.write("txwb", nullWritable,
			 * text);
			 * 
			 * } else if (key.equals("wxwb")) { mos.write("wxwb", nullWritable,
			 * text);
			 * 
			 * }else { System.out.println("NOT FOUND"); } }
			 */
			if ("rr".equals(key.toString())) {
				for (Text t : value)
					mos.write("rr", null, t);

			} else if (key.toString().equals("ds")) {
				for (Text t : value)
					mos.write("ds", null, t);

			} else if (key.toString().equals("kx")) {
				for (Text t : value)
					mos.write("kx", null, t);

			} else if (key.toString().equals("qz")) {
				for (Text t : value)
					mos.write("qz", null, t);

			} else if (key.toString().equals("rr")) {
				for (Text t : value)
					mos.write("rr", null, t);

			} else if (key.toString().equals("sina")) {
				for (Text t : value)
					mos.write("sina", null, t);

			} else if (key.toString().equals("txwb")) {
				for (Text t : value)
					mos.write("txwb", null, t);

			} else if (key.toString().equals("wxwb")) {
				for (Text t : value)
					mos.write("wxwb", null, t);

			} else {
				System.out.println("NOT FOUND");
			}
		}

		protected void cleanup(Context context) throws IOException,
				InterruptedException {

			mos.close();

		}

	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		FileSystem hdfs = FileSystem.get(conf);
		Path path = new Path("/input/20140816/");
//		Path path = new Path("hdfs://192.168.1.30:9000/input/20140816/");
		FileStatus status[] = hdfs.listStatus(path);
		String inputs="";
		for (int i = 0; i < status.length; i++) {
			System.out.println(status[i].getPath().toString());
			inputs=status[i].getPath().toString()+","+inputs;
		}
		inputs = inputs.substring(0,inputs.length()-1); 
		hdfs.close();

		Job job = new Job(conf);
		job.setJarByClass(SelectAllIP2.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);

		MultipleOutputs.addNamedOutput(job, "ds", TextOutputFormat.class,
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
		FileInputFormat.addInputPaths(job, inputs);
		FileOutputFormat.setOutputPath(job, new Path(
				"/home910/dingxiaoqiang/ipout/"));
		job.waitForCompletion(true);
		System.exit(0);

	}
}
