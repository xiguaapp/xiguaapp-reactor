package com.cn.xiguaapp.r2dbc.orm.rdb.oprator;

import org.reactivestreams.Publisher;

public interface ResultOperator<E, R> {

    default R block(){
        return sync();
    }

    R sync();

    Publisher<E> reactive();

}
