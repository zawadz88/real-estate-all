package com.zawadz88.realestate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
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
import com.zawadz88.realestate.model.Ad;
import com.zawadz88.realestate.model.Section;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionRes;

/**
 * Activity displaying an {@link com.zawadz88.realestate.model.Ad}.
 * It shows how to move things around for different screen sizes in layout XML files.
 *
 * @author Piotr Zawadzki
 */
@EActivity(R.layout.activity_ad)
public class AdActivity extends AbstractRealEstateActivity implements AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener {

    @ViewById(R.id.title)
    TextView mTitleView;

    @ViewById(R.id.basic_info_content)
    TextView mBasicInfoView;

    @ViewById(R.id.description_content)
    TextView mDescriptionView;

    @ViewById(R.id.contact_info_content)
    TextView mContactInfoView;

    @ViewById(R.id.price)
    TextView mPriceView;

    @ViewById(R.id.image_view_pager)
    ViewPager mImagePagerView;

    @ViewById(R.id.image_miniature_list)
    HorizontalListView mImageMiniatureListView;

    @DimensionRes(R.dimen.ad_photo_miniature_width)
    float mPhotoMiniatureWidth;

    @DimensionRes(R.dimen.ad_photo_miniature_divider_width)
    float mPhotoMiniatureDividerWidth;

    private Ad mAd;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initActionBar();
	}

    @AfterViews
    void populateViews() {
        //TODO fetch from API
        mAd = new Ad();
        mAd.setImages(new String[]{
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
                "http://galeria.domiporta.pl/pictures/original/6/mAd/17/f2190115f4f910aab04286d89088e62a/sprzedam-mieszkanie-szczecin-os._zawadzkiego-klonowica.jpg"
        });
        mAd.setTitle("SZCZECIN, OS. ZAWADZKIEGO-KLONOWICA, 44 M2");
        mAd.setPrice("179 000 zł");
        mAd.setBasicInfo("mieszkanie:na sprzedaż\n" +
                "materiał: WIELKA PŁYTA\n" +
                "forma własności: własność\n" +
                "liczba pokoi: 2\n" +
                "piętro: 3\n" +
                "typ budynku: NISKI BLOK\n" +
                "ilość pięter w budynku: 4\n" +
                "rok budowy: 1964\n" +
                "powierzchnia całkowita: 44 m2\n" +
                "powierzchnia mieszkalna: 44 m2\n" +
                "lokalizacja: województwo: zachodniopomorskie, powiat: Szczecin, gmina: Szczecin, miejscowość: Szczecin");
        mAd.setDescription("Rodzaj mieszkania: 2-pokojowe Numer oferty w SWO: 267857 Przynależne: piwnica Liczba WC: 0 Źródło c.w.: sieć miejska Dodatkowe: balkon Prawobrzeże/lewobrzeże: lewobrzeże   Czynsz dla spółdz./wspólnoty: 450,00 pln  !!! UWAGA !!! OFERTA 0%!! KUPUJĄCY NIE PŁACI PROWIZJI, PROWIZJĘ KUPUJĄCEGO POKRYWA SPRZEDAJĄCY.  Do sprzedania na osiedlu Zawadzkiego 2-pokojowe mieszkanie o powierzchni użytkowej 44,4 m2. Mieszkanie zlokalizowane na trzecim piętrze niskiego budynku w środkowej klatce. Pokoje są ustawne, a z dużego pokoju wyjście na balkon. W całym mieszkaniu są okna PCV, a na podłogach wykładzina. Mieszkanie jest zadbane i można w nim mieszkać, ale wymaga drobnego remontu. Drzwi do mieszkania są podwójne, przynależna piwnica o powierzchni 3,2m2. Doskonale nadaje się pod wynajem ze względu na lokalizację i dobre skomunikowanie z centrum miasta, jak i do zamieszkania. Termin wydania do uzgodnienia. Zapraszamy na prezentację.");
        mAd.setContactInfo("Multi Nieruchomości\n" +
                "ul. Pocztowa 39 (wejście od ul.5 lipca); ul.Jasna 1\n" +
                "70-357 Szczecin\n" +
                "zachodniopomorskie\n" +
                "Polska\n" +
                "tel.: (91) 488-34-44\n" +
                "tel.: (91) 462-64-64");

        mTitleView.setText(mAd.getTitle());
        mPriceView.setText(getString(R.string.price_prefix) + mAd.getPrice());
        mBasicInfoView.setText(mAd.getBasicInfo());
        mDescriptionView.setText(mAd.getDescription());
        mContactInfoView.setText(mAd.getContactInfo());
        mImagePagerView.setAdapter(new PhotoPagerAdapter(mAd.getImages()));
        mImagePagerView.setOnPageChangeListener(this);
        mImageMiniatureListView.setAdapter(new PhotoMiniatureAdapter(this, mAd.getImages()));
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
        mImageMiniatureListView.scrollTo(i * ((int) mPhotoMiniatureWidth + (i == 0 ? 0 : (int) mPhotoMiniatureDividerWidth)));
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    /**
     * A {@link android.support.v4.view.ViewPager} adapter that manages a list of images attached to the article
     */
    private class PhotoPagerAdapter extends PagerAdapter {
        private final String[] images;

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
        public View instantiateItem(final ViewGroup container, final int position) {
            String image = images[position];
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setBackgroundResource(R.drawable.re_vertical_gradient);

            // Now just add ImageView to ViewPager and return it
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            Picasso.with(AdActivity.this)
                    .load(image)
                    .placeholder(R.drawable.robot_icon)
                    .error(R.drawable.robot_icon)
                    .fit().centerCrop()
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdActivity.this, GalleryActivity.class);
                    intent.putExtra(GalleryActivity.AD_TAG, mAd);
                    intent.putExtra(GalleryActivity.START_POSITION_TAG, position);
                    startActivity(intent);
                }
            });
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

    /**
     * A {@link com.meetme.android.horizontallistview.HorizontalListView} adapter that manages an array of image miniatures
     */
    private class PhotoMiniatureAdapter extends BaseAdapter {
        private final String[] data;
        private final Context context;

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
                    .placeholder(R.drawable.robot_icon)
                    .error(R.drawable.robot_icon)
                    .fit().centerCrop()
                    .into(imageView);

            return convertView;
        }
    };
}