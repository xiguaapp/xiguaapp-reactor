package com.cn.xiguaapp.r2dbc.common.api.crud.entity;

import com.cn.xiguaapp.r2dbc.orm.dsl.Query;
import com.cn.xiguaapp.r2dbc.orm.param.QueryParam;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.param.TermType;
import com.cn.xiguaapp.r2dbc.orm.properties.NestConditional;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author xiguaapp
 * @desc 查询参数实体 详情查看common-r2dbc-rom
 * 可通过静态方法创建:<br>
 *     如：
 *     <code>
 *         QueryParamEtity.of("id",id)
 *     </code>
 * @since 1.0 11:36
 */
@Slf4j
public class QueryParamEntity extends QueryParam {
    private static final long serialVersionUID=8097500947924037523L;

    @Getter
    @Schema(description = "where条件表达式,与terms参数不能共存.语法: name = 张三 and age > 16")
    private String where;

    @Getter
    @Schema(description = "orderBy条件表达式,与sorts参数不能共存.语法: age asc,createTime desc")
    private String orderBy;

    //总数,设置了此值时,在分页查询的时候将不执行count.
    @Getter
    @Setter
    @Schema(description = "设置了此值后将不重复执行count查询总数")
    private Integer total;

    @Override
    @Hidden
    public boolean isForUpdate() {
        return super.isForUpdate();
    }

    @Override
    @Hidden
    public int getThinkPageIndex() {
        return super.getThinkPageIndex();
    }

    @Override
    @Hidden
    public int getPageIndexTmp() {
        return super.getPageIndexTmp();
    }

    @Override
    @Schema(description = "指定要查询的列")
    public Set<String> getIncludes() {
        return super.getIncludes();
    }

    @Override
    @Schema(description = "指定不查询的列")
    public Set<String> getExcludes() {
        return super.getExcludes();
    }

    /**
     * 创建一个空的查询参数实体,该实体无任何参数.
     *
     * @return 无条件的参数实体
     */
    public static QueryParamEntity of() {
        return new QueryParamEntity();
    }


    /**
     * @see this#of(String, Object)
     */
    public static QueryParamEntity of(String field, Object value) {
        return of().and(field, TermType.eq, value);
    }

    /**
     * @since 1.0.0
     */
    public static <T> Query<T, QueryParamEntity> newQuery() {
        return Query.of(new QueryParamEntity());
    }

    /**
     * @since 1.0.0
     */
    public <T> Query<T, QueryParamEntity> toQuery() {
        return Query.of(this);
    }

    /**
     * 将已有的条件包装到一个嵌套的条件里,并返回一个Query对象.例如:
     * <pre>
     *     entity.toNestQuery().and("userId",userId);
     * </pre>
     * <p>
     * 原有条件: name=? or type=?
     * <p>
     * 执行后条件: (name=? or type=?) and userId=?
     *
     * @see this#toNestQuery(Consumer)
     * @since 1.0.0
     */
    public <T> Query<T, QueryParamEntity> toNestQuery() {
        return toNestQuery(null);
    }

    /**
     * 将已有的条件包装到一个嵌套的条件里,并返回一个Query对象.例如:
     * <pre>
     *     entity.toNestQuery(query->query.and("userId",userId));
     * </pre>
     * <p>
     * 原有条件: name=? or type=?
     * <p>
     * 执行后条件: userId=? (name=? or type=?)
     *
     * @param before 在包装之前执行,将条件包装到已有条件之前
     * @since 1.0.0
     */
    public <T> Query<T, QueryParamEntity> toNestQuery(Consumer<Query<T, QueryParamEntity>> before) {
        List<Term> terms = getTerms();
        setTerms(new ArrayList<>());
        Query<T, QueryParamEntity> query = toQuery();
        if (null != before) {
            before.accept(query);
        }
        if (terms.isEmpty()) {
            return query;
        }
        return query
                .nest()
                .each(terms, NestConditional::accept)
                .end();
    }


    /**
     * 表达式方式排序
     *
     * @param orderBy 表达式
     * @since 1.0
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        if (StringUtils.isEmpty(orderBy)) {
            return;
        }
        setSorts(TermExpressionParser.parseOrder(orderBy));
    }

    /**
     * 表达式查询条件,没有SQL注入问题,随意使用
     * @param where 表达式
     * @since 1.0.0
     */
    public void setWhere(String where) {
        this.where = where;
        if (StringUtils.isEmpty(where)) {
            return;
        }
        setTerms(TermExpressionParser.parse(where));
    }

    @Override
    public List<Term> getTerms() {
        List<Term> terms = super.getTerms();
        if (CollectionUtils.isEmpty(terms) && StringUtils.hasText(where)) {
            setTerms(terms = TermExpressionParser.parse(where));
        }
        return terms;
    }

    @Override
    public QueryParamEntity noPaging() {
        setPaging(false);
        return this;
    }

    @Override
    public QueryParamEntity clone() {
        return (QueryParamEntity) super.clone();
    }


}
