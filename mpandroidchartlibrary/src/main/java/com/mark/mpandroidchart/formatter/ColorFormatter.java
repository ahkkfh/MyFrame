package com.mark.mpandroidchart.formatter;

import com.mark.mpandroidchart.data.Entry;
import com.mark.mpandroidchart.interfaces.datasets.IDataSet;

/**
 * Interface that can be used to return a customized color instead of setting
 * colors via the setColor(...) method of the DataSet.
 *
 * @author Philipp Jahoda
 */
public interface ColorFormatter {

    /**
     * Returns the color to be used for the given Entry at the given index (in the entries array)
     *
     * @param index index in the entries array
     * @param e     the entry to color
     * @param set   the DataSet the entry belongs to
     * @return
     */
    int getColor(int index, Entry e, IDataSet set);
}