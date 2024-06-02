package com.iweb.derxt.sso.service.impl;

import com.iweb.derxt.common.service.ServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {
    @Autowired
    protected ServiceTemplate serviceTemplate;
}
