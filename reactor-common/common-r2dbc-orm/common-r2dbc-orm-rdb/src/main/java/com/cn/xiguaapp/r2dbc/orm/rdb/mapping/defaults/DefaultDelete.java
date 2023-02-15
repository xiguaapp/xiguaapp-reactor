package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults;

import com.cn.xiguaapp.r2dbc.orm.param.QueryParam;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.properties.NestConditional;
import com.cn.xiguaapp.r2dbc.orm.properties.SimpleNestConditional;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeyValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.DSLDelete;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.EventResultOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingEventTypes;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete.DeleteOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete.DeleteResultOperator;
import com.cn.xiguaapp.r2dbc.orm.support.TermTypeConditionalSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeys.source;
import static com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeys.tableMetadata;
import static com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingContextKeys.delete;

@SuppressWarnings("all")
public class DefaultDelete<ME extends DSLDelete> implements DSLDelete<ME> {

    protected List<Term> terms = new ArrayList<>();

    protected TermTypeConditionalSupport.Accepter<ME, Object> accepter = this::and;

    protected DeleteOperator operator;

    private RDBTableMetadata metadata;
    protected List<ContextKeyValue<?>> contextKeyValues = new ArrayList<>();

    public DefaultDelete(RDBTableMetadata tableMetadata, DeleteOperator operator, ContextKeyValue<?>... mapping) {
        this.operator = operator;
        this.metadata=tableMetadata;
        contextKeyValues.add(source(this));
        contextKeyValues.add(delete(operator));
        contextKeyValues.add(tableMetadata(tableMetadata));
        contextKeyValues.addAll(Arrays.asList(mapping));
    }

    protected DeleteResultOperator doExecute() {
        return EventResultOperator.create(
                () -> {
                    return operator
                            .where(dsl -> terms.forEach(dsl::accept))
                            .execute();
                },
                DeleteResultOperator.class,
                metadata,
                MappingEventTypes.delete_before,
                MappingEventTypes.delete_after,
                contextKeyValues.toArray(new ContextKeyValue[0])
        );
    }

    @Override
    public QueryParam toQueryParam() {
        QueryParam param = new QueryParam();
        param.setTerms(terms);
        param.setPaging(false);
        return param;
    }

    @Override
    public NestConditional<ME> nest() {
        Term term = new Term();
        term.setType(Term.Type.and);
        terms.add(term);
        return new SimpleNestConditional<>((ME) this, term);
    }

    @Override
    public NestConditional<ME> orNest() {
        Term term = new Term();
        term.setType(Term.Type.or);
        terms.add(term);
        return new SimpleNestConditional<>((ME) this, term);
    }

    @Override
    public ME and() {
        this.accepter = this::and;
        return (ME) this;
    }

    @Override
    public ME or() {
        this.accepter = this::or;
        return (ME) this;
    }

    @Override
    public ME and(String column, String termType, Object value) {
        if (value != null) {
            Term term = new Term();
            term.setColumn(column);
            term.setTermType(termType);
            term.setValue(value);
            term.setType(Term.Type.and);
            terms.add(term);
        }
        return (ME) this;
    }

    @Override
    public ME or(String column, String termType, Object value) {
        if (value != null) {
            Term term = new Term();
            term.setColumn(column);
            term.setTermType(termType);
            term.setValue(value);
            term.setType(Term.Type.or);
            terms.add(term);
        }
        return (ME) this;
    }

    @Override
    public TermTypeConditionalSupport.Accepter<ME, Object> getAccepter() {
        return accepter;
    }

    @Override
    public ME accept(Term term) {
        terms.add(term);
        return (ME) this;
    }
}
