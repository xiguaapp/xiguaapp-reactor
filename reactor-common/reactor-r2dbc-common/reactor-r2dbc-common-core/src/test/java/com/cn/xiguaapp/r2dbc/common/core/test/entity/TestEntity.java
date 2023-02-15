package com.cn.xiguaapp.r2dbc.common.core.test.entity;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.GenericEntity;
import com.cn.xiguaapp.r2dbc.common.core.generator.Generators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "s_test")
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class TestEntity extends GenericEntity<String> {

    @Column(length = 32)
    private String name;

    @Column
    private Integer age;

    @Override
    @GeneratedValue(generator = Generators.DEFAULT_ID_GENERATOR)
    public String getId() {
        return super.getId();
    }
}
