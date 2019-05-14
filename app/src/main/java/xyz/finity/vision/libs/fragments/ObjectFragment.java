package xyz.finity.vision.libs.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xyz.finity.vision.R;
import xyz.finity.vision.ResultActivity;
import xyz.finity.vision.libs.adapters.ObjectAdapter;
import xyz.finity.vision.libs.models.Object;

/**
 * Created by dwiva on 5/13/19.
 * This code is a part of Vision project
 */
public class ObjectFragment extends Fragment {

    public static ObjectFragment newInstance(List<Object> list) {
        ObjectFragment fragment = new ObjectFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        args.putParcelableArrayList("list", new ArrayList<>(list));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_result, container, false);

        ObjectAdapter adapter = new ObjectAdapter(getArguments().<Object>getParcelableArrayList("list"));
        adapter.setListener(new ObjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ObjectAdapter adapter, int position) {
                ((ResultActivity) getActivity()).highlightVertices(ResultActivity.OBJECTS_RECT, position);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        RecyclerView.ItemDecoration decoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.left = getResources().getDimensionPixelSize(R.dimen.listItemHorizontalMargin);
                outRect.top = getResources().getDimensionPixelSize(R.dimen.listItemVerticalMargin);
                outRect.right = getResources().getDimensionPixelSize(R.dimen.listItemHorizontalMargin);
                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.listItemVerticalMargin);
            }
        };
        recyclerView.addItemDecoration(decoration);

        return recyclerView;
    }
}
