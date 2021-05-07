# Occam's razor
* clear / efficient code examples

## safe_get
* get value from a dictionary, add missing value if not there
```
def safe_get(dictx, key, default_value):
    v = dictx.get(key, None)
    if not v:
        v = default_value
        dictx[key] = v
    return v
```
