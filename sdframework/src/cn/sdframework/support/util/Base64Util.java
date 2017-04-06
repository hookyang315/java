package cn.sdframework.support.util;

import java.io.IOException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util {
	private static BASE64Encoder encoder = new sun.misc.BASE64Encoder();

    private static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

    public Base64Util() {
    }
    //加密函数
    public static String encode(String s) {
        return encoder.encode(s.getBytes());
    }
    
    //解密函数
    public static String decode(String s) {
        try {
            byte[] temp = decoder.decodeBuffer(s);
            return new String(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
//        String str = "123456";
//        System.out.println("加密 ===== " + Base64Util.encode(str));
//        System.out.println("解密 ===== " + Base64Util.decode(Base64Util.encode(str)));
//        
//        String str1 = "44444444";
        System.out.println("加密-->" + Base64Util.encode("passw0rd")); 
//        System.out.println("解密-->" + Base64Util.decode("MDAyNjI1"));
    }
}
