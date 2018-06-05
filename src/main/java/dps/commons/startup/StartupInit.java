package dps.commons.startup;

import dps.logging.HasLogger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Singleton;
import java.util.Set;

public class StartupInit implements HasLogger {

    public void init() {

        logInfo("Loading Startup Classes");

        BeanManager beanManager = CDI.current().getBeanManager();

        Set<Bean<?>> beans = beanManager.getBeans(Object.class);
        for (Bean<?> bean: beans) {
            Class<?> beanClass = bean.getBeanClass();
            if (bean.getScope() == ApplicationScoped.class || bean.getScope() == Dependent.class || bean.getScope() == Singleton.class) {
                if (beanClass.getAnnotation(Startup.class) != null) {
                    Object o = CDI.current().select(beanClass).get();
                    logInfo("initialized: " + o.toString());
                }
            }
        }

    }
}
