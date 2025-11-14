package org.example.intellibookmallapi.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SQLite日期类型处理器
 * 用于处理SQLite中的日期字符串与Java Date类型的转换
 */
@MappedTypes(Date.class)
public class SqliteDateTypeHandler extends BaseTypeHandler<Date> {
    
    private static final SimpleDateFormat[] DATE_FORMATS = {
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
        new SimpleDateFormat("yyyy-MM-dd"),
        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"),
        new SimpleDateFormat("yyyy/MM/dd")
    };
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(parameter));
    }
    
    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseDate(rs.getString(columnName));
    }
    
    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseDate(rs.getString(columnIndex));
    }
    
    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseDate(cs.getString(columnIndex));
    }
    
    /**
     * 解析日期字符串
     * 尝试多种日期格式
     */
    private Date parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        
        // 尝试各种日期格式
        for (SimpleDateFormat format : DATE_FORMATS) {
            try {
                return format.parse(dateString);
            } catch (ParseException e) {
                // 继续尝试下一个格式
            }
        }
        
        // 如果所有格式都失败，返回null
        return null;
    }
}
