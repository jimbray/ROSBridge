package xyz.jimbray.rosbridge.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class CircleButton extends android.support.v7.widget.AppCompatButton {

    private Paint mPaint;

    private int mViewWidth, mViewHeight;


    public CircleButton(Context context) {
        this(context, null);
    }

    public CircleButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getViewSize(widthMeasureSpec);
        int height = getViewSize(heightMeasureSpec);

        int finalSize = Math.min(width, height);

        setMeasuredDimension(finalSize, finalSize);

    }

    private int getViewSize(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch(mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                size = 250;
                break;

            case MeasureSpec.EXACTLY:
                break;
        }

        return size;


    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//
//        mViewWidth = w;
//        mViewHeight = h;
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        canvas.translate(mViewWidth/2, mViewHeight/2);
//
//        canvas.drawCircle(0, 0, mViewWidth/2, mPaint);
//    }
}
