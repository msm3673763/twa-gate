package com.ucsmy.ucas.certification.web;

import com.ucsmy.ucas.certification.service.CertificationService;
import com.ucsmy.ucas.commons.aop.exception.result.AosResult;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ucs_xujunwei on 2017/4/26.
 */
@Controller
@RequestMapping("/certification/")
public class CertificationController {

    @Autowired
    CertificationService certificationService;

    @RequestMapping("/refreshCrl")
    @ResponseBody
    public AosResult refreshCrl(){
        AosResult result = certificationService.refreshCrl();
        return result;
    }

}
