package com.cn.xiguaapp.common.gateway.validate.alipay;

import com.cn.xiguaapp.common.gateway.bean.ApiParam;
import com.cn.xiguaapp.common.gateway.message.ErrorEnum;
import com.cn.xiguaapp.common.gateway.validate.Signer;

/**
 * 支付宝签名验证实现。
 *
 * @author xiguaapp
 * @see <a href="https://docs.open.alipay.com/291/106118">支付宝签名</a>
 */
public class AlipaySigner implements Signer {

    @Override
    public boolean checkSign(ApiParam apiParam, String secret) {
        // 服务端存的是公钥
        String charset = apiParam.fetchCharset();
        String signType = apiParam.fetchSignMethod();
        if (signType == null) {
            throw ErrorEnum.ISV_DECRYPTION_ERROR_MISSING_ENCRYPT_TYPE.getErrorMeta().getException();
        }
        if (charset == null) {
            throw ErrorEnum.ISV_INVALID_CHARSET.getErrorMeta().getException();
        }
        return AlipaySignature.rsaCheckV2(apiParam, secret, charset, signType);
    }

}
