package com.cn.xiguaapp.datasource.api.test;

import com.cn.xiguaapp.datasource.api.switcher.DefaultSwitcher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DefaultSwitcherTest {

    @Test
    public void DefaultSwitcher() {
        DefaultSwitcher switcher = new DefaultSwitcher("test", "schema");

        assertFalse(switcher.current().isPresent());

        switcher.use("test");
        assertEquals(switcher.current().orElse(null), "test");

        switcher.use("test2");
        assertEquals(switcher.current().orElse(null), "test2");

        switcher.useLast();
        assertEquals(switcher.current().orElse(null), "test");

        switcher.reset();
        assertFalse(switcher.current().isPresent());

    }
}