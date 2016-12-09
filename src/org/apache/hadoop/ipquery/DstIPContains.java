package org.apache.hadoop.ipquery;

import java.util.ArrayList;
import java.util.List;

public class DstIPContains {
	static List<String> RRIP_List = new ArrayList<String>();
	static List<String> QZIP_List = new ArrayList<String>();
	static List<String> KXIP_List = new ArrayList<String>();
	static List<String> DBIP_List = new ArrayList<String>();
	static List<String> TXWBIP_List = new ArrayList<String>();
	static List<String> SinaWBIP_List = new ArrayList<String>();
	static List<String> WXWBIP_List = new ArrayList<String>();
	static {
		InitDstIPData.init(RRIP_List, QZIP_List, KXIP_List, DBIP_List,
				TXWBIP_List, SinaWBIP_List, WXWBIP_List);
	}

	// 查找人人网IP
	public static boolean isRRContains(String str) {

		return RRIP_List.contains(str);
	}

	// 查找QQ空间IP
	public static boolean isQZContains(String str) {

		return QZIP_List.contains(str);
	}

	// 查找开心网IP
	public static boolean isKXContains(String str) {

		return KXIP_List.contains(str);
	}

	// 查找豆瓣网IP
	public static boolean isDBContains(String str) {

		return DBIP_List.contains(str);
	}

	// 查找腾讯微博IP
	public static boolean isTXWBContains(String str) {

		return TXWBIP_List.contains(str);
	}

	// 查找新浪微博IP
	public static boolean isSinaWBContains(String str) {

		return SinaWBIP_List.contains(str);
	}

	// 查找网易微博IP
	public static boolean isWXWBContains(String str) {

		return WXWBIP_List.contains(str);
	}
}
