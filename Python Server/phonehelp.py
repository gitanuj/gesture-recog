from record_data import record
from parse_data import parse
from classify import classify
import pyautogui

def perform_action(clazz):
	if clazz == 'flip':
		pyautogui.hotkey('command', 'space')
	elif clazz == 'top_down':
		pyautogui.typewrite('Hello world!')
	elif clazz == 'right_left':
		pyautogui.typewrite('Hello world!')

if __name__ == '__main__':
	while 1:
		data = record()
		parsed_data = parse(data)

		ax = parsed_data[0]
		ay = parsed_data[1]
		az = parsed_data[2]
		gx = parsed_data[3]
		gy = parsed_data[4]
		gz = parsed_data[5]

		clazz = classify(ax, ay, az, gx, gy, gz)
		perform_action(clazz)
		print 'Detected: ' + clazz