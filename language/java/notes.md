# java notes
* Thinking in java
* Swing

## Inversion of control (IOC)
*

## "== vs equals"
* "==" checks type and never throws NullPointerException
* "equals" checks the underline object

## Java Heap vs Stack
* sharing:
    * stack: thread private
    * heap: shared between threads
* recycle:
    * stack: LIFO
    * heap:


## Memory:
* Young => Tenured => Perm
* GC runs in Perm as well if space is full or nearly full
* Issues:
    * GC pause application: -Xlog:safepoint
    * memory leaks: static final object...
    * High allocation rates can cause performance issues
    * Young objects are promoted too quickly
    * Too few GC threads: -XX:ParallelGCThreads=4
