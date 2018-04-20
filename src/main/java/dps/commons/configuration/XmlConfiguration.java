package dps.commons.configuration;

import dps.logging.HasLogger;
import org.apache.commons.beanutils.BeanUtils;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

public abstract class XmlConfiguration implements HasLogger {

    abstract protected String getSettingsFile();

    @PostConstruct
    void init()
    {
        boolean loaded = false;
        try (InputStream settingsInputStream = this.getClass().getClassLoader().getResourceAsStream(getSettingsFile())) {
            JAXBContext jaxbContext = JAXBContext.newInstance(this.getClass());
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Object settings = unmarshaller.unmarshal(settingsInputStream);
            logInfo("settings loaded: "+settings);
            BeanUtils.copyProperties(this, settings);
            loaded = true;
        } catch (IllegalAccessException | JAXBException | InvocationTargetException | IOException e) {
            logSevere("Couldn't load settings file",e);
        }
        if (!loaded) throw new IllegalArgumentException("Couldn't initialize settings");
    }
}
