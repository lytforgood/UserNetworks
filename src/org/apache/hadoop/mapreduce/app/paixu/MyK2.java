package org.apache.hadoop.mapreduce.app.paixu;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class MyK2 implements  WritableComparable<MyK2> {
	public long myk2;
	public long myv2;

	public MyK2() {
		// TODO Auto-generated constructor stub
	}

	public MyK2(long myk2, long myv2) {
		this.myk2 = myk2;
		this.myv2 = myv2;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(myk2);
		out.writeLong(myv2);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.myk2 = in.readLong();
		this.myv2 = in.readLong();
	}

	/**
	 * #首先按照第一列升序排列，当第一列相同时，第二列升序排列 当k2进行排序时，会调用该方法. 当第一列不同时，升序；当第一列相同时，第二列升序
	 */
	/*
	 * @Override public int compareTo(MyK2 my) { long temp=this.myk2-my.myk2;
	 * if(temp!=0){ return (int) temp; } return (int) (this.myv2-my.myv2); }
	 */
	/**
	 * ###作业：第一列降序排列，第一列相同时，第二列升序排列 当k2进行排序时，会调用该方法 第一列：降序，当第一列相同时，第二列：升序
	 */
	@Override
	public int compareTo(MyK2 my) {
		long temp = this.myk2 - my.myk2;
		if (temp > 0) {
			temp = -1;
			return (int) temp;
		} else if (temp < 0) {
			temp = 1;
			return (int) temp;
		}
//		if (temp !=0) {
//			return (int) (this.myk2 - my.myk2);
//		}
		return (int) (this.myv2 - my.myv2);
//		return 1;

	}

	/*
	 * (非 Javadoc)
	 * 
	 * 
	 * @param obj
	 * 
	 * @return
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * 
	 * @return
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
