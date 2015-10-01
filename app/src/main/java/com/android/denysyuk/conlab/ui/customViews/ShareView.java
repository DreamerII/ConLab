package com.android.denysyuk.conlab.ui.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

import com.android.denysyuk.conlab.R;
import com.android.denysyuk.conlab.database.DataManager;
import com.android.denysyuk.conlab.models.Currencies;
import com.android.denysyuk.conlab.models.Finance;

import java.util.List;

/**
 * Created by root on 29.09.15.
 */
public class ShareView extends View {
    private Finance mFinance;
    private int position;

    private int width;
    private int height;

    public ShareView(Context context, int _position) {
        super(context);
        position = _position;
        init();
    }

    public ShareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ShareView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public int getH() {
        return height;
    }

    private void init(){
        mFinance = DataManager.get(getContext()).getFinance();
        width = getMeasuredWidth();
        height = mFinance.getOrganizations().get(position).getCurrencies().size()*60+230;

        measure(width, height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        setMeasuredDimension(getMeasuredWidth(), height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        Paint paintId = new Paint();
        paintId.setTextSize(32);
        paintId.setColor(getResources().getColor(R.color.accent));
        paintId.setStrokeWidth(2);
        paintId.setStyle(Paint.Style.FILL);

        Paint paintAB = new Paint();
        paintAB.setTextSize(32);
        paintAB.setColor(getResources().getColor(R.color.primary_dark));
        paintAB.setStrokeWidth(2);
        paintAB.setStyle(Paint.Style.FILL);

        p.setAntiAlias(true);
        p.setColor(getResources().getColor(R.color.primary_text));
        p.setTextSize(32);
        p.setStrokeWidth(2);
        p.setStyle(Paint.Style.FILL);

        canvas.translate(30, 60);
        canvas.drawText(mFinance.getOrganizations().get(position).getTitle(), 0, 0, p);
        p.setTextSize(26);
        canvas.translate(0, 40);
        canvas.drawText(mFinance.getRegions().get(mFinance.getOrganizations().get(position).getRegionId()), 0, 0, p);
        canvas.translate(0, 40);
        canvas.drawText(mFinance.getCities().get(mFinance.getOrganizations().get(position).getCityId()), 0, 0, p);
        canvas.translate(60, 80);
        canvas.translate(80, 0);



        for(Currencies c : mFinance.getOrganizations().get(position).getCurrencies()) {
            canvas.translate(-120, 60);
            canvas.drawText(c.getId(), 0, 0, paintId);
            canvas.translate(120, 0);
            canvas.drawText(c.getAsk()+"/"+c.getBid(), 0, 0, paintAB);
        }

    }
}
