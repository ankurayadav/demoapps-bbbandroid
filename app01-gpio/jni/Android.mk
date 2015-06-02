LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_LDLIBS := -llog
LOCAL_MODULE := packtHAL
LOCAL_SRC_FILES := jni_wrapper.c gpio.c fram.c bmp183.c
include $(BUILD_SHARED_LIBRARY)