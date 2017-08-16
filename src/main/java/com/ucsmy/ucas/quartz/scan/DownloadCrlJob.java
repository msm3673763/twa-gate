package com.ucsmy.ucas.quartz.scan;


import com.ucsmy.ucas.certification.service.CertificationService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class DownloadCrlJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadCrlJob.class);

    @Autowired
    CertificationService certificationService;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        LOGGER.debug("开始执行DefaultScanJob...");
        try {
            this.certificationService.refreshCrl();
        } catch (Exception e) {
            LOGGER.error("执行DefaultScanJob异常", e);
            throw new JobExecutionException(e);
        }

    }

}
