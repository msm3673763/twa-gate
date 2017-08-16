package com.ucsmy.ucas.error.web;

import com.ucsmy.ucas.commons.aop.exception.result.AosResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ucs_xujunwei on 2017/4/26.
 */
@Controller
@RequestMapping(value = "error")
public class BasicErrorController implements  ErrorController{
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicErrorController.class);



    @RequestMapping
    @ResponseBody
    public AosResult error(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = this.getStatus(request);
        response.setStatus(status.value());
        return AosResult.retFailureMsg("请求路径错误或请求超时");
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode.intValue());
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
