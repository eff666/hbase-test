package com.dennyac.HbaseTest.util;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by shuyun on 2016/7/23.
 */
public class JsonUtil {

    public static void main(String[] args){

        //将json数组解析成java对象
//        String content = "{A:[{id:\"1\",name:\"aaa\"},{id:\"2\",name:\"bbb\"}],B:[{id:\"3\",name:\"ccc\"},{id:\"4\",name:\"ddd\"}],C:[{id:\"5\",name:\"eee\"},{id:\"6\",name:\"fff\"}]}";
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(org.codehaus.jackson.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//        JsonNode json = null;
//        try {
//            json = mapper.readValue(content, JsonNode.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ArrayNode array = (ArrayNode)json.findValue("A");
//        System.out.println(array);
//        System.out.println(array.get(0));


        //JSONArray jsonArr = JSONArray.fromObject(content);



        //将list转换为String
        List<String> shop_ids = new ArrayList<String>();
        shop_ids.add("11.11,22.2");
        //shop_ids.add("#");
        shop_ids.add("22.22,11.56");
        shop_ids.add("11.34, 56.89");

        String str = Joiner.on(",").join(shop_ids);
        StringBuffer shopId = new StringBuffer();

        for(int i = 0; i < shop_ids.size(); i++){
            shopId.append(shop_ids.get(i));
            if(i < shop_ids.size() - 1){
                shopId.append("#");
            }
        }

        System.out.println(shopId);



//        boolean flag=false;
//        for (String string : shop_ids) {
//            //shopId.append("\"");
//            if (flag) {
//                shopId.append(",");
//            }else {
//                flag=true;
//            }
//            shopId.append("\"" + string + "\"");
//            //shopId.append("\"");
//        }
//        System.out.println(shopId.toString());
    }
}
