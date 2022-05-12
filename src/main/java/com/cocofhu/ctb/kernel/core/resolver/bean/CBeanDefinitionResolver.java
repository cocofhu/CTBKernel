package com.cocofhu.ctb.kernel.core.resolver.bean;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.exception.CBeanException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cocofhu
 */
public interface CBeanDefinitionResolver {

    List<CBeanDefinition> resolveAll(CConfig config) throws CBeanException;
}
