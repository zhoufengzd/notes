# string encoding notes

## references:
* https://dmitripavlutin.com/what-every-javascript-developer-should-know-about-unicode/#3unicodeinjavascript

## how to decide encoding?
* by BOM: byte order mark.
    * big endian or little endian.
    * encoding: utf8? UTF-16BE (UTF-16 big endian)? UTF-32BE? UTF-32LE?
* by tag: Content-Type: application/javascript; charset=utf-8

## terms:
* Characters and graphemes:
    * digital entities vs atomic units of written languages
* Glyph: displaying. grapheme(1) -> (N) display
* Code point:
    * Code point is a number assigned to a single character.
    * 0x0 - 0x10FFFF, 17 * 16  = 272
    * Plane 0, Basic Multilingual Plane (BMP): 0x0000–0xFFFF
    * Plane 1, Supplementary Multilingual Plane (SMP): 0x10000–0x1FFFF
    * Plane 2, Supplementary Ideographic Plane (SIP): 0x20000–0x2FFFF
* Code unit:
    * bit sequence used to encode each character within a given encoding form.
* Surrogate pair: two 16-bit code units for a single abstract character

## JavaScript Source Code and Unicode
* Counting Characters: counts code units
