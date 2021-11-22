# general sql notes

## analytics
### lag & lead: from current row
```
select lag(<column>, 1, <default>) over (order by ... ) as prev_<column>
select lead(<column>, 1, <default>) over (order by ... ) as next_<column>
```

### rolling average
```
select avg(<column>) over(partition by ... order by ... rows between 6 preceding and current row) as rolling_avg_7
```
