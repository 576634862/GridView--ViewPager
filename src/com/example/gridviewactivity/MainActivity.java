package com.example.gridviewactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
public class MainActivity extends Activity {
	private LinearLayout mLinViewPager;
	private LinearLayout mLinViewGroup;
	private List<Map<String,Object>> listView;
	private int next = 0;
	private ViewPager adViewPager;//创建ViewPager
	private AdPageAdapter adapter; 
	private ImageView[] imageViews;//图片数组集合
	private ImageView imageView;  
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	private List<View> gridViewlist = new ArrayList<View>();//页数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}
	private void initView() {
		System.out.println("initView()");
		mLinViewPager = (LinearLayout)findViewById(R.id.view_pager_content);
		mLinViewGroup = (LinearLayout)findViewById(R.id.viewGroup);
		listView = new ArrayList<Map<String,Object>>();
		//创建ViewPager
    	adViewPager = new ViewPager(this);
    	
    	//获取屏幕像素相关信息
    	DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
    	
        //根据屏幕信息设置ViewPager广告容器的宽高
        adViewPager.setLayoutParams(new LayoutParams(dm.widthPixels, dm.heightPixels));
        
        //将ViewPager容器设置到布局文件父容器中
        mLinViewPager.addView(adViewPager);
    	
        getView();
    	
    	initCirclePoint();
    	
    	adViewPager.setAdapter(adapter);
    	adViewPager.setOnPageChangeListener(new AdPageChangeListener());
    	
    	/**自动切换的代码段*/
//    	new Thread(new Runnable() {  
//            @Override  
//            public void run() {  
//                while (true) {  
//                    if (isContinue) {  
//                        viewHandler.sendEmptyMessage(atomicInteger.get());  
//                        atomicOption();  
//                    }  
//                }  
//            }  
//        }).start(); 
	}
	/*
     * 每隔固定时间切换广告栏图片
     */
    private final Handler viewHandler = new Handler() {  
  
        @Override  
        public void handleMessage(Message msg) { 
        	System.out.println("handleMessage");
        	adViewPager.setCurrentItem(msg.what);  
            super.handleMessage(msg);  
        }  
  
    }; 
	private void atomicOption() {  
		System.out.println("atomicOption()");
        atomicInteger.incrementAndGet();  //切换下一页
        if (atomicInteger.get() > imageViews.length - 1) {  
        	atomicInteger.getAndAdd(-5);  
        }  
        try {  
            Thread.sleep(2000);  
        } catch (InterruptedException e) {  
              
        }  
    } 
	/**
	 *	ViewPager 页面改变监听器 
	 */
    private final class AdPageChangeListener implements OnPageChangeListener {  
    	
    	/**
    	 * 页面滚动状态发生改变的时候触发
    	 */
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
        }  
  
