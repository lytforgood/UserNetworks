package org.apache.hadoop.mapreduce.app.paixu;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.WritableComparable;

public class MyKt implements WritableComparable<MyKt> {
	public String myk2;
	public String myv2;

	public MyKt() {
		// TODO Auto-generated constructor stub
	}

	public MyKt(String myk2, String myv2) {
		this.myk2 = myk2;
		this.myv2 = myv2;
	}

	public String getMyk2() {
		return myk2;
	}

	public String getMyv2() {
		return myv2;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(myk2);
		out.writeUTF(myv2);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.myk2 = in.readUTF();
		this.myv2 = in.readUTF();
	}

	/**
	 * ###作业：第一个时间升序，第二个数字升序
	 */
	@Override
	public int compareTo(MyKt my) {
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = fmt.parse(this.myk2);
			Date d2 = fmt.parse(my.myk2);
			if (d2.after(d1)) {
				return 1;
			} else if (d2.before(d1)) {
				return -1;
			}
		} catch (ParseException e) {
			return 1;
		}
		return -(Integer.parseInt(this.myv2)-Integer.parseInt(my.getMyv2()));

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myk2 == null) ? 0 : myk2.hashCode());
		result = prime * result + ((myv2 == null) ? 0 : myv2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyKt other = (MyKt) obj;
		if (myk2 == null) {
			if (other.myk2 != null)
				return false;
		} else if (!myk2.equals(other.myk2))
			return false;
		if (myv2 == null) {
			if (other.myv2 != null)
				return false;
		} else if (!myv2.equals(other.myv2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MyKt [myk2=" + myk2 + ", myv2=" + myv2 + "]";
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




}
