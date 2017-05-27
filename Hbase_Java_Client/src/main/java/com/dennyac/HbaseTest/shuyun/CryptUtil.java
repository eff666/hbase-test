package com.dennyac.HbaseTest.shuyun;

import com.shuyun.crypt.CryptMode;
import com.shuyun.crypt.CryptTools;
import com.shuyun.crypt.taobaosdk.com.taobao.api.security.SecurityClient;


/**
 * Created by shuyun on 2016/8/23.
 */
public class CryptUtil   {
    /** 加密数据类型_收货人 */
    /** 加密数据类型_昵称 */
    public static final String TYPE_NICK = SecurityClient.NICK;
    /** 加密数据类型_手机 */
    public static final String TYPE_PHONE = SecurityClient.PHONE;
    /** 加密数据类型_其它 */
    public static final String TYPE_NORMAL = SecurityClient.NORMAL;

    /**
     * 对指定的数据进行加密
     *
     * @param dataType 加密数据类型
     */
    public static String encrypt(String data, String dataType, CryptMode cryptMode){
        return null;
    }

    /** 对指定的数据进行解密 */
    public static String decrypt(String data, String dataType, CryptMode cryptMode){
        return null;
    }

    /** 判断数据是否是密文 */
    public static boolean isEncrypt(String data, String dataType){
        return true;
    }

    public static void main(String[] args){

        //SecurityClient client = new SecurityClient(null, "123");
        String coder = CryptTools.decrypt("$158$AYuwnDWdfZgsbn/MNml4KQ==$1$", "phone");
        System.out.println(coder);

        //System.out.println(CryptTools.decrypt("$189$wH+YwA482jmoumfxL/V4yw==$1$", "phone"));

        //System.out.println(isEncrypt("18986342476", TYPE_PHONE));

        System.out.println(CryptTools.decrypt("~KsLt3l2DAMSfLSFqHhcwFA==~1~", "nick"));

        System.out.println(CryptTools.decrypt("~0dZINsqP5gDzf9hNll2LAg==~1~", "nick"));
    }
}
