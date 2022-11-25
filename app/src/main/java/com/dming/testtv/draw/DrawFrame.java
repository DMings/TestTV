package com.dming.testtv.draw;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.dming.testtv.R;


/**
 * Created by: fwc
 * Date: 2017/12/13
 */
public class DrawFrame {

    private int mProgram = 0;

    private GLDrawable mGLDrawable = new GLDrawable();

    private int aPositionLoc;
    private int aTextureCoordLoc;

    private int uMVPMatrixLoc;
    private int uTexMatrixLoc;

    private int sTextureLoc;

    private float[] vertexTransformMatrix;

    private float[] mVertexMatrix;

    public DrawFrame(Context context) {
        mProgram = GlUtil.createProgram(context, R.raw.base_vertex, R.raw.base_fragment);
        aPositionLoc = GLES20.glGetAttribLocation(mProgram, "aPosition");
        aTextureCoordLoc = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        uMVPMatrixLoc = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        uTexMatrixLoc = GLES20.glGetUniformLocation(mProgram, "uTexMatrix");
        sTextureLoc = GLES20.glGetUniformLocation(mProgram, "sourceImage");
        mVertexMatrix = new float[16];
        Matrix.setIdentityM(mVertexMatrix, 0);
    }


    public void setTransformMatrix(float[] vertexTransformMatrix) {
        this.vertexTransformMatrix = vertexTransformMatrix;
    }

    public void drawFrame(int textureId, float[] mvpMatrix, float[] texMatrix) {

        GLES20.glUseProgram(mProgram);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(sTextureLoc, 0);

        float[] verMatrix;
        if (vertexTransformMatrix != null) {
            Matrix.multiplyMM(mVertexMatrix, 0, mvpMatrix, 0, vertexTransformMatrix, 0);
            verMatrix = mVertexMatrix;
        } else {
            verMatrix = mvpMatrix;
        }

        GLES20.glUniformMatrix4fv(uMVPMatrixLoc, 1, false, verMatrix, 0);
        GLES20.glUniformMatrix4fv(uTexMatrixLoc, 1, false, texMatrix, 0);

        GLES20.glEnableVertexAttribArray(aPositionLoc);

        GLES20.glVertexAttribPointer(aPositionLoc, mGLDrawable.getCoordsPerVertex(),
                GLES20.GL_FLOAT, false, mGLDrawable.getVertexStride(), mGLDrawable.getVertexBuffer());

        GLES20.glEnableVertexAttribArray(aTextureCoordLoc);
        GLES20.glVertexAttribPointer(aTextureCoordLoc, 2,
                GLES20.GL_FLOAT, false, mGLDrawable.getTexCoordStride(), mGLDrawable.getTexCoordBuffer());

//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mGLDrawable.getVertexCount());

        GLES20.glDisableVertexAttribArray(aPositionLoc);
        GLES20.glDisableVertexAttribArray(aTextureCoordLoc);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        GLES20.glUseProgram(0);
    }

    public void release() {

        if (mProgram != 0) {
            GLES20.glDeleteProgram(mProgram);
            mProgram = 0;
        }
    }
}
