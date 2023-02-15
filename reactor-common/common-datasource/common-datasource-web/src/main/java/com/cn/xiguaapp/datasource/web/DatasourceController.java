
package com.cn.xiguaapp.datasource.web;

import com.cn.xiguaapp.datasource.api.config.DynamicDataSourceConfig;
import com.cn.xiguaapp.datasource.api.config.DynamicDataSourceConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/datasource")
public class DatasourceController {

    @Autowired
    private DynamicDataSourceConfigRepository<? extends DynamicDataSourceConfig> repository;

    @GetMapping
    public List<? extends DynamicDataSourceConfig> getAllConfig() {
        return repository.findAll();
    }

}
