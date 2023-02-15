package com.cn.xiguapp.common.core.gen;

import cn.hutool.core.util.RandomUtil;
import com.cn.xiguapp.common.core.utils.SnowflakeIdGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * @author xiguaapp
 * @Date 2020/8/20
 * @desc 订单号或工单号生成器(总共32位)
 * 1.第1-8位为当前时段日期(如20201212)
 * 2.第30-31位为店铺/维修工人/销售员工号倒数第四和第三位若为空 则以0替代
 * 3.第28-29位为店铺/维修工人/销售员工号倒数第二位和第一位为空以0代替
 * 4.第9-27位随机生成数(19位(时间戳+机器码偏移量+工作空间(0-31)等))
 * 5.第31-32位为随机数
 */
public class WorkOrderCoinGenerator {
    /**
     * 生成时间串为如20201212的
     * @return
     */
    private String getDay(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * 生成随机数10位
     * @return
     */
    private String getRandomNo(){
        return String.valueOf(SnowflakeIdGenerator.getInstance()).substring(2,12);
    }

    /**
     * 获取店铺/维修工人/销售员工号倒数第四和第三位若为空 则以0替代
     * 获取为店铺/维修工人/销售员工号倒数第二位和第一位为空以0代替
     * @param workNo
     * @return
     */
    private String getShopOrWorker(String workNo){
        //超过四位取后四位
        int length = workNo.length();
        if (length>4){
            workNo = workNo.substring(length-4);
            length = workNo.length();
        }
        //判断后四位是否为数字 不是则返回0000
        String num = "-?[0-9]+(\\\\.[0-9]+)?";
        Pattern compile = Pattern.compile(num);
        if (!compile.matcher(workNo.substring(0,length)).matches()){
            workNo="0000";
        }
        String bb =  switch (workNo.length()){
            case 0->"0000";
            case 1->String.format("0%s00",workNo);//1
            case 2->String.format("%s00",workNo);
            case 3->String.format("%s",workNo.substring(1,3)+"0"+workNo.charAt(0));//0112//0211
            default -> String.format("%s",workNo.substring(2,4)+workNo.substring(0,2));
        };
        return bb;
    }

    /**
     * 随机数
     * @return
     */
    private String getRandomLast(){
        return String.valueOf(RandomUtil.randomNumbers(3));
    }

    /**
     * 获取工单或支付流水号
     * @param workNo
     * @return
     */
    private String getWorkId(String workNo){
        return getDay()+getRandomNo()+getShopOrWorker(workNo)+getRandomLast();
    }
    public static void main(String[] args) {
        WorkOrderCoinGenerator workOrderCoinGenerator = new WorkOrderCoinGenerator();
        for (int i = 10; i < 15; i++) {
            System.out.println(String.format("订单流水号:%s",workOrderCoinGenerator.getWorkId(String.valueOf(i))));
        }
    }
}
