LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_LDLIBS := -llog
LOCAL_MODULE := BBBAndroidHAL
LOCAL_SRC_FILES := jni_wrapper.c adc.c
include $(BUILD_SHARED_LIBRARY)