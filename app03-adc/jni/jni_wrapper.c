#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <android/log.h>

#include "packtHAL.h"

#define PACKT_NATIVE_TAG "NDK_PacktApplication"
#define BUFFER_SIZE 64

/* Begin the JNI wrapper functions for the GPIO app */
jboolean Java_com_packt_gpio_MainActivity_openGPIO(JNIEnv *env, jobject this)
{
	jboolean ret = JNI_TRUE;
	if ( openGPIO(0) == 0 ) {
		__android_log_print(ANDROID_LOG_DEBUG, PACKT_NATIVE_TAG, "GPIO Opened.");
	} else {
		__android_log_print(ANDROID_LOG_ERROR, PACKT_NATIVE_TAG, "openGPIO() failed!");
		ret = JNI_FALSE;
	}
	return ret;
}

void Java_com_packt_gpio_MainActivity_closeGPIO(JNIEnv *env, jobject this)
{
	__android_log_print(ANDROID_LOG_DEBUG, PACKT_NATIVE_TAG, "GPIO closing...");
	closeGPIO();
}

jboolean Java_com_packt_gpio_MainActivity_readGPIO(JNIEnv *env, jobject this, jint header, jint pin)
{
	int ret = readGPIO((unsigned int) header, (unsigned int) pin);
	__android_log_print(ANDROID_LOG_DEBUG,
			PACKT_NATIVE_TAG,
			"readGPIO(%d, %d) == %d", (unsigned int) header, (unsigned int) pin, ret);
	return ret == 0 ? JNI_FALSE : JNI_TRUE;
}

void Java_com_packt_gpio_MainActivity_writeGPIO(JNIEnv *env, jobject this, jint header, jint pin, jint val)
{
	int ret = writeGPIO((unsigned int) header, (unsigned int) pin, (unsigned int) val);
	__android_log_print(ANDROID_LOG_DEBUG,
			PACKT_NATIVE_TAG,
			"writeGPIO(%d, %d, %d) == %d", (unsigned int) header, (unsigned int) pin, (unsigned int) val, ret);
}
/* End the JNI wrapper functions for the GPIO app */

/* Begin the JNI wrapper functions for the ADC app */

jint Java_com_packt_adc_MainActivity_readADC(JNIEnv *env, jobject this, jint channel)
{
	jint ret;
	ret = readADC(0) ;

	if ( ret == -1 ) {
		__android_log_print(ANDROID_LOG_DEBUG, PACKT_NATIVE_TAG, "readADC() failed!");
	} else {
		__android_log_print(ANDROID_LOG_ERROR, PACKT_NATIVE_TAG, "readADC() succeeded.");
		ret = JNI_FALSE;
	}
	return ret;
}

/* End the JNI wrapper funtions for the ADC app */

/* Begin the JNI wrapper functions for the FRAM app */
jboolean Java_com_packt_fram_HardwareTask_openFRAM(JNIEnv *env, jobject this, jint bus, jint address) 
{
	if ( !openFRAM(bus, address) ) {
		__android_log_print(ANDROID_LOG_DEBUG, PACKT_NATIVE_TAG, "FRAM Opened.");
	} else {
		__android_log_print(ANDROID_LOG_ERROR, PACKT_NATIVE_TAG, "openFRAM() failed!");
		return JNI_FALSE;
	}
	return JNI_TRUE;
}
jboolean Java_com_packt_fram_HardwareTask_writeFRAM(JNIEnv *env, jobject this, jint offset, jint bufferSize,
  jstring buffer)
{
	jboolean ret = JNI_TRUE;
	const char *str = (*env)->GetStringUTFChars(env, buffer, 0);
	
	if ( writeFRAM(offset, bufferSize, str) ) {
		__android_log_print(ANDROID_LOG_ERROR, PACKT_NATIVE_TAG, 
			"writeFRAM(%d, %d, ...) failed!", (unsigned int)offset, (unsigned int)bufferSize);
		ret = JNI_FALSE;
	} 
	(*env)->ReleaseStringUTFChars(env, buffer, str);
	
	return ret;
}
  
jstring Java_com_packt_fram_HardwareTask_readFRAM(JNIEnv *env, jobject this, jint offset, jint bufferSize)
{
	jstring ret;
	char buffer[BUFFER_SIZE];
	int size = bufferSize;
	
	// Make sure that we don't request more from the FRAM than we can store 
	if (size >= BUFFER_SIZE) {
		size = BUFFER_SIZE - 1;
	}

	// Fill the buffer with data from the FRAM 
	memset((void *)buffer, 0, BUFFER_SIZE);
	if ( readFRAM(offset, size, buffer) ) {
		__android_log_print(ANDROID_LOG_ERROR, PACKT_NATIVE_TAG, 
			"readFRAM(%d, %d, ...) failed!", (unsigned int)offset, (unsigned int)size);
	} else {
		__android_log_print(ANDROID_LOG_INFO, PACKT_NATIVE_TAG, 
			"readFRAM(%d, %d, ...) => '%s'", (unsigned int)offset, (unsigned int)size, buffer);
	}
	
	return (*env)->NewStringUTF(env, buffer);
}

jboolean Java_com_packt_fram_HardwareTask_closeFRAM(JNIEnv *env, jobject this)
{
	__android_log_print(ANDROID_LOG_DEBUG, PACKT_NATIVE_TAG, "FRAM closing...");
	closeFRAM();
	return JNI_TRUE;
}
/* End the JNI wrapper functions for the FRAM app */

