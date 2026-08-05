package com.example.fittrack.frontend.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleLineChartView extends View {
    private List<Float> dataPoints = new ArrayList<>();
    private Paint linePaint;
    private Paint fillPaint;
    private Paint pointPaint;
    private Path path;
    private Path fillPath;

    public SimpleLineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.parseColor("#1A56A0"));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(6f);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(Color.parseColor("#331A56A0")); // Semi-transparent
        fillPaint.setStyle(Paint.Style.FILL);

        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setColor(Color.parseColor("#1A56A0"));
        pointPaint.setStyle(Paint.Style.FILL);

        path = new Path();
        fillPath = new Path();
    }

    public void setDataPoints(List<Float> points) {
        this.dataPoints = points;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataPoints == null || dataPoints.size() < 2) {
            return;
        }

        float width = getWidth();
        float height = getHeight();
        float padding = 20f;

        float maxVal = Collections.max(dataPoints);
        float minVal = Collections.min(dataPoints);
        
        // Add some margin to min/max
        float range = maxVal - minVal;
        if (range == 0) {
            range = maxVal * 0.2f; // If all points are the same
        }
        maxVal += range * 0.2f;
        minVal -= range * 0.2f;
        range = maxVal - minVal;

        float xStep = (width - 2 * padding) / (dataPoints.size() - 1);

        path.reset();
        fillPath.reset();

        float startX = padding;
        float startY = height - padding - ((dataPoints.get(0) - minVal) / range) * (height - 2 * padding);
        
        path.moveTo(startX, startY);
        fillPath.moveTo(startX, height);
        fillPath.lineTo(startX, startY);

        for (int i = 0; i < dataPoints.size(); i++) {
            float x = padding + i * xStep;
            float y = height - padding - ((dataPoints.get(i) - minVal) / range) * (height - 2 * padding);
            
            if (i > 0) {
                path.lineTo(x, y);
                fillPath.lineTo(x, y);
            }
        }

        fillPath.lineTo(padding + (dataPoints.size() - 1) * xStep, height);
        fillPath.close();

        canvas.drawPath(fillPath, fillPaint);
        canvas.drawPath(path, linePaint);

        for (int i = 0; i < dataPoints.size(); i++) {
            float x = padding + i * xStep;
            float y = height - padding - ((dataPoints.get(i) - minVal) / range) * (height - 2 * padding);
            canvas.drawCircle(x, y, 8f, pointPaint);
        }
    }
}
