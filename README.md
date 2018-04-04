# AssFinder

Утилита для более детального рассмотрения частотных списков (в таблице с возможностью ранжирования) из файлов, созданных утилитой [AssDiary](https://github.com/akondratsky/AssDiary).

Построение таблицы самых частых ассоциаций производится нажатием кнопки "Построить". Для построения используются параметры:
* "Частота слова" - минимальное пороговое значение частоты из списка частот для добавления ассоциации в таблицу
* "Глубина ассоциирования" - минимальное пороговое значение частоты появления слова при появлении данного ключевого слова
* Опция "Только глубоко ассоциированные" - для которых частота появления ассоциации выше частоты появления ключевого слова.

Двойной клик на строках из списков ключевых слов и ассоциаций добавляет данное слово в текстовую область, упрощая составление заметок.