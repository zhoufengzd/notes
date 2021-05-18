# numpy notes
* highly optimized package to manipulate numeric arrays
* http://cs231n.github.io/python-numpy-tutorial/

## np array
* creating arrays:
```
a = np.arange(3)            # array([0, 1, 2])
b = np.arange(1, 9, 2)      # array([1, 3, 5, 7])
c = np.linspace(0, 1, 6)    # start, end, num-points
d = np.random.rand(4)       # uniform in [0, 1]
e = np.random.randn(4)      # Gaussian
f = np.array([True, False, False, True])

# check type
f.dtype                     # dtype('bool')
```
* indexing and slicing:
    * a[-1]: last item
    * data[from:to]: slice
    * train = data[:split_index, :]     # data split from 0 to split_index
    * test = data[split_index:, :]      # data split from split_index to the end
    * np.r_: row wise merging
        * example: np.r_[np.array([1,2,3]), 0, 0, np.array([4,5,6])]
```
a = np.diag(np.arange(3))
a[1, 1]
a[1:3]
a[-1]    # last item
```
* array reshaping
```
np.array([1,2,3]).reshape((-1,1))   # [[1], [2]]
```
