package com.ucsmy.ucas.certification.dao;

import com.ucsmy.ucas.certification.ext.CaCerMongoFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Mongodb 接口
 *
 * @author zhengfucheng
 * @version 1.0.0 2017-04-26
 */
@Repository
public interface MongoFileMapper extends MongoRepository<CaCerMongoFile, String> {

}
