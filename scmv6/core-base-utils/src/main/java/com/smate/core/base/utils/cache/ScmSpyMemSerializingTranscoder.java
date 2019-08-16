package com.smate.core.base.utils.cache;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.spy.memcached.transcoders.SerializingTranscoder;

/**
 * <pre>
 * 序列化错误，使用自定义序列化转换.
 * WARN net.spy.memcached.transcoders.SerializingTranscoder:  Caught CNFE decoding 690 bytes of data
 * java.lang.ClassNotFoundException: org.kb.ws.summary.model.Data
 *         at java.net.URLClassLoader$1.run(URLClassLoader.java:202)
 *         at java.security.AccessController.doPrivileged(Native Method)
 *         at java.net.URLClassLoader.findClass(URLClassLoader.java:190)
 *         at java.lang.ClassLoader.loadClass(ClassLoader.java:307)
 * http://code.google.com/p/spymemcached/issues/detail?id=155
 * </pre>
 * 
 * @author liqinghua
 * 
 */
public class ScmSpyMemSerializingTranscoder extends SerializingTranscoder {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected Object deserialize(byte[] bytes) {
    final ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
    ObjectInputStream in = null;
    try {
      ByteArrayInputStream bs = new ByteArrayInputStream(bytes);
      in = new ObjectInputStream(bs) {
        @Override
        protected Class<ObjectStreamClass> resolveClass(ObjectStreamClass objectStreamClass)
            throws IOException, ClassNotFoundException {
          try {

            if (objectStreamClass.getClass().isArray()) {
              return (Class<ObjectStreamClass>) super.resolveClass(objectStreamClass);
            } else {
              return (Class<ObjectStreamClass>) currentClassLoader.loadClass(objectStreamClass.getName());
            }
          } catch (Exception e) {
            try {
              return (Class<ObjectStreamClass>) super.resolveClass(objectStreamClass);
            } catch (Exception ex) {
              logger.warn("使用自定义序列化(ScmSpyMemSerializingTranscoder)转换出错,类名字:" + objectStreamClass.getName()
                  + "     objectStreamClass.getClass().isArray():" + objectStreamClass.getClass().isArray());
              return null;
            }

          }
        }
      };
      return in.readObject();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      closeStream(in);
    }
  }

  private static void closeStream(Closeable c) {
    if (c != null) {
      try {
        c.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
