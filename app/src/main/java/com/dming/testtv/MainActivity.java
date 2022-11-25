package com.dming.testtv;

import android.graphics.SurfaceTexture;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextureView textureView = findViewById(R.id.textureView);
        initTextureView(textureView);
    }


    private Handler mHandle;
    private HandlerThread mHandlerThread;

    private void initTextureView(TextureView textureView) {
        mHandlerThread = new HandlerThread("gl");
        mHandlerThread.start();
        mHandle = new Handler(mHandlerThread.getLooper());
        textureView.setOpaque(false);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {

            EglCore mEglCore;
            private EGLSurface mEglSurface;

            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                mHandle.post(() -> {
                    mEglCore = new EglCore(null, EglCore.FLAG_TRY_GLES3);
                    mEglSurface = mEglCore.createWindowSurface(surface);
                    mEglCore.makeCurrent(mEglSurface);
                });
                mHandle.postDelayed(() -> {
                    GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.3f);
                    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
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

    private Handler mHandle2;
    private HandlerThread mHandlerThread2;

    private void initTextureView2(TextureView textureView) {
        mHandlerThread2 = new HandlerThread("gl");
        mHandlerThread2.start();
        mHandle2 = new Handler(mHandlerThread2.getLooper());
        textureView.setOpaque(false);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {

            EglCore mEglCore;
            private EGLSurface mEglSurface;

            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                mHandle2.post(() -> {
                    mEglCore = new EglCore(null, EglCore.FLAG_TRY_GLES3);
                    mEglSurface = mEglCore.createWindowSurface(surface);
                    mEglCore.makeCurrent(mEglSurface);
                });
                mHandle2.postDelayed(() -> {
                    GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.3f);
                    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
                    mEglCore.swapBuffers(mEglSurface);
                }, 500);
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                mHandle2.post(() -> {
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