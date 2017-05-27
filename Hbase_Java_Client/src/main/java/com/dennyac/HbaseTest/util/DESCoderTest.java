package com.dennyac.HbaseTest.util;

import org.junit.Test;

/**
 * Created by shuyun on 2016/7/6.
 */
public class DESCoderTest {
    @Test
    public void test() throws Exception {
        //String inputStr = "卖家代充123abc";
        String inputStr = "鄢间";
        System.err.println("原文:\t" + inputStr);

        //生成秘钥
        String key = DESCoder.initKey("shuyun123");
        System.err.println("密钥:\t" + key);

        //加密
        byte[] inputData = inputStr.getBytes();
        inputData = DESCoder.encrypt(inputData, key);
        System.err.println("加密后:\t" + inputData);

        //解密
        byte[] outputData = DESCoder.decrypt(inputData, key);
        String outputStr = new String(outputData);
        System.err.println("解密后:\t" + outputStr);

        //验证
        Boolean isEquals = assertEquals(inputStr, outputStr);
        System.out.println(isEquals);
    }

    public Boolean assertEquals(String inPut, String outPut){
        if(inPut.equals(outPut)){
            return true;
        }
        return false;
    }
}
