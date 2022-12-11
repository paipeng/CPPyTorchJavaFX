package com.paipeng.cppytorch.util;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AsynchronTaskUtil {
    public static Logger logger = LoggerFactory.getLogger(AsynchronTaskUtil.class);
    private static boolean running;
    public static class AsynchronSleepTask<E> extends Task<List<E>> {

        private final AsynchronTaskInterface asynchronTaskInterface;

        public AsynchronSleepTask(AsynchronTaskInterface asynchronTaskInterface) {
            updateTitle("AsynchronSleepTask");
            this.asynchronTaskInterface = asynchronTaskInterface;

        }

        @Override
        protected List<E> call() {
            logger.trace("call");
            asynchronTaskInterface.doTask();
            logger.trace("call end");
            return null;
        }

        @Override
        protected void running() {
            logger.trace("Sorting task is running...");
        }

        @Override
        protected void succeeded() {
            logger.trace("Sorting task successful.");
            running = false;
        }

    }

    public static boolean startTask(AsynchronTaskInterface asynchronTaskInterface) {
        if (!running) {
            running = true;
            logger.trace("startTask");
            AsynchronSleepTask<Object> task = new AsynchronSleepTask<>(asynchronTaskInterface);
            task.setOnSucceeded(event -> asynchronTaskInterface.taskEnd());

            Thread t = new Thread(task, "wait-sleep-thread");
            t.setDaemon(true);
            t.setPriority(2);
            t.start();
            return true;
        } else {
            return false;
        }
    }

    public interface AsynchronTaskInterface {
        void doTask();
        void taskEnd();
    }


}
