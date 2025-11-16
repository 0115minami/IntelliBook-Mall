package org.example.intellibookmallapi.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 订单号生成工具类
 * 格式：yyyyMMddHHmmss + 6位随机数
 * 例如：20241117105230123456
 */
public class OrderNoGenerator {
    
    private static final Random RANDOM = new Random();
    
    /**
     * 生成订单号
     * @return 订单号
     */
    public static String generateOrderNo() {
        // 时间戳部分：yyyyMMddHHmmss
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        
        // 随机数部分：6位
        int randomNum = RANDOM.nextInt(900000) + 100000; // 100000-999999
        
        return timestamp + randomNum;
    }
}
