package org.apache.hadoop.mapreduce.app.top;

import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;

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
 * 多个文件某列数据的top值（前三，key+value）
 * 
 */
public class TopKMapReduceV4 {

	public static final int KEY = 3;

	// Mapper Class
	static class TopKMapper extends
			Mapper<LongWritable, Text, Text, LongWritable> {
		private Text mapOutputKey = new Text();
		private LongWritable mapOutputValue = new LongWritable();

		public void setup(Context context) throws java.io.IOException,
				InterruptedException {
			super.setup(context);
		};

		public void map(LongWritable key, Text value, Context context)
				throws java.io.IOException, InterruptedException {
			// get value
			String lineValue = value.toString();
			// split
			String[] strs = lineValue.split("\t");
			// get 中间值
			long temValue = Long.valueOf(strs[1]);
			// set
			mapOutputKey.set(strs[0]);
			mapOutputValue.set(temValue);
			// set map output
			context.write(mapOutputKey, mapOutputValue);
		};

		public void cleanup(Context context) throws java.io.IOException,
				InterruptedException {
			super.cleanup(context);
		};
	}

	// Reducer Class
	static class TopKReducer extends
			Reducer<Text, LongWritable, Text, LongWritable> {
		
		TreeSet<TopKWritable> topSet = new TreeSet<TopKWritable>(
				new Comparator<TopKWritable>() {

					public int compare(TopKWritable o1, TopKWritable o2) {
						return o1.getCount().compareTo(o2.getCount());
					}
				});

		
		protected void setup(Context context) throws IOException,
				InterruptedException {
			super.setup(context);
		}

		protected void reduce(Text key, Iterable<LongWritable> values,
				Context context) throws IOException, InterruptedException {
			long count = 0L;
			// get count
			for (LongWritable value : values) {
				count += value.get();
			}
			// add
			topSet.add(new TopKWritable(key.toString(), count));
			// comparator
			if (topSet.size() > KEY) {
				topSet.remove(topSet.first());
			}
		}

		
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			for (TopKWritable top : topSet) {
				context.write(new Text(top.getWord()),
						new LongWritable(top.getCount()));

			}
		}

	}

	// Driver Class
	public int run(String[] args) throws Exception {
		// get conf
		Configuration conf = new Configuration();
		// creat job
		Job job = new Job(conf, TopKMapReduceV4.class.getSimpleName());
		// set job
		job.setJarByClass(TopKMapReduceV4.class);
		// 1)input
		Path intputDir = new Path(args[0]);
		FileInputFormat.addInputPath(job, intputDir);
		// 2)map
		job.setMapperClass(TopKMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		// 3)reduce
		job.setReducerClass(TopKReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		job.setNumReduceTasks(1); // default 1

		// 4)output
		Path outputDir = new Path(args[1]);
		FileOutputFormat.setOutputPath(job, outputDir);
		// sumbit job
		boolean isSuccess = job.waitForCompletion(true);
		// return status
		return isSuccess ? 0 : 1;
	}

	// run mapreduce
	public static void main(String[] args) throws Exception {
		// set args
		args = new String[] {
				// input path
				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/input",
				// output path
				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/topkoutputV4" };
		// run job
		int status = new TopKMapReduceV4().run(args);
		// exit
		System.exit(status);
	}

}