        /**
         * 页面滚动的时候触发
         */
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
        }  
  
        /**
         * 页面选中的时候触发
         */
        @Override  
        public void onPageSelected(int arg0) {  
        	//获取当前显示的页面是哪个页面
        	System.out.println("onPageSelected");
            atomicInteger.getAndSet(arg0);  
            //重新设置原点布局集合
            for (int i = 0; i < imageViews.length; i++) {  
                imageViews[arg0].setBackgroundResource(R.drawable.shape_dot_display);  
                if (arg0 != i) {  
                    imageViews[i].setBackgroundResource(R.drawable.shape_dot_default);  
                }  
            }  
        }  
    } 
    /**初始化小圆点*/
	private void initCirclePoint(){
		System.out.println("initCirclePoint()");
        imageViews = new ImageView[gridViewlist.size()];  
        //广告栏的小圆点图标
        for (int i = 0; i < gridViewlist.size(); i++) {  
        	//创建一个ImageView, 并设置宽高. 将该对象放入到数组中
        	imageView = new ImageView(this);
        	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);  
    		layoutParams.setMargins(10,0,10, 0);  //可以根据主流的分辨率设置
            imageView.setLayoutParams(layoutParams); 
            imageViews[i] = imageView;  
            
            //初始值, 默认第0个选中
            if (i == 0) {  
                imageViews[i].setBackgroundResource(R.drawable.shape_dot_display);  
            } else {  
                imageViews[i].setBackgroundResource(R.drawable.shape_dot_default);  
            } 
            //将小圆点放入到布局中
            mLinViewGroup.addView(imageViews[i]); 

        } 
	}
	private void getView() {
		
		int[] icon = {R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,R.drawable.five,R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine,R.drawable.ten
				,R.drawable.eleven,R.drawable.twelve,R.drawable.thirteen,R.drawable.fourteen,R.drawable.fifteen,R.drawable. sixteen,R.drawable.seventeen};
		for (int i = 0; i < icon.length; i++) {
			Map<String,Object> mapView = new HashMap<String, Object>();
			mapView.put("image", icon[i]);
			listView.add(mapView);
		}
		getGridView();
	}
	/**
	 * 主要逻辑代码实现
	 */
	private void getGridView() {
		System.out.println("getGridView"+listView.size());//所有item的总个数17
		boolean bool = true;
		while (bool) {
			int result = next+8;
			System.out.println("加载的result"+result);//当前页的item个数8.--》16--》24  viewpager默认把前后也都初始化 24
			if(listView.size() != 0 && result<listView.size()) {
				System.out.println("item的个数"+result);//8------》16
				GridView gridView = new GridView(this);//新建GridView
				gridView.setNumColumns(4);
				List<Map<String,Object>> gridlist = new ArrayList<Map<String,Object>>();
				for (int i = next; i < result; i++) {
					gridlist.add(listView.get(i));
				}
				MyAdapter myAdapter = new MyAdapter(gridlist);
				gridView.setAdapter(myAdapter);
				next = result;
				System.out.println("当前页的"+next);//当前页加上后一页的   8---》16
				gridViewlist.add(gridView);
				
			}else if(result-listView.size()<=8){
				System.out.println("剩余多少"+(result-listView.size()));//最后一页剩余的空间 7
				List<Map<String,Object>> gridlist = new ArrayList<Map<String,Object>>();
				for (int i = next; i < listView.size(); i++) {
					gridlist.add(listView.get(i));
				}
				GridView gridView = new GridView(this);//新建第二个Gridview
				gridView.setNumColumns(4);
				MyAdapter myAdapter = new MyAdapter(gridlist);
				gridView.setAdapter(myAdapter);
				next = listView.size()-1;
				System.out.println("viewpager初始化的"+next);//16
				gridViewlist.add(gridView);
				bool = false;
			}else {
				System.out.println("执行了这这句话");
				bool = false;
			}
		}
		adapter = new AdPageAdapter(gridViewlist);
	}
	private final class AdPageAdapter extends PagerAdapter {  
        private List<View> views = null;  
  
        /**
         * 初始化数据源, 即View数组
         */
        public AdPageAdapter(List<View> views) {  
            this.views = views;  
        }  
        
        /**
         * 从ViewPager中删除集合中对应索引的View对象
         */
        @Override  
        public void destroyItem(View container, int position, Object object) {  
            ((ViewPager) container).removeView(views.get(position));  
        }  
  
        /**
         * 获取ViewPager的个数
         */
        @Override  
        public int getCount() {  
            return views.size();  
        }  
  
        /**
         * 从View集合中获取对应索引的元素, 并添加到ViewPager中
         */
        @Override  
        public Object instantiateItem(View container, final int position) {  
            ((ViewPager) container).addView(views.get(position), 0);  
            return views.get(position);  
        }  
  
        /**
         * 是否将显示的ViewPager页面与instantiateItem返回的对象进行关联
         * 这个方法是必须实现的
         */
        @Override  
        public boolean isViewFromObject(View view, Object object) {  
            return view == object;  
        }  
    }
	private class MyAdapter extends BaseAdapter{
		List<Map<String,Object>> listgrid;
		private MyAdapter(List<Map<String,Object>> listgrid ) {
			this.listgrid = listgrid;
		}
		@Override
		public int getCount() {
			return listgrid.size();
		}

		@Override
		public Object getItem(int position) {
			return listgrid.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int pos = position;
			convertView = getLayoutInflater().inflate(R.layout.grid_view_item, null);
			ImageView getViewLinear = (ImageView)convertView.findViewById(R.id.getViewLinear);
			getViewLinear.setBackgroundResource(Integer.parseInt(listgrid.get(pos).get("image").toString()));
			getViewLinear.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {//点击时间的处理
					System.out.println("aaa"+pos);
				}
			});
			return convertView;
		}
		
	}
}
