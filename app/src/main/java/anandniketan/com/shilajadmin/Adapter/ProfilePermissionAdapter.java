package anandniketan.com.shilajadmin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import anandniketan.com.shilajadmin.Model.Student.FinalArrayStudentModel;
import anandniketan.com.shilajadmin.Model.Student.StudentAttendanceModel;
import anandniketan.com.shilajadmin.R;

/**
 * Created by admsandroid on 1/25/2018.
 */

public class ProfilePermissionAdapter extends RecyclerView.Adapter<ProfilePermissionAdapter.MyViewHolder> {
    private Context context;
    private StudentAttendanceModel profilePermissionModel;


    public ProfilePermissionAdapter(Context mContext, StudentAttendanceModel profilePermissionModel) {
        this.context=mContext;
        this.profilePermissionModel=profilePermissionModel;
    }


    @Override
    public ProfilePermissionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_permission_list, parent, false);
        return new ProfilePermissionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProfilePermissionAdapter.MyViewHolder holder, int position) {
        String sr = String.valueOf(position + 1);
        final FinalArrayStudentModel result = profilePermissionModel.getFinalArray().get(position);

        holder.standard_txt.setText(result.getStandard());
        holder.class_txt.setText(String.valueOf(result.getClasses()));
        holder.status_txt.setText(result.getStatus());


    }

    @Override
    public int getItemCount() {
        return profilePermissionModel.getFinalArray().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  standard_txt, class_txt, status_txt;

        public MyViewHolder(View itemView) {
            super(itemView);
            standard_txt = (TextView) itemView.findViewById(R.id.standard_txt);
            class_txt = (TextView) itemView.findViewById(R.id.class_txt);
            status_txt = (TextView) itemView.findViewById(R.id.status_txt);
        }
    }
}
