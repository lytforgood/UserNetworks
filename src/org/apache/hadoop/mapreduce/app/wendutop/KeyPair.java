package org.apache.hadoop.mapreduce.app.wendutop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class KeyPair implements WritableComparable<KeyPair>{
	
	private int year;
	private int hot;
	
	public KeyPair() {
		// TODO Auto-generated constructor stub
	}

	public KeyPair(int year, int hot) {
		this.set(year, hot);
	}

	public void set(int year, int hot) {
		this.year = year;
		this.hot = hot;
	}
	
	public int getYear() {
		return year;
	}


	public int getHot() {
		return hot;
	}


	@Override
	public void write(DataOutput out) throws IOException {
		out.write(year);
		out.write(hot);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.year=in.readInt();
		this.hot=in.readInt();
	}

	@Override
	public int compareTo(KeyPair o) {
		int res=this.year-o.getYear();
		if (res!=0) {
			return res;
		}
		return this.hot-o.getHot();
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hot;
		result = prime * result + year;
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
		KeyPair other = (KeyPair) obj;
		if (hot != other.hot)
			return false;
		if (year != other.year)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return year + "\t" + hot ;
	}
	
}
