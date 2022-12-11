package com.paipeng.cppytorch.util;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SleepAsynchronTaskUtil {
    public static Logger logger = LoggerFactory.getLogger(SleepAsynchronTaskUtil.class);

    public static class AsynchronSleepTask<E> extends Task<List<E>> {

        private final long timeout;

        public AsynchronSleepTask(long timeout) {
            updateTitle("AsynchronSleepTask");
            this.timeout = timeout;
        }

        @Override
        protected List<E> call() throws Exception {
            Thread.sleep(timeout);


            return null;
        }

        @Override
        protected void running() {
            logger.trace("Sorting task is running...");
        }

        @Override
        protected void succeeded() {
            logger.trace("Sorting task successful.");
        }

    }

    public static void startTask(long timeout, AsynchronTaskInterface asynchronTaskInterface) {
        AsynchronSleepTask<Integer> task = new AsynchronSleepTask<>(timeout);
        task.setOnSucceeded(event -> asynchronTaskInterface.taskEnd()
        );

        Thread t = new Thread(task, "wait-sleep-thread");
        t.setDaemon(true);
        t.start();

    }

    public interface AsynchronTaskInterface {
        void taskEnd();
    }


}
