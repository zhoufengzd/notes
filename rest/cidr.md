# CIDR (/ˈsaɪdər, ˈsɪ-/) notes
* CIDR: Classless Inter-Domain Routing
* based on variable-length subnet masking (VLSM)

## CIDR notation
* IP address / number = base ip / mask bit count
    * example for a 3 bit address subnet:
        * 7/2 == 111/2 => (11)* => subnet: 110, 111

## References:
### Classful addressing definition (A-E)
* A: 8 bit, 128 addresses (2^7)
* B: 16 bit, 16,384 addresses (2^14)
* C: 24 bit, 2,097,152 addresses (2^21)
```
In the following bit-wise representation,

    n indicates a bit used for the network ID.
    H indicates a bit used for the host ID.
    X indicates a bit without a specified purpose.
Class A
  0.  0.  0.  0 = 00000000.00000000.00000000.00000000
127.255.255.255 = 01111111.11111111.11111111.11111111
                  0nnnnnnn.HHHHHHHH.HHHHHHHH.HHHHHHHH

Class B
128.  0.  0.  0 = 10000000.00000000.00000000.00000000
191.255.255.255 = 10111111.11111111.11111111.11111111
                  10nnnnnn.nnnnnnnn.HHHHHHHH.HHHHHHHH

Class C
192.  0.  0.  0 = 11000000.00000000.00000000.00000000
223.255.255.255 = 11011111.11111111.11111111.11111111
                  110nnnnn.nnnnnnnn.nnnnnnnn.HHHHHHHH
```
