#ifndef KERNEL_CUH
#define KERNEL_CUH

#include <cuda_runtime.h>
#include <device_launch_parameters.h>

extern "C" {
    __declspec(dllexport) unsigned char* allocate_image(int width, int height);
    __declspec(dllexport)  void free_image(unsigned char* image);
    __declspec(dllexport)  void generate_mandelbrot(unsigned char* image, int width, int height,
        double x_center, double y_center,
        double x_min, double x_max,
        double y_min, double y_max,
        int max_iter, int zoom_steps);
    __declspec(dllexport) void save_image(const char* filename, unsigned char* image, int width, int height);
}

#endif // KERNEL_CUH