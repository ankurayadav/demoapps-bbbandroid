#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <android/log.h>

#include "bbbandroidHAL.h"

#define JAVA_CLASS_PATH(funtion_name) Java_com_bbbandroid_adc_MainActivity_##funtion_name

#define BBBANDROID_NATIVE_TAG "NDK_BBBAndroidApplication"
#define BUFFER_SIZE 64



/* Begin the JNI wrapper functions for the ADC app */

jint JAVA_CLASS_PATH(readADC)(JNIEnv *env, jobject this, jint channel)
{
	jint ret;
	ret = readADC(channel) ;

	if ( ret == -1 ) {
		__android_log_print(ANDROID_LOG_ERROR, BBBANDROID_NATIVE_TAG, "readADC(%d) failed!", (unsigned int) channel);
		ret = -1;
	} else {
		__android_log_print(ANDROID_LOG_DEBUG, BBBANDROID_NATIVE_TAG, "readADC(%d) succeeded", (unsigned int) channel);
	}

	return ret;
}

/* End the JNI wrapper funtions for the ADC app */

/* End the JNI wrapper functions for the Complete app */