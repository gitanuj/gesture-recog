import numpy as np
import sys
import os
from StringIO import StringIO
from dtw_multi import DTWDistance
from fastdtw import fastdtw

def format_data(data):
    return [float(i) for i in data.split()]
    # return np.genfromtxt(StringIO(data)).reshape(-1, 1)

# returns a class for space separated time series strings
def classify(ax, ay, az, gx, gy, gz):
    dataPath = ''
    dataDirs = []

    # first get the directory where data lives, and all of the subdirectories of data
    currDir = os.path.dirname(os.path.realpath(__file__))
    for dirName, subDirList, fileList in os.walk(currDir):
        if dirName.endswith('data'):
            dataPath = dirName
            dataDirs = subDirList

    # prepare input data
    ax = format_data(ax)
    ay = format_data(ay)
    az = format_data(az)
    gx = format_data(gx)
    gy = format_data(gy)
    gz = format_data(gz)

    minDTWDistance = sys.float_info.max
    theClass = 'undefined'

    # now calculate DTWDistance for each gesture
    for dataDir in dataDirs:
        print 'testing against ' + dataDir
        currPath = dataPath + "/" + dataDir + "/"

        # get the training data
        arraysAx = []
        arraysAy = []
        arraysAz = []
        arraysGx = []
        arraysGy = []
        arraysGz = []

        for line in open(currPath + "ax.txt").readlines():
            arraysAx.append(format_data(line))

        for line in open(currPath + "ay.txt").readlines():
            arraysAy.append(format_data(line))

        for line in open(currPath + "ay.txt").readlines():
            arraysAz.append(format_data(line))

        for line in open(currPath + "gx.txt").readlines():
            arraysGx.append(format_data(line))

        for line in open(currPath + "gy.txt").readlines():
            arraysGy.append(format_data(line))

        for line in open(currPath + "gz.txt").readlines():
            arraysGz.append(format_data(line))

        #assuming all training sets have equal length
        for i in range(0, len(arraysAx)):
            # first add training data
            train_test = []
            train_test.append(arraysAx[i])
            train_test.append(arraysAy[i])
            train_test.append(arraysAz[i])
            # train_test.append(arraysGx[i])
            # train_test.append(arraysGy[i])
            # train_test.append(arraysGz[i])
            
            # then add test data
            train_test.append(ax)
            train_test.append(ay)
            train_test.append(az)
            # train_test.append(gx)
            # train_test.append(gy)
            # train_test.append(gz)

            # calculate DTW
            # currDTW = DTWDistance(train_test)
            dtwx = fastdtw(arraysAx[i], ax)[0]
            dtwy = fastdtw(arraysAy[i], ay)[0]
            dtwz = fastdtw(arraysAz[i], az)[0]

            currDTW = dtwx+dtwy+dtwx
            print currDTW

            if currDTW < minDTWDistance:
                minDTWDistance = currDTW
                theClass = dataDir

    return theClass

if __name__ == '__main__':
    ax = open(str(sys.argv[1])).read()
    ay = open(str(sys.argv[2])).read()
    az = open(str(sys.argv[3])).read()
    gx = open(str(sys.argv[4])).read()
    gy = open(str(sys.argv[5])).read()
    gz = open(str(sys.argv[6])).read()

    clazz = classify(ax, ay, az, gx, gy, gz)
    print 'Detected: ' + clazz