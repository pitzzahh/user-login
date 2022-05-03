# user-login
## simple user login command line interface program
* On a Windows machine. All the files can be found in the ```C:\ ```drive
```BASH
    C:\Users\<user>\TaskPerf6Files\
```
* On a Linux machine. All files can be found in
``` BASH
    /home/<user>/TaskPerf6Files
```
* User credentials are stored in users own folder in ```C:\Users\<user>\TaskPerf6Files\ + username```. User folders are named the same as the username.
* There is a text file called ```records``` that contains the summary of all user credentials. The text file is in
```BASH
    C:\Users\<user>\TaskPerf6Files\
```
#### When the program runs on the first time, the directory will be created. If it does exist, it will ignore creating the directory.
### The program contains handling exceptions for:
* Invalid input (inputs that do not meet instructions, containing special characters, containing numbers, and blank inputs)
* Invalid credentials (username or password)
* User checking (if a user already exists)
## Note: 
* ~~The directory where the files are stored is only available on Windows machines. No implementation for Linux machines. I don't know how.
Will implement soon.~~
* The program can now run on both Windows and Linux machines.
## snapshots
<p align="left">
  <img src="https://github.com/pitzzahh/user-login/blob/main/main_menu.png" height="200"/>
</p>
<p align="left">
  <img src="https://github.com/pitzzahh/user-login/blob/main/login_menu.png" height="200"/>
</p>
<p align="left">
  <img src="https://github.com/pitzzahh/user-login/blob/main/register_menu.png" height="200"/>
</p>

![GitHub Issues](https://img.shields.io/github/issues/pitzzahh/user-login)
![Forks](https://img.shields.io/github/forks/pitzzahh/user-login)
![Stars](https://img.shields.io/github/stars/pitzzahh/user-login)
![License](https://img.shields.io/github/license/pitzzahh/user-login)
