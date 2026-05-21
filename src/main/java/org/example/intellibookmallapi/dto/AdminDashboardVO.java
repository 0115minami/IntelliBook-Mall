package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.List;

/**
 * 管理员仪表盘统计VO
 */
@Data
public class AdminDashboardVO {

    /** 今日订单总数 */
    private Integer todayOrderCount;

    /** 今日已支付订单数 */
    private Integer todayPaidCount;

    /** 今日已取消订单数 */
    private Integer todayCancelCount;

    /** 今日销售额（分） */
    private Integer todaySales;

    /** 最近7天每日数据 */
    private List<DailyStatVO> dailyStats;

    @Data
    public static class DailyStatVO {
        /** 日期（yyyy-MM-dd） */
        private String date;
        /** 当日订单数 */
        private Integer orderCount;
        /** 当日销售额（分） */
        private Integer sales;
    }
}
