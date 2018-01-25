# jdtwGLVQ

jdtwGLVQ is a Java implementation of Generalized Learning Vector Quantization for time series in dynamic time warping spaces.

### Requirements:
* Java 8 and higher
* No dependencies to other libraries

### Table of Contents


* [Quick Start](#start)
* [Data Format](*format)
* [Datasets](#data)
* [Reference](#refs)


### <a name='start'>Quick Start</a>

1. Integrate project jdtwGLVQ into an IDE (e.g. Intellij Idea).
1. Specify datasets and parameters in file resources/DTWGLVQ.properties.
1. Run app.AppDTWGLVQ.

The property-file DGLVQ.properties describes all parameters that can be set. Using the default-setting, the program can be started without modification.


### <a name='format'>Data Format</a>

A data file contains one or more labeled time series. Every line of a data file contains a class label y and a time series (x_1, ..., x_n) in the following format:

    y  x_1  x_2  ...  x_n

The class label y must be an integer. The elements x_i of the time series are real numbers. All numbers are separated by spaces.


### <a name='data'>Datasets</a>

The folder ./data/ contains the following [UCR datasets](http://www.cs.ucr.edu/~eamonn/time_series_data/):

* CBF
* Coffee
* ECG200

Every UCR dataset consists of a directory containing a training and a test file.


### References

[1] Brijnesh Jain and David Schultz. Asymmetric Learning Vector Quantization in Dynamic Time Warping Spaces for
fast Nearest Neighbor Classification. Pattern Recognition, 76:49-366, 2018.
