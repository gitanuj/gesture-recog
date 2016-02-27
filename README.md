# gesture-recog
CS 290F: Smartphone centric systems and applications
Project: PhoneHelp-Motion Gesture Recognition
Team Members:
* [Alick](https://github.com/alickrxu)
* [Harshitha](https://github.com/harshithachidanand)
* [Tanuj](https://github.com/gitanuj)

##ABSTRACT
Motion sensors like accelerometers and gyroscopes are present in almost all the smartphones nowadays. They are capable of detecting motion reasonably well and can be used with a training model to accurately recognize gestures. These gestures can then be used to perform actions on a PC, like switching slides in a presentation or playing/pausing music. The smartphone can stream raw motion sensor readings to a PC over local network. The PC can process the data, recognize gestures and perform corresponding actions. Such a setup can be useful for controlling the PC remotely without requiring user’s attention.

##USE CASES

* When presenting a slide deck, a user can use their phone to move through the slides.
* When a computer is far away, the user can gesture to open up different applications.
* At home, a user can use their phone for home automation, i.e raising/lowering temperature or volume on their TV.

##DEPENDENCIES

### Simulating keystrokes Dependencies

* On Windows, PyAutoGUI has no dependencies (other than Pillow and some other modules, which are installed by pip along with PyAutoGUI). It does not need the pywin32 module installed since it uses Python’s own ctypes module.

* On OS X, PyAutoGUI requires PyObjC installed for the AppKit and Quartz modules. The module names on PyPI to install are pyobjc-core and pyobjc (in that order).

* On Linux, PyAutoGUI requires python-xlib (for Python 2) or python3-Xlib (for Python 3) module installed.
