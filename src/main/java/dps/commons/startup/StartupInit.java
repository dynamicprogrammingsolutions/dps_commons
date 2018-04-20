package dps.commons.startup;

import dps.logging.HasLogger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.util.Set;

@ApplicationScoped
public class StartupInit implements HasLogger {

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {

        logInfo("Loading Startup Classes");

        BeanManager beanManager = CDI.current().getBeanManager();

        Set<Bean<?>> beans = beanManager.getBeans(Object.class);
        for (Bean<?> bean: beans) {
            Class<?> beanClass = bean.getBeanClass();
            if (bean.getScope() != ApplicationScoped.class) continue;
            if (beanClass.getAnnotation(Startup.class) != null) {
                Object o = CDI.current().select(beanClass).get();
                logInfo("initialized: "+o);
            }
        }

    }
}
