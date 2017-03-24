package com.keydak.log;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by admin on 2017/3/22.
 */
public class LogUtil {
        public static void trace(Class clazz, String msg) {
            Logger logger = (Logger) LoggerFactory.getLogger(clazz);
            logger.trace(msg);
        }

        public static void trace(Class clazz, String msg, Throwable t) {
            Logger logger = (Logger) LoggerFactory.getLogger(clazz);
            logger.trace(msg, t);
        }

        public static void debug(Class clazz, String msg) {
            Logger logger = (Logger) LoggerFactory.getLogger(clazz);
            logger.debug(msg);
        }

        public static void debug(Class clazz, String msg, Throwable t) {
            Logger logger = (Logger) LoggerFactory.getLogger(clazz);
            logger.debug(msg, t);
        }

        public static void info(Class clazz, String msg) {
            Logger logger = (Logger) LoggerFactory.getLogger(clazz);
            logger.info(msg);
        }

        public static void info(Class clazz, String msg, Throwable t) {
            Logger logger = (Logger) LoggerFactory.getLogger(clazz);
            logger.info(msg, t);
        }

        public void warn(Class clazz, String msg) {
            Logger logger = (Logger) LoggerFactory.getLogger(clazz);
            logger.warn(msg);
        }

        public static void warn(Class clazz, String msg, Throwable t) {
            Logger logger = (Logger) LoggerFactory.getLogger(clazz);
            logger.warn(msg, t);
        }

        public static void error(Class clazz, String msg) {
            Logger logger = (Logger) LoggerFactory.getLogger(clazz);
            logger.error(msg);
        }

        public static void error(Class clazz, String msg, Throwable t) {
            Logger logger = (Logger) LoggerFactory.getLogger(clazz);
            logger.error(msg, t);
        }
}

