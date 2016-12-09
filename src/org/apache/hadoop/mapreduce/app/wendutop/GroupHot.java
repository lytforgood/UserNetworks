package org.apache.hadoop.mapreduce.app.wendutop;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class GroupHot extends WritableComparator{

	public GroupHot(){
		super(KeyPair.class,true);
	}
	
	//自定义分组，按年份分组，不用按年份+温度分组,返回0相同非零不同
	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		KeyPair o1=(KeyPair) a;
		KeyPair o2=(KeyPair) b;
		return Integer.compare(o1.getYear(), o2.getYear());//顺序排序
	}

}
