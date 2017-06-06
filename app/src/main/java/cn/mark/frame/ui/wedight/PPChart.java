package cn.mark.frame.ui.wedight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.List;

import cn.mark.frame.R;


/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-24 16:56
 */
public class PPChart extends View {
    Context mContext;

    Paint mPaint;
    private int mXDown, mLastX;
    //最短滑动距离
    int a = 0;


    float startX = 0;
    float lastStartX = 0;//抬起手指后，当前控件最左边X的坐标
    float cellCountW = 9.5f;//一个屏幕的宽度会显示的格子数
    float cellCountH = 12.5f;//整个控件的高度会显示的格子数

    float cellH, cellW;

    float topPadding = 0.25f;

    PathEffect mEffect = new CornerPathEffect(20);//平滑过渡的角度

    int state = -100;
    int lineWidth;

    public void setData(List<DataObject> data) {
        this.data = data;
        state = -100;
        postInvalidate();
    }

    List<DataObject> data;

    public PPChart(Context context) {
        super(context);
        mContext = context;
    }

    public PPChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        a = DPUnitUtil.px2dip(context, ViewConfiguration.get(context).getScaledDoubleTapSlop());
        setClickable(true);
        lineWidth = DPUnitUtil.dip2px(mContext, 1);
    }


    public PPChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //线的颜色
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        cellH = getHeight() / cellCountH;
        cellW = getWidth() / cellCountW;
        //画底部背景
        mPaint.setColor(0xff44b391);
        canvas.drawRect(0, (((int) cellCountH - 1) + topPadding) * cellH, getWidth(), cellCountH * cellH, mPaint);

        if (data == null || data.size() == 0) {
            return;
        }

        DrawAbscissaLines(canvas);
        DrawOrdinate(canvas);
        //------------到此背景结束---------------

        canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        DrawDataBackground(canvas);
        canvas.restore();

        canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        DrawDataLine(canvas);
        canvas.restore();

        canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        DrawAbscissa(canvas);
        canvas.restore();

        showPop(canvas);

        if (state == -100) {
            gotoEnd();
        }

    }

    //画横坐标
    private void DrawOrdinate(Canvas canvas) {
        mPaint.reset();

        float i = 0.5f;
        for (DataObject tmp : data) {
            mPaint.setColor(0xffb4e1d3);
            mPaint.setTextSize(getWidth() / cellCountW / 3.2f);
            DataObject tmp2 = getDataByX(mLastX);

            //选中的那一项需要加深
            if (tmp2 != null && tmp2.getHappenTime().equals(tmp.getHappenTime()) && state == MotionEvent.ACTION_UP && Math.abs(mLastX - mXDown) < a) {
                mPaint.setColor(0xffffffff);
            } else {
                mPaint.setColor(0xffb4e1d3);
            }
            String str1 = tmp.getHappenTime().split("-")[0];
            canvas.drawText(str1,
                    startX + cellW * i - mPaint.measureText(str1) / 2,
                    (((int) cellCountH - 1) + topPadding + cellCountH) / 2 * cellH,
                    mPaint);
            mPaint.setTextSize(getWidth() / cellCountW / 3.5f);
            String str2 = tmp.getHappenTime().split("-")[1] + "." + tmp.getHappenTime().split("-")[2];
            canvas.drawText(str2,
                    startX + cellW * i - mPaint.measureText(str2) / 2,
                    (((int) cellCountH - 1) + topPadding + cellCountH) / 2 * cellH - 1.5f * (mPaint.ascent() + mPaint.descent()),
                    mPaint);

            //画背景竖线
            mPaint.setColor(0xff92dac4);
            canvas.drawLine(startX + cellW * i,
                    topPadding * cellH,
                    startX + cellW * i,
                    (topPadding + 10.5f) * cellH,
                    mPaint);

            i++;
        }

        mPaint.setColor(0xffb4e1d3);
        mPaint.setTextSize(getWidth() / cellCountW / 3f);
        canvas.drawText("end",
                startX + cellW * i - mPaint.measureText("end") / 2,
                (((int) cellCountH - 1) + topPadding + cellCountH) / 2 * cellH - (mPaint.ascent() + mPaint.descent()) / 2,
                mPaint);
    }

    //画纵坐标
    public void DrawAbscissaLines(Canvas canvas) {

        mPaint.setColor(0xff92dac4);

        //画背景横线
        canvas.drawLine(0,
                topPadding * cellH,
                cellW * 9.5f,
                topPadding * cellH,
                mPaint);
        canvas.drawLine(0,
                (topPadding + 1) * cellH,
                cellW * 9.5f,
                (topPadding + 1) * cellH,
                mPaint);
        canvas.drawLine(0,
                (topPadding + 2) * cellH,
                cellW * 9.5f,
                (topPadding + 2) * cellH,
                mPaint);
        canvas.drawLine(0,
                (topPadding + 3) * cellH,
                cellW * 9.5f,
                (topPadding + 3) * cellH,
                mPaint);
        canvas.drawLine(0,
                (topPadding + 4) * cellH,
                cellW * 9.5f,
                (topPadding + 4) * cellH,
                mPaint);
        canvas.drawLine(0,
                (topPadding + 5) * cellH,
                cellW * 9.5f,
                (topPadding + 5) * cellH,
                mPaint);
        canvas.drawLine(0,
                (topPadding + 6) * cellH,
                cellW * 9.5f,
                (topPadding + 6) * cellH,
                mPaint);
        canvas.drawLine(0,
                (topPadding + 7) * cellH,
                cellW * 9.5f,
                (topPadding + 7) * cellH,
                mPaint);
        canvas.drawLine(0,
                (topPadding + 8) * cellH,
                cellW * 9.5f,
                (topPadding + 8) * cellH,
                mPaint);
        canvas.drawLine(0,
                (topPadding + 9) * cellH,
                cellW * 9.5f,
                (topPadding + 9) * cellH,
                mPaint);
        canvas.drawLine(0,
                (topPadding + 10) * cellH,
                cellW * 9.5f,
                (topPadding + 10) * cellH,
                mPaint);
    }

    //画纵坐标
    public void DrawAbscissa(Canvas canvas) {
        mPaint.reset();
        mPaint.setColor(mContext.getResources().getColor(R.color.colorPrimary));
        //画纵坐标背景9.51 = 10 - 0.5（i 的起步）+ 0.01（把最后一条线露出来）
        canvas.drawRect(cellW * ((int) cellCountW - 0.5f + 0.01f), 0, cellW * ((int) cellCountW + 1), 11.2f * cellH, mPaint);

        mPaint.setColor(0xffb6e6d7);
        mPaint.setTextSize(getWidth() / cellCountW / 3);


        canvas.drawText("100%",
                cellW * (int) cellCountW - mPaint.measureText("100%") / 2,
                topPadding * cellH - (mPaint.ascent() + mPaint.descent()) / 2,
                mPaint);

        canvas.drawText("80%",
                cellW * (int) cellCountW - mPaint.measureText("80%") / 2,
                (topPadding + 2) * cellH - (mPaint.ascent() + mPaint.descent()) / 2,
                mPaint);

        canvas.drawText("60%",
                cellW * (int) cellCountW - mPaint.measureText("60%") / 2,
                (topPadding + 4) * cellH - (mPaint.ascent() + mPaint.descent()) / 2,
                mPaint);

        canvas.drawText("40%",
                cellW * (int) cellCountW - mPaint.measureText("40%") / 2,
                (topPadding + 6) * cellH - (mPaint.ascent() + mPaint.descent()) / 2,
                mPaint);

        canvas.drawText("20%",
                cellW * (int) cellCountW - mPaint.measureText("20%") / 2,
                (topPadding + 8) * cellH - (mPaint.ascent() + mPaint.descent()) / 2,
                mPaint);

        canvas.drawText("0%",
                cellW * (int) cellCountW - mPaint.measureText("0%") / 2,
                (topPadding + 10) * cellH - (mPaint.ascent() + mPaint.descent()) / 2,
                mPaint);

    }

    //画渐变背景
    private void DrawDataBackground(Canvas canvas) {
        if (data == null || data.size() == 0) {
            return;
        }
        LinearGradient lg = new LinearGradient(getWidth() / 2, topPadding * cellH, getWidth() / 2, (topPadding + 10) * cellH, 0xaaffffff, 0xaa61ccab, Shader.TileMode.CLAMP);
        mPaint.setShader(lg);

        float i = 0.5f;
        Path path = new Path();

        //起点和终点要多画2次，防止圆角出现
        path.moveTo(startX + cellW * i, (topPadding + 10) * cellH);
        path.lineTo(startX + cellW * i, (topPadding + 10) * cellH);
        path.lineTo(startX + cellW * i, getHByValue(data.get(0).getNum()));
        for (DataObject tmp : data) {
            path.lineTo(startX + cellW * i, getHByValue(tmp.getNum()));
            i++;
        }
        path.lineTo(startX + cellW * (i -1), getHByValue(data.get(data.size()-1).getNum()));
        path.lineTo(startX + cellW * (i - 1), (topPadding + 10) * cellH -1);
        path.lineTo(startX + cellW * (i - 1), (topPadding + 10) * cellH);
        path.close();
        mPaint.setPathEffect(mEffect);
        canvas.drawPath(path, mPaint);

    }

    //画数据线
    public void DrawDataLine(Canvas canvas) {

        float i = 0.5f;
        mPaint.reset();
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setColor(0xffffffff);

        Path path = new Path();
        path.moveTo(startX + cellW * i -1, getHByValue(data.get(0).getNum()));
        path.lineTo(startX + cellW * i, getHByValue(data.get(0).getNum()));
        for (DataObject tmp : data) {
            path.lineTo(startX + cellW * i, getHByValue(tmp.getNum()));
            i++;
        }
        path.lineTo(startX + cellW * (i -1), getHByValue(data.get(data.size()-1).getNum()));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(mEffect);
        canvas.drawPath(path, mPaint);

    }

    //显示数据气泡
    private void showPop(Canvas canvas) {
        //点击了
        if (state == MotionEvent.ACTION_UP && Math.abs(mLastX - mXDown) < a) {
            DataObject data = getDataByX(mLastX);
            if (data == null) {
                return;
            }
            initPaint();
            // 选中的线
            mPaint.setColor(0xaaffffff);
            canvas.drawLine(getXBykey(data.getHappenTime()), getHByValue(data.getNum()), getXBykey(data.getHappenTime()), (topPadding + 10f) * cellH, mPaint);
            //画气泡背景
            mPaint.setColor(0xffffffff);
            mPaint.setTextSize(getWidth() / cellCountW / 3f);
            Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
            RectF r;

            //气泡距离顶点有0.5个格子高度的距离，气泡的高度是文字高度的1.5倍。宽度是文字宽度的1.6倍（0.8+0.8）
            float left = getXBykey(data.getHappenTime()) - mPaint.measureText(data.getNum() + "%") * 0.8f;
            if(left < 0 ){
                left = 0;
            }
            float right = left + 2 * mPaint.measureText(data.getNum() + "%") * 0.8f;
            if (data.getNum() >= 10) {
                r = new RectF(left,
                        getHByValue(data.getNum()) + 0.5f * cellH,
                        right,
                        getHByValue(data.getNum()) + 0.5f * cellH + 1.5f * (fontMetrics.bottom - fontMetrics.top));
            } else {
                r = new RectF(left,
                        getHByValue(data.getNum()) - 0.5f * cellH - 1.5f * (fontMetrics.bottom - fontMetrics.top),
                        right,
                        getHByValue(data.getNum()) - 0.5f * cellH);
            }
            //画气泡上的文字
            canvas.drawRoundRect(r, 90, 90, mPaint);
            mPaint.setColor(0xff414141);

            float baseline = (r.bottom + r.top - fontMetrics.bottom - fontMetrics.top) / 2;

            canvas.drawText(data.getNum() + "%",
                    (r.left+ r.right)/2 - mPaint.measureText(data.getNum() + "%") / 2f,
                    baseline, mPaint);

            //画线上的圆
            mPaint.setStrokeWidth(lineWidth * 2);
            mPaint.setColor(0xff49c29d);
            canvas.drawCircle(getXBykey(data.getHappenTime()), getHByValue(data.getNum()), lineWidth * 5, mPaint);
            mPaint.setColor(0xffffffff);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(getXBykey(data.getHappenTime()), getHByValue(data.getNum()), lineWidth * 5, mPaint);


            mPaint.setStrokeWidth(lineWidth);

        }
    }

    //触摸处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (data == null || data.size() == 0) {
            return super.onTouchEvent(event);
        }
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mXDown = (int) event.getRawX();
                state = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastX = (int) event.getRawX();

                if (Math.abs(lastStartX - mXDown) < a) {
                    break;
                }

                //滑动限制
                if (lastStartX + mLastX - mXDown > 0.5f * cellW || lastStartX + mLastX - mXDown + cellW * (data.size() + 0.5f) < cellW * (cellCountW - 1)) {
                    break;
                }
                state = MotionEvent.ACTION_MOVE;
                startX = lastStartX + mLastX - mXDown;
                postInvalidate();
                break;

            case MotionEvent.ACTION_UP:
                // 抬起
                lastStartX = startX;
                state = MotionEvent.ACTION_UP;
                postInvalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    //通过坐标，获得附近的点
    private DataObject getDataByX(int pointX) {
        float i = 0.5f;
        DataObject result = null;
        for (DataObject tmp : data) {
            float x = startX + cellW * i;
            if (Math.abs(x - pointX) < cellW / 2) {
                result = tmp;
                return result;
            }
            i++;
        }
        return result;
    }

    private float getHByValue(float value) {
        return (topPadding + 10) * cellH - (cellH * 10) * value / 100;
    }

    //通过横坐标文字获取该点的X坐标
    private float getXBykey(String key) {
        float i = 0.5f;
        for (DataObject tmp : data) {

            if (tmp.getHappenTime().equals(key)) {
                return startX + cellW * i;
            }
            i++;
        }
        return 0;
    }


    //显示最右边的最新数据
    public void gotoEnd() {
        if (data == null || data.size() == 0) {
            return;
        }
        if (data.size() < cellCountW - 1) {
            startX = 0;
            lastStartX = startX;
            postInvalidate();
            return;
        }

        startX = -(cellW) * (data.size() - cellCountW + 1);
        lastStartX = startX;
        postInvalidate();
    }

    public static class DataObject{
        String happenTime;
        float num;

        public DataObject(String happenTime, float num) {
            this.happenTime = happenTime;
            this.num = num;
        }

        public String getHappenTime() {
            return happenTime;
        }

        public void setHappenTime(String happenTime) {
            this.happenTime = happenTime;
        }

        public float getNum() {
            return num;
        }

        public void setNum(float num) {
            this.num = num;
        }
    }
}
