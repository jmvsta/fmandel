<h1>This is fractal explorer in-browser application</h1>
<p>Application allows to zoom in and out Mandelbrot fractal. There are 2 backend realizations: CPU and GPU</p>
<h2>CPU fractal explorer with kt coroutines</h2>
<h3>Local start</h3>
<li>
    Start backend application by executing commands (from project directory):
    <p><code>cd fmandel-backend</code></p>
    <p><code>./gradlew run</code></p>
</li>
<li>Start kt js application by executing commands:
    <p><code>cd fmandel-frontend</code></p>
    <p><code>./gradlew run</code></p>
</li>
<h2>GPU fractal calculation with CUDA dll lib on C++</h2>
<h3>Local start</h3>
<li>Firstly replace FmandelBackendCPUApplicationKt with FmandelBackendGPUApplicationKt in build.gradle.kts of fmandel-backend module</li>
<li>
    Start backend application by executing commands (from project directory):
    <p><code>cd fmandel-backend</code></p>
    <p><code>./gradlew run</code></p>
</li>
<li>Start kt js application by executing commands:
    <p><code>cd fmandel-frontend</code></p>
    <p><code>./gradlew run</code></p>
</li>
