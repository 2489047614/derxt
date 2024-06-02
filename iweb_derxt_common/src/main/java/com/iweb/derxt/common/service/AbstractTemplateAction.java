package com.iweb.derxt.common.service;

import com.iweb.derxt.common.model.CallResult;

public abstract class AbstractTemplateAction<T> implements TemplateAction<T>{
    @Override
    public CallResult<T> checkParam() {
        return CallResult.success();
    }

    @Override
    public CallResult<T> checkBiz() {
        return CallResult.success();
    }
    //doAction是必须重写的方法 所以 不能重写
    @Override
    public void finishUp(CallResult<T> callResult) {

    }
}
