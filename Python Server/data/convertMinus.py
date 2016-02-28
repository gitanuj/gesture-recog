#-*- coding: utf-8 -*-
import sys

with open(sys.argv[1]) as file:
    for line in file:
        line.replace(u'\u2212', '-')
