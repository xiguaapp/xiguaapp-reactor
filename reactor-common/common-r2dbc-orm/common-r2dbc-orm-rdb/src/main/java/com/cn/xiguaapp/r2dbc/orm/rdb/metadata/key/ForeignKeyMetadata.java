package com.cn.xiguaapp.r2dbc.orm.rdb.metadata.key;


import com.cn.xiguaapp.r2dbc.orm.meta.ObjectMetadata;
import com.cn.xiguaapp.r2dbc.orm.meta.ObjectType;
import com.cn.xiguaapp.r2dbc.orm.meta.RDBObjectType;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.TableOrViewMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.JoinType;

import java.util.List;
import java.util.Optional;

/**
 * 外键
 * @see ForeignKeyBuilder
 */
public interface ForeignKeyMetadata extends ObjectMetadata {

    @Override
    default ObjectType getObjectType() {
        return RDBObjectType.foreignKey;
    }

    /**
     * @return 是否为逻辑外键
     */
    boolean isLogical();

    /**
     * @return 是否n对多
     */
    boolean isToMany();

    AssociationType getType();

    TableOrViewMetadata getSource();

    TableOrViewMetadata getTarget();

    List<ForeignKeyColumn> getColumns();

    boolean isAutoJoin();

    //自动关联表类型
    JoinType getJoinType();

    List<Term> getTerms();

    Optional<ForeignKeyMetadata> getMiddleForeignKey(String name);

    List<ForeignKeyMetadata> getMiddleForeignKeys();
}
