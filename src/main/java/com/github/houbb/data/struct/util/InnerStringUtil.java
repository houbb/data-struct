package com.github.houbb.data.struct.util;

import com.github.houbb.heaven.util.lang.CharUtil;

/**
 * 内部的字符串工具类
 * @author binbin.hou
 * @since 0.0.5
 */
public class InnerStringUtil {

    /**
     * 左边进行条虫
     * @param xoffset x 轴位置
     * @param offset 当前偏移
     * @param value 值
     * @return 结果
     * @since 0.0.5
     */
    public static String leftPad(int xoffset, int offset, Object value) {
        int left = xoffset - offset;
        if(left <= 0) {
            return value.toString();
        }

        // 直接填充
        return CharUtil.repeat(' ', left)+value.toString();
    }

}
