package com.paipeng.cppytorch.util;

import java.io.File;
import java.io.FilenameFilter;

public class CommonUtil {
    public static File[] getFiles(String path, String[] filters) {
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
}
