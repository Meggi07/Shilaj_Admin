package anandniketan.com.shilajadmin.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anandniketan.com.shilajadmin.Adapter.OtherPageDeatilListAdapter;
import anandniketan.com.shilajadmin.Adapter.PageDeatilListAdapter;
import anandniketan.com.shilajadmin.Model.HR.FinalArrayPageListModel;
import anandniketan.com.shilajadmin.Model.HR.GetPageListModel;
import anandniketan.com.shilajadmin.Model.HR.InsertMenuPermissionModel;
import anandniketan.com.shilajadmin.Model.Staff.FinalArrayTeachersModel;
import anandniketan.com.shilajadmin.Model.Staff.GetTeachersModel;
import anandniketan.com.shilajadmin.Model.Staff.InsertAssignSubjectModel;
import anandniketan.com.shilajadmin.R;
import anandniketan.com.shilajadmin.Utility.ApiHandler;
import anandniketan.com.shilajadmin.Utility.Utils;
import anandniketan.com.shilajadmin.databinding.FragmentMenuPermissionBinding;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MenuPermissionFragment extends Fragment {

    private FragmentMenuPermissionBinding fragmentMenuPermissionBinding;
    private View rootView;
    private Context mContext;
    private Fragment fragment = null;
    private FragmentManager fragmentManager = null;
    private RadioGroup radioGroup;

    List<FinalArrayTeachersModel> finalArrayTeachersModelList;
    HashMap<Integer, String> spinnerTeacherMap;
    String FinalTeacherIdStr, Finalflag, FinalPageStr;

    List<FinalArrayPageListModel> finalArrayPageListModelList;
    PageDeatilListAdapter pageDeatilListAdapter;
    OtherPageDeatilListAdapter otherPageDeatilListAdapter;


    public MenuPermissionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentMenuPermissionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu_permission, container, false);

        rootView = fragmentMenuPermissionBinding.getRoot();
        mContext = getActivity().getApplicationContext();
        Finalflag = "Teacher";
        setListners();
        callTeacherApi();

        return rootView;
    }


    public void setListners() {
        fragmentMenuPermissionBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new HRFragment();
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(0, 0)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });
        fragmentMenuPermissionBinding.teacherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = fragmentMenuPermissionBinding.teacherSpinner.getSelectedItem().toString();
                String getid = spinnerTeacherMap.get(fragmentMenuPermissionBinding.teacherSpinner.getSelectedItemPosition());

                Log.d("value", name + " " + getid);
                FinalTeacherIdStr = getid.toString();
                Log.d("FinalTeacherIdStr", FinalTeacherIdStr);
                callPageListApi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fragmentMenuPermissionBinding.usertypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    // checkedId is the RadioButton selected
                    switch (checkedId) {
                        case R.id.teacher_radiobutton:
                            Finalflag = rb.getText().toString();
                            Log.d("FInalflag", Finalflag);
                            callPageListApi();
                            break;

                        case R.id.other_radiobutton:
                            Finalflag = rb.getText().toString();
                            Log.d("FInalflag", Finalflag);
                            callPageListApi();
                            break;
                        default:
                            break;
                    }

                }
            }
        });
        fragmentMenuPermissionBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callInsertMenuPermissionApi();
            }
        });
    }


    // CALL Teacher API HERE
    private void callTeacherApi() {

        if (!Utils.checkNetwork(mContext)) {
            Utils.showCustomDialog(getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), getActivity());
            return;
        }

//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getTeachers(getTeacherDetail(), new retrofit.Callback<GetTeachersModel>() {
            @Override
            public void success(GetTeachersModel teachersModel, Response response) {
                Utils.dismissDialog();
                if (teachersModel == null) {
                    Utils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (teachersModel.getSuccess() == null) {
                    Utils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (teachersModel.getSuccess().equalsIgnoreCase("false")) {
                    Utils.ping(mContext, getString(R.string.false_msg));
                    return;
                }
                if (teachersModel.getSuccess().equalsIgnoreCase("True")) {
                    finalArrayTeachersModelList = teachersModel.getFinalArray();
                    if (finalArrayTeachersModelList != null) {
                        fillTeacherSpinner();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                Utils.ping(mContext, getString(R.string.something_wrong));
            }
        });

    }

    private Map<String, String> getTeacherDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillTeacherSpinner() {
        ArrayList<Integer> TeacherId = new ArrayList<Integer>();
        for (int i = 0; i < finalArrayTeachersModelList.size(); i++) {
            TeacherId.add(finalArrayTeachersModelList.get(i).getPkEmployeeID());
        }
        ArrayList<String> Teacher = new ArrayList<String>();
        for (int j = 0; j < finalArrayTeachersModelList.size(); j++) {
            Teacher.add(finalArrayTeachersModelList.get(j).getTeacher());
        }

        String[] spinnerteacherIdArray = new String[TeacherId.size()];

        spinnerTeacherMap = new HashMap<Integer, String>();
        for (int i = 0; i < TeacherId.size(); i++) {
            spinnerTeacherMap.put(i, String.valueOf(TeacherId.get(i)));
            spinnerteacherIdArray[i] = Teacher.get(i).trim();
        }
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(fragmentMenuPermissionBinding.teacherSpinner);

            popupWindow.setHeight(spinnerteacherIdArray.length > 5 ? 500 : spinnerteacherIdArray.length * 100);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, spinnerteacherIdArray);
        fragmentMenuPermissionBinding.teacherSpinner.setAdapter(adapterTerm);
        FinalTeacherIdStr = spinnerTeacherMap.get(0);
        callPageListApi();
    }

    // CALL PageList API HERE
    private void callPageListApi() {

        if (!Utils.checkNetwork(mContext)) {
            Utils.showCustomDialog(getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), getActivity());
            return;
        }

