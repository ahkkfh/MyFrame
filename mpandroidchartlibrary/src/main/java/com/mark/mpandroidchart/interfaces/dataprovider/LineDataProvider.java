package com.mark.mpandroidchart.interfaces.dataprovider;

import com.mark.mpandroidchart.components.YAxis;
import com.mark.mpandroidchart.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