/* Begin the JNI wrapper functions for the Sensor app */
jboolean Java_com_packt_sensor_HardwareTask_openSensor(JNIEnv *env, jobject this)
{
	/* Attempt to open the SPI bus */
	if ( openSensor() == 0 ) {
		__android_log_print(ANDROID_LOG_DEBUG, PACKT_NATIVE_TAG, "Sensor Opened.");
	} else {
		__android_log_print(ANDROID_LOG_ERROR, PACKT_NATIVE_TAG, "openSensor() failed!");
		return JNI_FALSE;
	}
  
	return JNI_TRUE;
}

jfloat Java_com_packt_sensor_HardwareTask_getSensorTemperature(JNIEnv *env, jobject this)
{	
	return getSensorTemperature();
}

jfloat Java_com_packt_sensor_HardwareTask_getSensorPressure(JNIEnv *env, jobject this)
{	
	return getSensorPressure();
}

jboolean Java_com_packt_sensor_HardwareTask_closeSensor(JNIEnv *env, jobject this)
{
	__android_log_print(ANDROID_LOG_DEBUG, PACKT_NATIVE_TAG, "Sensor closing...");
	closeSensor();
	return JNI_TRUE;
}
/* End the JNI wrapper functions for the Sensor app */

/* Begin the JNI wrapper functions for the Complete app */
jboolean Java_com_packt_complete_HardwareTask_openHardware(JNIEnv *env, jobject this)
{
	jboolean ret = JNI_TRUE;

	/* Attempt to open the GPIO interface */
	if ( openGPIO(0) == 0 ) {
		__android_log_print(ANDROID_LOG_DEBUG, PACKT_NATIVE_TAG, "GPIO Opened.");
	} else {
		__android_log_print(ANDROID_LOG_ERROR, PACKT_NATIVE_TAG, "openGPIO() failed!");
		return JNI_FALSE;
	}
	
	/* Attempt to open the FRAM interface on I2C logical bus 2, address 0x50 */
	if ( openFRAM(2, 0x50) == 0 ) {
		__android_log_print(ANDROID_LOG_DEBUG, PACKT_NATIVE_TAG, "FRAM Opened.");
	} else {
		__android_log_print(ANDROID_LOG_ERROR, PACKT_NATIVE_TAG, "openFRAM() failed!");
		return JNI_FALSE;
	}
	
	/* Attempt to open the SPI bus */
	if ( openSensor() == 0 ) {
		__android_log_print(ANDROID_LOG_DEBUG, PACKT_NATIVE_TAG, "Sensor Opened.");
	} else {
		__android_log_print(ANDROID_LOG_ERROR, PACKT_NATIVE_TAG, "openSensor() failed!");
		return JNI_FALSE;
	}

	return JNI_TRUE;
}

jboolean Java_com_packt_complete_HardwareTask_pollButton(JNIEnv *env, jobject this)
{
	if ( readGPIO(9, 13) ) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

jboolean Java_com_packt_complete_HardwareTask_changeLED(JNIEnv *env, jobject this, jboolean lit)
{
	if ( writeGPIO(9, 11, (unsigned int)lit) ) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

jboolean Java_com_packt_complete_HardwareTask_readSensor(JNIEnv *env, jobject this)
{
	float temperature, pressure;
	jboolean ret = JNI_TRUE;
	
	/* First, fetch the temperature and pressure from the sensor */
	temperature = getSensorTemperature();
	pressure = getSensorPressure();	
	__android_log_print(ANDROID_LOG_DEBUG,
			PACKT_NATIVE_TAG,
			"temperature: %f, pressure: %f", temperature, pressure);

	/* Second, write the temperature data to the FRAM */
	if ( writeFRAM(0, 4, (const char *)&temperature) ) {
		__android_log_print(ANDROID_LOG_ERROR, PACKT_NATIVE_TAG, "writeFRAM() for temperature failed!");
		ret = JNI_FALSE;
	} 

	/* Third, write the pressure data to the FRAM */
	if ( writeFRAM(4, 4, (const char *)&pressure) ) {
		__android_log_print(ANDROID_LOG_ERROR, PACKT_NATIVE_TAG, "writeFRAM() for pressure failed!");
		ret = JNI_FALSE;
	} 
	
	return ret;
}

jfloat Java_com_packt_complete_HardwareTask_getSensorTemperature(JNIEnv *env, jobject this)
{
	float temperature = 0.0f;

	/* Read the temperature data to the FRAM */
	if ( readFRAM(0, 4, (const char *)&temperature) ) {
		__android_log_print(ANDROID_LOG_ERROR, PACKT_NATIVE_TAG, "readFRAM() for temperature failed!");
	} 

	return (jfloat)temperature;
}

jfloat Java_com_packt_complete_HardwareTask_getSensorPressure(JNIEnv *env, jobject this)
{
	float pressure = 0.0f;

	/* Read the pressure data to the FRAM */
	if ( readFRAM(4, 4, (const char *)&pressure) ) {
		__android_log_print(ANDROID_LOG_ERROR, PACKT_NATIVE_TAG, "readFRAM() for pressure failed!");
	} 

	return (jfloat)pressure;
}

jboolean Java_com_packt_complete_HardwareTask_closeHardware(JNIEnv *env, jobject this)
{
	__android_log_print(ANDROID_LOG_DEBUG, PACKT_NATIVE_TAG, "Sensor closing...");
	closeSensor();
	__android_log_print(ANDROID_LOG_DEBUG, PACKT_NATIVE_TAG, "FRAM closing...");
	closeFRAM();
	__android_log_print(ANDROID_LOG_DEBUG, PACKT_NATIVE_TAG, "GPIO closing...");
	closeGPIO();

	return JNI_TRUE;
}
/* End the JNI wrapper functions for the Complete app */

