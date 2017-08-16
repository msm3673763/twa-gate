package com.ucsmy.ucas.certification.service;

import com.ucsmy.ucas.commons.aop.exception.result.AosResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ucs_fucong
 * @date 2017/08/15
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CertificationServiceTest {
    @Autowired
    CertificationService certificationService;

    @Test
    public void refreshCrl() throws Exception {
        AosResult aosResult = certificationService.refreshCrl();
        System.out.println(aosResult);
    }

}