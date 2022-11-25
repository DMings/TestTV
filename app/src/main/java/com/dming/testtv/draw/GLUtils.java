package com.dming.testtv.draw;

import android.graphics.Bitmap;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Description:
 * Created by odm 2022/6/22 18:00
 */
public class GLUtils {

    public static void bindBitmap(int textureId, Bitmap bitmap) {
        if (textureId != GLES20.GL_NONE && bitmap != null) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            android.opengl.GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
    }

    public static int createTexture() {
        int textureTarget = GLES20.GL_TEXTURE_2D;
        int minFilter = GLES20.GL_LINEAR;
        int magFilter = GLES20.GL_LINEAR;
        int wrapS = GLES20.GL_CLAMP_TO_EDGE;
        int wrapT = GLES20.GL_CLAMP_TO_EDGE;
        int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);
        GLES20.glBindTexture(textureTarget, textureHandle[0]);
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_MAG_FILTER, magFilter); //线性插值
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_S, wrapS);
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_T, wrapT);
        GLES20.glBindTexture(textureTarget, 0);
        return textureHandle[0];
    }

    public static void deleteTexture(int textureId) {
        int[] textureHandle = new int[1];
        textureHandle[0] = textureId;
        GLES20.glDeleteBuffers(1, textureHandle, 0);
    }

    public static Bitmap get2DTextureBitmap(int textureId, int width, int height) {
        Bitmap bitmap = null;
        int[] frameBuffers = new int[1];
        GLES20.glGenFramebuffers(1, frameBuffers, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[0]);
        if (frameBuffers[0] != 0 && textureId != 0 && width > 0 && height > 0) {
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textureId, 0);
            ByteBuffer mByteBuffer = ByteBuffer.allocateDirect(width * height * 4);
            mByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, mByteBuffer);
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            if (bitmap != null) {
                bitmap.copyPixelsFromBuffer(mByteBuffer);
            }
        }
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glDeleteFramebuffers(1, frameBuffers, 0);
        return bitmap;
    }

    public static Bitmap getOES2DTextureBitmap(int textureId, int width, int height) {
        Bitmap bitmap = null;
        int[] frameBuffers = new int[1];
        GLES20.glGenFramebuffers(1, frameBuffers, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[0]);
        if (frameBuffers[0] != 0 && textureId != 0 && width > 0 && height > 0) {
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId, 0);
            ByteBuffer mByteBuffer = ByteBuffer.allocateDirect(width * height * 4);
            mByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, mByteBuffer);
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            if (bitmap != null) {
                bitmap.copyPixelsFromBuffer(mByteBuffer);
            }
        }
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glDeleteFramebuffers(1, frameBuffers, 0);
        return bitmap;
    }


    private boolean checkIfContextSupportsExtension(String extension) {
        String extensions = " " + GLES20.glGetString(GLES20.GL_EXTENSIONS) + " ";
        // The extensions string is padded with spaces between extensions, but not
        // necessarily at the beginning or end. For simplicity, add spaces at the
        // beginning and end of the extensions string and the extension string.
        // This means we can avoid special-case checks for the first or last
        // extension, as well as avoid special-case checks when an extension name
        // is the same as the first part of another extension name.
//        GL_OES_framebuffer_object no this!!!
//        "GL_EXT_debug_marker " +
//                "GL_ARM_rgba8 " +
//                "GL_ARM_mali_shader_binary " +
//                "GL_OES_depth24 " +
//                "GL_OES_depth_texture " +
//                "GL_OES_depth_texture_cube_map " +
//                "GL_OES_packed_depth_stencil " +
//                "GL_OES_rgb8_rgba8 " +
//                "GL_EXT_read_format_bgra " +
//                "GL_OES_compressed_paletted_texture " +
//                "GL_OES_compressed_ETC1_RGB8_texture " +
//                "GL_OES_standard_derivatives " +
//                "GL_OES_EGL_image " +
//                "GL_OES_EGL_image_external " +
//                "GL_OES_EGL_image_external_essl3 " +
//                "GL_OES_EGL_sync " +
//                "GL_OES_texture_npot " +
//                "GL_OES_vertex_half_float " +
//                "GL_OES_required_internalformat " +
//                "GL_OES_vertex_array_object " +
//                "GL_OES_mapbuffer " +
//                "GL_EXT_texture_format_BGRA8888 " +
//                "GL_EXT_texture_rg " +
//                "GL_EXT_texture_type_2_10_10_10_REV " +
//                "GL_OES_fbo_render_mipmap " +
//                "GL_OES_element_index_uint " +
//                "GL_EXT_shadow_samplers " +
//                "GL_OES_texture_compression_astc " +
//                "GL_KHR_texture_compression_astc_ldr " +
//                "GL_KHR_texture_compression_astc_hdr " +
//                "GL_KHR_texture_compression_astc_sliced_3d " +
//                "GL_KHR_debug GL_EXT_occlusion_query_boolean " +
//                "GL_EXT_disjoint_timer_query " +
//                "GL_EXT_blend_minmax " +
//                "GL_EXT_discard_framebuffer " +
//                "GL_OES_get_program_binary " +
//                "GL_OES_texture_3D " +
//                "GL_EXT_texture_storage " +
//                "GL_EXT_multisampled_render_to_texture " +
//                "GL_EXT_multisampled_render_to_texture2 " +
//                "GL_OES_surfaceless_context " +
//                "GL_OES_texture_stencil8 " +
//                "GL_EXT_shader_pixel_local_storage " +
//                "GL_ARM_shader_framebuffer_fetch " +
//                "GL_ARM_shader_framebuffer_fetch_depth_stencil " +
//                "GL_ARM_mali_program_binary " +
//                "GL_EXT_sRGB GL_EXT_sRGB_write_control " +
//                "GL_EXT_texture_sRGB_decode " +
//                "GL_EXT_texture_sRGB_R8 " +
//                "GL_EXT_texture_sRGB_RG8 " +
//                "GL_KHR_blend_equation_advanced " +
//                "GL_KHR_blend_equation_advanced_coherent " +
//                "GL_OES_texture_storage_multisample_2d_array " +
//                "GL_OES_shader_image_atomic " +
//                "GL_EXT_robustness " +
//                "GL_EXT_draw_buffers_indexed " +
//                "GL_OES_draw_buffers_indexed " +
//                "GL_EXT_texture_border_clamp " +
//                "GL_OES_texture_border_clamp " +
//                "GL_EXT_texture_cube_map_array " +
//                "GL_OES_texture_cube_map_array " +
//                "GL_OES_sample_variables " +
//                "GL_OES_sample_shading " +
//                "GL_OES_shader_multisample_interpolation " +
//                "GL_EXT_shader_io_blocks " +
//                "GL_OES_shader_io_blocks " +
//                "GL_EXT_tessellation_shader " +
//                "GL_OES_tessellation_shader " +
//                "GL_EXT_primitive_bounding_box " +
//                "GL_OES_primitive_bounding_box " +
//                "GL_EXT_geometry_shader " +
//                "GL_OES_geometry_shader " +
//                "GL_ANDROID_extension_pack_es31a " +
//                "GL_EXT_gpu_shader5 " +
//                "GL_OES_gpu_shader5 " +
//                "GL_EXT_texture_buffer " +
//                "GL_OES_texture_buffer " +
//                "GL_EXT_copy_image " +
//                "GL_OES_copy_image " +
//                "GL_EXT_shader_non_constant_global_initializers " +
//                "GL_EXT_color_buffer_half_float GL_EXT_color_buffer_float " +
//                "GL_EXT_YUV_target GL_OVR_multiview " +
//                "GL_OVR_multiview2 " +
//                "GL_OVR_multiview_multisampled_render_to_texture" +
//                " GL_KHR_robustness GL_KHR_robust_buffer_access_behavior " +
//                "GL_EXT_draw_elements_base_vertex " +
//                "GL_OES_draw_elements_base_vertex " +
//                "GL_EXT_protected_textures " +
//                "GL_EXT_buffer_storage " +
//                "GL_EXT_external_buffer " +
//                "GL_EXT_EGL_image_array " +
//                "GL_EXT_texture_filter_anisotropic "

        return extensions.indexOf(" " + extension + " ") >= 0;
    }


}
