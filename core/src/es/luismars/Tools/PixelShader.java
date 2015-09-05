package es.luismars.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by Dalek on 01/08/2015.
 */
public class PixelShader {

    final static String VERT_SRC = Gdx.files.internal("data/pass2.vert").readString();
    final static String FRAG_SRC = Gdx.files.internal("data/coverage_pixel.glsl").readString();

    public static ShaderProgram createShader() {
        ShaderProgram prog = new ShaderProgram(VERT_SRC, FRAG_SRC);
        if (!prog.isCompiled())
            throw new GdxRuntimeException("could not compile shader: " + prog.getLog());
        if (prog.getLog().length() != 0)
            Gdx.app.log("GpuShadows", prog.getLog());
        return prog;
    }
}
