package com.dev_training.imos.imosbleexample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by i-MOS on 2014/08/22.
 */
public class GraphView extends View {

    private  ValueWithTimestampList mValues;

    public GraphView(Context context) {
        super(context);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GraphView(Context activity, ValueWithTimestampList sampleData) {
        super(activity);
        mValues = sampleData;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 余白
        int contentPadding = 48;
        // x軸の描画
        Paint axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        // X軸
        canvas.drawLine(
                getWidth() - contentPadding, getHeight() - contentPadding,
                contentPadding, getHeight() - contentPadding, axisPaint );
        // Y軸
        canvas.drawLine(contentPadding, contentPadding,
                contentPadding, getHeight() - contentPadding, axisPaint );



        // サンプルの値と時刻の最小値と最大値の取得
        long minTime = Long.MAX_VALUE, maxTime = Long.MIN_VALUE;
        float minValue = Float.MAX_VALUE, maxValue = Float.MIN_VALUE;
        for (ValueWithTimestamp value: mValues){
            if (minTime > value.timestamp) minTime = value.timestamp;
            if (minValue > value.value) minValue = value.value;
            if (maxTime < value.timestamp) maxTime = value.timestamp;
            if (maxValue < value.value) maxValue = value.value;
        }

        Paint linePaint = new Paint();
        linePaint.setColor(Color.RED);
        float prevX = contentPadding, prevY = getHeight() - contentPadding;
        for (ValueWithTimestamp valueWithTimestamp: mValues) {
            // X座標の算出
            float xRatio = (float) (valueWithTimestamp.timestamp - minTime)
                    / (float) (maxTime - minTime);
            float x = contentPadding
                    + xRatio * (getWidth() - 2 * contentPadding); // グラフコンテンツの幅
            // Y座標の算出
            float yRatio = (valueWithTimestamp.value - minValue) / (maxValue - minValue);
            float y = getHeight() - contentPadding
                    - (float) (getHeight() - 2 * contentPadding) * yRatio;

            canvas.drawLine(prevX, prevY, x, y, linePaint);
            prevX = x;
            prevY = y;
        }
    }
}
