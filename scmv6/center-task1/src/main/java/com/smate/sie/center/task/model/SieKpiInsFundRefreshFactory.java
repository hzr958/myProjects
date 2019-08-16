package com.smate.sie.center.task.model;

import java.util.HashMap;

/**
 * SieKpiInsFundRefresh对象运用享元模式的对象
 * 
 * @author lijianming
 *
 */
public class SieKpiInsFundRefreshFactory {

    private static final HashMap<int[], SieKpiInsFundRefresh> refreshMap = new HashMap<>();

    public static SieKpiInsFundRefresh getSieKpiInsFundRefresh(int[] arr) {
        SieKpiInsFundRefresh refresh = refreshMap.get(arr);

        if (refresh == null) {
            refresh = new SieKpiInsFundRefresh(arr[0], arr[1]);
            refreshMap.put(arr, refresh);
        }

        return refresh;
    }
}
