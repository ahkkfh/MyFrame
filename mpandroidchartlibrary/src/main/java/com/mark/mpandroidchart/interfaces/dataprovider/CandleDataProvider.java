package com.mark.mpandroidchart.interfaces.dataprovider;


import com.mark.mpandroidchart.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
