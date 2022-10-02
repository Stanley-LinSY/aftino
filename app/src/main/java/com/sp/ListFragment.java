package com.sp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DecimalFormat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ListFragment extends Fragment {
    private RecyclerView list;
    private Cursor model = null;
    private RealEstateAdapter adapter = null;
    private RealEstateHelper helper = null;
    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new RealEstateHelper(getContext());
        model = helper.getAll();
        adapter = new RealEstateAdapter(getContext(), model);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        list = view.findViewById(R.id.realestates);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (model != null) {
            model.close();
        }
        model = helper.getAll();
        adapter.swapCursor(model);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        helper.close();
    }

    public class RealEstateAdapter extends RecyclerView.Adapter<RealEstateAdapter.RealEstateHolder> {
        private Context context;
        private RealEstateHelper helper = null;
        private Cursor cursor;
        RealEstateAdapter(Context context, Cursor cursor) {
            this.context = context;
            this.cursor = cursor;
            helper = new RealEstateHelper(context);
        }
        public void swapCursor(Cursor newcursor) {
            Cursor oldCursor = this.cursor;
            this.cursor = newcursor;
            oldCursor.close();
        }

        @Override
        public RealEstateAdapter.RealEstateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            return new RealEstateHolder(view);
        }

        @Override
        public void onBindViewHolder(RealEstateAdapter.RealEstateHolder holder, final int position) {
            if (!cursor.moveToPosition(position)) {
                return;
            }
            DecimalFormat precision = new DecimalFormat("0.00000");
            holder.realTypeSizeStatus.setText(helper.getRealEstateType(cursor) + " ("
                    + helper.getRealEstateSize(cursor) + " sq ft) for " + helper.getRealEstateStatus(cursor));
            holder.realPri.setText("SG$" + helper.getRealEstatePrice(cursor));
            holder.realAddr.setText(helper.getRealEstateAddress(cursor));
            holder.realAgen.setText("Agent " + helper.getRealEstateAgent(cursor));
            if (helper.getRealEstateStatus(cursor).equals("Rent")) {
                holder.icon.setImageResource(R.drawable.studio);
            } else {
                holder.icon.setImageResource(R.drawable.mansion);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    cursor.moveToPosition(position);
                    String recordID = helper.getID(cursor);
                    Bundle bundle = new Bundle(); //create bundle
                    bundle.putString("id", recordID);
                    //pass bundle to DetailFragment
                    getParentFragmentManager().setFragmentResult("listToDetailKey", bundle);
                    //get the hosting activity's BottomNavigationView
                    BottomNavigationView nav = (BottomNavigationView)getActivity().findViewById(R.id.bottomNavigationView);
                    //activate the BottomNavigationView's detail menu
                    nav.setSelectedItemId(R.id.realdetail);
                }
            });
        }

        @Override
        public int getItemCount() { return cursor.getCount(); }

        class RealEstateHolder extends RecyclerView.ViewHolder{
            private TextView realPri = null;
            private TextView realAddr = null;
            private TextView realAgen = null;
            private ImageView icon = null;
            private TextView realTypeSizeStatus = null;
            public RealEstateHolder(View itemView) {
                super(itemView);
                realPri = itemView.findViewById(R.id.realPri);
                realAddr = itemView.findViewById(R.id.realAddr);
                icon = itemView.findViewById(R.id.icon);
                realAgen = itemView.findViewById(R.id.realAgen);
                realTypeSizeStatus = itemView.findViewById(R.id.realTypeSizeStatus);
            }
        }
    }
}