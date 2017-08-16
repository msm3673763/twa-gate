package com.ucsmy.ucas.certification.service;


import com.ucsmy.ucas.commons.aop.exception.result.AosResult;

/**
 * Created by ucs_xujunwei on 2017/4/26.
 */
public interface CertificationService {

    /**
     * 刷新crl
     * 从redis获取最新的crl，如果redis的不存在，则从mongodb里面获取
     * @return
     */
    AosResult refreshCrl();

}
