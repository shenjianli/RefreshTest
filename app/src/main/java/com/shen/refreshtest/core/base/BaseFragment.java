package com.shen.refreshtest.core.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shen.netclient.util.LogUtils;
import com.shen.refreshtest.R;

public abstract class BaseFragment extends Fragment {

	private final String TAG = this.getClass().getSimpleName();
	protected BaseFragment mCurrentFragment;
	protected Context context;
	protected View mView;

	@Override
	public void onAttach(Context activity) {
		super.onAttach(activity);
		LogUtils.d(TAG, "onAttach");
		context = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils.d(TAG, "onCreate");

	}

	public void initData()
	{
		LogUtils.d(TAG, "initData");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils.d(TAG, "onCreateView");
		initData();
		initView();
		return mView;
	}

	public void initView() {
		LogUtils.d(TAG, "initView");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtils.d(TAG, "onActivityCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtils.d(TAG, "onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtils.d(TAG, "onResume");
	}

	public void switchToDifferentFragment(Fragment toFragment, int resId,
										  String tag) {
		int fragmentIndex = toFragment.getArguments().getInt("index");
		int currentIndex = mCurrentFragment.getArguments().getInt("index");
		if (currentIndex != fragmentIndex) {
			if (!toFragment.isAdded()) {
				getChildFragmentManager().beginTransaction()
						.add(resId, toFragment, tag).hide(mCurrentFragment).show(toFragment).commit();
			} else {
				getChildFragmentManager().beginTransaction()
						.hide(mCurrentFragment).show(toFragment).commit();
			}
			mCurrentFragment = (BaseFragment) toFragment;
		}
	}
	
	public void switchToDifferentFragment(Fragment toFragment, int resId,
										  String tag, boolean toLeft) {
		int fragmentIndex = toFragment.getArguments().getInt("index");
		int currentIndex = mCurrentFragment.getArguments().getInt("index");
		if (currentIndex != fragmentIndex) {
			
			FragmentTransaction fragmentTransaction;
			fragmentTransaction = getChildFragmentManager().beginTransaction();
			if(toLeft){
				fragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
			}
			else {
				fragmentTransaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out);
			}
			if (!toFragment.isAdded()) {
				fragmentTransaction
						.hide(mCurrentFragment).add(resId, toFragment, tag).commit();
			} else {
				fragmentTransaction
						.hide(mCurrentFragment).show(toFragment).commit();
			}
			mCurrentFragment = (BaseFragment) toFragment;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtils.d(TAG, "onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtils.d(TAG, "onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtils.d(TAG, "onDestroyView");
		clearObject();
		clearView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtils.d(TAG, "onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtils.d(TAG, "onDetach");
	}

	public abstract void clearObject();

	public abstract void clearView();
	
}
