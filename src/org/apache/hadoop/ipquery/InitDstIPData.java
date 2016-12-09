package org.apache.hadoop.ipquery;

import java.util.List;

public class InitDstIPData {
	public static void init(List<String>  RRIP_List,List<String> QZIP_List,List<String> KXIP_List,List<String> DBIP_List,List<String> TXWBIP_List,List<String> SinaWBIP_List,List<String> WXWBIP_List){
		initRRIP_List(RRIP_List);
		initDBIP_List(DBIP_List);
		initKXIP_List(KXIP_List);
		initQZIP_List(QZIP_List);
		initSinaWBIP_List(SinaWBIP_List);
		initTXWBIP_List(TXWBIP_List);
		initWXWBIP_List(WXWBIP_List);
	}
	public static void initWXWBIP_List(List<String> WXWBIP_List){
		WXWBIP_List.add("123.58.179.173");		
	}
	public static void initSinaWBIP_List(List<String> SinaWBIP_List){
		SinaWBIP_List.add("123.125.104.197");
		SinaWBIP_List.add("180.149.134.18");
		SinaWBIP_List.add("180.149.134.17");
		SinaWBIP_List.add("180.149.134.141");
		SinaWBIP_List.add("180.149.134.142");
	}
	public static void initTXWBIP_List(List<String> TXWBIP_List){
		 TXWBIP_List.add("113.142.13.156");	
		 TXWBIP_List.add("125.39.240.51");	
	}
	public static void initRRIP_List(List<String>  RRIP_List){
		 RRIP_List.add("123.125.38.2");											
	     RRIP_List.add("123.125.38.3");
	     RRIP_List.add("123.125.38.239");
	     RRIP_List.add("123.125.38.240");
	     RRIP_List.add("123.125.38.245");
	     RRIP_List.add("123.125.38.246");
	     RRIP_List.add("123.125.38.247");
		 RRIP_List.add("220.181.181.221");
		 RRIP_List.add("220.181.181.222");
		 RRIP_List.add("220.181.181.223");
		 RRIP_List.add("220.181.181.224");
		 RRIP_List.add("220.181.181.229");
		 RRIP_List.add("220.181.181.230");
		 RRIP_List.add("220.181.181.231");
		 RRIP_List.add("220.181.181.235");
		 RRIP_List.add("220.181.181.237");
	}
	public static void initDBIP_List(List<String> DBIP_List){
		DBIP_List.add("211.147.4.31");											
        DBIP_List.add("211.147.4.32");
        DBIP_List.add("211.147.4.49");
	}
	public static void initQZIP_List(List<String> QZIP_List) {
		QZIP_List.add("183.61.38.214");	
		QZIP_List.add("125.39.240.42");
		QZIP_List.add("125.39.240.43");
		QZIP_List.add("125.39.213.83");
	}
	public static void initKXIP_List(List<String> KXIP_List){
		KXIP_List.add("123.125.56.20");											
        KXIP_List.add("123.125.56.21");
        KXIP_List.add("123.125.56.22");
        KXIP_List.add("123.125.56.23");
        KXIP_List.add("220.181.103.140");
        KXIP_List.add("220.181.103.141");
	}
}