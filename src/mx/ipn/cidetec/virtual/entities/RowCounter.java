package mx.ipn.cidetec.virtual.entities;

import org.jboss.seam.annotations.Name;

import java.io.Serializable;

/**
 * Created by root on 28/10/15.
 */
@Name( "rowcounter" )
public class RowCounter implements Serializable {

    private transient int row = 0;
    private transient int row1 = 0;
    private transient int row2 = 0;
    public int getRow() {

        return ++row;
    }
    public int getRow1() {
        return ++row1;
    }
    public int getRow2() {
        return ++row2;
    }


}
