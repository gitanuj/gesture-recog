import sys
import os
from StringIO import StringIO
from dtw_multi import DTWDistance
from fastdtw import fastdtw
import time
import threading

class dtwThred (threading.Thread):
    MIN_DTW_DISTANCE = sys.float_info.max
    CLAZZ = 'undefined'
    LOCK = threading.Lock()

    def __init__(self, currPath, clazz, ax, ay, az, gx, gy, gz):
        threading.Thread.__init__(self)
        self.currPath = currPath
        self.clazz = clazz
        self.ax = ax
        self.ay = ay
        self.az = az
        self.gx = gx
        self.gy = gy
        self.gz = gz

    def run(self):
        # get the training data
        arraysAx = []
        arraysAy = []
        arraysAz = []
        arraysGx = []
        arraysGy = []
        arraysGz = []

        for line in open(self.currPath + "ax.txt").readlines():
            arraysAx.append(format_data(line))

        for line in open(self.currPath + "ay.txt").readlines():
            arraysAy.append(format_data(line))

        for line in open(self.currPath + "ay.txt").readlines():
            arraysAz.append(format_data(line))

        for line in open(self.currPath + "gx.txt").readlines():
            arraysGx.append(format_data(line))

        for line in open(self.currPath + "gy.txt").readlines():
            arraysGy.append(format_data(line))

        for line in open(self.currPath + "gz.txt").readlines():
            arraysGz.append(format_data(line))

        minDTWDistance = sys.float_info.max

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
            train_test.append(self.ax)
            train_test.append(self.ay)
            train_test.append(self.az)
            # train_test.append(self.gx)
            # train_test.append(self.gy)
            # train_test.append(self.gz)

            # calculate DTW
            dtwx = fastdtw(arraysAx[i], self.ax)[0]
            dtwy = fastdtw(arraysAy[i], self.ay)[0]
            dtwz = fastdtw(arraysAz[i], self.az)[0]

            currDTW = dtwx+dtwy+dtwx

            if (currDTW < minDTWDistance):
                minDTWDistance = currDTW

        print self.clazz + ": ", minDTWDistance

        dtwThred.LOCK.acquire()
        if(minDTWDistance < dtwThred.MIN_DTW_DISTANCE):
            dtwThred.MIN_DTW_DISTANCE = minDTWDistance
            dtwThred.CLAZZ = self.clazz
        dtwThred.LOCK.release()

def format_data(data):
    return [float(i) for i in data.split()]

# returns a class for space separated time series strings
def classify(ax, ay, az, gx, gy, gz):
    start_time = time.time()

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

    threads = []

    # now calculate DTWDistance for each gesture
    for dataDir in dataDirs:
        currPath = dataPath + "/" + dataDir + "/"
        t = dtwThred(currPath, dataDir, ax, ay, az, gx, gy, gz)
        threads.append(t)
        t.start()

    for t in threads:
        t.join()

    print "Took time: ", time.time() - start_time

    return dtwThred.CLAZZ

if __name__ == '__main__':
    ax = open(str(sys.argv[1])).read()
    ay = open(str(sys.argv[2])).read()
    az = open(str(sys.argv[3])).read()
    gx = open(str(sys.argv[4])).read()
    gy = open(str(sys.argv[5])).read()
    gz = open(str(sys.argv[6])).read()

    clazz = classify(ax, ay, az, gx, gy, gz)
    print 'Detected: ' + clazz