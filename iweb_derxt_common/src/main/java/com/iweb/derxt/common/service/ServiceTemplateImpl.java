package com.iweb.derxt.common.service;

import com.iweb.derxt.common.model.BusinessCodeEnum;
import com.iweb.derxt.common.model.CallResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Component
@Slf4j
public class ServiceTemplateImpl implements ServiceTemplate{
    @Override
    @Transactional
    public <T> CallResult<T> execute(TemplateAction<T> action) {
        try {
            CallResult<T> callResult = action.checkParam();
            if (callResult == null) {
                log.warn("execute:null result while checkParam");
                return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getMsg());
            }
            if (!callResult.isSuccess()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return callResult;
            }
            callResult = action.checkBiz();
            long start = System.currentTimeMillis();
            CallResult<T> cr = action.doAction();
            log.info("execute datasource method run time:{}ms", System.currentTimeMillis() - start);
            if (cr == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return CallResult.fail(BusinessCodeEnum.CHECK_ACTION_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_ACTION_NO_RESULT.getMsg());
            }
            if (!cr.isSuccess()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return cr;
            }
            if (cr.isSuccess()) {
                action.finishUp(cr);
            }
            return cr;
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("execute error",e);
            return CallResult.fail();
        }

    }

    @Override
    public <T> CallResult<T> executeQuery(TemplateAction<T> action) {
        try{
            CallResult<T> callResult = action.checkParam();
            if (callResult == null) {
                log.warn("executeQuery:null result while checkParam");
                return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getMsg());
            }
            if (!callResult.isSuccess()){
                return callResult;
            }
            callResult = action.checkBiz();
            if (callResult==null){
                log.warn("executeQuery:null result while checkBiz");
                return CallResult.fail(BusinessCodeEnum.CHECK_BIZ_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_BIZ_NO_RESULT.getMsg());
            }
            if (!callResult.isSuccess()){
                return callResult;
            }

            long start = System.currentTimeMillis();
            CallResult<T> cr = action.doAction();
            log.info("executeQuery datasource method run time:{}ms", System.currentTimeMillis() - start);
            if (cr==null){
                return CallResult.fail(BusinessCodeEnum.CHECK_ACTION_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_ACTION_NO_RESULT.getMsg());
            }
            if (!cr.isSuccess()){
                return cr;
            }
            if (cr.isSuccess()){
                action.finishUp(cr);
            }
            return cr;
        }catch (Exception e){
            log.error("executeQuery error",e);
            return CallResult.fail();
        }
    }
}