//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getPageList(getPageListDetail(), new retrofit.Callback<GetPageListModel>() {
            @Override
            public void success(GetPageListModel getPageListModel, Response response) {
                Utils.dismissDialog();
                if (getPageListModel == null) {
                    Utils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (getPageListModel.getSuccess() == null) {
                    Utils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (getPageListModel.getSuccess().equalsIgnoreCase("false")) {
                    Utils.ping(mContext, getString(R.string.false_msg));
                    if (getPageListModel.getFinalArray().size() == 0) {
                        fragmentMenuPermissionBinding.txtNoRecords.setVisibility(View.VISIBLE);
                        fragmentMenuPermissionBinding.pageListDetailList.setVisibility(View.GONE);
                        fragmentMenuPermissionBinding.recyclerLinear.setVisibility(View.GONE);
                        fragmentMenuPermissionBinding.listHeader.setVisibility(View.GONE);
                    }
                    return;
                }
                if (getPageListModel.getSuccess().equalsIgnoreCase("True")) {
                    finalArrayPageListModelList = getPageListModel.getFinalArray();
                    if (finalArrayPageListModelList != null) {
                        fillDataList();
                        Utils.dismissDialog();
                    } else {
                        fragmentMenuPermissionBinding.txtNoRecords.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                Utils.ping(mContext, getString(R.string.something_wrong));
            }
        });

    }

    private Map<String, String> getPageListDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("EmployeeID", FinalTeacherIdStr);
        map.put("flag", Finalflag);
        return map;
    }

    public void fillDataList() {
        fragmentMenuPermissionBinding.txtNoRecords.setVisibility(View.GONE);
        fragmentMenuPermissionBinding.pageListDetailList.setVisibility(View.VISIBLE);
        fragmentMenuPermissionBinding.recyclerLinear.setVisibility(View.VISIBLE);
        fragmentMenuPermissionBinding.listHeader.setVisibility(View.VISIBLE);

        if (Finalflag.equalsIgnoreCase("Teacher")) {
            fragmentMenuPermissionBinding.pageListDetailList.setVisibility(View.VISIBLE);
            fragmentMenuPermissionBinding.recyclerLinear.setVisibility(View.VISIBLE);
            fragmentMenuPermissionBinding.listHeader.setVisibility(View.VISIBLE);

            fragmentMenuPermissionBinding.pageListDetailList1.setVisibility(View.GONE);
            fragmentMenuPermissionBinding.listHeader1.setVisibility(View.GONE);
            fragmentMenuPermissionBinding.recyclerLinear1.setVisibility(View.GONE);

            pageDeatilListAdapter = new PageDeatilListAdapter(mContext, finalArrayPageListModelList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            fragmentMenuPermissionBinding.pageListDetailList.setLayoutManager(mLayoutManager);
            fragmentMenuPermissionBinding.pageListDetailList.setItemAnimator(new DefaultItemAnimator());
            fragmentMenuPermissionBinding.pageListDetailList.setAdapter(pageDeatilListAdapter);
        } else if (Finalflag.equalsIgnoreCase("Other")) {
            fragmentMenuPermissionBinding.pageListDetailList.setVisibility(View.GONE);
            fragmentMenuPermissionBinding.recyclerLinear.setVisibility(View.GONE);
            fragmentMenuPermissionBinding.listHeader.setVisibility(View.GONE);

            fragmentMenuPermissionBinding.pageListDetailList1.setVisibility(View.VISIBLE);
            fragmentMenuPermissionBinding.listHeader1.setVisibility(View.VISIBLE);
            fragmentMenuPermissionBinding.recyclerLinear1.setVisibility(View.VISIBLE);

            otherPageDeatilListAdapter = new OtherPageDeatilListAdapter(mContext, finalArrayPageListModelList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            fragmentMenuPermissionBinding.pageListDetailList1.setLayoutManager(mLayoutManager);
            fragmentMenuPermissionBinding.pageListDetailList1.setItemAnimator(new DefaultItemAnimator());
            fragmentMenuPermissionBinding.pageListDetailList1.setAdapter(otherPageDeatilListAdapter);
        }
    }

    // CALL InsertMenuPermission API HERE
    private void callInsertMenuPermissionApi() {

        if (!Utils.checkNetwork(mContext)) {
            Utils.showCustomDialog(getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), getActivity());
            return;
        }

        Utils.showDialog(getActivity());
        ApiHandler.getApiService().InsertMenuPermission(getInsertMenuPermissionDetail(), new retrofit.Callback<InsertMenuPermissionModel>() {
            @Override
            public void success(InsertMenuPermissionModel insertMenuPermissionModel, Response response) {
                Utils.dismissDialog();
                if (insertMenuPermissionModel == null) {
                    Utils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (insertMenuPermissionModel.getSuccess() == null) {
                    Utils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (insertMenuPermissionModel.getSuccess().equalsIgnoreCase("false")) {
                    Utils.ping(mContext, getString(R.string.false_msg));
                    if (insertMenuPermissionModel.getFinalArray().size() == 0) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                    }
                    return;
                }
                if (insertMenuPermissionModel.getSuccess().equalsIgnoreCase("True")) {
                    callPageListApi();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                Utils.ping(mContext, getString(R.string.something_wrong));
            }
        });

    }

    private Map<String, String> getInsertMenuPermissionDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("Pk_EmployeID", FinalTeacherIdStr);
        map.put("Pages", FinalPageStr);
        return map;
    }


    public void FatchInsertPermissionData() {
        if (Finalflag.equalsIgnoreCase("Teacher")) {




        } else if (Finalflag.equalsIgnoreCase("Other")) {
        }
    }
}

