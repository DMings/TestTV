/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dming.testtv.draw;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Base class for stuff we like to draw.
 */
class GLDrawable2d {

    private static final int SIZEOF_FLOAT = 4;

    private static final float[] FULL_RECTANGLE_COORDS = {
            -1.0f, -1.0f,   // 0 bottom left
            1.0f, -1.0f,   // 1 bottom right
            -1.0f, 1.0f,   // 2 top left
            1.0f, 1.0f,   // 3 top right
    };
    private static final float[] FULL_RECTANGLE_TEX_COORDS = {
            0.0f, 0.0f,     // 0 bottom left
            1.0f, 0.0f,     // 1 bottom right
            0.0f, 1.0f,     // 2 top left
            1.0f, 1.0f      // 3 top right
    };

    private static final FloatBuffer FULL_RECTANGLE_BUF = createFloatBuffer(FULL_RECTANGLE_COORDS);
    private static final FloatBuffer FULL_RECTANGLE_TEX_BUF = createFloatBuffer(FULL_RECTANGLE_TEX_COORDS);

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTexCoordBuffer;
    /**
     * 0b00:not static buffer,
     * 0b01:static vertex buffer,
     * 0b10:static texture buffer
     * 0b11:static vertex and texture buffer
     */
    private int mBufferFlag;
    private int mVertexCount;
    private int mCoordsPerVertex;
    private int mVertexStride;
    private int mTexCoordStride;

    public GLDrawable2d() {
        mVertexBuffer = FULL_RECTANGLE_BUF;
        mTexCoordBuffer = FULL_RECTANGLE_TEX_BUF;
        mBufferFlag = 0b11;

        mCoordsPerVertex = 2;
        mVertexStride = mCoordsPerVertex * SIZEOF_FLOAT;
        mVertexCount = FULL_RECTANGLE_COORDS.length / mCoordsPerVertex;
        mTexCoordStride = 2 * SIZEOF_FLOAT;
    }

    public GLDrawable2d(float[] vertexCoors, float[] textureCoors) {
        mVertexBuffer = createFloatBuffer(vertexCoors);
        mTexCoordBuffer = createFloatBuffer(textureCoors);

        mCoordsPerVertex = 2;
        mVertexStride = mCoordsPerVertex * SIZEOF_FLOAT;
        mVertexCount = vertexCoors.length / mCoordsPerVertex;
        mTexCoordStride = 2 * SIZEOF_FLOAT;
    }

    public static FloatBuffer createFloatBuffer(float[] coords) {
        // Allocate a direct ByteBuffer, using 4 bytes per float, and copy coords into it.
        FloatBuffer fb = ByteBuffer.allocateDirect(coords.length * SIZEOF_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(coords);
        fb.position(0);
        return fb;
    }

    public void updateVertexCoors(float[] vertexCoors) {
        mVertexCount = vertexCoors.length / mCoordsPerVertex;
        if (mVertexBuffer == null
                || mVertexBuffer.capacity() != vertexCoors.length
                || (mBufferFlag & 0b01) != 0) {
            mBufferFlag &= ~0b01;
            mVertexBuffer = createFloatBuffer(vertexCoors);
        } else {
            mVertexBuffer.clear();
            mVertexBuffer.put(vertexCoors);
            mVertexBuffer.position(0);
        }
    }

    public void updateTextureCoors(float[] textureCoors) {
        if (mTexCoordBuffer == null
                || mTexCoordBuffer.capacity() != textureCoors.length
                || (mBufferFlag & 0b10) != 0) {
            mBufferFlag &= ~0b10;
            mTexCoordBuffer = createFloatBuffer(textureCoors);
        } else {
            mTexCoordBuffer.clear();
            mTexCoordBuffer.put(textureCoors);
            mTexCoordBuffer.position(0);
        }
    }

    /**
     * Returns the buffer of vertices.
     * <p>
     * To avoid allocations, this returns internal state.  The caller must not modify it.
     */
    public FloatBuffer getVertexBuffer() {
        return mVertexBuffer;
    }

    /**
     * Returns the buffer of texture coordinates.
     * <p>
     * To avoid allocations, this returns internal state.  The caller must not modify it.
     */
    public FloatBuffer getTexCoordBuffer() {
        return mTexCoordBuffer;
    }

    /**
     * Returns the number of vertices stored in the vertex array.
     */
    public int getVertexCount() {
        return mVertexCount;
    }

    /**
     * Returns the width, in bytes, of the data for each vertex.
     */
    public int getVertexStride() {
        return mVertexStride;
    }

    /**
     * Returns the width, in bytes, of the data for each texture coordinate.
     */
    public int getTexCoordStride() {
        return mTexCoordStride;
    }

    /**
     * Returns the number of position coordinates per vertex.  This will be 2 or 3.
     */
    public int getCoordsPerVertex() {
        return mCoordsPerVertex;
    }
}
