package com.example.shuaijia.numberrunningtextview.numberrunningtextview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.example.shuaijia.numberrunningtextview.utils.UIUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * Created by JiaShuai on 2018/6/12.
 */

public class NumberRunningTextView extends View {

    private String defaultStr = "";
    private Paint mTextPaint;
    private Paint mRectPaint;
    private int backColor = Color.parseColor("#ffffff");
    private int textSize = 24;//文字大小
    private int textColor = Color.parseColor("#000000");
    private int strokeWidth = 1;//矩形线宽
    private int strokeColor = Color.parseColor("#000000");
    private int strokeMarginRightLeft = 4;//marg值
    private int rectWidth = 20;//矩形宽度
    private int rectHeight = 28;//矩形高度
    private float singlePointWith;//单个标点宽度
    private int roundAngle = 2;//圆角角度
    private int viewHeight = 30;//view宽度
    private int totalPointWith;//用来记录每个标点符号站位宽度，带marg值
    private char[] chars;//拆分数组
    private int pointCount;//标点符号个数

    public NumberRunningTextView(Context context) {
        this(context, null);
    }

    public NumberRunningTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberRunningTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //字库
        Typeface fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/DINAlternateBold.ttf");//字库
        mTextPaint = new Paint();
        mTextPaint.setTypeface(fromAsset);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(UIUtils.sp2px(context, textSize));
        mTextPaint.setColor(textColor);

        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setColor(strokeColor);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(strokeWidth);

        viewHeight = UIUtils.dp2px(viewHeight);
        strokeWidth = UIUtils.dp2px(strokeWidth);
        strokeMarginRightLeft = UIUtils.dp2px(strokeMarginRightLeft);
        rectWidth = UIUtils.dp2px(rectWidth);
        rectHeight = UIUtils.dp2px(rectHeight);
        roundAngle = UIUtils.dp2px(roundAngle);
        singlePointWith = mTextPaint.measureText(",");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        re = 0;
        totalPointWith = 0;
        chars = defaultStr.toCharArray();
        int length = defaultStr.split(",").length;
        if (length > 0) {
            pointCount = length - 1;
        }
        float totalTextWith = (pointCount * singlePointWith + (chars.length - pointCount) * rectWidth + (chars.length - 1) * strokeMarginRightLeft - (pointCount) * strokeMarginRightLeft);
        canvas.drawColor(backColor);
        canvas.translate(getWidth() / 2 - totalTextWith / 2, 0);
        for (int i = 0; i < chars.length; i++) {
            drawRect(canvas, i);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(widthSize, viewHeight);
    }

    int re;//re = 标点符号个数*rectWidt，减去原理矩形的站位宽度，

    private void drawRect(Canvas canvas, int i) {

        String str = String.valueOf(chars[i]);
        RectF rectF = new RectF();
        int left = i * rectWidth + i * strokeMarginRightLeft - re + totalPointWith;
        int rigth = (i + 1) * rectWidth + (i) * strokeMarginRightLeft - re + totalPointWith;
        if (",".equals(String.valueOf(chars[i])) || ".".equals(String.valueOf(chars[i]))) {
            left = left - strokeMarginRightLeft / 2;
            re += rectWidth;
            totalPointWith = (int) (totalPointWith + singlePointWith) - strokeMarginRightLeft;

        }
        int top = viewHeight / 2 - rectHeight / 2;
        int bottom = viewHeight / 2 - rectHeight / 2 + rectHeight;
        rectF.set(left, top, rigth, bottom);
        if (",".equals(String.valueOf(chars[i])) || ".".equals(String.valueOf(chars[i]))) {
            int x = left;
            int y = bottom;
            canvas.drawText(str, x, y, mTextPaint);

        } else {
            canvas.drawRoundRect(rectF, roundAngle, roundAngle, mRectPaint);
            int x = left;
            int y = bottom;
            x = (int) (x + rectWidth / 2 - mTextPaint.measureText(str) / 2);
            y = (int) (y - rectHeight / 2 - (mTextPaint.ascent() + mTextPaint.descent()) / 2);
            canvas.drawText(str, x, y, mTextPaint);
        }
    }


    private boolean runWhenChange = true;//是否当内容有改变才使用动画,默认是
    private int duration = 1180;//动画的周期，默认为800ms
    private float minMoney = 0.3f;//显示金额最少要达到这个数字才滚动 默认为0.3

    private DecimalFormat formatter = new DecimalFormat("###,###");// 格式化金额，保留两位小数
    private String preStr;


    private String str = "";//这里不能为null

    public void setNum(String str) {
        this.str = str;
    }

    /**
     * 当view显示在页面上的时候调用，
     * 用途：recycle滑动到该view时启动滚动文字
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setContent(str);
    }

    /**
     * 设置需要滚动的金钱(必须为正数)或整数(必须为正数)的字符串
     *
     * @param str
     */
    public void setContent(String str) {
        //如果是当内容改变的时候才执行滚动动画,判断内容是否有变化
        if (runWhenChange) {
            if (TextUtils.isEmpty(preStr)) {
                //如果上一次的str为空
                preStr = str;
                useAnimByType(str);
                return;
            }

            //如果上一次的str不为空,判断两次内容是否一致
            if (preStr.equals(str)) {
                //如果两次内容一致，则不做处理
                return;
            }

            preStr = str;//如果两次内容不一致，记录最新的str
        }
        useAnimByType(str);
    }

    private void useAnimByType(String str) {
        playMoneyAnim(str);
    }


    /**
     * 播放金钱数字动画的方法
     *
     * @param moneyStr
     */
    public void playMoneyAnim(String moneyStr) {
        String money = moneyStr.replace(",", "").replace("-", "");//如果传入的数字已经是使用逗号格式化过的，或者含有符号,去除逗号和负号
        try {
            BigDecimal bigDecimal = new BigDecimal(money);
            float finalFloat = bigDecimal.floatValue();
            if (finalFloat < minMoney) {
                //如果传入的为0，则直接使用setText()
                defaultStr = moneyStr;
                invalidate();
                return;
            }
            ValueAnimator floatAnimator = ValueAnimator.ofObject(new BigDecimalEvaluator(), new BigDecimal(0), bigDecimal);
            floatAnimator.setDuration(duration);
            floatAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    BigDecimal currentNum = (BigDecimal) animation.getAnimatedValue();
                    // 更新显示的内容
//                    String formatStr = NumberRuningUtils.addComma(str);//三位一个逗号格式的字符串
                    defaultStr = formatter.format(Double.parseDouble(currentNum.toString()));//格式化三位分割
                    invalidate();
                }
            });
            floatAnimator.start();
        } catch (NumberFormatException e) {
            e.printStackTrace();

            defaultStr = moneyStr;
            invalidate();
        }
    }

}
