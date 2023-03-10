package com.cn.xiguapp.common.core.test.dict;

import com.cn.xiguapp.common.core.utils.EnumDict;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonDeserialize(contentUsing = EnumDict.EnumDictJSONDeserializer.class)
public enum TestEnum implements EnumDict<String> {
    E1("e1"), E2("e2");

    private String text;

    @Override
    public String getValue() {
        return name();
    }
}
