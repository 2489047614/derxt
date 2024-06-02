package com.iweb.derxt.common.service;

import com.iweb.derxt.common.model.CallResult;

public interface ServiceTemplate {

    <T> CallResult<T> execute(TemplateAction<T> action);

    <T> CallResult<T> executeQuery(TemplateAction<T> action);
}
