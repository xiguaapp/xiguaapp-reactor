package com.cn.xiguaapp.datasource.api.switcher;

import java.util.Optional;

/**
 * @author xiguaapp
 */
public interface Switcher {

    void useLast();

    void use(String id);

    void useDefault();

    Optional<String> current();

    void reset();

}
