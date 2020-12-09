package org.example.Utils;

import java.net.URL;

public abstract class ReflectUtil {
    public ReflectUtil() {
    }

    public static URL getResource(String name) {
        return getResource(name, (ClassLoader)null);
    }

    public static URL getResource(String name, ClassLoader customClassLoader) {
        URL resourceURL = null;
        if (customClassLoader != null) {
            resourceURL = customClassLoader.getResource(name);
        }

        if (resourceURL == null) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader != null) {
                resourceURL = classLoader.getResource(name);
            }

            if (resourceURL == null) {
                classLoader = ReflectUtil.class.getClassLoader();
                resourceURL = classLoader.getResource(name);
            }
        }

        return resourceURL;
    }
}