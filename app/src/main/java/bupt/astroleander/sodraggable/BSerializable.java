package bupt.astroleander.sodraggable;

import java.io.Serializable;

/**
 * <h1> ${className} </h1>
 * <p>Created by Astroleander on 2018/11/5<p>.
 *
 * <p>
 *
 * @author Astroleander
 * @version 1.0.0
 * @description </p>
 */
public class BSerializable implements Serializable {
    private int b;

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public BSerializable(int b){
        this.b = b;
    }
}
