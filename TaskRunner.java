package imgconvertt.compressimg.convrtformat.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskRunner {

    private final Executor executor = Executors.newSingleThreadExecutor(); // change according to your requirements
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback<R> {
        void onComplete(R result);
    }


    public <R> void executeAsync(Callable<R> callable, Callback<R> callback) {

        executor.execute(() -> {
            try {
                final R result = callable.call();
                handler.post(() -> {
                    callback.onComplete(result);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    //---------
    /*interface for recyclerview loading*/

    public interface CustomCallable<R> extends Callable<R> {
        void setDataAfterLoading(R result);
        void setUiForLoading();
    }

    public <R> void executeAsync(CustomCallable<R> callable) {

        try {
            callable.setUiForLoading();
            executor.execute(new RunnableTask<R>(handler, callable));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static class RunnableTask<R> implements Runnable{
        private final Handler handler;
        private final CustomCallable<R> callable;

        public RunnableTask(Handler handler, CustomCallable<R> callable) {
            this.handler = handler;
            this.callable = callable;

        }

        @Override
        public void run() {
            try {
                final R result = callable.call();
                handler.post(new RunnableTaskForHandler(callable, result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class RunnableTaskForHandler<R> implements Runnable{
        private final CustomCallable<R> callable;
        private final R result;

        public RunnableTaskForHandler(CustomCallable<R> callable, R result) {
            this.callable = callable;
            this.result = result;
        }

        @Override
        public void run() {
             callable.setDataAfterLoading(result);
        }
    }
}