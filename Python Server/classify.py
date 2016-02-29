import numpy as np
import sys
import os
from StringIO import StringIO
from dtw_multi import DTWDistance

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
    ax = np.genfromtxt(StringIO(ax)).reshape(-1, 1)
    ay = np.genfromtxt(StringIO(ay)).reshape(-1, 1)
    az = np.genfromtxt(StringIO(az)).reshape(-1, 1)
    gx = np.genfromtxt(StringIO(gx)).reshape(-1, 1)
    gy = np.genfromtxt(StringIO(gy)).reshape(-1, 1)
    gz = np.genfromtxt(StringIO(gz)).reshape(-1, 1)

    minDTWDistance = sys.float_info.max
    theClass = ''

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
            arraysAx.append(np.genfromtxt(StringIO(line)).reshape(-1, 1))

        for line in open(currPath + "ay.txt").readlines():
            arraysAy.append(np.genfromtxt(StringIO(line)).reshape(-1, 1))

        for line in open(currPath + "ay.txt").readlines():
            arraysAz.append(np.genfromtxt(StringIO(line)).reshape(-1, 1))

        for line in open(currPath + "gx.txt").readlines():
            arraysGx.append(np.genfromtxt(StringIO(line)).reshape(-1, 1))

        for line in open(currPath + "gy.txt").readlines():
            arraysGy.append(np.genfromtxt(StringIO(line)).reshape(-1, 1))

        for line in open(currPath + "gz.txt").readlines():
            arraysGz.append(np.genfromtxt(StringIO(line)).reshape(-1, 1))

        #assuming all training sets have equal length
        for i in range(0, len(arraysAx)):
            # first add training data
            train_test = []
            train_test.append(arraysAx[i])
            train_test.append(arraysAy[i])
            train_test.append(arraysAz[i])
            train_test.append(arraysGx[i])
            train_test.append(arraysGy[i])
            train_test.append(arraysGz[i])
            
            # then add test data
            train_test.append(ax)
            train_test.append(ay)
            train_test.append(az)
            train_test.append(gx)
            train_test.append(gy)
            train_test.append(gz)

            # calculate DTW
            currDTW = DTWDistance(train_test)
            print currDTW

            if currDTW < minDTWDistance:
                minDTWDistance = currDTW
                theClass = dataDir

    return theClass