package hu.pe.yummykart.yummykart;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter
{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<SliderUtils> sliderImg;
    private ImageLoader imageLoader;
    //private Integer [] images = {R.drawable.slide1,R.drawable.slide2,R.drawable.slide3};

    public ViewPagerAdapter(List<SliderUtils> sliderImg , Context context)
    {
        this.sliderImg = sliderImg;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);

        SliderUtils utils = sliderImg.get(position);

        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(utils.getSliderImageUrl(), ImageLoader.getImageListener(imageView, R.drawable.slide1,  R.drawable.slide1));

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ViewPager vp = (ViewPager)container;
        View view = (View) object;
        vp.removeView(view);
    }

}

