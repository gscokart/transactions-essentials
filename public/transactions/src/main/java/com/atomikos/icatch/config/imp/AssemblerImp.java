package com.atomikos.icatch.config.imp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.atomikos.icatch.config.Assembler;
import com.atomikos.icatch.config.ConfigProperties;
import com.atomikos.logging.LoggerFactory;
import com.atomikos.util.ClassLoadingHelper;

public class AssemblerImp implements Assembler {
	
	private static final String DEFAULT_PROPERTIES_FILE_NAME = "transactions-defaults.properties";
	
	private static com.atomikos.logging.Logger LOGGER = LoggerFactory.createLogger(AssemblerImp.class);
	
    private void loadPropertiesFromClasspath(Properties p, String fileName){
    		URL url = null;
    		
    		//first look in application classpath (cf ISSUE 10091)
    		url = ClassLoadingHelper.loadResourceFromClasspath(getClass(), fileName);		
    		if (url == null) {
    			url = getClass().getClassLoader().getSystemResource ( fileName );
    		}
    		if (url != null) {
    			InputStream in;
				try {
					in = url.openStream();
					p.load ( in );
					in.close();
				} catch (IOException e) {
					LOGGER.logWarning("Failed to load properties", e);
				}
    		} else {
    			LOGGER.logWarning("Failed to load property file from classpath: " + fileName);
    		}
    }

	/**
	 * Called by ServiceLoader.
	 */
	public AssemblerImp() {
		
	}

	@Override
	public ConfigProperties getConfigProperties() {
		Properties p = new Properties();
		loadPropertiesFromClasspath(p, DEFAULT_PROPERTIES_FILE_NAME);
		return new ConfigProperties(p);
	}
}
