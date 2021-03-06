package com.cocofhu.ctb.kernel.core.resolver.bean;

import com.cocofhu.ctb.kernel.anno.CBean;
import com.cocofhu.ctb.kernel.core.config.CAbstractDefinition;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CConfig;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author cocofhu
 */
public class CClassPathAnnotationBeanDefinitionResolver implements CBeanDefinitionResolver {
    @Override
    public List<CBeanDefinition> resolveAll(CConfig config) {
        List<CBeanDefinition> beanDefinitions = new ArrayList<>();
        try {
            String url = getClassPath();
            List<String> classes = getClassesList(url);
            for (String str : classes) {
                Class<?> clazz = Class.forName(str);
                CBean bean = clazz.getAnnotation(CBean.class);
                if (bean != null) {
                    beanDefinitions.add(new CAbstractDefinition(clazz, CBeanDefinition.CBeanScope.SINGLETON) {
                        @Override
                        public String getBeanName() {
                            return bean.value();
                        }
                    });
                }
            }
        } catch (UnsupportedEncodingException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return beanDefinitions;
    }

    private static List<String> getAllClass(File file) {
        List<String> ret = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    ret.addAll(getAllClass(f));
                }
            }
        } else {
            ret.add(file.getAbsolutePath());
        }
        return ret;
    }

    private static List<String> getClassesList(String url) {
        File file = new File(url);
        List<String> classes = getAllClass(file);
        classes.replaceAll(s -> s.replace(url, "").replace(".class", "").replace("/", ".").replace("\\", "."));
        return classes;
    }

    private static String getClassPath() throws UnsupportedEncodingException {
        String url = URLDecoder.decode(Objects.requireNonNull(CBeanDefinitionResolver.class.getResource("/")).getPath(), Charset.defaultCharset().name());
        return url.replaceAll("//", "\\\\");
    }
}
