package com.squareprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author: songbinbin
 * Date: 2017/6/21 14:04
 * Description:
 */

public class MySquareProcessView extends View {
    private static final int MAX_PROGRESS = 100;//最大进度
    private static final int PER_LINE_MAX_PROCESS = 100 / 4;//正方形每条边的最大进度

    private Context mContext;
    private Paint paint;
    private Paint processPaint;
    private Paint textPaint;
    private Canvas canvas;

    private int currentPogress;
    private int strokeColor = Color.BLACK;//正方形默认颜色
    private float strokeWith = 5.0f;//正方形边框默认宽度
    private int progressColor = Color.RED;//进度条默认颜色
    private int textColor = Color.BLUE;//百分比文字默认颜色
    private float textSize = 10.0f;//百分比文字默认字体大小

    public MySquareProcessView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initValue(attrs);
    }


    private void initValue(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SquareProcessView);
        currentPogress = typedArray.getInteger(R.styleable.SquareProcessView_currentPogress, 0);
        strokeColor = typedArray.getColor(R.styleable.SquareProcessView_strokeColor, ContextCompat.getColor(mContext, R.color.colorPrimary));
        strokeWith = typedArray.getDimension(R.styleable.SquareProcessView_strokeWith, strokeWith);
        progressColor = typedArray.getColor(R.styleable.SquareProcessView_progressColor, ContextCompat.getColor(mContext, R.color.colorAccent));
        textColor = typedArray.getColor(R.styleable.SquareProcessView_textColor, ContextCompat.getColor(mContext, R.color.colorAccent));
        textSize = typedArray.getDimension(R.styleable.SquareProcessView_textSize, textSize);
        initPaints();
    }

    private void initPaints() {
        paint = new Paint();
        paint.setColor(strokeColor);
        paint.setStrokeWidth(strokeWith);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        initProcessPaint();
        initTextPaint();
    }

    private void initProcessPaint() {
        processPaint = new Paint();
        processPaint.setColor(progressColor);
        processPaint.setStrokeWidth(strokeWith);
        processPaint.setAntiAlias(true);
        processPaint.setStyle(Paint.Style.STROKE);
    }

    private void initTextPaint() {
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(textSize);

    }

    public void setCurrentPogress(int currentPogress) {
        this.currentPogress = currentPogress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        /*画正方形*/
        drawSquare();
        /*画进度条*/
        drawProcessSquare(currentPogress);
        /*画百分比*/
        drawPercent();
    }

    /**
     * 四条线组成一个方形
     */
    private void drawSquare() {
        drawTopLine();
        drawRightLine();
        drawBottomLine();
        drawLeftLine();
    }


    private void drawTopLine() {
        Path path = new Path();
        /*把坐标移动到坐上下角*/
        path.moveTo(0, 0);
        path.lineTo(canvas.getWidth(), 0);
        canvas.drawPath(path, paint);

    }

    private void drawRightLine() {
        Path path = new Path();
        /*把坐标移动到右上角*/
        path.moveTo(canvas.getWidth(), 0);
        path.lineTo(canvas.getWidth(), canvas.getHeight());
        canvas.drawPath(path, paint);
    }

    private void drawBottomLine() {
        Path path = new Path();
        /*把坐标移动到右下角*/
        path.moveTo(canvas.getWidth(), canvas.getHeight());
        path.lineTo(0, canvas.getHeight());
        canvas.drawPath(path, paint);
    }

    private void drawLeftLine() {
        Path path = new Path();
        /*把坐标移动到坐上下角*/
        path.moveTo(0, 0);
        path.lineTo(0, canvas.getHeight());
        canvas.drawPath(path, paint);
    }


    /**
     * 画进度
     */
    private void drawProcessSquare(int progress) {
        int topProcess = 0;
        int rightProcess = 0;
        int bottomProcess = 0;
        int leftProcess = 0;
        if (progress <= PER_LINE_MAX_PROCESS) {//进度值小于等于一条边的进度   画 top进度线条
            topProcess = progress;
        } else if (progress <= PER_LINE_MAX_PROCESS * 2) {//进度值小于等于两条边的进度 top 进度为线条最大值；right进度值为progress-一条线条的总进度(减去top 的值)
            topProcess = PER_LINE_MAX_PROCESS;
            rightProcess = progress - PER_LINE_MAX_PROCESS;
        } else if (progress <= PER_LINE_MAX_PROCESS * 3) {//进度值小于等于三条边的进度 top 进度为线条最大值;right 进度为线条最大值；bottom进度值为progress-两条线条的总进度(减去top与right的值)
            topProcess = PER_LINE_MAX_PROCESS;
            rightProcess = PER_LINE_MAX_PROCESS;
            bottomProcess = progress - PER_LINE_MAX_PROCESS * 2;
        } else if (progress <= MAX_PROGRESS) {//进度值小于等于四条边的进度 top 进度为线条最大值;right 进度为线条最大值;bottom 进度为线条最大值；left进度值为progress-三条线条的总进度(减去top、right、bottom的值)
            topProcess = PER_LINE_MAX_PROCESS;
            rightProcess = PER_LINE_MAX_PROCESS;
            bottomProcess = PER_LINE_MAX_PROCESS;
            leftProcess = progress - PER_LINE_MAX_PROCESS * 3;
        }
        drawProgressTopLine(topProcess);
        drawProgressRightLine(rightProcess);
        drawProgressBottomLine(bottomProcess);
        drawProgressLeftLine(leftProcess);
    }

    private void drawProgressTopLine(int progress) {
        Path path = new Path();
        /*把坐标移动到左上角*/
        path.moveTo(0, 0);
        path.lineTo(canvas.getWidth() / PER_LINE_MAX_PROCESS * progress, 0);
        canvas.drawPath(path, processPaint);
    }

    private void drawProgressRightLine(int progress) {
        Path path = new Path();
        /*把坐标移动到右上角*/
        path.moveTo(canvas.getWidth(), 0);
        path.lineTo(canvas.getWidth(), canvas.getHeight() / PER_LINE_MAX_PROCESS * progress);
        canvas.drawPath(path, processPaint);
    }

    /*比较特殊  lineTo是从左往右画的 进度是从右往左走的  所以取 当前line 的进度 减去总 line的总长度的 绝对值*/
    private void drawProgressBottomLine(int progress) {
        Path path = new Path();
        /*把坐标移动到右下角*/
        path.moveTo(canvas.getWidth(), canvas.getHeight());
        path.lineTo(canvas.getHeight() / PER_LINE_MAX_PROCESS * Math.abs(progress - PER_LINE_MAX_PROCESS), canvas.getHeight());
        canvas.drawPath(path, processPaint);
    }

    private void drawProgressLeftLine(int progress) {
        Path path = new Path();
        /*把坐标移动到左下角*/
        path.moveTo(0, canvas.getHeight());
        path.lineTo(0, canvas.getHeight() / PER_LINE_MAX_PROCESS * Math.abs(progress - PER_LINE_MAX_PROCESS));
        canvas.drawPath(path, processPaint);
    }

    private void drawPercent() {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        canvas.drawText(currentPogress + "%", width / 2, height / 2 - top / 2 - bottom / 2, textPaint);
    }
}
