import numpy as np
import sys
import os
from math import sqrt
from dtw_multi import DTWDistance

dataPath = ''
dataDirs = []

# first get the directory where data lives, and all of the subdirectories of data
currDir = os.path.dirname(os.path.realpath(__file__))
for dirName, subDirList, fileList in os.walk(currDir):
    if dirName.endswith('data'):
        dataPath = dirName
        dataDirs = subDirList

def ClassifyGesture(ax, ay, az, gx, gy, gz):
    minDTWDistance = sys.float_info.max

    theClass = ''

    # now calculate DTWDistance for each gesture
    for dataDir in dataDirs:
        currPath = dataPath + ("/" + dataDir + "/")

        # get the training data
        arraysAx = []
        arraysAy = []
        arraysAz = []
        arraysGx = []
        arraysGy = []
        arraysGz = []

        temp = []
        with open(currPath + "ax.txt") as f:
            temp = f.readlines()
        for line in temp:
            arraysAx.append(np.genfromtxt(StringIO(line)).reshape(-1, 1))

        temp = []
        with open(currPath + "ay.txt") as f:
            temp = f.readlines()
        for line in temp:
            arraysAy.append(np.genfromtxt(StringIO(line)).reshape(-1, 1))

        temp = []
        with open(currPath + "az.txt") as f:
            temp = f.readlines()
        for line in temp:
            arraysAz.append(np.genfromtxt(StringIO(line)).reshape(-1, 1))

        temp = []
        with open(currPath + "gx.txt") as f:
            temp = f.readlines()
        for line in temp:
            arraysGx.append(np.genfromtxt(StringIO(line)).reshape(-1, 1))

        temp = []
        with open(currPath + "gy.txt") as f:
            temp = f.readlines()
        for line in temp:
            arraysGy.append(np.genfromtxt(StringIO(line)).reshape(-1, 1))

        temp = []
        with open(currPath + "gz.txt") as f:
            temp = f.readlines()
        for line in temp:
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
            train_text.append(ax)
            train_text.append(ay)
            train_text.append(az)
            train_text.append(gx)
            train_text.append(gy)
            train_text.append(gz)

            # calculate DTW
            currDTW = DTWDistance(train_test)
            if currDTW < minDTWDistance:
                print currDTW
                minDTWDistance = currDTW
                theClass = dataDir

    return theClass

