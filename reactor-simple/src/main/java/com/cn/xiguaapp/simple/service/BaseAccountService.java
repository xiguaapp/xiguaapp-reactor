package com.cn.xiguaapp.simple.service;

import com.cn.xiguaapp.r2dbc.common.core.service.GenericReactiveCrudService;
import com.cn.xiguaapp.simple.entity.BaseAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xiguaapp
 * @Date 2020/9/11
 * @desc
 */
@Slf4j
@Service
public class BaseAccountService extends GenericReactiveCrudService<BaseAccount,Long> {

}
