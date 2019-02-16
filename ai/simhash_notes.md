# simhash notes
* Mapping high-dimensional vectors to small-sized fingerprints.
* Similar documents have similar hash values.

## references
* https://ferd.ca/simhashing-hopefully-made-simple.html
* https://datawhatnow.com/simhash-question-deduplicatoin/
* https://godoc.org/github.com/mfonda/simhash
* sunset.usc.edu/classes/cs572_2010/YHung.ppt
* https://janzhou.org/lsh/

## what's simhash feature?
* words, word n-grams, character n-grams

## simhash hash functions

## terms
* n-grams:
    * contiguous sequence of n items from a given sample of text or speech.
    * When the items are words, n-grams may also be called shingles
    * {1 2 3 4} => {1 2, 2 3, 3 4}
* skip gram:
    * {1 2 3 4} => {1 3, 2 4} + {1 2, 2 3, 3 4}
* n-gram model:
    * models sequences, notably natural languages, using n-grams.
* minhash:
    * Jaccard similarity coefficient: set similarity
        * J(A,B) = (A intersect B) / (A + B)
