package es.luismars.Tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Dalek on 03/08/2015.
 */
public class IntPair implements Serializable {
    int level;
    int id;

    public IntPair() {}

    public IntPair(int level, int id) {
        this.level = level;
        this.id = id;
    }

    @Override
    public int hashCode() {
        return level * 1000 + id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        IntPair other = (IntPair) obj;
        return other.level == level && other.id == id;
    }
}
