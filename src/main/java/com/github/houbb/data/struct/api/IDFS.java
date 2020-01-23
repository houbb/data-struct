package com.github.houbb.data.struct.api;

import java.util.List;

/**
 * @author binbin.hou
 * @since 0.0.2
 */
public interface IDFS<V> {

    /**
     * 深度遍历
     * @return 遍历
     * @since 0.0.2
     */
    List<V> dfs();

}
