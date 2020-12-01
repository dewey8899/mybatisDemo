package test.threadPool;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author dewey.du
 * @create at  2020/12/1 0001 下午 21:18
 */

public class ThreadPoolFutureTaskMutiCompute {
    public static void main(String args[]){

        List<DataVo> list = new ArrayList<>();
        ThreadPoolFutureTaskMutiCompute threadPoolFutureTaskMutiCompute = new ThreadPoolFutureTaskMutiCompute();
        // 创建任务集合
        List<FutureTask<DataVo>> taskList = new ArrayList<>();
        // 创建线程池
        ExecutorService exec = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            // 传入Callable对象创建FutureTask对象
            DataVo vo = new DataVo();
            FutureTask<DataVo> ft = new FutureTask<>(threadPoolFutureTaskMutiCompute.new ComputeTask(vo, "" + i));
            taskList.add(ft);
            // 提交给线程池执行任务，也可以通过exec.invokeAll(taskList)一次性提交所有任务;
            exec.submit(ft);
        }
        System.out.println("所有计算任务提交完毕, 主线程接着干其他事情！");
        // 开始统计各计算线程计算结果
        for (FutureTask<DataVo> ft : taskList) {
            try {
                //FutureTask的get方法会自动阻塞,直到获取计算结果为止
                list.add(ft.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // 关闭线程池
        exec.shutdown();
        System.out.println("多任务计算后的总结果是:" + list.get(list.size()-1).getAge());
    }

    private class ComputeTask implements Callable<DataVo> {

        private DataVo result;
        private String taskName = "";

        public ComputeTask(DataVo iniResult, String taskName) {
            result = iniResult;
            this.taskName = taskName;
            System.out.println("生成子线程计算任务: " + taskName);
        }
        @Override
        public DataVo call() throws Exception {
            for (int i = 0; i <= 100; i++) {
                result.age = i;
            }
            // 休眠5秒钟，观察主线程行为，预期的结果是主线程会继续执行，到要取得FutureTask的结果是等待直至完成。
            Thread.sleep(5000);
            System.out.println("子线程计算任务: " + taskName + " 执行完成!");
            return result;
        }
    }
    @Data
    private static class DataVo {
        private String name;
        private Integer age;
    }
}
