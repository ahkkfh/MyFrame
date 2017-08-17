package com.mark.mpandroidchart.interfaces.dataprovider;


import com.mark.mpandroidchart.components.YAxis;
import com.mark.mpandroidchart.data.BarLineScatterCandleBubbleData;
import com.mark.mpandroidchart.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(YAxis.AxisDependency axis);
    boolean isInverted(YAxis.AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
