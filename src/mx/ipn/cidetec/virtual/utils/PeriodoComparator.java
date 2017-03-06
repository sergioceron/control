package mx.ipn.cidetec.virtual.utils;

import mx.ipn.cidetec.virtual.entities.Periodo;

import java.util.Comparator;

/**
 * Created by sergio on 04/12/16.
 */
public class PeriodoComparator implements Comparator<Periodo> {
    public int compare(Periodo o1, Periodo o2) {
        return o1.toStringOrder().compareTo(o2.toStringOrder());
    }
}