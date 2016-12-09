package org.apache.hadoop.mapreduce.app.topkey;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class TopKeyWritable implements WritableComparable<TopKeyWritable> {
	private String languageType;
	private String singName;
	private Long playTimes;

	public TopKeyWritable() {
		// TODO Auto-generated constructor stub
	}

	public TopKeyWritable(String languageType, String singName, Long playTimes) {
		this.set(languageType, singName, playTimes);
	}

	public void set(String languageType, String singName, Long playTimes) {
		this.languageType = languageType;
		this.singName = singName;
		this.playTimes = playTimes;
	}

	public String getLanguageType() {
		return languageType;
	}

	public String getSingName() {
		return singName;
	}

	public Long getPlayTimes() {
		return playTimes;
	}

	public void write(DataOutput out) throws IOException {
		out.writeUTF(languageType);
		out.writeUTF(singName);
		out.writeLong(playTimes);
	}

	public void readFields(DataInput in) throws IOException {
		this.languageType = in.readUTF();
		this.singName = in.readUTF();
		this.playTimes = in.readLong();

	}
	//排序
	public int compareTo(TopKeyWritable o) {
		return -(this.getPlayTimes().compareTo(o.getPlayTimes()));
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((languageType == null) ? 0 : languageType.hashCode());
		result = prime * result
				+ ((playTimes == null) ? 0 : playTimes.hashCode());
		result = prime * result
				+ ((singName == null) ? 0 : singName.hashCode());
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
		TopKeyWritable other = (TopKeyWritable) obj;
		if (languageType == null) {
			if (other.languageType != null)
				return false;
		} else if (!languageType.equals(other.languageType))
			return false;
		if (playTimes == null) {
			if (other.playTimes != null)
				return false;
		} else if (!playTimes.equals(other.playTimes))
			return false;
		if (singName == null) {
			if (other.singName != null)
				return false;
		} else if (!singName.equals(other.singName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return languageType + "\t" + singName + "\t" + playTimes;
	}

}
