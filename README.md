# Stealth
Final Project for SWEN222 MMXV

Bieleski, Bryers, Gill and Thompson MMXV

Welcome to our game Stealth Pre Alpha! (working title)

The game has two teams, spies and guards. It is the spies' mission to capture the intel
to win the game and it is the guards duty to stop the spies by well, killing them.

The game as of now only has basic functionality, proper game mechanics are on the way.

Right now the Main class is StartUpScreen.java

# Running the game

Get the file jar file of the game, make sure you have Java on your computer as well.

On Windows you should be able to just double click on the jar file to run it. If that does nothing, here's what you need to do:
- Open the command prompt.
- Navigate to the directory your jar file is placed in, this can be done by entering "cd ", followed by the directory. For example "cd /u/students/bryerscame/Downloads". If you are currently in the folder in which the jar file is in you can copy the file path and paste it into the command prompt. The file path should be near the top of the window. To check if you have navigated to the right directory you can enter "ls", this will display the contents of the directory.
- Finally to run the game enter "java -jar Stealth.jar".
- Note: the jar file may have a version number, make sure you enter that too. "java -jar Stealth-0.3.2" etc.
- Note: do not enter " or " characters into the command line.
- Note: you may not be able to use "Ctrl V" to paste into the command line, it might be a different shortcut.

# Starting a game

To start a new game the press the "Host" button and enter a username. Please make sure your username is different
from everyone elses. Same usernames may cause unwanted bugs. The host also needs to enter the number of players that want to connect, when this number is reached the game begins.

To join a user should run a new game, press "Client" and enter a username as well.

# Connecting (Same computer)

If you are running multiple games on the same computer you do not have to enter an IP address as it is already defaulted to "localhost".

# Connecting (Local)

The hosting player should give out their IP address so the clients can enter the address into the IP field.
The IP address can be obtained in the command line:
  
  Linux - # /sbin/ifconfig
  
  Windows - # /username/ipconfig
  
  Mac - # ifconfig | grep inet
  
# Controls

- Movement: (Arrow Keys)
- Change Camera Angle: (Z, X)




