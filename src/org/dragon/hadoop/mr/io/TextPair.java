package org.dragon.hadoop.mr.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class TextPair implements WritableComparable<TextPair> {
	
	/**不建议使用如下hadoop封装类推荐JAVA原生态数据类**/
	private Text first;
	private Text second;

	public TextPair() {
		// TODO Auto-generated constructor stub
	}

	public TextPair(Text first, Text second) {
		this.set(first, second);
	}

	public void set(Text first, Text second) {
		this.first = first;
		this.second = second;
	}

	public Text getFirst() {
		return first;
	}

	public Text getSecond() {
		return second;
	}

	public void write(DataOutput out) throws IOException {
		first.write(out);
		second.write(out);
	}

	public void readFields(DataInput in) throws IOException {
		first.readFields(in);
		second.readFields(in);
	}
	
	public int compareTo(TextPair o) {
		int cmp = this.first.compareTo(o.getFirst());
		if (0 != cmp) {
			return cmp;
		}
		return this.second.compareTo(o.getSecond());
	}

	@Override
	public int hashCode() {
		return first.hashCode() * 163 + second.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TextPair) {
			TextPair pair = (TextPair) obj;
			return first.equals(pair.getFirst())
					&& second.equals(((TextPair) obj).getSecond());

		}
		return false;
	}

	@Override
	public String toString() {
		return first + "\t" + second;
	}

}
