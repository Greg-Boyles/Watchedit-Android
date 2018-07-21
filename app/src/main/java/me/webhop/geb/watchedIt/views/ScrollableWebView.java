package me.webhop.geb.watchedIt.views;

import android.content.Context;
import android.util.AttributeSet;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Greg on 08/03/2017.
 */
public class ScrollableWebView extends android.webkit.WebView {

    private OnScrollChangedCallback mOnScrollChangedCallback;
    private final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledFuture = null;

    public ScrollableWebView(final Context context)
    {
        super(context);
    }

    public ScrollableWebView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ScrollableWebView(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l, t);

            if(scheduledFuture != null) {
                scheduledFuture.cancel(false);
            }

            Runnable task = new Runnable() {
                public void run() {
                    mOnScrollChangedCallback.onFinishedScroll(l, t);
                }
            };
            scheduledFuture = worker.schedule(task, 1, TimeUnit.SECONDS);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback()
    {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback)
    {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    public static interface OnScrollChangedCallback
    {
        public void onScroll(int l, int t);
        public void onFinishedScroll(int l, int t);
    }
}
