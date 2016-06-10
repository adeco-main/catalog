package com.sherdle.universal.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sherdle.universal.PermissionsFragment;
import com.sherdle.universal.R;
import com.sherdle.universal.SettingsFragment;
import com.sherdle.universal.activity.MainActivity;
import com.sherdle.universal.ads.InterstitialAds;
import com.sherdle.universal.ads.NativeAds;
import com.sherdle.universal.custom.entity.ButtonElement;
import com.sherdle.universal.custom.entity.Element;
import com.sherdle.universal.custom.entity.ImageElement;
import com.sherdle.universal.custom.entity.ListElement;
import com.sherdle.universal.custom.entity.NativeOfferElement;
import com.sherdle.universal.custom.entity.SettingsElement;
import com.sherdle.universal.custom.entity.TextElement;
import com.sherdle.universal.db.objects.OfferObject;
import com.sherdle.universal.db.objects.SettingsObject;
import com.sherdle.universal.json.JSONLocalParser;
import com.sherdle.universal.nav.NavItem;
import com.sherdle.universal.nav.NavListConfig;
import com.sherdle.universal.offers.OfferWallActivity;
import com.sherdle.universal.util.Helper;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class CustomFragment extends Fragment {

    private final static String TAG = "Custom Fragment";
    public static String DATA = "transaction_data";

    private String jsonCustomElements;
    private List<Element> customElements;
    private InterstitialAds interstitialAds;
    private NavItem queueItem;
    private FragmentActivity myContext;
    private LinearLayout linearLayout;

    public static int marginTop = 2, marginDown = 2, marginLeft = 5, marginRight = 5;
    public static int marginTopAds = 15, marginDownAds = 15, marginLeftAds = 5, marginRightAds = 5;


    List<SettingsObject> settingsObjectList = SettingsObject.listAll(SettingsObject.class);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_custom, container, false);

        setHasOptionsMenu(true);

        interstitialAds = new InterstitialAds(getContext());

        linearLayout = (LinearLayout) rootView.findViewById(R.id.mainContent);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        jsonCustomElements = getArguments().getStringArray(MainActivity.FRAGMENT_DATA)[0];
        Log.d(TAG, "onCreateView: " + jsonCustomElements);

        initCustomElements();
        buildCustomElements();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.custom_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (!settingsObjectList.get(0).isOfferwallVisible()) {
            menu.removeItem(R.id.offerwall);
        }
        if (!settingsObjectList.get(0).isShareVisible()) {
            menu.removeItem(R.id.share);
        }
        if (!settingsObjectList.get(0).isRateVisible()) {
            menu.removeItem(R.id.rate);
        }
        if (!settingsObjectList.get(0).isSettingsVisible()) {
            menu.removeItem(R.id.settings);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.offerwall:
                Helper.initOpenActivity(getContext(), OfferWallActivity.class);
                return true;
            case R.id.share:
                Helper.initShareApplication(getContext());
                return true;
            case R.id.rate:
                Helper.initRateApplication(getContext());
                return true;
            case R.id.settings:
                for (int i = 0; i < NavListConfig.getNavFragment().size(); i++) {
                    if (NavListConfig.getNavFragment().get(i).getTitle(getContext()).equals("Settings")) {
                        openNavigationItem(NavListConfig.getNavFragment().get(i));
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void initCustomElements() {
        try {
            customElements = new JSONLocalParser(getContext()).getCustomElementList(jsonCustomElements);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void buildCustomElements() {
        boolean nativeAdmobOn = false;
        for (final Element element : customElements) {
            if (element instanceof SettingsElement) {
                setSettingsFragment(element);
            } else if (element instanceof TextElement) {
                linearLayout.addView(buildTextElement(element));
            } else if (element instanceof ButtonElement) {
                linearLayout.addView(buildCustomButtonElement(element));
            } else if (element instanceof ImageElement) {
                linearLayout.addView(buildCustomImage(element));
            } else if (element instanceof ListElement) {
                linearLayout.addView(buildListElement(element));
            } else if (element instanceof NativeOfferElement) {
                linearLayout.addView(buildNativeOffer());
            } else if (element instanceof NativeAds) {
                if (!nativeAdmobOn && settingsObjectList.get(0).getNativeID() != null && !settingsObjectList.get(0).getNativeID().equals("")) {
                    linearLayout.addView(buildNativeAdmobFragment(element));
                    nativeAdmobOn = true;
                }
            }
        }
        if (!nativeAdmobOn && settingsObjectList.get(0).getNativeID() != null && !settingsObjectList.get(0).getNativeID().equals("")) {
            linearLayout.addView(buildNativeAdmobFragment(), linearLayout.getChildCount());
        }
    }

    private void setSettingsFragment(Element element) {
        final SettingsElement settingsElement = (SettingsElement) element;

        if (!settingsElement.getBackgroundImage().equals("")) {
            final Bitmap[] bitmap = new Bitmap[1];
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    bitmap[0] = ImageLoader.getInstance().loadImageSync(Helper.onVerificationUrl(settingsElement.getBackgroundImage()));

                    return null;
                }

                protected void onPostExecute(Void result) {

                    Drawable d = new BitmapDrawable(getResources(), bitmap[0]);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        linearLayout.setBackground(d);
                    }

                }
            }.execute();
        } else if (!settingsElement.getBackgroundColor().equals("")) {
            linearLayout.setBackgroundColor(Color.parseColor(settingsElement.getBackgroundColor()));
        }

    }

    private View buildTextElement(Element element) {
        final TextElement newTextElement = (TextElement) element;
        TextView textView = new TextView(getContext());
        String resultText = newTextElement.getTextContent().replace("${tabulate}", "\t")
                .replace("${newline}", "\n")
                .replace("${return}", "\r")
                .replace("${doublequote}", "\'");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginDown);
        layoutParams.weight = 1.0f;

        if (newTextElement.getTextGravity().equals("right")) {
            layoutParams.gravity = Gravity.RIGHT;
        }
        if (newTextElement.getTextGravity().equals("left")) {
            layoutParams.gravity = Gravity.LEFT;
        }
        if (newTextElement.getTextGravity().equals("center")) {
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        }

        textView.setLayoutParams(layoutParams);

        Typeface face = null;

        if (!newTextElement.getTextFonts().equals("") && newTextElement.getTextFonts() != null) {
            face = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + newTextElement.getTextFonts());
        }
        if (newTextElement.getTextTypeFace().equals("bold")) {
            textView.setTypeface(face, Typeface.BOLD);
        }
        if (newTextElement.getTextTypeFace().equals("italic")) {
            textView.setTypeface(face, Typeface.ITALIC);
        }
        if (newTextElement.getTextTypeFace().equals("bold_italic")) {
            textView.setTypeface(face, Typeface.BOLD_ITALIC);
        }
        if (newTextElement.getTextTypeFace().equals("normal")) {
            textView.setTypeface(face, Typeface.NORMAL);
        }
        textView.setText(resultText);

        if (!newTextElement.getTextColor().equals("")) {
            textView.setTextColor(Color.parseColor(newTextElement.getTextColor()));
        }
        if (newTextElement.getTextSize() != 0) {
            textView.setTextSize(newTextElement.getTextSize());
        }
        if (!newTextElement.getTextLink().equals("")) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initOpenUrl(newTextElement.getTextLink());
                }
            });
            textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        return textView;
    }

    private View buildCustomButtonElement(final Element element) {
        final ButtonElement buttonElement = (ButtonElement) element;
        Button button = new Button(getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginDown);
        layoutParams.weight = 1.0f;

        if (buttonElement.getButtonGravity().equals("right")) {
            layoutParams.gravity = Gravity.RIGHT;
        }
        if (buttonElement.getButtonGravity().equals("left")) {
            layoutParams.gravity = Gravity.LEFT;
        }
        if (buttonElement.getButtonGravity().equals("center")) {
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        }

        button.setLayoutParams(layoutParams);
        button.setText(buttonElement.getTextButton());
        if (buttonElement.getSizeButton() != 0) {
            button.setTextSize(buttonElement.getSizeButton());
            int pad = (buttonElement.getSizeButton()) / 2;
            button.setPadding(pad, pad, pad, pad);
        }
        if (!buttonElement.getColorButton().equals("")) {
            button.getBackground().setColorFilter(Color.parseColor(buttonElement.getColorButton()), PorterDuff.Mode.MULTIPLY);
        }
        if (!buttonElement.getTextColorButton().equals("")) {
            button.setTextColor(Color.parseColor(buttonElement.getTextColorButton()));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonElement.getLinkButton().equals("") && buttonElement.getLinkButton() != null) {
                    initOpenUrl(buttonElement.getLinkButton());
                } else {
                    initOpenFragment(buttonElement.getLinkFragment());
                }
            }
        });
        return button;
    }

    private View buildCustomImage(final Element element) {
        final ImageElement imageElement = (ImageElement) element;
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginDown);
        layoutParams.weight = 1.0f;

        if (imageElement.getImageGravity().equals("right")) {
            layoutParams.gravity = Gravity.RIGHT;
        }
        if (imageElement.getImageGravity().equals("left")) {
            layoutParams.gravity = Gravity.LEFT;
        }
        if (imageElement.getImageGravity().equals("center")) {
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        }
        imageView.setLayoutParams(layoutParams);
        ImageLoader.getInstance().displayImage(imageElement.getImageUrl(), imageView);
        if (!imageElement.getLinkUrl().equals("")) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initOpenUrl(imageElement.getLinkUrl());
                }
            });
        }

        return imageView;
    }

    private View buildNativeOffer() {

        List<OfferObject> offerList = OfferObject.listAll(OfferObject.class);
        final OfferObject offerObject = offerList.get(new Random().nextInt(offerList.size()));

        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_offer, null);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginDown);
        layoutParams.weight = 1.0f;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        view.setLayoutParams(layoutParams);

        TextView title = (TextView) view.findViewById(R.id.textViewTitle);
        title.setText(offerObject.getName());

        ImageView image = (ImageView) view.findViewById(R.id.imageViewThumbl);
        ImageLoader.getInstance().displayImage(offerObject.getThumbUrl(), image);
        if (!offerObject.getDownloadUrl().equals("")) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initOpenUrl(offerObject.getDownloadUrl());
                }
            });
        }
        return view;
    }


    private View buildListElement(final Element element) {
        final ListElement listElement = (ListElement) element;
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = null;

        switch (listElement.getType()) {
            case 1:
                view = inflater.inflate(R.layout.list_item_custom_one, null);
                break;
            case 2:
                view = inflater.inflate(R.layout.list_item_custom_two, null);
                break;
            case 3:
                view = inflater.inflate(R.layout.list_item_custom_three, null);
                break;

        }

        if (view != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(marginLeft, marginTop, marginRight, marginDown);
            layoutParams.weight = 1.0f;
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            view.setLayoutParams(layoutParams);
        }

        TextView title = (TextView) view.findViewById(R.id.textViewTitle);
        if (!listElement.getTitle().equals("")) {
            title.setText(listElement.getTitle());
        } else {
            title.setVisibility(View.GONE);
        }

        TextView description = (TextView) view.findViewById(R.id.textViewDescription);
        if (!listElement.getDescription().equals("")) {
            description.setText(listElement.getDescription());
        } else {
            description.setVisibility(View.GONE);
        }

        Button button = (Button) view.findViewById(R.id.buttonView);
        if (listElement.getButtonTextSize() != 0) {
            button.setTextSize(listElement.getButtonTextSize());
            int pad = (listElement.getButtonTextSize()) / 2;
            button.setPadding(pad, pad, pad, pad);
        }
        if (!listElement.getButtonColor().equals("")) {
            button.getBackground().setColorFilter(Color.parseColor(listElement.getButtonColor()), PorterDuff.Mode.SRC);
        }
        if (!listElement.getButtonTextColor().equals("")) {
            button.setTextColor(Color.parseColor(listElement.getButtonTextColor()));
        }
        if (!listElement.getTextButton().equals("")) {
            button.setText(listElement.getTextButton());
            if (!listElement.getLinkUrl().equals("") || !listElement.getLinkFragment().equals("")) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!listElement.getLinkUrl().equals("") && listElement.getLinkUrl() != null) {
                            initOpenUrl(listElement.getLinkUrl());
                        } else {
                            initOpenFragment(listElement.getLinkFragment());
                        }
                    }
                });
            }
        } else {
            button.setVisibility(View.GONE);
        }

        ImageView image = (ImageView) view.findViewById(R.id.imageViewItem);
        if (!listElement.getLinkImage().equals("")) {
            ImageLoader.getInstance().displayImage(listElement.getLinkImage(), image);
        } else {
            image.setVisibility(View.GONE);
        }
        if (!listElement.getLinkUrl().equals("") || !listElement.getLinkFragment().equals("")) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!listElement.getLinkUrl().equals("") && listElement.getLinkUrl() != null) {
                        initOpenUrl(listElement.getLinkUrl());
                    } else {
                        initOpenFragment(listElement.getLinkFragment());
                    }
                }
            });
        }
        return view;
    }

    private View buildNativeAdmobFragment(Element element) {
        NativeAds nativeAdmob = (NativeAds) element;
        View view = nativeAdmob.getAdView();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(marginLeftAds, marginTopAds, marginRightAds, marginDownAds);
        layoutParams.weight = 1.0f;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        view.setLayoutParams(layoutParams);
        return view;
    }

    private View buildNativeAdmobFragment() {
        NativeAds nativeAdmob = new NativeAds(getContext());
        View view = nativeAdmob.getAdView();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(marginLeftAds, marginTopAds, marginRightAds, marginDownAds);
        layoutParams.weight = 1.0f;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        view.setLayoutParams(layoutParams);
        return view;
    }


    private void openNavigationItem(NavItem item) {
        try {
            Fragment fragment = item.getFragment().newInstance();

            if (fragment != null) {

                //Verify if we can safely open the fragment by checking for permissions
                if (checkPermissionsHandleIfNeeded(item, fragment) && checkPurchaseHandleIfNeeded(item)) {
                    String[] extra = item.getData();
                    showFragment(fragment, extra, item.getTitle(getContext()));
                } else {
                    //We do nothing, the check method will handle this for us.
                }

            } else {
                Log.v("INFO", "Error creating fragment");
            }
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    public void showFragment(Fragment fragment, String[] extra, String title) {
        Bundle bundle = new Bundle();

        bundle.putStringArray(MainActivity.FRAGMENT_DATA, extra);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = myContext.getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.container, fragment)
                .commitAllowingStateLoss();

//        if(!useTabletMenu())
//            setTitle(title);
    }

    private boolean checkPermissionsHandleIfNeeded(NavItem item, Fragment fragment) {
        if (fragment instanceof PermissionsFragment && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            String[] permissions = ((PermissionsFragment) fragment).requiredPermissions();

            boolean allGranted = true;
            for (String permission : permissions) {
                if (getContext().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                    allGranted = false;
            }
            if (!allGranted) {
                //TODO An explaination before asking
                requestPermissions(permissions, 1);
                queueItem = item;
                return false;
            }
            return true;
        }
        return true;
    }


    private boolean checkPurchaseHandleIfNeeded(NavItem item) {
        String license = getResources().getString(R.string.google_play_license);
        // if item does not require purchase, or app has purchased, or license is null/empty (app has no in app purchases)
        if (item.requiresPurchase() == true
                && !SettingsFragment.getIsPurchased(getContext())
                && null != license && !license.equals("")) {
            Fragment fragment = new SettingsFragment();
            String[] extra = new String[]{SettingsFragment.SHOW_DIALOG};
            showFragment(fragment, extra, item.getTitle(getContext()));

            return false;
        }

        return true;
    }

    private void initOpenFragment(String fragmentName) {
        for (int i = 0; i < NavListConfig.getAllFragment().size(); i++) {
            if (NavListConfig.getAllFragment().get(i).getTitle(getContext()).equals(fragmentName)) {
                openNavigationItem(NavListConfig.getAllFragment().get(i));
                break;
            }
        }
    }

    private void initOpenUrl(String url) {
        if (!settingsObjectList.get(0).getInterstitialID().equals("")) {
            interstitialAds.showInterstitial(url);
        } else {
            getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(((Helper.onVerificationUrl(url))))));
        }
    }

    private void initOpenActivity(Class className) {
        if (!settingsObjectList.get(0).getInterstitialID().equals("")) {
            interstitialAds.showInterstitial(className);
        } else {
            getActivity().startActivity(new Intent(getActivity(), className));
            getActivity().overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
        }
    }

}