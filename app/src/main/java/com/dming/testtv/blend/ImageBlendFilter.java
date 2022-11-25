package com.dming.testtv.blend;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.dming.testtv.R;
import com.dming.testtv.draw.GLDrawable;
import com.dming.testtv.draw.GlUtil;


/**
 * Description: 所有混合类型
 * Created by odm 2022/4/6 17:12
 */
public class ImageBlendFilter {

    private int mProgram = 0;
    private final GLDrawable mGLDrawable = new GLDrawable();
    private final int aPositionLoc;
    private final int aTextureCoordLoc;
    private final int uMVPMatrixLoc;
    private final int uBaseTexMatrixLoc;
    private final int uBlendTexMatrixLoc;
    private final int uBaseTextureLoc;
    private final int uBlendTextureLoc;
    private final int uAlphaLoc;
    private final float[] mIdentityMatrix = new float[16];

    public ImageBlendFilter(Context context) {
        Matrix.setIdentityM(mIdentityMatrix, 0);
        mProgram = GlUtil.createProgram(context, R.raw.blend_image_vertex, R.raw.blend_image_normal_fragment);
        aPositionLoc = GLES20.glGetAttribLocation(mProgram, "aPosition");
        aTextureCoordLoc = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");

        uMVPMatrixLoc = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        uBaseTexMatrixLoc = GLES20.glGetUniformLocation(mProgram, "uBaseTexMatrix");
        uBlendTexMatrixLoc = GLES20.glGetUniformLocation(mProgram, "uBlendTexMatrix");
        uBaseTextureLoc = GLES20.glGetUniformLocation(mProgram, "uBaseTexture");
        uBlendTextureLoc = GLES20.glGetUniformLocation(mProgram, "uBlendTexture");
        uAlphaLoc = GLES20.glGetUniformLocation(mProgram, "uAlpha");
    }

    public void drawFrame(int textureId, int maskTextureId, float alpha) {
        drawFrame(textureId, maskTextureId, alpha, mIdentityMatrix, mIdentityMatrix, mIdentityMatrix);
    }

    public void drawFrame(int textureId, int maskTextureId, float alpha, float[] blendTexMatrix) {
        drawFrame(textureId, maskTextureId, alpha, mIdentityMatrix, mIdentityMatrix, blendTexMatrix);
    }

    public void drawFrame(int textureId, int maskTextureId, float alpha, float[] mvpMatrix, float[] baseTexMatrix, float[] blendTexMatrix) {

        GLES20.glUseProgram(mProgram);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(uBaseTextureLoc, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, maskTextureId);
        GLES20.glUniform1i(uBlendTextureLoc, 1);

        GLES20.glUniformMatrix4fv(uMVPMatrixLoc, 1, false, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(uBaseTexMatrixLoc, 1, false, baseTexMatrix, 0);
        GLES20.glUniformMatrix4fv(uBlendTexMatrixLoc, 1, false, blendTexMatrix, 0);
        GLES20.glUniform1f(uAlphaLoc, alpha);

        GLES20.glEnableVertexAttribArray(aPositionLoc);
        GLES20.glVertexAttribPointer(aPositionLoc, mGLDrawable.getCoordsPerVertex(),
                GLES20.GL_FLOAT, false, mGLDrawable.getVertexStride(), mGLDrawable.getVertexBuffer());

        GLES20.glEnableVertexAttribArray(aTextureCoordLoc);
        GLES20.glVertexAttribPointer(aTextureCoordLoc, 2,
                GLES20.GL_FLOAT, false, mGLDrawable.getTexCoordStride(), mGLDrawable.getTexCoordBuffer());

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