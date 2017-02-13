package com.example.zhaojungao.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * 自定义控件之继承view
 * 自定义了控件属性
 * Created by aurora_bessie on 2017/2/13.
 */
public class CustomTextView extends View implements View.OnClickListener{

    private int mCount=100;
    private Paint mPaint;
    private Rect mRect;
    private int mTextSize;
    private String mTextString;
    private int mTextColor;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
        setOnClickListener(this);
//        this(context, attrs, com.android.internal.R.attr.textViewStyle);        
    }

    //该含有三个参数的构造函数系统是不调用的，需要view显示调用
    //arg3:这里的默认的Style是指它在当前Application或Activity所用的Theme中的默认Style
    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("CustomTextView", "==========================================");
        /**获取自定义的样式属性*/
        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView, defStyleAttr, 0);
        final int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            final int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.CustomTextView_customText:
                    mTextString = typedArray.getString(index);
                    break;
                case R.styleable.CustomTextView_customTextColor:
                    //设置默认颜色为黑色
                    mTextColor = typedArray.getColor(index, Color.BLACK);
                    break;
                case R.styleable.CustomTextView_customTextSize:                          //将数值统一转换成px  将16dp的值通过value * metrics.density转换成多少px
                    mTextSize = typedArray.getDimensionPixelSize(index, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();
        /**
         * 绘制文本宽，高
         * (mPaint.setTextSize(mTextSize);
         * mPaint.getTextBounds(mTextString, 0, mTextString.length(), mRect);//获取文字的宽高
         * 还是设置一下为好，因为不论view宽高设置成哪种模式，都统一了文本大小为15sp，宽高也确定)
         */
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mRect = new Rect();
        mPaint.getTextBounds(mTextString, 0, mTextString.length(), mRect);//获取文字的宽高
    }

//    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        Log.e("四个参数", "=================================");
//    }

    @Override
    public void onClick(View v) {
        mCount++;
        postInvalidate();
    }
    
    /***
     * widthMeasureSpec：宽详细测量值，heightMeasureSpec：高详细测量值，决定View的大小只需要两个值。
     * 也可以把详细测量值理解为视图View想要的大小说明（想要的未必就是最终大小）。
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    //当在xml文件中设置控件宽高为wrap_content时，需要重写该方法明确具体宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("onMeasure", "==========================================");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY)//对应match_parent和具体数值两种
        {
            width = widthSize;//view宽度用系统的测量值
        } else {//wrap_content  默认设置为wrap_content时，大小是由父控件决定，所以需要重新设置
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mTextString, 0, mTextString.length(), mRect);
            float textWidth = mRect.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());//view宽度用计算的值
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mTextString, 0, mTextString.length(), mRect);
            float textHeight = mRect.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }

    //控制view的位置。
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e("onLayout", "==========================================");
    }

    //用于绘制view，系统会频繁调用该方法，所以不要做耗时操作
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("onDraw", "==========================================");
        mPaint.setColor(Color.YELLOW);//画笔置黄色
        //绘制一个矩形
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(mTextColor);//画笔置红色
        //绘制文本  
        canvas.drawText(String.valueOf(mCount), getWidth() / 2 - mRect.width() / 2, getHeight() / 2 + mRect.height() / 2, mPaint);
    }
}
