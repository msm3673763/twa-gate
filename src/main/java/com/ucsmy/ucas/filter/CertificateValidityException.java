package com.ucsmy.ucas.filter;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import com.ucsmy.common.exception.BizException;

/**
 * @author ucs_fucong
 * @date 2017/08/15
 */
public class CertificateValidityException extends BizException {

    public CertificateValidityException(String summary) {
        super(summary);
    }

    public String toJson() {
        Map<String, Object> propMap = new HashMap<>();
        propMap.put("code", this.getClass().getSimpleName());
        propMap.put("summary", this.summary);
        propMap.put("detail", this.detail);
        return JSON.toJSONString(propMap);
    }
}
