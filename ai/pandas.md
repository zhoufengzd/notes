# pandas notes
* https://www.datadan.io/python-pandas-pitfalls-hard-lessons-learned-over-time/

## data structures
* Series: homogeneous array, size immutable.
* DataFrame: size-mutable table, column may contain different data type
* Panel: 3D labeled, size-mutable array
* Index: row labels. could use columns. identification / selection / alignment

### example:
```
import pandas as pd
import numpy as np

#### initialize dataframe
city_names = pd.Series(['San Francisco', 'San Jose', 'Sacramento'])
population = pd.Series([852469, 1015785, 485199])
cities = pd.DataFrame({ 'name': city_names, 'Population': population })

#### add new columns
cities['size'] = pd.Series([46.87, 176.53, 97.92])
cities['pop_density'] = cities['Population'] / cities['size']

cities['san-metro'] = (cities['size'].apply(lambda val: val > 50)) & (cities['name'].apply(lambda val: val.startswith("San ")))

#### check row index and column info
cities.index  ## RangeIndex(start=0, stop=3, step=1)
cities.columns ## Index(['Population', 'name', 'size', 'pop_density', 'san-metro'], dtype='object')

#### access cell
cities.loc[1, "name"]

#### set row index by column value
cities.set_index('san-metro')
cities.set_index([[1,2,0]])
cities.reindex(np.random.permutation(cities.index))  ## randomize the order

#### general statistics (mean, std, count, etc)
cities.describe()

##### dataframe read / write to csv file
df = pd.read_csv("data.csv")
df.to_csv("data.csv", sep=",", index=False)
df.info()
print(df.describe())
df.columns

df.head(n)
```
