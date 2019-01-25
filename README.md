# NumberRunningTextView RecyclerView中使用

滑动到该view自动滚动数字，带框

```java
/**
 * Created by JiaShuai on 2018/6/22.
 * RecycleView 中使用情参考该类
 * 仅供参考，懒得写实例了
 */

    @Override
    public void onBindViewHolder(OperationInfoHolder holder, int position) {
        //只需要调用setNum即可
         holder.number_running_tv.setNum(String.valueOf(data.loanAmount));

    }


```

![](https://github.com/jiashuaishuai/NumberRunningTextView/blob/master/6EBFB1CA24A498FA7E2E61F916356858.gif)
