package com.cn.xiguapp.common.core.excel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author xiguaapp
 */
@Getter
@Setter
@NoArgsConstructor
public class ExcelHeader implements OptionSupport {

    private String key;

    private String text;

    private CellDataType type;

    private Options options = Options.of();

    public ExcelHeader(String key, String text, CellDataType type) {
        this.key = key;
        this.text = text;
        this.type = type;
    }

    @Override
    public Options options() {
        return options;
    }
}
