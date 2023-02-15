package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "test_address")
@Getter
@Setter
public class Address {

    @Id
    @Column(length = 32)
    private String id;

    @Column
    private String name;
}
