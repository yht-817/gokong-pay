package com.pay.tool.gokongpay.util;


import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * 对文件统一加解密
 */
public class DecryptionFileConf {
    public static Properties decrypt(Properties props) {
        try {
            Set keyValue = props.keySet();
            for (Iterator it = keyValue.iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                if (key != null) {
                    props.setProperty(key,
                            DesEncrypt.decrypt(props.getProperty(key), DesEncrypt.PASSWORD_CRYPT_KEY));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(DesEncrypt.decrypt("DBD079DC0328811EF18B88E6962AD9502E90C08B734D95F1F189CDC08BCD107E0CF41C20B77D31CD", DesEncrypt.PASSWORD_CRYPT_KEY));
    }

}
