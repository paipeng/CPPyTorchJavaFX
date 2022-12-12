package com.paipeng.cppytorch.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;

public class CommonUtil {
    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    public static File[] getFiles(String path, String[] filters) {
        logger.debug("getFiles: " + path);
        File dir = new File(path);
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (filters != null) {
                    boolean found = false;
                    for (String filter : filters) {
                        found = name.toLowerCase().endsWith(filter);
                        if (found) {
                            break;
                        }
                    }
                    return found;
                } else {
                    return true;
                }
            }
        });
        return files;
    }

    public static String[] getSubFolders(String path) {
        logger.debug("getSubFolders: " + path);
        File file = new File(path);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        return directories;
    }
}
