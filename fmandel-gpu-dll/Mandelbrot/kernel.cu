#include "kernel.cuh"
#include <jni.h>
#include <fstream>
#include <vector>
#define STB_IMAGE_WRITE_IMPLEMENTATION
#include "stb_image_write.h"

__global__ void mandelbrot_kernel(unsigned char* image, int width, int height, double x_min, double x_max, double y_min, double y_max, int max_iter, double zoom_x, double zoom_y, int zoom_level) {
    int px = blockIdx.x * blockDim.x + threadIdx.x;
    int py = blockIdx.y * blockDim.y + threadIdx.y;

    if (px < width && py < height) {

        double zoomed_x_min = zoom_x - (x_max - x_min) / (2.0f * zoom_level);
        double zoomed_x_max = zoom_x + (x_max - x_min) / (2.0f * zoom_level);
        double zoomed_y_min = zoom_y - (y_max - y_min) / (2.0f * zoom_level);
        double zoomed_y_max = zoom_y + (y_max - y_min) / (2.0f * zoom_level);

        double x0 = zoomed_x_min + (px / (double)width) * (zoomed_x_max - zoomed_x_min);
        double y0 = zoomed_y_min + (py / (double)height) * (zoomed_y_max - zoomed_y_min);
        double x = 0.0;
        double y = 0.0;
        int iteration = 0;

        while (x * x + y * y <= 4.0 && iteration < max_iter) {
            double xtemp = x * x - y * y + x0;
            y = 2 * x * y + y0;
            x = xtemp;
            iteration++;
        }
        int pixelIndex = py * width + px * 3;
        
        double t = double(iteration) / max_iter;
        image[pixelIndex] = (unsigned char)(9 * (1 - t) * t * t * t * 255);
        image[pixelIndex + 1] = (unsigned char)(15 * (1 - t) * (1 - t) * t * t * 255);
        image[pixelIndex + 2] = (unsigned char)(8.5 * (1 - t) * (1 - t) * (1 - t) * t * 255);
        
    }
}

extern "C" {

    __declspec(dllexport) unsigned char* allocate_image(int width, int height) {
        unsigned char* image;
        cudaMallocManaged(&image, width * height * 3);
        return image;
    }

    __declspec(dllexport)  void free_image(unsigned char* image) {
        cudaFree(image);
    }

    __declspec(dllexport)  void generate_mandelbrot(unsigned char* image, int width, int height,
        double x_center, double y_center,
        double x_min, double x_max,
        double y_min, double y_max,
        int max_iter, int zoom_steps) {
        dim3 threads_per_block(16, 16);
        dim3 num_blocks((width + threads_per_block.x - 1) / threads_per_block.x,
            (height + threads_per_block.y - 1) / threads_per_block.y);

        mandelbrot_kernel <<<num_blocks, threads_per_block>>> (image, width, height, x_min, x_max, y_min, y_max, max_iter, x_center, y_center, zoom_steps);
        cudaDeviceSynchronize();
    }

    __declspec(dllexport)  void save_image(const char* filename, unsigned char* image, int width, int height) {
        std::ofstream ofs(filename, std::ios::binary);
        ofs << "P6\n" << width << " " << height << "\n255\n";
        ofs.write(reinterpret_cast<char*>(image), width * height * 3);
        ofs.close();
    }

}

//extern "C" JNIEXPORT jobject JNICALL Java_kt_MandelbrotLibrary_allocateImage(JNIEnv* env, jobject obj, jint width, jint height) {
//    unsigned char* image = allocate_image(width, height);
//    return env->NewDirectByteBuffer(image, width * height * 3);  // Create a direct ByteBuffer from native memory
//}
//
//extern "C" JNIEXPORT void JNICALL Java_kt_MandelbrotLibrary_freeImage(JNIEnv* env, jobject obj, jobject buffer) {
//    unsigned char* image = (unsigned char*)env->GetDirectBufferAddress(buffer);  // Get the pointer from ByteBuffer
//    free_image(image);  // Free the native memory
//}
//
//extern "C" JNIEXPORT void JNICALL Java_kt_MandelbrotLibrary_generateMandelbrot(JNIEnv* env, jobject obj,
//    jobject imageBuffer, jint width, jint height,
//    jdouble xCenter, jdouble yCenter,
//    jdouble xMin, jdouble xMax,
//    jdouble yMin, jdouble yMax,
//    jint maxIter, jint zoomSteps) {
//    // Get the pointer from the ByteBuffer
//    unsigned char* image = (unsigned char*)env->GetDirectBufferAddress(imageBuffer);
//
//    // Check if the pointer is valid
//    if (image == nullptr) {
//        printf("Error: GetDirectBufferAddress returned NULL\n");
//        return;
//    }
//
//    // Call the CUDA function to generate the Mandelbrot set
//    generate_mandelbrot(image, width, height, xCenter, yCenter, xMin, xMax, yMin, yMax, maxIter, zoomSteps);
//}
//
//extern "C" JNIEXPORT void JNICALL Java_kt_MandelbrotLibrary_saveImage(JNIEnv* env, jobject obj,
//    jstring filename, jobject imageBuffer,
//    jint width, jint height) {
//    // Get the pointer from the ByteBuffer
//    unsigned char* image = (unsigned char*)env->GetDirectBufferAddress(imageBuffer);
//
//    // Get the filename from the jstring
//    const char* file = env->GetStringUTFChars(filename, nullptr);
//
//    // Call the CUDA function to save the image
//    save_image(file, image, width, height);
//
//    // Release the filename memory
//    env->ReleaseStringUTFChars(filename, file);
//}
void write_to_memory(void* context, void* data, int size) {
    std::vector<unsigned char>* buffer = static_cast<std::vector<unsigned char>*>(context);
    buffer->insert(buffer->end(), (unsigned char*)data, (unsigned char*)data + size);
}


extern "C" JNIEXPORT jbyteArray JNICALL Java_com_jmvsta_fmandelbackend_MandelbrotLibrary_makePicture(JNIEnv* env, jobject obj,
    jint width, jint height,
    jdouble xCenter, jdouble yCenter,
    jdouble xMin, jdouble xMax,
    jdouble yMin, jdouble yMax,
    jint maxIter, jint zoomSteps) {

    unsigned char* image = allocate_image(width, height);
    
    generate_mandelbrot(image, width, height, xCenter, yCenter, xMin, xMax, yMin, yMax, maxIter, zoomSteps);
    std::vector<unsigned char> jpegData;
    stbi_write_jpg_to_func(write_to_memory, &jpegData, width, height, 3, image, 95);

    
    free_image(image);
    if (jpegData.size() > static_cast<size_t>(std::numeric_limits<jsize>::max())) {
        return nullptr;
    }

    jsize arraySize = static_cast<jsize>(jpegData.size());


    jbyteArray result = env->NewByteArray(arraySize);
    env->SetByteArrayRegion(result, 0, arraySize, reinterpret_cast<jbyte*>(jpegData.data()));
    return result;
}
