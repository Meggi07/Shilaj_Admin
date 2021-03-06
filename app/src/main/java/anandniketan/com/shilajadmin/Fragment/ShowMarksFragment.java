package anandniketan.com.shilajadmin.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import anandniketan.com.shilajadmin.Activity.DashboardActivity;
import anandniketan.com.shilajadmin.R;
import anandniketan.com.shilajadmin.databinding.FragmentShowMarksBinding;


public class ShowMarksFragment extends Fragment {

    private FragmentShowMarksBinding fragmentShowMarksBinding;
    private View rootView;
    private Context mContext;
    private Fragment fragment = null;
    private FragmentManager fragmentManager = null;
    private String url;

    public ShowMarksFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentShowMarksBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_marks, container, false);

        rootView = fragmentShowMarksBinding.getRoot();
        mContext = getActivity().getApplicationContext();

        url = getArguments().getString("Url");
        setListners();

        return rootView;
    }


    public void setListners() {
        fragmentShowMarksBinding.btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardActivity.onLeft();
            }
        });
        fragmentShowMarksBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new StudentViewMarksFragment();
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });
    }

    public void LoadData() {
        fragmentShowMarksBinding.webView.loadData(url, "text/html", "UTF-8");
    }
}

