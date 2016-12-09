package org.apache.hadoop.mapreduce.app.wendutop;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class FirstPartition extends Partitioner<KeyPair, Text>{
	
	//自定义分区
	@Override
	public int getPartition(KeyPair key, Text value, int numPartitions) {
		return (key.getYear()*127) % numPartitions;//年份相同的返回的值相同，相同值在一个分区
	}

}
