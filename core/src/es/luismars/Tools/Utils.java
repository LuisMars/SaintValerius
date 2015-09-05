package es.luismars.Tools;

/**
 * Created by Dalek on 01/08/2015.
 */
public class Utils {

    public static int roundUp(int numToRound, float multiple)
    {
        return ((int) (Math.ceil(numToRound / multiple) * multiple));
    }
}
