package com.sherdle.universal.offers;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sherdle.universal.AbstractTabFragment;
import com.sherdle.universal.R;
import com.sherdle.universal.db.objects.OfferObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OfferWallFragment extends AbstractTabFragment {

    private static final String TAG = "OfferWallFragment";
    private List<OfferObject> offers;

    public static OfferWallFragment getInstance() {
        Bundle args = new Bundle();
        OfferWallFragment fragment = new OfferWallFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_offerwall, container, false);
        initOfferList();
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.Adapter recyclerAdapder = new RecyclerOfferAdapder(getContext(), offers);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapder);

        return rootView;
    }

    private void initOfferList() {
        offers = new ArrayList<>();
        List<OfferObject> offerList = OfferObject.listAll(OfferObject.class);
        for (int i = 0; i < offerList.size(); i++) {
            if (offerList.get(i).getCategory().equals(getTitle())) {
                offers.add(offerList.get(i));
            }
        }
        Collections.sort(offers, Collections.reverseOrder(new Comparator<OfferObject>() {
            @Override
            public int compare(OfferObject o1, OfferObject o2) {
                return o1.getPriority().compareTo(o2.getPriority());
            }

        }));

    }
}
