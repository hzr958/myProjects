package com.smate.core.base.utils.factory.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import com.irissz.codec.utils.encrypt.ECCUtil;

/**
 * 加载自定义环境变量
 * 
 * @author zk
 *
 */
public class DynamicServerConfig extends PropertyPlaceholderConfigurer {

  /**
   * Set a location of a properties file to be loaded.
   * <p>
   * Can point to a classic properties file or to an XML file that follows JDK 1.5's properties XML
   * format.
   */
  private boolean localOverride = false;

  private Properties[] localProperties;

  private Resource[] locations;

  private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

  private boolean ignoreResourceNotFound = false;

  /**
   * Set if failure to find the property resource should be ignored. True is appropriate if the
   * properties file is completely optional. Default is "false".
   */
  public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
    this.ignoreResourceNotFound = ignoreResourceNotFound;
  }

  /**
   * Set local properties, e.g. via the "props" tag in XML bean definitions. These can be considered
   * defaults, to be overridden by properties loaded from files.
   */
  public void setProperties(Properties properties) {
    this.localProperties = new Properties[] {properties};
    super.setProperties(properties);
  }

  /**
   * Set local properties, e.g. via the "props" tag in XML bean definitions, allowing for merging
   * multiple properties sets into one.
   */
  public void setPropertiesArray(Properties[] propertiesArray) {
    this.localProperties = propertiesArray;
  }

  public void setLocation(Resource location) {
    this.locations = new Resource[] {location};
    super.setLocation(location);
  }

  /**
   * Set locations of properties files to be loaded.
   * <p>
   * Can point to classic properties files or to XML files that follow JDK 1.5's properties XML
   * format.
   */

  public void setLocations(Resource[] locations) {
    this.locations = locations;
    super.setLocations(locations);
  }

  /**
   * Set whether local properties override properties from files. Default is "false": properties from
   * files override local defaults. Can be switched to "true" to let local properties override
   * defaults from files.
   */
  public void setLocalOverride(boolean localOverride) {
    this.localOverride = localOverride;
  }

  public Properties mergeProperties() throws IOException {
    Properties result = new Properties();

    if (this.localOverride) {
      // Load properties from file upfront, to let local properties
      // override.
      loadProperties(result);
    }

    if (this.localProperties != null) {
      for (int i = 0; i < this.localProperties.length; i++) {
        CollectionUtils.mergePropertiesIntoMap(this.localProperties[i], result);
      }
    }

    if (!this.localOverride) {
      // Load properties from file afterwards, to let those properties
      // override.
      loadProperties(result);
    }

    return result;
  }


  /**
   * 重写这个方法. 需要对mongodb密码进行解密操作.
   */
  @Override
  protected String convertProperty(String propertyName, String propertyValue) {
    String privateKey = System.getenv("SNS_PRIVATE_KEY");
    if (StringUtils.isBlank(privateKey)) {
      logger.debug("privateKey=" + privateKey);
      logger.debug("私钥为空 不能触发解密！！.");
    } else if ("mongo.password".equals(propertyName)) {
      // propertyValue 对值 解密
      try {
        propertyValue = ECCUtil.privateDecrypt(propertyValue, ECCUtil.string2PrivateKey(privateKey));
      } catch (Exception e) {
        logger.error("privateKey=" + privateKey + "," + propertyName + "=" + propertyValue);
        logger.error("密码解密失败.", e);
      }
    }
    return convertPropertyValue(propertyValue);
  }

  /**
   * Load properties into the given instance.
   * 
   * @param props the Properties instance to load into
   * @throws java.io.IOException in case of I/O errors
   * @see #setLocations
   */
  @Override
  protected void loadProperties(Properties props) throws IOException {

    if (this.locations != null) {
      for (int i = 0; i < this.locations.length; i++) {
        Resource location = this.locations[i];
        if (logger.isInfoEnabled()) {
          logger.info("Loading properties file from " + location);
        }
        InputStream is = null;
        try {
          /**
           * 根据环境变量判断是否在开发，生产和测试计算机上面
           */
          String runEnv = System.getenv("RUN_ENV");
          logger.info("runEnv ------------------------->" + runEnv);
          if (location.getFilename().indexOf("." + runEnv + ".") > 0) {
            is = location.getInputStream();
            this.propertiesPersister.load(props, is);
            break;
          }

        } catch (IOException ex) {
          if (this.ignoreResourceNotFound) {
            if (logger.isWarnEnabled()) {
              logger.warn("Could not load properties from " + location + ": " + ex.getMessage());
            }
          } else {
            throw ex;
          }
        } finally {
          if (is != null) {
            is.close();
          }
        }
      }
    }
  }
}
