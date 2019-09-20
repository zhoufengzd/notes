# javascript / node Notes
* ECMAScript (ES) is a standard, while JavaScript is the implementation
* Version:
    * ES8: 2017
    * ES7: 2016
    * ES6: 2015
    * ES3: 1999

## 1. installation
### node, npm, webpack
```
brew update && install node # npm will be installed with Node
npm install -g npm@latest   # update to latest
npm i -g webpack-cli
npm i -g webpack
npm i -g swagger-cli
npm i node-config
npm config ls -l
npm install -g npm-check-updates    # npm package update
ncu -a && npm   # update package.json and update all the packages

* list installed
npm list -g --depth 0
```

### npm
* use npm publish and npm install to maintain a centralized module registry
* npm init: generate package.json
* modules:
    * installed under node_modules/<package>
    * var <pkg> = require('<package>')
    * var <local_packge> = require("./<path_to_local_packge>")
* npm publish

### Node.js
* EventEmitter: observer pattern
* express: web application framework for Node.js

### Webpack
* Accepts both require() and import module syntaxes
* Allows for very advanced code splitting

## 2. run & debug
### command line
```
node test.js
.exit
```

### ui debugger: chrome
* https://developers.google.com/web/tools/chrome-devtools/javascript/

## 3. programming notes:
* references:
    * Eloquent JavaScript
    * https://medium.freecodecamp.org/javascript-modules-a-beginner-s-guide-783f7d7a5fcc
* values: generic object, could be values or functions or anything
* bindings: variable that was assigned to a value
* higher-order functions: function as parameter or return value
* getters / setters:
    * let v = { get size() { return 100; } };
    * console.log(v.size);
* modules: dependencies(import) + interface(exports)
* CommonJS: require function
* script: for regular scripts with a global namespace
* map-reduce:
```
    * collection.reduce((accumulator, currentElement, currentIndex, collectionCopy) =>
            {/*function body*/},
            initialAccumulatorValue
        );
```
* spread syntax: ...

## 4. common libraries
* https://github.com/sindresorhus/awesome-nodejs

### puppeteer:
* api to control headless chrome over the devtools protocol
* crawl a spa and generate pre-rendered content (i.e. "ssr").
* automate form submission, ui testing, keyboard input
* limitations:
    * does not support licensed formats such as AAC or H.264.
    * does not support HTTP Live Streaming (HLS).

### testing frameworks:
* mocha: runs on Node.js.
* jest: Facebook. react project
* jasmine: does not rely on browsers, DOM, or any particular framework

### cheerio:
* subset of core jQuery
* blazingly fast
* incredibly flexible

### apify:
* web scraping and automation SDK for JavaScript / Node.js
