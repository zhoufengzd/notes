# Puppeteer Notes
* api to control headless chrome over the devtools protocol

## side notes:
* puppeteer creates new chromium user profile on every run.
* chromium: an open-source on Google Chrome.
* promise: result of an asynchronous operation, succeeded or failed.

## debug:
* const browser = await puppeteer.launch({headless:false, slowMo:250});
* page.on('console', msg => console.log('PAGE LOG:', msg.text()));

## devtools protocol
* use web socket?

## classes

### Page
* Events: page.on('close'), etc
* Methods:
    * page.content(): full html contents of the page
    * page.cookies()
    * page.frames()
    * output: page.pdf(), page.screenshot()

### SecurityDetail
* not always available


## Other language
* Chrome Debugging Protocol (CDP)
