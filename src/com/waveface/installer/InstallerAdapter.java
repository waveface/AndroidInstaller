package com.waveface.installer;


import java.util.ArrayList;

import com.waveface.installer.storage.InstallerEntity;
import com.waveface.installer.util.ImageDownloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InstallerAdapter extends BaseAdapter {
	private ArrayList<InstallerEntity> mInstallers = null;
	private LayoutInflater mInflater;
	private ImageDownloader mDownloader;

	public InstallerAdapter(Context context) {
		mInstallers = new ArrayList<InstallerEntity>();
		mInflater = LayoutInflater.from(context);
		mDownloader = new ImageDownloader(context);
	}
	
	@Override
	public int getCount() {
		return mInstallers.size();
	}

	@Override
	public Object getItem(int position) {
		return mInstallers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		final InstallerEntity installer = mInstallers.get(pos);
		
		if (view == null) {
			view = mInflater.inflate(R.layout.listitem, null);
			holder = new ViewHolder();
			holder.avatar = (ImageView)view.findViewById(R.id.avatar);
			holder.createDate = (TextView)view.findViewById(R.id.createDate);
			holder.version = (TextView)view.findViewById(R.id.version);
			holder.realDate = (TextView)view.findViewById(R.id.realDate);
			
			view.setTag(holder);
		}
		else {
			holder = (ViewHolder)view.getTag();
		}
		
		holder.version.setText(installer.name+"   Version:"+installer.version);
		holder.realDate.setText(installer.createdDate);
		mDownloader.download(Constant.SERVER_URL+installer.image, holder.avatar, R.drawable.default_avatar);
		return view;
	}

	
	public void setInstallers(ArrayList<InstallerEntity> installers) {
		mInstallers = installers;
		notifyDataSetChanged();
	}
	
	class ViewHolder {
		ImageView avatar;
		TextView createDate;
		TextView version;
		TextView realDate;
	}


}
