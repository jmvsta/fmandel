package com.jmvsta.fmandelbackend.frac

import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB

class FractalExplorer(
    /**
     * Целочисленный размер отображения - это ширина и высота отображения в пикселях.
     */
    private val width: Int,
    private val height: Int
) {
    /**
     * Ссылка JImageDisplay для обновления отображения с помощью различных методов как
     * фрактал вычислен.
     */
    var display: BufferedImage? = null

    /**
     * Объект FractalGenerator для каждого типа фрактала.
     */
    private val fractal: FractalGenerator

    /**
     * Объект Rectangle2D.Double, который определяет диапазон
     * то, что мы в настоящее время показываем.
     */
    private var range: Rectangle2D

    /**
     * Конструктор, который принимает размер отображения, сохраняет его и
     * инициализирует объекты диапазона и фрактал-генератора.
     */
    init {
        /** Размер дисплея   */
        /** Инициализирует фрактальный генератор и объекты диапазона.  */
        fractal = Mandelbrot()
        range = Rectangle2D(width = 4.0 * width / height)
//        display = BufferedImage(width, width, TYPE_INT_ARGB)
    }

    /**
     * Этот метод инициализирует графический интерфейс Swing с помощью JFrame, содержащего
     * Объект JImageDisplay и кнопку для очистки дисплея
     */
//    fun createAndShowGUI() {
//        /** Установите для frame использование java.awt.BorderLayout для своего содержимого.  */
//        display.setLayout(BorderLayout())
//        val myFrame = JFrame("Fractal Explorer")
//        /** Добавьте объект отображения изображения в
//         * BorderLayout.CENTER position.
//         */
//        myFrame.add(display, BorderLayout.CENTER)
//        /** Создаем кнопку очистки.  */
//        val resetButton = JButton("Reset")
//
//        /** Экземпляр ButtonHandler на кнопке сброса.  */
//        val resetHandler: com.jmvsta.FractalExplorer.ButtonHandler = com.jmvsta.FractalExplorer.ButtonHandler()
//        resetButton.addActionListener(resetHandler)
//        /** Экземпляр MouseWheelHandler в компоненте фрактального отображения.  */
//        val wheel: com.jmvsta.FractalExplorer.MouseWheelHandler = com.jmvsta.FractalExplorer.MouseWheelHandler()
//        display.addMouseWheelListener(wheel)
//        /** Вызываем операцию закрытия фрейма по умолчанию на "выход"..  */
//        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
//        /**
//         * Создаем новый объект JPanel, и добавляем панель в рамку в NORTH
//         * позиции в макете.
//         */
//        val myPanel = JPanel()
//        myFrame.add(myPanel, BorderLayout.NORTH)
//        /**
//         * Добавляем кнопку очистки
//         */
//        val myBottomPanel = JPanel()
//        myBottomPanel.add(resetButton)
//        myFrame.add(myBottomPanel, BorderLayout.SOUTH)
//        /**
//         * Размещаем содержимое фрейма, делаем его видимым и
//         * запрещаем изменение размера окна.
//         */
//        myFrame.pack()
//        myFrame.setVisible(true)
//        myFrame.setResizable(false)
//    }

    /**
     * Приватный вспомогательный метод для отображения фрактала. Этот метод проходит
     * через каждый пиксель на дисплее и вычисляет количество
     * итераций для соответствующих координат во фрактале
     * Область отображения. Если количество итераций равно -1, установит цвет пикселя.
     * в черный. В противном случае выберет значение в зависимости от количества итераций.
     * Обновит дисплей цветом для каждого пикселя и перекрасит
     * JImageDisplay, когда все пиксели нарисованы.
     */
    fun drawFractal(): BufferedImage? {
        if (display == null) {
            display = BufferedImage(width, height, TYPE_INT_ARGB)
        }
        for (i in 0 until width) {
            for (j in 0 until height) {
                drawPixel(i, j)
            }
        }
//        display.repaint()
        return display
    }

    fun drawFractal(x: Int, y: Int, direction: String): BufferedImage? {
        val xCoordinate = FractalGenerator.getCoord(range.x, range.x + range.width, width, x);
        val yCoordinate = FractalGenerator.getCoord(range.y, range.y + range.height, height, y);

        fractal.recenterAndZoomRange(range, xCoordinate, yCoordinate, if (direction == "UP") 1.5 else 0.5)

        return drawFractal()
    }

    private fun drawPixel(x: Int, y: Int) {
        /**
         * Находим соответствующие координаты xCoord и yCoord
         * в области отображения фрактала.
         */
        val xCoordinate = FractalGenerator.getCoord(range.x, range.x + range.width, width, x)
        val yCoordinate = FractalGenerator.getCoord(range.y, range.y + range.height, height, y)

        /**
         * Вычисляем количество итераций для координат в
         * область отображения фрактала.
         */
        val iteration = fractal.numIterations(xCoordinate, yCoordinate)
        /** If number of iterations is -1, set the pixel to black.  */
        if (iteration == -1) {
            display?.setRGB(x, y, java.awt.Color.yellow.rgb)
        } else {
            /**
             * В противном случае выбераем значение оттенка на основе числа
             * итераций.
             */
            val hue = 0.6f + iteration.toFloat() / 200f
            val rgbColor: Int = java.awt.Color.HSBtoRGB(hue, 13f, 1f)
            /** Обновляем дисплей цветом для каждого пикселя.  */
            display?.setRGB(x, y, rgbColor)
        }
    }

    fun reset() {
        range = Rectangle2D(width = 4.0 * width / height)
        display = null
    }


}
