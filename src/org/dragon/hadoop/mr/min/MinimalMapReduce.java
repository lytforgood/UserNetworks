package org.dragon.hadoop.mr.min;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.app.topkey.TopKeyMapReduce;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 
 * MapReduce的最小驱动配置
 *
 */
public class MinimalMapReduce {
	/**
	 * Mapper Class
	 */
	
	/**
	 * Reducer Class
	 */
	
	/**
	 * 最小配置MapReduce：读取输入文件中的内容，输出到指定目录的输出文件中，此时文件中内容
	 * key:输入文件每行内容的起始位置         value:输入文件每行的原始内容
	 * 输出文科：key+\t+value
	 * Driver Class
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		args =new String[]{
				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/topkeyinput/",
				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/minDriveroutput/"};
		//conf
		Configuration conf	=new Configuration();
		//create job
		Job job = new Job(conf,MinimalMapReduce.class.getSimpleName());
		//set job
		job.setJarByClass(TopKeyMapReduce.class);
		//set input/output path
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		//submin job
		boolean isSuccess=job.waitForCompletion(true);
		//exit
		System.exit(isSuccess ? 0 : 1);
	}
	
}
