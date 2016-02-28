import numpy as np
import sys
import os
from math import sqrt
from decimal import *
from StringIO import StringIO
from dtw_multi import DTWDistance

# start script
#test gesture should be a single line containing all (ax,ay,az,gx,gy,gz) readings of a single gesture
testgesture1 = np.genfromtxt(str(sys.argv[1])).reshape(-1, 1)

dataPath = ''
dataDirs = []

# first get the directory where data lives, and all of the subdirectories of data
currDir = os.path.dirname(os.path.realpath(__file__))
for dirName, subDirList, fileList in os.walk(currDir):
    if dirName.endswith('data'):
        print 'whoa'
        dataPath = dirName
        dataDirs = subDirList

print dataPath
print dataDirs

minDTWDistance = Decimal(sys.float_info.max)

minAX = Decimal(sys.float_info.max)
minAY = Decimal(sys.float_info.max)
minAZ = Decimal(sys.float_info.max)
minGX = Decimal(sys.float_info.max)
minGY = Decimal(sys.float_info.max)
minGZ = Decimal(sys.float_info.max)

theClass = ''

# now calculate DTWDistance for each gesture
for dataDir in dataDirs:
    currPath = dataPath + ("/" + dataDir + "/")
    print currPath

    axTrain = None
    ayTrain = None
    azTrain = None
    gxTrain = None
    gyTrain = None
    gzTrain = None

    try:
        axTrain = open(currPath + "ax.txt", "r")
    except:
        print 'couldnt open ax train'

    try:
        ayTrain = open(currPath + "ay.txt", "r")
    except:
        print 'couldnt open ay train'

    try:
        azTrain = open(currPath + "az.txt", "r")
    except:
        print 'couldnt open az train'

    try:
        gxTrain = open(currPath + "gx.txt", "r")
    except:
        print 'couldnt open gx train'

    try:
        gyTrain = open(currPath + "gy.txt", "r")
    except:
        print 'couldnt open gy train'

    try:
        gzTrain = open(currPath + "gz.txt", "r")
    except:
        print 'couldnt open gz train'

    # arraysAx = [np.array(map(Decimal, line.split())) for line in axTrain]
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
        for i in range(1, len(sys.argv)):
            train_test.append(np.genfromtxt(str(sys.argv[i])).reshape(-1, 1))

        # calculate DTW
        currDTW = DTWDistance(train_test)
        if currDTW < minDTWDistance:
            print currDTW
            minDTWDistance = currDTW
            theClass = dataDir

print theClass
print minDTWDistance