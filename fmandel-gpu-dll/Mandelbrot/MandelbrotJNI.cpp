#include <jni.h>
#include "kernel.cuh"

extern "C" JNIEXPORT jlong JNICALL Java_MandelbrotLib_allocateImage(JNIEnv* env, jobject obj, jint width, jint height) {
    unsigned char* image = allocate_image(width, height);
    return (jlong)image;
}

extern "C" JNIEXPORT void JNICALL Java_MandelbrotLib_freeImage(JNIEnv* env, jobject obj, jlong imagePtr) {
    unsigned char* image = (unsigned char*)imagePtr;
    free_image(image);
}

// This function will generate the Mandelbrot set
extern "C" JNIEXPORT void JNICALL Java_MandelbrotLib_generateMandelbrot(JNIEnv* env, jobject obj, jlong imagePtr, jint width, jint height,
    jdouble xCenter, jdouble yCenter, jdouble xMin, jdouble xMax, jdouble yMin, jdouble yMax, jint maxIter, jint zoomSteps) {
    unsigned char* image = (unsigned char*)imagePtr;
    generate_mandelbrot(image, width, height, xCenter, yCenter, xMin, xMax, yMin, yMax, maxIter, zoomSteps);
}

// This function will save the generated image as a PPM file
extern "C" JNIEXPORT void JNICALL Java_MandelbrotLib_saveImage(JNIEnv* env, jobject obj, jstring filename, jlong imagePtr, jint width, jint height) {
    const char* file = env->GetStringUTFChars(filename, nullptr);
    unsigned char* image = (unsigned char*)imagePtr;
    save_image(file, image, width, height);  // Call the CUDA function to save the image
    env->ReleaseStringUTFChars(filename, file);
}
