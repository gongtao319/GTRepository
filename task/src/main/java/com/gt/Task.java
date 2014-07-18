package com.gt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * @author gongtao
 * @date 2014年7月18日
 */
public abstract class Task<T extends TaskResult> extends RecursiveTask<List<T>> {

    /**
     * 序列化
     */
    private static final long serialVersionUID = -4831319852383255317L;

    /**
     * 是否子任务
     */
    private boolean isSubTask = false;

    /**
     * 子任务列表
     */
    private List<Task<T>> subTasks = new ArrayList<Task<T>>();

    /**
     * 设置是否子任务
     * 
     * @param isSubTask 是否子任务
     */
    private void setSubTask(boolean isSubTask) {
        this.isSubTask = isSubTask;
    }

    /**
     * 添加子任务
     * 
     * @param task 子任务
     */
    public void addSubTask(Task<T> task) {
        task.setSubTask(true);
        subTasks.add(task);
    }

    @Override
    protected List<T> compute() {

        if (isSubTask) {
            List<T> result = null;
            T handleresult = handleTask();
            if (handleresult != null) {
                result = new ArrayList<T>(1);
                result.add(handleresult);
            }
            return result;
        } else {
            List<T> result = new ArrayList<T>();
            for (Task<T> task : invokeAll(subTasks)) {
                List<T> join = task.join();
                if (CollectionUtils.isNotEmpty(join)) {
                    result.addAll(join);
                }
            }
            return result;
        }
    }

    /**
     * 处理任务
     * 
     * @return 任务处理结果
     */
    protected abstract T handleTask();

}
