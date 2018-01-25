# jDGLVQ - Java Implementation of DTW Generalized Learning Vector Quantization

This library extends GLVQ to DTW spaces using an asymmetric update rule.

Requirements:
- Java 8 and higher
- no dependencies to other libraries


# Table of Contents -----------------------------------------------------------

- Quick Start
- Data Format
- Datasets
- Reference
- Contact


# Quick Start -----------------------------------------------------------------

1. Integrate project jDGLVQ into an IDE (e.g. Eclipse).
2. Specify datasets and parameters in file resources/DGLVQ.properties.
3. Run app.AppDTWGLVQ.

The property-file DGLVQ.properties describes all parameters that can be set.
Using the default-setting, the program can be started without modification.


# Data Format  ----------------------------------------------------------------

A data file contains one or more labeled time series. Every line of a data file
contains a class label y and a time series (x_1, ..., x_n) in the following format:

    y  x_1  x_2  ...  x_n

The class label y must be an integer. The elements x_i of the time series are
real numbers. All numbers are separated by spaces.


# Datasets  -------------------------------------------------------------------

The folder ./data/ contains the following UCR datasets:

- CBF
- Coffee
- ECG200

Every UCR dataset consists of a directory containing a training and a test file.
UCR Source: http://www.cs.ucr.edu/~eamonn/time_series_data/


# References  -----------------------------------------------------------------

If you find jDGLVQ helpful, please cite it as

B. Jain and D. Schultz.
Asymmetric Learning Vector Quantization in Dynamic Time Warping Spaces for
fast Nearest Neighbor Classification.
arXiv, 2017.


# Contact  --------------------------------------------------------------------

For any questions and comments, please email brijnesh.jain(at)gmail.com


