package com.mark.mpandroidchart.interfaces.dataprovider;


import com.mark.mpandroidchart.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
