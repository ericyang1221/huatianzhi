package com.eric.huatianzhi.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.eric.huatianzhi.BaseActivity;
import com.eric.huatianzhi.MainActivity;
import com.eric.huatianzhi.MyApplication;
import com.eric.huatianzhi.R;
import com.eric.huatianzhi.dialogs.ProgressDialogFragment;
import com.eric.huatianzhi.utils.MLog;
import com.eric.volley.VolleySingleton;

public class BaseFragment extends Fragment {
	private final String TAG = "BaseFragment";
	private MyApplication myApp;
	protected RequestQueue mRequestQueue;
	private final String PROGRESS_DIALOG_TAG = "PROGRESS_DIALOG_TAG";
	private ProgressDialogFragment pd;

	public MyApplication getMyApplication() {
		if (myApp == null) {
			myApp = ((BaseActivity) this.getActivity()).getMyApplication();
		}
		return myApp;
	}

	public void go(BaseFragment dest) {
		FragmentTransaction t = getTheFragmentManager().beginTransaction();
		t.replace(R.id.content, dest);
		t.addToBackStack(null);
		t.commit();
		getTheFragmentManager().executePendingTransactions();
	}

	public FragmentManager getTheFragmentManager() {
		return getBaseActivity().getTheFragmentManager();
	}

	public void goBack() {
		getTheFragmentManager().popBackStack();
	}

	public BaseActivity getBaseActivity() {
		return (BaseActivity) this.getActivity();
	}

	public MainActivity getMainActivity() {
		return (MainActivity) this.getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MLog.d(TAG, "onCreateView");
		mRequestQueue = VolleySingleton.getInstance().getRequestQueue();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	protected void addRequest(@SuppressWarnings("rawtypes") Request request) {
		if (mRequestQueue != null) {
			mRequestQueue.add(request);
		}
	}

	@Override
	public void onPause() {
		MLog.d(TAG, "onPause");
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(this);
		}
		super.onPause();
	}

	public void showProgressDialog() {
		if(pd == null){
			pd = new ProgressDialogFragment();
		}
		pd.show(getTheFragmentManager(), PROGRESS_DIALOG_TAG);
	}

	public void dismissProgressDialog() {
		if (pd != null && pd.isVisible()) {
			pd.dismiss();
			pd = null;
		}
	}
}
