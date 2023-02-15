package com.cn.xiguaapp.r2dbc.orm.rdb.metadata;

import com.cn.xiguaapp.r2dbc.orm.codec.ValueCodec;
import com.cn.xiguaapp.r2dbc.orm.feature.FeatureType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.TermFragmentBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.function.FunctionFragmentBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据库描述
 */
@Getter
@AllArgsConstructor
public enum RDBFeatureType implements FeatureType {

    /**
     * @see Dialect
     */
    dialect("数据库方言"),
    /**
     * @see TermFragmentBuilder
     */
    termType("SQL条件"),

    termsType("SQL条件组合"),

    query("查询"),

    paginator("分页器"),

    sqlBuilder("SQL构造器"),

    sqlExecutor("SQL执行器"),

    metadataParser("元数据解析器"),

    /**
     * @see FunctionFragmentBuilder
     */
    function("函数"),


    fragment("SQL片段"),

    foreignKeyTerm("外键关联条件"),

    /**
     * @see ValueCodec
     * @see ValueCodecFactory
     */
    codec("编解码器"),

    exceptionTranslation("异常转换"),
    saveOrUpdateOperator("新增或者保存操作器")
    ;


    @Override
    public String getId() {
        return name();
    }

    private String name;

    public String getFeatureId(String suffix) {
        return getId().concat(":").concat(suffix);
    }
}
