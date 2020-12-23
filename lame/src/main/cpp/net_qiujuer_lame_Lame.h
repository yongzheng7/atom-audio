/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class net_qiujuer_lame_Lame */

#ifndef _Included_net_qiujuer_lame_Lame
#define _Included_net_qiujuer_lame_Lame
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nInit
 * Signature: (IIIIII)J
 */
JNIEXPORT jlong JNICALL Java_net_qiujuer_lame_Lame_nInit
  (JNIEnv *, jclass, jint, jint, jint, jint, jint, jint);

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nGetVersion
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_nGetVersion
  (JNIEnv *, jclass, jlong);

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    mGetMp3bufferSize
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_mGetMp3bufferSize
  (JNIEnv *, jclass, jlong);

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    mGetMp3bufferSizeWithSamples
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_mGetMp3bufferSizeWithSamples
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nEncodeShortInterleaved
 * Signature: (J[SI[B)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_nEncodeShortInterleaved
  (JNIEnv *, jclass, jlong, jshortArray, jint, jbyteArray);

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nEncodeShort
 * Signature: (J[S[SI[B)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_nEncodeShort
  (JNIEnv *, jclass, jlong, jshortArray, jshortArray, jint, jbyteArray);

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nFlush
 * Signature: (J[B)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_nFlush
  (JNIEnv *, jclass, jlong, jbyteArray);

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nClose
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_qiujuer_lame_Lame_nClose
  (JNIEnv *, jclass, jlong);

#ifdef __cplusplus
}
#endif
#endif
