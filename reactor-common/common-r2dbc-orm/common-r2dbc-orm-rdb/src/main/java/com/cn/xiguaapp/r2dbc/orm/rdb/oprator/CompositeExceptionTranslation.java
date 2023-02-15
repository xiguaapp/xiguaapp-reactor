package com.cn.xiguaapp.r2dbc.orm.rdb.oprator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * @author xiguaapp
 */
public class CompositeExceptionTranslation implements ExceptionTranslation {

    private List<ExceptionTranslation> translations = new CopyOnWriteArrayList<>();

    @Override
    public Throwable translate(Throwable e) {
        for (ExceptionTranslation translation : translations) {
            Throwable newErr = translation.translate(e);
            if (newErr != e) {
                return newErr;
            }
        }
        return e;
    }

    public CompositeExceptionTranslation add(ExceptionTranslation translation) {
        translations.add(translation);
        return this;
    }

    public CompositeExceptionTranslation add(boolean when, Supplier<ExceptionTranslation> translation) {
        if (when) {
            translations.add(translation.get());
        }
        return this;
    }
}
