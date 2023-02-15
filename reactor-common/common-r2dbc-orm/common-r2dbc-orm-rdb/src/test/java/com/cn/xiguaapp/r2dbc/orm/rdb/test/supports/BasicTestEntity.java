package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.ColumnType;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.DefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.JsonCodec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.JDBCType;
import java.util.Date;
import java.util.List;

@Table(name = "entity_test_table", indexes = @Index(name = "test_index", columnList = "name,state desc"))
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicTestEntity implements Serializable {

    @Column(length = 32)
    @Id
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @Column(nullable = false)
    private Byte state;

    @Column
//    @DefaultValue(generator = "random")
    private Long balance;

    @Column(table = "entity_test_table_detail")
    private String detail;

    @Column
    @ColumnType(jdbcType = JDBCType.LONGNVARCHAR)
    @JsonCodec
    private List<String> tags;

    @Column
    @ColumnType(javaType = String.class)
    @DefaultValue("disabled")
    private StateEnum stateEnum;

    @Column(name = "address_id")
    private String addressId;

    @JoinColumn(name = "address_id")
    private Address address;
}
