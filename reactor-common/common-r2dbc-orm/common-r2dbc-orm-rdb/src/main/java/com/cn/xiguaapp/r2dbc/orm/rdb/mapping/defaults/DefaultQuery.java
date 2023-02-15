package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults;

import com.cn.xiguaapp.r2dbc.orm.param.MethodReferenceColumn;
import com.cn.xiguaapp.r2dbc.orm.param.QueryParam;
import com.cn.xiguaapp.r2dbc.orm.param.StaticMethodReferenceColumn;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.properties.NestConditional;
import com.cn.xiguaapp.r2dbc.orm.properties.SimpleNestConditional;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeyValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.DSLQuery;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityColumnMapping;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.TableOrViewMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DMLOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.SortOrderSupplier;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.SelectColumn;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.SortOrder;
import com.cn.xiguaapp.r2dbc.orm.support.TermTypeConditionalSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("all")
public class DefaultQuery<T, ME extends DSLQuery> implements DSLQuery<ME> {

    protected QueryParam param = new QueryParam();

    protected Term.Type currentTermType = Term.Type.and;

    protected TermTypeConditionalSupport.Accepter<ME, Object> accepter = this::and;

    protected DMLOperator operator;

    protected ResultWrapper<T, ?> wrapper;

    protected TableOrViewMetadata tableMetadata;

    protected EntityColumnMapping columnMapping;

    protected List<SortOrder> orders = new ArrayList<>();

    public DefaultQuery(TableOrViewMetadata tableMetadata,
                        EntityColumnMapping mapping,
                        DMLOperator operator,
                        ResultWrapper<T, ?> wrapper,
                        ContextKeyValue<?>... keyValues) {
        this.operator = operator;
        this.wrapper = wrapper;
        this.tableMetadata = tableMetadata;
        this.columnMapping = mapping;
        param.setPaging(false);
    }

    @Override
    public ME select(String... columns) {
        param.includes(columns);
        return (ME) this;
    }

    @Override
    @SafeVarargs
    public final <T> ME select(StaticMethodReferenceColumn<T>... column) {
        return select(Arrays.stream(column).map(StaticMethodReferenceColumn::getColumn).toArray(String[]::new));
    }

    @SafeVarargs
    @Override
    public final <T> ME select(MethodReferenceColumn<T>... column) {
        return select(Arrays.stream(column).map(MethodReferenceColumn::getColumn).toArray(String[]::new));
    }

    @SafeVarargs
    @Override
    public final <T> ME selectExcludes(StaticMethodReferenceColumn<T>... column) {
        return selectExcludes(Arrays.stream(column).map(StaticMethodReferenceColumn::getColumn).toArray(String[]::new));
    }

    @SafeVarargs
    @Override
    public final <T> ME selectExcludes(MethodReferenceColumn<T>... column) {
        return selectExcludes(Arrays.stream(column).map(MethodReferenceColumn::getColumn).toArray(String[]::new));
    }

    @Override
    public ME selectExcludes(String... columns) {
        param.excludes(columns);
        return (ME) this;
    }

    public ME paging(int pageIndex, int pageSize) {
        param.doPaging(pageIndex, pageSize);
        return (ME) this;
    }

    @Override
    public ME orderBy(SortOrder... orders) {
        this.orders.addAll(Arrays.asList(orders));
        return (ME) this;
    }

    @Override
    public ME orderBy(SortOrderSupplier... orders) {
        return orderBy(Arrays.stream(orders)
                .map(SortOrderSupplier::get)
                .toArray(SortOrder[]::new));
    }

    @Override
    public ME setParam(QueryParam param) {
        this.param = param;
        return (ME) this;
    }

    private boolean isSelectInclude(SelectColumn column) {
        return param.getIncludes().isEmpty() ||
                param.getIncludes()
                        .stream()
                        .anyMatch(s -> s.equals(column.getColumn()) || s.equals(column.getAlias()));
    }

    private boolean isSelectExclude(SelectColumn column) {
        return param.getExcludes()
                .stream()
                .anyMatch(s -> s.equals(column.getColumn()) || s.equals(column.getAlias()));
    }

    protected SelectColumn[] getSelectColumn() {

        return Stream.concat(
                tableMetadata.getForeignKeys().stream()
                        .map(key -> key.getAlias() == null ? key.getTarget().getName() : key.getAlias())
                        .map(alias -> alias.concat(".*"))
                        .map(name -> SelectColumn.of(name))
                , columnMapping.getColumnPropertyMapping()
                        .entrySet()
                        .stream()
                        .map(entry -> SelectColumn.of(entry.getKey(), entry.getValue())))
                .filter(this::isSelectInclude)
                .filter(e -> !isSelectExclude(e))
                .toArray(SelectColumn[]::new);
    }

    protected SortOrder[] getSortOrder() {
        return Stream.concat(
                param.getSorts()
                        .stream()
                        .map(sort -> sort.getOrder().equalsIgnoreCase("asc") ? SortOrder.asc(sort.getName()) : SortOrder.desc(sort.getName()))
                , orders.stream())
                .toArray(SortOrder[]::new);
    }


    @Override
    public NestConditional<ME> nest() {
        return new SimpleNestConditional<>((ME) this, param.nest());
    }

    @Override
    public NestConditional<ME> orNest() {
        return new SimpleNestConditional<>((ME) this, param.orNest());
    }

    @Override
    public ME and() {
        accepter = this::and;
        currentTermType = Term.Type.and;
        return (ME) this;
    }

    @Override
    public ME or() {
        accepter = this::or;
        currentTermType = Term.Type.or;
        return (ME) this;
    }

    @Override
    public ME and(String column, String termType, Object value) {
        if (value != null) {
            param.and(column, termType, value);
        }
        return (ME) this;
    }

    @Override
    public ME or(String column, String termType, Object value) {
        if (value != null) {
            param.or(column, termType, value);
        }
        return (ME) this;
    }

    @Override
    public TermTypeConditionalSupport.Accepter<ME, Object> getAccepter() {
        return accepter;
    }

    @Override
    public ME accept(Term term) {
        param.getTerms().add(term);

        return (ME) this;
    }


}
