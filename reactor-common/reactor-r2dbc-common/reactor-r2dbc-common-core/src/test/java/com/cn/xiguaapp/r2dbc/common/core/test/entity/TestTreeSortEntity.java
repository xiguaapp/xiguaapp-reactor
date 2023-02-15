package com.cn.xiguaapp.r2dbc.common.core.test.entity;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.GenericTreeSortSupportEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Table(name = "test_tree_sort")
public class TestTreeSortEntity  extends GenericTreeSortSupportEntity<String> {


    @Column
    private String name;


    private List<TestTreeSortEntity> children;


}
