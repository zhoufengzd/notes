# Mac Notes
* special keys: command, control(^), option, shift, function (fn)


## Basic setup
### sudoer
```
sudoer=$USER && sudo sh -c "echo \"$sudoer ALL=(ALL:ALL) NOPASSWD:ALL\""  #* verify first!
sudoer=$USER && sudo sh -c "echo \"$sudoer ALL=(ALL:ALL) NOPASSWD:ALL\" > /private/etc/sudoers.d/me"
```

### Service account
```
sudo dscl . -create /Users/svc
sudo dscl . -create /Users/svc UserShell /usr/local/bin/bash
sudo dscl . -create /Users/svc NFSHomeDirectory /var/svc
sudo dscl . -create /Users/svc RealName "local service"
sudo dscl . -create /Users/svc UniqueID 402
sudo dscl . -create /Users/svc PrimaryGroupID 20
sudo mkdir /var/svc
sudo chown -R svc /var/svc
```

### packages
* check system packages: `pkgutil --pkgs`
* install brew: `/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"`
    * `brew update && brew upgrade && brew tap caskroom/cask`
* services / tools:
    * core: `bash`
        * Update shell preferences -> General -> Command: /usr/local/bin/bash
    * dev language: `python@3.9 node openjdk maven gradle go`
    * dev tool: `ca-certificates mysql@5.7 minikube`
* app packages: `brew install --cask ...`
    * dev core: `docker github pycharm-ce visual-studio-code intellij-idea-ce miniconda`
    * dev tool: `google-cloud-sdk lens mysqlworkbench sequel-pro insomnia postman`
    * util: `firefox brave-browser caffeine`
    * office: `zoom microsoft-teams`
    * misc: `macfuse sfdx(salesforce)`

* xcode
```
xcode-select --install
xcode-select -p  #* check Xcode package
```

## Convenience
```
* locate command
sudo launchctl load -w /System/Library/LaunchDaemons/com.apple.locate.plist
sudo /usr/libexec/locate.updatedb

* command line
ls -l@                                          ## Show extended attributes
lsof -i 4 -a                                    ## Check active port
ifconfig | grep "inet " | grep -v 127.0.0.1     ## get local ip
```

## Terminal command line shortcut
```
* command line editor
Ctrl + A 	Go to the beginning of the line you are currently typing on
Ctrl + E 	Go to the end of the line you are currently typing on

Ctrl + L 	Clears the Screen, similar to the clear command
Ctrl + U 	Clears the line before the cursor position. If you are at the end of the line, clears the entire line.
Ctrl + H 	Same as backspace
Ctrl + C 	Kill whatever you are running
Ctrl + D 	Exit the current shell
Ctrl + Z 	Puts whatever you are running into a suspended background process. fg restores it.
Ctrl + K 	Clear the line after the cursor

* IDE window/view command
Command-F 	Find: Open a Find window, or find items in a document.
Command-G 	Find Again. Use Command-Shift-G to find the previous occurrence.
Command-H 	Hide the windows of the front app. To view the front app but hide all other apps, press Command-Option-H.
Command-M 	Minimize the front window to the Dock. To minimize all windows of the front app, press Command-Option-M.
Command-N 	New: Open an new document or window.
Command-O 	Open the selected item, or open a dialog to select a file to open.
Command-P 	Print the current document.
Command-S 	Save the current document.
Command-W 	Close the front window. To close all windows of the app, press Command-Option-W.
Command-Q 	Quit the app.

* Application shortcut
Command–Space bar 	Spotlight: Show or hide the Spotlight search field.
Command-Tab 	Switch apps: Switch to the next most recently used app among your open apps.
Command-Comma (,) 	Preferences: Open preferences for the front app.
Option-Command-Esc 	Force Quit: Choose an app to force quit.

* Screen sanpshot
Command + SHIFT + 3: print screen
Command + SHIFT + 4: screen shot selected area
```

## Show desktop
`FN + F11`

## IntelliJ
```
Command + F12: view class outline
Command + SHIFT + -: collapse definitions
```

## Visual Studio Code
```
Command + k  then Command + S: show all the shortcuts
Command + k
```