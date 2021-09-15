package com.xevensolutions.baseapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.xevensolutions.baseapp.R;
import com.xevensolutions.baseapp.activities.BaseActivity;
import com.xevensolutions.baseapp.interfaces.BaseFragmentListener;
import com.xevensolutions.baseapp.interfaces.BaseFragmentMethods;
import com.xevensolutions.baseapp.presenters.BaseFragmentView;
import com.xevensolutions.baseapp.utils.AlertUtils;
import com.xevensolutions.baseapp.utils.Constants;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment implements BaseFragmentMethods,
        BaseFragmentView {


    ProgressDialog progressDialog;


    /**
     * if some child fragment is requesting for some permissions and
     * baseFragmentToPassResults !=null, then the results will be passed to this fragment
     * instead of currentFragment of BaseActivity
     */
    BaseFragment baseFragmentToPassResults;
    private boolean dismissLoading;


    public void onBackPressed() {
        ((BaseActivity) getActivity()).onSuperBackPressed();

    }


    public void initViewModel() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (getActivity() instanceof BaseFragmentListener)
                setBaseFragmentListener((BaseFragmentListener) getActivity());
            else if (getParentFragment() instanceof BaseFragmentListener)
                setBaseFragmentListener((BaseFragmentListener) getParentFragment());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFragment(BaseFragment baseFragmentChild, int fragmentToLoadLayoutId) {

        baseFragmentChild.setAllowEnterTransitionOverlap(true);
        getChildFragmentManager().beginTransaction().replace(fragmentToLoadLayoutId,
                baseFragmentChild).setCustomAnimations(baseFragmentChild.getEnterAnimation(),
                baseFragmentChild.getExitAnimation(), baseFragmentChild.getPopEnterAnimation(),
                baseFragmentChild.getPopExitAnimation()).commit();
    }


    public void showProgressBar(String progressBarText) {


        progressDialog.setMessage(progressBarText);
        if (!progressDialog.isShowing())
            progressDialog.show();
        /*fragmentDialogProgress = FragmentDialogProgress.newInstance(progressBarText);
        fragmentDialogProgress.show(getChildFragmentManager());*/
    }


    public void observeData() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            receiveExtras(getArguments());
        }


    }


    public void requestPermissions(BaseFragment requestingFragment,
                                   int requestCode, String[] permissions) {
        if (requestingFragment.isChildFragment())
            ((BaseFragment) getParentFragment()).baseFragmentToPassResults = requestingFragment;
        else
            this.baseFragmentToPassResults = null;
        ((BaseActivity) getActivity()).requestPermissions(requestCode, permissions);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getRootLayoutId() == 0) {
            throw new NullPointerException("No layout id returned in getRootLayoutId to inflate");

        } else {


            View view = inflater.inflate(getRootLayoutId(), container, false);
            ButterKnife.bind(this, view);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            initViewModel();
            setListeners();
            onCreateView(view);

            return view;
        }

    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        observeData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (baseFragmentToPassResults != null)
            baseFragmentToPassResults.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public int getEnterAnimation() {
        return 0;
    }

    public int getExitAnimation() {
        return 0;

    }

    public int getPopEnterAnimation() {
        return 0;
    }

    public int getPopExitAnimation() {
        return 0;
    }


    /**
     * Whenever you set some listeners on some views .e.g do set those listeners in this method,
     * so that it could be easy to find where the listeners are set instead of scrolling through
     * the whole file.
     */
    public void setListeners() {


    }


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        if (nextAnim == R.anim.slide_in_right) {
            ViewCompat.setTranslationZ(getView(), 1f);
        } else {
            ViewCompat.setTranslationZ(getView(), 0f);
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }


    public void onCreateView(View view) {
    }

    @Override
    public void showLoading(String message) {
        showProgressBar(message);
    }

    @Override
    public void dismissLoading() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();

        }

    }


    @Override
    public void showError(String error, boolean shouldEndActivity, boolean showToast) {
        dismissLoading();
        try {
            AlertUtils.showSuccessErrorAlert(getActivity(), false, error, shouldEndActivity, -1, showToast);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    public String getLoadingText() {
        return "Loading";
    }

    @Override
    public void showSuccessMessage(String message, boolean shouldEndActivity, int requestCode, boolean showToast) {

        String successMessage = null;
        if (message == null || message.isEmpty()) successMessage = Constants.GenericSuccess;
        else successMessage = message;
        try {
            AlertUtils.showSuccessErrorAlert(getActivity(), true, successMessage, shouldEndActivity, requestCode,
                    showToast);
        } catch (IllegalStateException e) {

        }
    }

    @Override
    public void showNoData() {

    }


    @Override
    public void hideNoData() {

    }

    public void onDataCount(int count) {
        if (count == 0)
            showNoData();
        else
            hideNoData();
    }


}
