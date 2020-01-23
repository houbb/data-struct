package com.github.houbb.data.struct.api;

import java.util.List;

/**
 * @author binbin.hou
 * @since 0.0.2
 */
public interface IBFS<V> {

    /**
     * 广度遍历
     * @param root 根节点
     * @return 遍历
     * @since 0.0.2
     */
    List<V> bfs(final V root);

}
