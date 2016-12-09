package org.apache.hadoop.mapreduce.app.top;

import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 
 * 某个文件某列数据的top值（前三，单个数据）
 * 
 */
public class TopKMapReduceV2 {
	static class TopKMapper extends
			Mapper<LongWritable, Text, LongWritable, NullWritable> {
		public static final int KEY =3 ;
//		// map map value
//		private Text mapValue = new Text();
//		// map map key
//		private LongWritable mapKey = new LongWritable();
//		//
//		TreeMap<LongWritable, Text> topMap=new TreeMap<LongWritable, Text>();
		TreeSet<Long> topsetSet = new TreeSet<Long>();

		protected void map(LongWritable key, Text value, Context context)
				throws java.io.IOException, InterruptedException {
			// get value
			String lineValue = value.toString();
			// split
			String[] strs = lineValue.split("\t");
			// get 中间值
			long temValue = Long.valueOf(strs[1]);
//			//set map 
//			mapKey.set(temValue);
//			mapValue.set(strs[0]);
//			//add topmap
//			topMap.put(mapKey, mapValue);
//			
//			if (topMap.size()>KEY) {
//				topMap.remove(topMap.lastEntry());
//				
//			}
			topsetSet.add(temValue);
			if (topsetSet.size()>KEY) {
				topsetSet.remove(topsetSet.first());
			}

		};

		protected void setup(Context context) throws java.io.IOException,
				InterruptedException {
			super.setup(context);
		};

		protected void cleanup(Context context) throws java.io.IOException,
				InterruptedException {
//			 Set<LongWritable>  keySet= topMap.keySet();
//			 for (LongWritable key : keySet) {
//				Text outputValue = topMap.get(key);
//				context.write(outputValue, key);
//			}
			LongWritable setKey = new LongWritable();
			for (Long top : topsetSet) {
				setKey.set(top);
				context.write(setKey, NullWritable.get());
				
			}
		};
	}

	// Driver Class
		public int run(String[] args) throws Exception{
			//get conf
			Configuration conf = new Configuration();
			//creat job
			Job job = new Job(conf, TopKMapReduceV2.class.getSimpleName());
			//set job
			job.setJarByClass(TopKMapReduceV2.class);
			//1)input
			Path intputDir = new Path(args[0]);
			FileInputFormat.addInputPath(job, intputDir);
			//2)map
			job.setMapperClass(TopKMapper.class);
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(LongWritable.class);
			//3)reduce
			//set reduce task number is 0,no reduce
			job.setNumReduceTasks(0);
			
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
					"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/topkoutputV2/"
			};
			//run job
			int status = new TopKMapReduceV2().run(args);
			//exit
			System.exit(status);
		}
		
}
