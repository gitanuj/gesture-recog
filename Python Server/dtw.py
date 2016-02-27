import numpy as np
import sys
import os
from math import sqrt

def DTWDistance(s1, s2,w):
    DTW={}
    
    w = max(w, abs(len(s1)-len(s2)))
    
    for i in range(-1,len(s1)):
        for j in range(-1,len(s2)):
            DTW[(i, j)] = float('inf')
    DTW[(-1, -1)] = 0
  
    for i in range(len(s1)):
        for j in range(max(0, i-w), min(len(s2), i+w)):
            dist= (s1[i]-s2[j])**2
            DTW[(i, j)] = dist + min(DTW[(i-1, j)],DTW[(i, j-1)], DTW[(i-1, j-1)])
        
    return sqrt(DTW[len(s1)-1, len(s2)-1])

# start script
testgesture1 = np.genfromtxt(str(sys.argv[1])).reshape(-1, 1)
#testgesture2 = np.genfromtxt(str(sys.argv[2])).reshape(-1, 1)

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

minDTWDistance = sys.float_info.max
theClass = ''

# now calculate DTWDistance for each gesture
for dataDir in dataDirs:
    currPath = dataPath + ("/" + dataDir + "/")
    print currPath
    axTrain = None

    try:
        axTrain = open(currPath + "ax.txt", "r")
    except:
        print 'couldnt open file'

    print axTrain

    arrays = [np.array(map(float, line.split())) for line in axTrain]

    for array in arrays:
        currDTW = DTWDistance(testgesture1, array, 25)
        if currDTW < minDTWDistance:
            print currDTW
            minDTWDistance = currDTW
            theClass = dataDir

print theClass
print minDTWDistance