package com.jmvsta.fmandelbackend.frac
/**
 * Этот метод позволяет генератору фракталов указать, какая часть
 * комплексной плоскости наиболее интересен для фрактала.
 * Ему передается объект прямоугольника, и метод изменяет
 * поля прямоугольника, чтобы показать правильный начальный диапазон для фрактала.
 * Эта реализация устанавливает начальный диапазон в (-2 - 1.5i) - (1 + 1.5i)
 * или x = -3, y = -1,7, width = height = 4.
 */
/**
 * Задает указанный прямоугольник, содержащий начальный диапазон,
 * подходящий для создаваемого фрактала.
 */
class Rectangle2D(var width: Double = 8.0, var height: Double = 4.0, var x: Double = -3.0, var y: Double = -1.7)