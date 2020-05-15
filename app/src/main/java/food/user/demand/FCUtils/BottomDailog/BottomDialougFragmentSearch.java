package food.user.demand.FCUtils.BottomDailog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import food.user.demand.R;

public class BottomDialougFragmentSearch extends BottomSheetDialogFragment {


    public static BottomDialougFragmentSearch newInstance() {
        return new BottomDialougFragmentSearch();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_search_bottom_sheet, container,
                false);


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FindViewById(view);
    }

    private void FindViewById(View view) {


    }


    @Override
    public void onResume() {
        super.onResume();
    }
}