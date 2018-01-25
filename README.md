# jdtwGLVQ

jdtwGLVQ is a Java implementation of Generalized Learning Vector Quantization for time series in dynamic time warping 
spaces [[1]](#ref1).



### Table of Contents

* [Requirements](#requirements)
* [Quick Start](#start)
* [Data Format](*format)
* [Datasets](#data)
* [Reference](#refs)


### <a name='requirements'>Requirements</a>
* Java 8 and higher
* No dependencies to other libraries

### <a name='start'>Quick Start</a>

1. Integrate project ``jdtwGLVQ` into an IDE (e.g. Intellij Idea).
1. Specify datasets and parameters in file ``resources/DTWGLVQ.properties``.
1. Run main class ``AppDTWGLVQ`` in package ``app``.

The property-file ``DGLVQ.properties` describes all parameters that can be set. Using the default-setting, the program 
can be started without modification.


### <a name='format'>Data Format</a>

A data file contains one or more labeled time series. Every line of a data file contains a class label ``y`` and a time 
series ``(x_1, ..., x_n)`` in the following format:

    y  x_1  x_2  ...  x_n

The class label ``y`` must be an integer. The elements ```x_i` of the time series are real numbers. All numbers are 
separated by spaces.


### <a name='data'>Datasets</a>

The folder ``./data/`` contains the following datasets from the 
[UCR time series repository ](http://www.cs.ucr.edu/~eamonn/time_series_data/)[[2]](#refs2):

* CBF
* Coffee
* ECG200

Every UCR dataset consists of a directory containing a training and a test file.


### References

<a name='ref1'></a>
[1] Brijnesh Jain and David Schultz. *Asymmetric Learning Vector Quantization in Dynamic Time Warping Spaces for
fast Nearest Neighbor Classification*. Pattern Recognition, 76:49-366, 2018.

<a name='ref2'></a>
[2] Yanping Chen, Eamonn Keogh, Bing Hu, Nurjahan Begum, Anthony Bagnall, Abdullah Mueen and Gustavo Batista. The UCR 
Time Series Classification Archive, 2015. URL: www.cs.ucr.edu/~eamonn/time_series_data/.
