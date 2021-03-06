package anandniketan.com.shilajadmin.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import anandniketan.com.shilajadmin.Model.Account.FinalArrayAccountFeesModel;
import anandniketan.com.shilajadmin.R;
import anandniketan.com.shilajadmin.databinding.ListGroupStudentDiscountDetailBinding;
import anandniketan.com.shilajadmin.databinding.ListItemDailyCollectionBinding;

/**
 * Created by admsandroid on 11/24/2017.
 */

public class ExpandbleListAdapterDailyCollection extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, ArrayList<FinalArrayAccountFeesModel>> _listDataChild;


    public ExpandbleListAdapterDailyCollection(Context context, List<String> listDataHeader,
                                       HashMap<String, ArrayList<FinalArrayAccountFeesModel>> listDataChild) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listDataChild;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
         ListItemDailyCollectionBinding itembinding;
        ArrayList<FinalArrayAccountFeesModel> detail = getChild(groupPosition, 0);
        if (convertView == null) {

        }
        itembinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_daily_collection, parent, false);
        convertView = itembinding.getRoot();

        itembinding.totalFeesTxt.setText("₹" + " " +detail.get(childPosition).getTotalFees());
        itembinding.revFeesTxt.setText("₹" + " " +detail.get(childPosition).getReceivedFees());
        itembinding.penddingFeesTxt.setText("₹" + " " +detail.get(childPosition).getPendingFees());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public ArrayList<FinalArrayAccountFeesModel> getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition));
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ListGroupStudentDiscountDetailBinding groupbinding;
        String headerTitle = (String) getGroup(groupPosition);
        String[] spiltValue = headerTitle.split("\\|");
        if (convertView == null) {

        }
        groupbinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_group_student_discount_detail, parent, false);
        convertView = groupbinding.getRoot();

        String sr = String.valueOf(groupPosition + 1);
        groupbinding.indexTxt.setText(sr);
        groupbinding.studentnameTxt.setText(spiltValue[0]);
        groupbinding.grnnoTxt.setText(spiltValue[1]);
        groupbinding.sectionTxt.setText(spiltValue[2]);
        Log.d("studentname", spiltValue[0]);
        if (isExpanded) {
            groupbinding.viewTxt.setTextColor(_context.getResources().getColor(R.color.present));
        } else {
            groupbinding.viewTxt.setTextColor(_context.getResources().getColor(R.color.absent));
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}




