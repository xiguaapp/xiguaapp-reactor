package com.cn.xiguaapp.datagrap.sqltemplate.service;

import com.cn.xiguapp.common.core.exception.ApiException;
import com.cn.xiguapp.common.core.utils.StringUtils;
import com.cn.xiguaapp.datagrap.sqltemplate.config.SqlTemplateConfigration;
import com.cn.xiguaapp.datagrap.sqltemplate.utils.FreeMakerParser;
import com.cn.xiguaapp.datagrap.sqltemplate.utils.SqlTemplateUtils;
import com.cn.ykyoung.server.exception.ServiceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author xiguaapp
 * @date 2021-02-22
 * @desc sqltemplate服务实现类
 */
@Data
@Slf4j
public class SqlTemplateServiceImpl implements SqlTemplateService{
    private SqlTemplateConfigration sqlTemplateConfigration;

    private Map<String, Map<String, String>> sqlContainer;

    private static final String SQL_TEMPLATE_PATH = "classpath*:sqltemplates/**/*.xml";
    public SqlTemplateServiceImpl(SqlTemplateConfigration sqlTemplateConfigration) throws ApiException {
        //不在使用配置这里写死
        sqlContainer = SqlTemplateUtils.getSqlContainer(SQL_TEMPLATE_PATH);
    }
    @Override
    public BiFunction<String, Map<String, Object>, String> getSql() {
        return (sqlId,params)->{
            if (StringUtils.isBlank(sqlId)){
                throw new ServiceException("40004","isv.sqlId-can-not-be-null");
            }
            int i = sqlId.lastIndexOf(".");

            if (i == -1) {
                throw new ServiceException("40004","SQLID-NOT-EXIST");
            }
            //获取sqlID中文件路径
            String filePath = sqlId.substring(0, i);
            Map<String, String> stringStringMap = sqlContainer.get(filePath);
            if (CollectionUtils.isEmpty(stringStringMap)) {
                log.error("【sql模板处理】sqlContainer中{}.xml不存在", filePath);
                throw new ServiceException("40004","SQL-XMLFILE-NOT-EXIST");
            }
            //获取sqlID中sql的id值
            String id = sqlId.substring(i + 1);
            String sql = stringStringMap.get(id);
            if (org.springframework.util.StringUtils.isEmpty(sql)) {
                throw new ServiceException("40004","SQLID-NOT-EXIST");
            }
            // sql还需要freemarker转换
            return getSqlFromStringTemplate().apply(sql, params);
        };
    }

    @Override
    public BiFunction<String, Map<String, Object>, String> getSqlFromStringTemplate() {
        return (sqlStringTemplate,params)->{
            // sql还需要freemarker转换
            //多加一层 用params.**访问数据
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("params", params);
            //在freemarker中#{}是会被解析的，解决与mybatis冲突了
            String s = sqlStringTemplate.replaceAll("#\\{", "|=|");
            String process = FreeMakerParser.process(s, stringObjectHashMap);
            String s1 = process.replaceAll("\\|=\\|", "#\\{");
            return s1;
        };
    }
}
