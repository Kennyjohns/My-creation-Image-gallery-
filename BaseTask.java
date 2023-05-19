package imgconvertt.compressimg.convrtformat.utils;

public abstract class BaseTask<R> implements TaskRunner.CustomCallable<R> {
    @Override
    public void setUiForLoading() {

    }

    @Override
    public void setDataAfterLoading(R result) {

    }

    @Override
    public R call() {
        return null;
    }

}