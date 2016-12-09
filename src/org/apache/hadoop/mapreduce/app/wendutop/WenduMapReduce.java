package org.apache.hadoop.mapreduce.app.wendutop;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 
 * MapReduce 计算每年温度最高的时间 计算每年温度最高前十天 思路：1按年份升序排序，同时每一年温度降序排序
 * 2按照年份分组，每一年对应一个reduce任务 目的：自定排序、自定分区、自定分组
 * 未解决reduce运行不成功
 */
public class WenduMapReduce{

	public static SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * Mapper Class
	 */
	public static class WenducountMapper extends
			Mapper<LongWritable, Text, KeyPair, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			// 获取每行数据的之
			String lineValue = value.toString();
			String[] ss = lineValue.split("\t");
			// 遍历
			if (ss.length == 2) {
				try {
					Date date = SDF.parse(ss[0]);
					Calendar c = Calendar.getInstance();
					c.setTime(date);
					int year = c.get(1);
					String hot = ss[1].substring(0, ss[1].indexOf("℃"));
					KeyPair kp = new KeyPair();
					kp.set(year, Integer.parseInt(hot));
					context.write(kp, value);
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
		}

	}

	/**
	 * Reducer Class
	 */
	public static class WenduReducer extends
			Reducer<KeyPair, Text, KeyPair, Text> {
		
		@Override
		protected void reduce(
				KeyPair arg0,
				Iterable<Text> arg1,
				Context arg2)
				throws IOException, InterruptedException {
			System.out.println(111111);
			for (Text text : arg1) {
				arg2.write(arg0, text);
			}
			
		};

	}

	/**
	 * Driver
	 */
	
	public static void main(String[] args) throws Exception {
		args =new String[]{
				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/input/wendu.data",
				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/hotput/"};
		//获取配置信息
		Configuration configuration = new Configuration();
		//优化代码去掉警告WARN mapred.JobClient: Use GenericOptionsParser for parsing the arguments. Applications should implement Tool for the same
		String[] otherArgs = new GenericOptionsParser(configuration, args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage:wordcount <in><out>");
			System.exit(2);
		}
		//创建Job 设置配置和Job
		Job job = new Job(configuration, "wc");
		//1:设置Job运行的类
		job.setJarByClass(WenduMapReduce.class);
		//2：设置Mapper和Reduce类
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapperClass(WenducountMapper.class);
		job.setMapOutputKeyClass(KeyPair.class);
		job.setMapOutputValueClass(Text.class);
		job.setNumReduceTasks(1);
		job.setReducerClass(WenduReducer.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		//3：设置输入文件的目录和输出文件的目录
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		//4：设置输出结果的key和value的类型
		job.setOutputKeyClass(KeyPair.class);
		job.setOutputValueClass(Text.class);
		//5:提交Job,等待运行结果，并在客户端显示运行信息
		boolean isSuccess = job.waitForCompletion(true);
		//6:结束程序
		System.exit(isSuccess ? 0 : 1);
	}


}
