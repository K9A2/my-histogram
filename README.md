# Course Project One, Fall 2018: Plotting static and dynamic BarCharts

The goal of the project is to finish 3 programs (Task A, B, C) to plot simple 
barcharts (histograms), grouped-barcharts, stacked-barcharts and barchart 
animations with given tools.

## Task A (Enhancing HistogramA)

Given HistogramA.java and HistogramATest.java, use two given tools, 
javax.json (glassfish implementation javax-json-1.1.4.jar is given, refer
https://www.oracle.com/technetwork/articles/java/json-1973242.html) and Stddraw 
(Stddraw.java, Stddraw.html are given), to draw a color histogram with 
annotations and scales by reading a Json-formatted file with data and specific 
drawing formats.

Based-on the given programs, add the following features:
+ A.1 All the parameter values in the formats part and the minvalue setting of 
  the data part could be either customized by the given values or by default 
  values.
+ A.2 Optimize the setting values of the default parameters in order to get a 
  professional outlook for the histogram using default values. (Refer RGB color 
  selection: https://www.rapidtables.com/web/color/RGB_Color.html )
+ A.3 Complete plotRightRuler();
+ A.4 To achieve better outlook, various types of fonts and location setting for
  all kinds of texts in the histogram should be customized by the given 
  parameters in the input Json file and should also work with default values.

Three sample output of HistogramA are given in the project reference.

## Task B (Grouped-Barcharts and Stacked-Barcharts)

Based on the Task A, write a program to read a Json formatted data and drawing 
specific requirements file and draw color multi-dimensional (grouped) histograms
or stacked barcharts (drawing categories based on the input parameter) with 
scales, annotations and legends. Four sample drawing barcharts are given as 
examples in the project reference.

## Task C (Animation of Dynamic Barcharts)

View the given video, write a program to implement an animation of dynamic 
barcharts with sorted data according to the data and drawing parameters reading 
from a Json file.

Tips: 
+ Use StdDraw's show(), pause(), enabledoublebuffering(), 
  disabledoublebuffering() to implement animation.
+ Between two sets of data, linear interpolation can be used to calculate 
  multiple sets of intermediate evolution data, each time show a set (if 
  necessary, sort or adjust the order), pause a number of milliseconds, and 
  continue to draw the next set of data.

To ensure the continuous visual effect of the animation, you may calculate the 
data for all groups (including interpolation groups) first, and then draw them 
separately in a sequence to the canvas.

All the data used in plotting should be came from real world and it is 
recommended to use the latest domestic and international statistics data 
published by the National Bureau of Statistics 
(http://data.stats.gov.cn/index.htm).

For Task A, at least 3 sets of data are required. For Task B, at least 4 sets of
data are required (at least 2 sets for grouped barcharts, and at least 2 sets 
for stacked barcharts). For Task C, at least 2 sets of data are required for 
barchart animations.

The project working duration is in four weeks. Feel free to use the Project 
Reference Materials attached.


