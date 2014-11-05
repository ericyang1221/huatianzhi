package com.eric.huatianzhi.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.eric.huatianzhi.R;

public class ProgressDialogFragment extends DialogFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(false);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
		dialog.setTitle(getString(R.string.loading));
		// dialog.setMessage(getString(R.string.webpage_being_loaded));
		dialog.setIndeterminate(true);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		return dialog;
	}
}