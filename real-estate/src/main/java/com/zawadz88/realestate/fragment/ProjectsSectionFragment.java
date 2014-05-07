package com.zawadz88.realestate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.j256.ormlite.dao.Dao;
import com.zawadz88.realestate.R;
import com.zawadz88.realestate.db.DatabaseHelper;
import com.zawadz88.realestate.model.Project;
import com.zawadz88.realestate.model.Section;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.UiThread;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment displaying a list of projects
 *
 * @author Piotr Zawadzki
 */
@EFragment
public class ProjectsSectionFragment extends AbstractSectionFragment {

    private static final String LAST_KNOWN_SCROLL_POSITION = "scrollPosition";

    @OrmLiteDao(helper = DatabaseHelper.class, model = Project.class)
    Dao<Project, Integer> projectDAO;

	public static ProjectsSectionFragment newInstance() {
		ProjectsSectionFragment fragment = new ProjectsSectionFragment_();
		Bundle args = new Bundle();
		args.putSerializable(ARG_SECTION, Section.PROJECTS);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_section_default_grid, container, false);

		mGridView = (GridView) view.findViewById(R.id.ads_gridview);
        loadProjectFromDAO();

		return view;
	}

    @Background
    void loadProjectFromDAO() {
        try {
            List<Project> elements = projectDAO.queryForAll();
            setAdapterWithElements(elements);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void setAdapterWithElements(List<Project> elements) {
        mGridView.setAdapter(new ProjectsAdapter(elements));
    }

    @Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.containsKey(LAST_KNOWN_SCROLL_POSITION)) {
			int position = savedInstanceState.getInt(LAST_KNOWN_SCROLL_POSITION);
			mGridView.smoothScrollToPosition(position);
		}
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		int position = mGridView.getFirstVisiblePosition();
		outState.putInt(LAST_KNOWN_SCROLL_POSITION, position);
	}

	private class ProjectsAdapter extends BaseAdapter {
        public static final String RESOURCE_TYPE_DRAWABLE = "drawable";
        private List<Project> elements;

        public ProjectsAdapter(List<Project> elements) {
            super();
            this.elements = elements;
        }

        @Override
		public int getCount() {
			return elements.size();  //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public Object getItem(final int position) {
			return elements.get(position);  //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public long getItemId(final int position) {
			return 0;  //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {
			View view;
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				LayoutInflater inflater = (LayoutInflater) ProjectsSectionFragment.this.getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.section_gridview_item, parent, false);
			} else {
				view = convertView;
			}
            Project project = elements.get(position);
			((TextView)view.findViewById(R.id.gridview_item_title)).setText(project.getTitle());

            if (!TextUtils.isEmpty(project.getResourceName())) {
                try {
                    int resId = getResources().getIdentifier(project.getResourceName(), RESOURCE_TYPE_DRAWABLE, getActivity().getPackageName());

                    ImageView imageView = (ImageView) view.findViewById(R.id.gridview_item_image);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    //TODO the above should be changed by Picasso after loading the image since ScaleType.CENTER_INSIDE is used for preloaders
                    imageView.setImageResource(resId);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            return view;  //To change body of implemented methods use File | Settings | File Templates.
		}
	}
}
