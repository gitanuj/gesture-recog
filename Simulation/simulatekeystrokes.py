import pyautogui
#set screen dimensions
screenWidth, screenHeight = pyautogui.size()
currentMouseX, currentMouseY = pyautogui.position()

#type letters with interval 0.25 seconds
pyautogui.typewrite('Hey, Whats up?', interval=0.25) 

#move up arrow, then left arrow
pyautogui.press(['up','left'])

#type letters with interval 0.25 seconds
pyautogui.typewrite('Hey, HOw are you?', interval=0.25)

#move up twice
pyautogui.press(['up','up'])

# move mouse 10 pixels down
pyautogui.moveRel(None, 10)

#double click twice  
pyautogui.doubleClick()

#move to x and y 500 in duration 2 seconds
pyautogui.moveTo(500, 500, duration=2)  

# type with quarter-second pause in between each key
pyautogui.typewrite('Hello world!', interval=0.25)  

#key simulations
pyautogui.press('esc')
pyautogui.keyDown('shift')

#arrow simulations
pyautogui.press(['left', 'left', 'left', 'left', 'left', 'left'])
pyautogui.keyUp('shift')

#keyboard interrupt
pyautogui.hotkey('ctrl', 'c')
