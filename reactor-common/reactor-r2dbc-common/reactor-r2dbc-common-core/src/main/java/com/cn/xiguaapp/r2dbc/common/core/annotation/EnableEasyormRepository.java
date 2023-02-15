package com.cn.xiguaapp.r2dbc.common.core.annotation;

import com.cn.xiguaapp.r2dbc.common.core.configuration.EasyormRepositoryRegistrar;
import org.springframework.context.annotation.Import;

import javax.persistence.Table;
import java.lang.annotation.*;

/**
 * @author xiguaapp
 * @desc orm支持类注解 实体扫描位置
 * @see com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository
 * @see com.cn.xiguaapp.r2dbc.orm.rdb.mapping.SyncRepository
 * @since 1.0 17:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({EasyormRepositoryRegistrar.class})
public @interface EnableEasyormRepository {
    /**
     * 实体类包名
     * <pre>com.cn.xiguaapp.*.entity</pre>
     * @return String
     */
    String[]value();

    /**
     * @see com.cn.xiguaapp.r2dbc.orm.rdb.mapping.jpa.JpaEntityTableMetadataParser
     * @return Class
     */
    Class<? extends Annotation>[] annotation() default Table.class;
}
