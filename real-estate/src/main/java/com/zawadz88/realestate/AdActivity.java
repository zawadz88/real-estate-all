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
import com.zawadz88.realestate.api.model.Ad;
import com.zawadz88.realestate.api.model.Section;
import com.zawadz88.realestate.fragment.ArticlesGridFragment;

/**
 * Activity displaying an {@link com.zawadz88.realestate.api.model.Ad}.
 * It shows how to move things around for different screen sizes in layout XML files.
 *
 * @author Piotr Zawadzki
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

        //TODO fetch from API
        Ad ad = new Ad();
        ad.setImages(new String[] {
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
        });
        ad.setTitle("SZCZECIN, OS. ZAWADZKIEGO-KLONOWICA, 44 M2");
        ad.setPrice("179 000 zł");
        ad.setBasicInfo("mieszkanie:na sprzedaż\n" +
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
        ad.setDescription("Rodzaj mieszkania: 2-pokojowe Numer oferty w SWO: 267857 Przynależne: piwnica Liczba WC: 0 Źródło c.w.: sieć miejska Dodatkowe: balkon Prawobrzeże/lewobrzeże: lewobrzeże   Czynsz dla spółdz./wspólnoty: 450,00 pln  !!! UWAGA !!! OFERTA 0%!! KUPUJĄCY NIE PŁACI PROWIZJI, PROWIZJĘ KUPUJĄCEGO POKRYWA SPRZEDAJĄCY.  Do sprzedania na osiedlu Zawadzkiego 2-pokojowe mieszkanie o powierzchni użytkowej 44,4 m2. Mieszkanie zlokalizowane na trzecim piętrze niskiego budynku w środkowej klatce. Pokoje są ustawne, a z dużego pokoju wyjście na balkon. W całym mieszkaniu są okna PCV, a na podłogach wykładzina. Mieszkanie jest zadbane i można w nim mieszkać, ale wymaga drobnego remontu. Drzwi do mieszkania są podwójne, przynależna piwnica o powierzchni 3,2m2. Doskonale nadaje się pod wynajem ze względu na lokalizację i dobre skomunikowanie z centrum miasta, jak i do zamieszkania. Termin wydania do uzgodnienia. Zapraszamy na prezentację.");
        ad.setContactInfo("Multi Nieruchomości\n" +
                "ul. Pocztowa 39 (wejście od ul.5 lipca); ul.Jasna 1\n" +
                "70-357 Szczecin\n" +
                "zachodniopomorskie\n" +
                "Polska\n" +
                "tel.: (91) 488-34-44\n" +
                "tel.: (91) 462-64-64");

        mTitleView.setText(ad.getTitle());
        mPriceView.setText(getString(R.string.price_prefix) + ad.getPrice());
        mBasicInfoView.setText(ad.getBasicInfo());
        mDescriptionView.setText(ad.getDescription());
        mContactInfoView.setText(ad.getContactInfo());
        mImagePagerView.setAdapter(new PhotoPagerAdapter(ad.getImages()));
        mImagePagerView.setOnPageChangeListener(this);
        mImageMiniatureListView.setAdapter(new PhotoMiniatureAdapter(this, ad.getImages()));
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
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdActivity.this, GalleryActivity.class);
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
                    .placeholder(R.drawable.sample3)
                    .error(R.drawable.sample2)
                    .into(imageView);

            return convertView;
        }
    };
}