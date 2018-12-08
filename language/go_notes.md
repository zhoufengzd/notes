# Golang Notes

## design thoughts:
* address issues: unsafe, slow builds, concurrency support, tools
* support:
    * interfaces and type embedding
    * statically linked native binaries / single, self-contained binary
* exclude intentionally:
    * type inheritance, but mixings
    * method or operator overloading
    * pointer: no need to figure out if it's on stack or heap
    * generics
* advocates:
    * easy to adopt for new graduates
    * verbose but readable, unsophisticated but intelligible, tedious but predictable
    * language of infrastructure as a service, cloud orchestration, and devops
* cons:
    * dependency management / package version
    * vibrant community, but profoundly stubborn

## programming notes:
### references:
* https://talks.golang.org/2012/goforc.slide
* https://www.toptal.com/go/go-programming-a-step-by-step-introductory-tutorial
* https://bravenewgeek.com/go-is-unapologetically-flawed-heres-why-we-use-it/
* https://peter.bourgon.org/go-in-production/
* https://gobyexample.com/
* https://bluxte.net/musings/2018/04/10/go-good-bad-ugly/
* http://devs.cloudimmunity.com/gotchas-and-common-mistakes-in-go-golang/

### some language notes:
* struct types and methods
* Upper case name is exported
* comments: c style
* compiler: gc, gccgo
* No enums: use const
* array and slice:
    * array: fixed size
    * slice: make([]T, len, cap) []T. copy(dst, src []T)
* blank identifier: underscore, to mark a discarded part.
* go routing:
* unit test:

### command line and environment
* build, clean, doc, fix, fmt, get, install, list, run, test, vet
* environment:
    * src: source files, used by import
    * pkg: package objects
    * bin: executable commands
    * setup:
        * go get: download to $GOPATH/src
        * go install: $GOPATH/pkg, $GOPATH/bin

### recommendations:
* config: use package flag
* test: package testing

## web service:
* echo: https://github.com/labstack/echo
* martini: https://github.com/go-martini/martini
