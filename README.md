# SwipeReflashLayout_demo

google官方实现的下啦刷新控件，swipereflashlayout，默认没有上拉加载功能，所以职能自己实现了

原理：自定义listview，LoadMoreListview设置footerview，自定义接口监听，子类实现onLoadMore方法


    @Override
    public void onLoadMore(AbsListView view) {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                curNum++;
//                if(curNum>=5){
                    getNewBottomData();
                    myAdapter.notifyDataSetChanged();
                    loadMoreListView.onLoadMoreComplete();
                    loadMoreListView.setNoMoreToLoad(false);
//                }


            }
        }, 1000);

    }
    
    swipereflashlayout，实现OnRefreshListener方法，然后重写：onRefresh
    
    
    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_swipe_refresh.setRefreshing(true); //请求开始的时候
                getNewTopData();
                myAdapter.notifyDataSetChanged();
                layout_swipe_refresh.setRefreshing(false); // xx 请求结束的时候
            }
        }, 1000);
        
        
