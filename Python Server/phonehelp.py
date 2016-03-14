from record_data import record
from parse_data import parse
from classify import classify
import pyautogui
from keyboard_input import record_keyboard_data
import threading
import time

class keyboardThread (threading.Thread):
	def run(self):
		while 1:
			try:
				data = record_keyboard_data()
				pyautogui.typewrite(data)
				pyautogui.press('enter')
			except Exception as e:
				print "Something went wrong in keyboardThread: ", e

class gestureThread (threading.Thread):
	def perform_action(self, clazz):
		if clazz == 'flip':
			pyautogui.hotkey('command', 'space')
		elif clazz == 'top_down':
			pyautogui.press('space')
		elif clazz == 'right_left':
			pyautogui.press('right')
		print "dummy"

	def run(self):
		while 1:
			try:
				data = record()
				parsed_data = parse(data)

				ax = parsed_data[0]
				ay = parsed_data[1]
				az = parsed_data[2]
				gx = parsed_data[3]
				gy = parsed_data[4]
				gz = parsed_data[5]

				clazz = classify(ax, ay, az, gx, gy, gz)
				self.perform_action(clazz)
				print 'Detected: ' + clazz
			except Exception as e:
				print "Something went wrong in gestureThread: ", e

if __name__ == '__main__':
	t1 = keyboardThread()
	t1.setDaemon(True)
	t1.start()
	
	t2 = gestureThread()
	t2.setDaemon(True)
	t2.start()

	while threading.active_count() > 0:
		time.sleep(1)
	