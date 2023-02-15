package com.cn.xiguapp.common.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc
 */
@Data
@Accessors(chain = true)
public class TreeNode implements Serializable {
    private static final long serialVersionUID = 8772115911922451037L;
    protected Integer id;
    protected String label;
    protected Integer parentId;
    protected Integer sort;
    protected List<TreeNode> children = new ArrayList<>();

    public void add(TreeNode node) {
        children.add(node);
    }
}
