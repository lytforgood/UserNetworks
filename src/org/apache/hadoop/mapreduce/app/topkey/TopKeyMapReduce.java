package org.apache.hadoop.mapreduce.app.topkey;

import java.io.IOException;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 数据格式：语音类别 歌曲名称 收藏次数 播放次数 歌手名称 需求：统计前十首播放次数最多的歌曲名称和次数
 * 
 */
public class TopKeyMapReduce {

	private static final int KEY = 10;

	// mapper
	public static class TopKeyMapper extends
			Mapper<LongWritable, Text, Text, LongWritable> {
		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			// TODO Auto-generated method stub
			super.setup(context);
		}

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String lineValue = value.toString();
			// invalidate
			if (null == lineValue) {
				return;
			}
			// split
			String[] strs = lineValue.split("\t");
			if (null != strs && strs.length == 5) {
				String languageType = strs[0];
				String singName = strs[1];
				String playTimes = strs[3];
				context.write(//
						new Text(languageType + "\t" + singName), //
						new LongWritable(Long.valueOf(playTimes))//
				);
			}

		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			// TODO Auto-generated method stub
			super.cleanup(context);
		}

	}

	// reduce
	public static class TopKeyReducer extends
			Reducer<Text, LongWritable, TopKeyWritable, NullWritable> {
		//store data
		TreeSet<TopKeyWritable> topSet = new TreeSet<TopKeyWritable>();
		

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			// TODO Auto-generated method stub
			super.setup(context);
		}

		@Override
		protected void reduce(Text key, Iterable<LongWritable> values,
				Context context) throws IOException, InterruptedException {
			if (null == key) {
				return;
			}
			String[] splited = key.toString().split("\t");
			if (null==splited || splited.length==0) {
				return;
			}
			String languageType = splited[0];
			String singName = splited[1];
			Long playtimes = 0L;
			for (LongWritable value : values) {
				playtimes+=value.get();
			}
			topSet.add(new TopKeyWritable(languageType,singName,playtimes));
			if (topSet.size()>KEY) {
				topSet.remove(topSet.last());
			}
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			for (TopKeyWritable top : topSet) {
				context.write(top, NullWritable.get());
			}
		}

	}
	// driver
	public int run(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job =new Job(conf,TopKeyMapReduce.class.getSimpleName());
		job.setJarByClass(TopKeyMapReduce.class);
		Path intputPath = new Path(args[0]);
		FileInputFormat.addInputPath(job, intputPath);
		job.setMapperClass(TopKeyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		job.setReducerClass(TopKeyReducer.class);
		job.setNumReduceTasks(1);
		job.setOutputKeyClass(TopKeyWritable.class);
		job.setOutputValueClass(NullWritable.class);
		Path outputDir = new Path(args[1]);
		FileOutputFormat.setOutputPath(job, outputDir);
		boolean isSuccess=job.waitForCompletion(true);
		return isSuccess ? 0 : 1;
	}
	public static void main(String[] args) throws Exception {
		args =new String[]{
				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/topkeyinput/",
				"hdfs://hadoop-master.dragon.org:9000/opt/data/wc/output/"};
		int status =new TopKeyMapReduce().run(args);
		System.exit(status);
		
	}
}
