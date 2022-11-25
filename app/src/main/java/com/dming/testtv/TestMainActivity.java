package com.dming.testtv;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.TextureView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dming.testtv.blend.ImageBlendFilter;
import com.dming.testtv.draw.DrawFrame;
import com.dming.testtv.draw.GLUtils;


public class TestMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
        TextureView textureView = findViewById(R.id.textureView);
        initTextureView(textureView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 999);
                return;
            }
        }

        ImageView imageView = findViewById(R.id.iv_test);
        Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/DCIM/Camera/retouch_2022033016370434.png");
        Bitmap bg = BitmapFactory.decodeFile("/sdcard/DCIM/Camera/1660906197925.jpg");
        imageView.setImageBitmap(bitmap);
        imageView.setBackground(new BitmapDrawable(bg));
    }


    private Handler mHandle;
    private HandlerThread mHandlerThread;
    protected float[] mMatrix = new float[16];
    protected float[] mIdentityMatrix = new float[16];

    private void initTextureView(TextureView textureView) {
        mHandlerThread = new HandlerThread("gl");
        mHandlerThread.start();
        mHandle = new Handler(mHandlerThread.getLooper());
        textureView.setOpaque(false);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {

            EglCore mEglCore;
            private EGLSurface mEglSurface;
            private DrawFrame mDrawFrame;
            private ImageBlendFilter mImageBlendFilter;
            private int mTextureId;
            private int mTextureId2;

            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                mHandle.post(() -> {
                    mEglCore = new EglCore(null, EglCore.FLAG_TRY_GLES3);
                    mEglSurface = mEglCore.createWindowSurface(surface);
                    mEglCore.makeCurrent(mEglSurface);
                    mDrawFrame = new DrawFrame(TestMainActivity.this);
                    mImageBlendFilter = new ImageBlendFilter(TestMainActivity.this);
                    Matrix.setIdentityM(mIdentityMatrix, 0);
                    Matrix.setIdentityM(mMatrix, 0);
                    Matrix.scaleM(mMatrix, 0, 1, -1, 0);
                    mTextureId = GLUtils.createTexture();
                    Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/DCIM/Camera/retouch_2022033016370434.png");
                    GLUtils.bindBitmap(mTextureId, bitmap);

                    mTextureId2 = GLUtils.createTexture();
//                    Bitmap bitmap2 = BitmapFactory.decodeFile("/sdcard/DCIM/Camera/1660906197925.jpg");
                    Bitmap bitmap2 = BitmapFactory.decodeFile("/storage/emulated/0/DCIM/Camera/retouch_2022040115113251.png");
                    GLUtils.bindBitmap(mTextureId2, bitmap2);
                });
                mHandle.postDelayed(() -> {
                    int mWidth = width;
                    int mHeight = height;
//                    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//                    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
                    GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
                    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

//                    GLES20.glEnable(GLES20.GL_BLEND);
//                    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//                    mDrawFrame.drawFrame(mTextureId2, mMatrix, mIdentityMatrix);
//                    GLES20.glDisable(GLES20.GL_BLEND);

                    GLES20.glEnable(GLES20.GL_BLEND);
                    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                    mDrawFrame.drawFrame(mTextureId, mMatrix, mIdentityMatrix);
//                    mImageBlendFilter.drawFrame(mTextureId2, mTextureId, 1.0f, mMatrix, mIdentityMatrix, mIdentityMatrix);
                    GLES20.glDisable(GLES20.GL_BLEND);

                    mEglCore.swapBuffers(mEglSurface);
                }, 500);
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                mHandle.post(() -> {
                    if (mEglSurface != null) {
                        if (mEglCore != null) {
                            mEglCore.releaseSurface(mEglSurface);
                            mEglCore.release();
                        }
                    }
                    mEglSurface = null;
                    mEglCore = null;
                });
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
//                mHandle2.post(() -> {
//                    surface.updateTexImage();
//                });
            }
        });
    }

}