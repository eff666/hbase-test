package com.dennyac.HbaseTest.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shuyun on 2016/8/10.
 */
public class TestString {
    private static final String ASSET_MOVIE = "movieId";
    private static final String ASSET_VIDEO = "videoId";
    public static void main(String[] args){


        String str="34.2%";//字符串类型的百分数
       NumberFormat nf = NumberFormat.getPercentInstance();//NumberFormat是一个工厂，可以直接getXXX创建，而getPercentInstance()
//        是返回当前默认语言环境的百分比格式。
        try {
            Number m = nf.parse(str);//提供了带有 ParsePosition 和 FieldPosition 的
            System.out.println(m);//打印数值
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        parse 和 format 方法的形式，parse(xx)表示解析给定字符串开头的文本，生成一个数值。
//        逐步地解析字符串的各部分



        //1、list转为json对象
//		List<String> a = new ArrayList<String>();
//		a.add("a");
//		a.add("b");
//		ObjectMapper mapper = new ObjectMapper();
//        String source = null;
//        try {
//            source = mapper.writeValueAsString(a);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        System.out.println(source);


//2、map
//	Map<String, Integer> innerMap = new HashMap<String, Integer>();
//	innerMap.put("innerKey", 2014);
//	System.out.println(innerMap);//{innerKey=2014}
//
//	Map<String, Map<String,Integer>> map = new HashMap<String, Map<String, Integer>>();
//	map.put("outerKey", innerMap);
//	System.out.println(map);//{outerKey={innerKey=2014}}
//
//	Map<String, Integer> targetMap = map.get("outerKey");
//	System.out.println(targetMap);//{innerKey=2014}
//
//	if(targetMap == innerMap){
//	    System.out.println("You got the inner Map, and it is saved in targetMap!!!");
//	}





//	//从后往前拆分
//	String str = "http://s3.amazonaws.com/nxsglobal/rocketcity/theme/images/mycentx.png";
//	String strr = str.substring(str.lastIndexOf("/")+1, str.lastIndexOf("."));
//	System.out.println(strr);//mycentx


//	String str = "2015(2014)22YPQD000655";
//    String jieguo = str.substring(str.indexOf("Y"), str.indexOf("6"));
//    System.out.println(jieguo);//YPQD000


//	  //取出字符串中的指定字符
//	  String url = "http://s3.amazonaws.com/nxsglobal/rocketcity/theme/images/mycentx.png";
//	  String split[] = url.split("/");
//	  String str1[] = split;
//	  System.out.println(str1[4]);//rocketcity
//
//
//	  String[] split1 = url.split("/");
//	  for(String str2 : split1){
//	      System.out.println(str2);
//	  }
//


	/*
	1.java.lang.String.copyValueOf(char[] data)
	 	方法返回一个字符串，它表示的字符序列中指定数组

	2.public static String copyValueOf( char data[], int offset, int count )
	data
		the character array.
	offset
		initial offset of the subarray.
	count
		length of the subarray.
	Returns
		a String that contains the characters of the specified subarray of the character array.
		返回一个字符串,该字符串包含指定的子数组的字符的字符数组。
	*/
//	  String url = "http://zhidao.baidu.com/question/147458024.html";
//	  //从后面第一个“/"为拆分，返回它的位置
//	  int index = url.lastIndexOf("/");
//	  System.out.println(index);
//	  //取出147458024.html
//	  char[] ch = url.toCharArray();
//	  String lastString = String.copyValueOf(ch, index + 1, ch.length - index - 1);
//	  System.out.println(lastString);//147458024.html



//
//	  String url = "http://zhidao.baidu.com/question/147458024.html";
//	  //取出指定的字符
//	  int index = url.lastIndexOf("/");
//	  //substring(int beginIndex)
//	  String newString = url.substring(index + 1);
//	  System.out.println(newString);//147458024.html




//
//	  String url = "http://zhidao.baidu.com/question/147458024.html";
//	  //以"/"拆分，循环对象拿出其中的每一个
//	  String[] split = url.split("/");
//	  for(String str : split){
//	   System.out.println(str);
//	  }

//
//	  String url = "http://s3.amazonaws.com/nxsglobal/rocketcity/theme/images/mycentx.png";
//	  String split[] = url.split("/");
//	  String str1[] = split;
//	  System.out.println(str1[4]);//rocketcity


//		//字符串中有双引号
//		String str1 = "\"name\"";//字符串两边含有双引号
//        String str2 = "name \"is\" wgb";//字符串中间含有双引号
//        String str3 = "\\name";//使用转义字符还可以使字符串包含其他字符
//        System.out.println("字符串一：" + str1);
//        System.out.println("字符串二：" + str2);
//        System.out.println("字符串三：" + str3);

//       //今天在项目中使用java中replaceAll方法将字符串中的反斜杠("\")替换成空字符串("")，结果出现如下的异常：
//       1 java.util.regex.PatternSyntaxException: Unexpected internal error near index 1 \^
//        上网找了一下错误的原因：在regex中"\\"表示一个"\"，在java中一个"\"也要用"\\"表示。这样，前一个"\\"代表regex中的"\"，后一个"\\"代表java中的"\"。所以要想使用replaceAll方法将字符串中的反斜杠("\")替换成空字符串("")，则需要这样写：str.replaceAll("\\\\","");
//        写一段测试代码演示上面出现的异常：
//
//       1 String s="C:\盘";
//       2 s.replaceAll("\\","");
//		使用上面的代码会导致
//
//      1 java.util.regex.PatternSyntaxException: Unexpected internal error near index 1 \^
//       要想将"C:\盘"中的"\"替换成空字符串，正确的写法是：
//
//       s.replaceAll("\\\\","");//样就可以正常替换了。


        //Java中把"替换为/"
//	    String message="test \"book\"  test";
//	    System.out.println(message);
//	    System.out.println(message.indexOf("\""));
//
//	    String message2=message.replace("\"","\\\"");
//	    //需要五个
//	    String message1=message.replaceAll("\"","\"");
//	    System.out.println(message1);
//	    System.out.println(message2);
//
//	    String test ="_test test_";
        //System.out.println(test.replace("_", "%"));

//
//	    String json = "{\"name\":\"value\"}";
//	    String t = json.replaceAll("\"(\\w+)\"(\\s*:\\s*)", "$1$2");
//	    System.out.println(t);//{name:"value"}
//
//		String description = "<P>Bizet's gorgeous 123 '' <p>  opera of lust and longing set in the.</P>";
//		description = description.replaceAll("\\<[^\\>]+\\>", "");
//		System.out.println(description);



//		  //1. List
//		  ArrayList<String> list = new ArrayList<String>();
//		  list.add(0,"aa");
//		  list.add(1,"bb");
//		  list.add(2,"cc");
//		  System.out.println(list);//[aa, bb, cc]
//		  //2. Map
//		  Map<String, Object> map=new HashMap<String, Object>();
//	      map.put("id",list.get(0));
//	      map.put("hello", list.get(2));
//	      //取出map中的某一条数据
//	      System.out.println(map);//{hello=cc, id=aa}
//	      //取出map中的所有键值对
//	      System.out.println(map.entrySet());//[hello=cc, id=aa]
//	      //取出map中的所有key
//	      System.out.println(map.keySet());//[hello, id]
//	      //取出map里面所有的value
//	      System.out.println(map.values());//[cc, aa]
//	      //获取map里面的每个value
//	      for(Object obj : map.keySet()) {
//	    	  System.out.println(map.get(obj));//cc  //aa
//	      }
//
//	      //3. List当中存放Map
//	      List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
//	      listMap.add(map);
//	      System.out.println(listMap);//[{hello=cc, id=aa}]
//	      //如何从list当中取出数据
//	      //可以先for循环，取出来List中的每条数据（Map<>形式的），放到一个Map中，再把Map中的数据取出来存到String数组当中
//	      for(int index = 0; index < listMap.size(); index++) {
//	    	  Map<String, Object> map2 = listMap.get(index);
//	    	  System.out.println(map2);//{hello=cc, id=aa}
//	    	  String map2Value = (String) map2.get("id");
//	    	  System.out.println(map2Value);//aa
//	      }



//		List<String> list = new ArrayList<String>();
//		list.add( "first" );
//		list.add( "second" );
//		System.out.println(list);//[first, second]
//		String jsonArray2 = JSONArray.toJSONString(list);
//		System.out.println(jsonArray2);//["first","second"]
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("name", "json");
//		map.put("bool", Boolean.TRUE);
//		map.put("int", new Integer(1));
//		map.put("arr", new String[] { "a", "b" });
//		map.put("func", "function(i){ return this.arr[i]; }");
//		JSONObject json = JSONObject.fromObject(map);


//	    //从List对象中取值的三种方法
//		List<String> list = new ArrayList<String>();
//        long t1,t2;
//        for(int j = 0; j < 100000; j++)
//        {
//            list.add("aaaaaa" + j);
//        }
//        System.out.println("List first visit method:");
//        t1=System.currentTimeMillis();
//        //1.遍历，效率最低
//        for(String tmp:list)
//        {
//            //System.out.println(tmp);
//        }
//        t2=System.currentTimeMillis();
//        System.out.println("Run Time:" + (t2 -t1) + "(ms)");
//
//
//
//
//		System.out.println("List second visit method:");
//        t1=System.currentTimeMillis();
//        //2.用size()方法来取值，效率最高
//        for(int i = 0; i < list.size(); i++)
//        {
//            list.get(i);
//            //System.out.println(list.get(i));
//        }
//        t2=System.currentTimeMillis();
//        System.out.println("Run Time:" + (t2 -t1) + "(ms)");
//
//
//
//        System.out.println("List Third visit method:");
//        t1=System.currentTimeMillis();
//        //3.用Iterator迭代器取值，效率一般
//        Iterator<String> iter = list.iterator();
//        while(iter.hasNext())
//        {
//            iter.next();
//            //System.out.println(iter.next());
//        }
//        t2=System.currentTimeMillis();
//        System.out.println("Run Time:" + (t2 -t1) + "(ms)");
//
//        System.out.println("Finished!!!!!!!!");


//Java如何跳出多重循环？
//1.在循环的外层定义一个标号，在内层循环中使用带有这个标号的break语句跳出循环
//            stop:
//        	  for (int i = 0 ;i < 5 ; i++) {
//        		  for (int j = 0; j < 5; j++) {
//        			  System.out.println("i=" + i + ", j = " + j);
//        			  if (j == 2 ) break stop;
//        		  }
//        	  }
//2.在外层定义一个变量，外层循环表达式的结果可以受到内层循环体代码的控制（推荐做法）
//		  boolean stop = true;
//  	  for (int i = 0 ;i < 5 && stop ; i++) {
//  		  for (int j = 0; j < 5; j++) {
//  			  System.out.println("i=" + i + ", j = " + j);
//  			  if (j == 2 ) {
//  				  stop = false;
//  				  break;
//  			  }
//  		  }
//  	  }


//		Map<String, String> map = new HashMap<String, String>();
//		map.put("1", "value1");
//		  map.put("2", "value2");
//		  map.put("3", "value3");
//
//		  //第一种：普遍使用，二次取值
//		  System.out.println("通过Map.keySet遍历key和value：");
//		  for (String key : map.keySet()) {
//		   System.out.println("key= "+ key + " and value= " + map.get(key));
//		  }
//
//		  //第二种
//		  System.out.println("通过Map.entrySet使用iterator遍历key和value：");
////		  Set<Entry<String, String>> map1 = map.entrySet();
////		  Iterator<Entry<String, String>> its = map1.iterator();
//		  Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
//
//		  while (it.hasNext()) {
//		   Map.Entry<String, String> entry = it.next();
//		   System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
//		  }
//
//		  //第三种：推荐，尤其是容量大时
//		  System.out.println("通过Map.entrySet遍历key和value");
//		  for (Map.Entry<String, String> entry : map.entrySet()) {
//		   System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
//		  }
//
//		  //第四种
//		  System.out.println("通过Map.values()遍历所有的value，但不能遍历key");
//		  for (String v : map.values()) {
//		   System.out.println("value= " + v);
//		  }


        //TreeSet可按照一定顺序排序
//		   TreeSet<String> ts=new TreeSet<String>();
//	       ts.add("orange");
//	       ts.add("apple");
//	       ts.add("banana");
//	       ts.add("grape");
//
//	       Iterator<String> it=ts.iterator();
//	       while(it.hasNext())
//	       {
//	           String fruit=(String)it.next();
//	           System.out.println(fruit);//apple banana grape orangle
//	       }

//		//统计每个单词出现的次数，并按照字典顺序排序
//		  String[] str = {"apple", "apple", "orange", "bana"};
//		  Map<String, Integer> map = new HashMap<String, Integer>();
//		  for(int i =0 ;i < str.length; i++){
//			  if(map.containsKey(str[i])){
//				  int num = map.get(str[i]);
//				  map.put(str[i], ++num);
//			  } else {
//				  map.put(str[i], 1);
//			  }
//		  }
//
////		  Iterator<Map.Entry<String, Integer>> its = map.entrySet().iterator();
////		  while(its.hasNext()){
////			  Map.Entry<String, Integer> it = its.next();
////			  String key = it.getKey();
////			  int num = it.getValue();
////			  System.out.println(key + "=" + num);
////		  }
//		  Set<String> set = new TreeSet<String>();
//		  set.addAll(map.keySet());//排序
//		//Set<String> set = map.keySet();
//		  for(Iterator<String> ite = set.iterator(); ite.hasNext(); ){
//			  String key = ite.next();
//			  int num = map.get(key);
//			  System.out.println(key + "=" + num);
//		  }



//		//获得java所支持的语言
//		Locale[] locale = Locale.getAvailableLocales();
//		for(int i = 0; i< locale.length; i++){
//			String country = locale[i].getDisplayCountry();
//			String language = locale[i].getDisplayLanguage();
//			System.out.println("country=" + country + ",language=" + language);
//			System.out.println();
//		}
//		//获得当前系统所使用语言
//		Locale  currentLocale = Locale.getDefault();
//		System.out.println(currentLocale);
//		System.out.println(locale.length);
//
//		//根据指定语言，加载资源文件
//		ResourceBundle res = ResourceBundle.getBundle("baseName", currentLocale);
//		System.out.println(res.getString("hello"));



//		Parent parent = new Parent();
//		Child child = new Child();
//		System.out.println(parent.getName());
//		System.out.println(child.getName());

//        //String i = "0";
//        String i = "";
//        System.out.println(i);
//        System.out.println(i + 0);
//        System.out.println(i == i + 0);
//        if(i != i + 0) {
//            System.out.println(i);
//            System.out.println(i + 0);
//        }


        Map<String, String> map = new HashMap<>();
        map.put("123", "456");
        map.put("234", "456");
        map.put("123", "741");
        for(String key : map.keySet()){
            System.out.println(key + ":" + map.get(key));
        }


    }
}
