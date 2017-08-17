package com.mark.mpandroidchart.interfaces.dataprovider;

import com.mark.mpandroidchart.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
