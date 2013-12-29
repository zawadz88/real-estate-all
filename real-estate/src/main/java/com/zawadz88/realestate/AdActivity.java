package com.zawadz88.realestate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.meetme.android.horizontallistview.HorizontalListView;
import com.squareup.picasso.Picasso;
import com.zawadz88.realestate.api.model.Section;

/**
 * Created: 11.11.13
 *
 * @author Zawada
 */
public class AdActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener {

    private TextView mTitleView;
    private TextView mBasicInfoView;
    private TextView mDescriptionView;
    private TextView mContactInfoView;
    private TextView mPriceView;
    private ViewPager mImagePagerView;
    private HorizontalListView mImageMiniatureListView;

    private int mPhotoMiniatureWidth;
    private int mPhotoMiniatureDividerWidth;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad);
        initActionBar();
        mPhotoMiniatureWidth = (int) getResources().getDimension(R.dimen.ad_photo_miniature_width);
        mPhotoMiniatureDividerWidth = (int) getResources().getDimension(R.dimen.ad_photo_miniature_divider_width);

        mTitleView = (TextView) findViewById(R.id.title);
        mBasicInfoView = (TextView) findViewById(R.id.basic_info_content);
        mDescriptionView = (TextView) findViewById(R.id.description_content);
        mContactInfoView = (TextView) findViewById(R.id.contact_info_content);
        mPriceView = (TextView) findViewById(R.id.price);
        mImagePagerView = (ViewPager) findViewById(R.id.image_view_pager);
        mImageMiniatureListView = (HorizontalListView) findViewById(R.id.image_miniature_list);

        String [] images = {
                "http://galeria.domiporta.pl/pictures/original/6/26/54/f6323b0282e87a5cc486782d558235df/sprzedam-mieszkanie-szczecin-os._zawadzkiego-klonowica.jpg",
                "http://galeria.domiporta.pl/pictures/original/2/95/c8/cb361c8609be0e4fdb9ea406ed58bcb9/sprzedam-mieszkanie-szczecin-os._zawadzkiego-klonowica.jpg",
                "http://galeria.domiporta.pl/pictures/original/8/fb/73/af6ccc71ee00d0ac1cfafa4a4c05c7c6/sprzedam-mieszkanie-szczecin-os._zawadzkiego-klonowica.jpg",
                "http://galeria.domiporta.pl/pictures/original/2/95/c8/cb361c8609be0e4fdb9ea406ed58bcb9/sprzedam-mieszkanie-szczecin-os._zawadzkiego-klonowica.jpg",
                "http://galeria.domiporta.pl/pictures/original/8/fb/73/af6ccc71ee00d0ac1cfafa4a4c05c7c6/sprzedam-mieszkanie-szczecin-os._zawadzkiego-klonowica.jpg",
                "http://galeria.domiporta.pl/pictures/original/2/95/c8/cb361c8609be0e4fdb9ea406ed58bcb9/sprzedam-mieszkanie-szczecin-os._zawadzkiego-klonowica.jpg",
                "http://galeria.domiporta.pl/pictures/original/8/fb/73/af6ccc71ee00d0ac1cfafa4a4c05c7c6/sprzedam-mieszkanie-szczecin-os._zawadzkiego-klonowica.jpg",
                "http://galeria.domiporta.pl/pictures/original/2/95/c8/cb361c8609be0e4fdb9ea406ed58bcb9/sprzedam-mieszkanie-szczecin-os._zawadzkiego-klonowica.jpg",
                "http://galeria.domiporta.pl/pictures/original/8/fb/73/af6ccc71ee00d0ac1cfafa4a4c05c7c6/sprzedam-mieszkanie-szczecin-os._zawadzkiego-klonowica.jpg",
                "http://galeria.domiporta.pl/pictures/original/2/95/c8/cb361c8609be0e4fdb9ea406ed58bcb9/sprzedam-mieszkanie-szczecin-os._zawadzkiego-klonowica.jpg",
                "http://galeria.domiporta.pl/pictures/original/8/fb/73/af6ccc71ee00d0ac1cfafa4a4c05c7c6/sprzedam-mieszkanie-szczecin-os._zawadzkiego-klonowica.jpg",
                "http://galeria.domiporta.pl/pictures/original/6/ad/17/f2190115f4f910aab04286d89088e62a/sprzedam-mieszkanie-szczecin-os._zawadzkiego-klonowica.jpg"
        };
        mImagePagerView.setAdapter(new PhotoPagerAdapter(images));
        mImagePagerView.setOnPageChangeListener(this);
        mImageMiniatureListView.setAdapter(new PhotoMiniatureAdapter(this, images));
        mImageMiniatureListView.setOnItemClickListener(this);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(Section.ADS.getTitleResourceId());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mImagePagerView.setCurrentItem(i, true);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        mImageMiniatureListView.scrollTo(i * (mPhotoMiniatureWidth + (i == 0 ? 0 : mPhotoMiniatureDividerWidth)));
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    private class PhotoPagerAdapter extends PagerAdapter {
        private String[] images;

        public PhotoPagerAdapter(String[] images) {
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        public String getPhotoAt(int position) {
            return images[position];
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            String image = images[position];
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Now just add ImageView to ViewPager and return it
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            Picasso.with(AdActivity.this)
                    .load(image)
                    .placeholder(R.drawable.sample3)
                    .error(R.drawable.sample2)
                    .into(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private class PhotoMiniatureAdapter extends BaseAdapter {
        String[] data = null;
        private Context context;

        public PhotoMiniatureAdapter(Context context, String[] images) {
            super();
            this.context = context;
            this.data = images;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = AdActivity.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.view_photo_miniature, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.photo_miniature);
            String image = data[position];
            Picasso.with(AdActivity.this)
                    .load(image)
                    .placeholder(R.drawable.sample3)
                    .error(R.drawable.sample2)
                    .into(imageView);

            return convertView;
        }

    };
}