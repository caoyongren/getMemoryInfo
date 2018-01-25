package com.getmemoryinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.getmemoryinfo.R;
import com.getmemoryinfo.entity.AppEntity;

import java.util.List;

;

public class AppAdapter extends BaseAdapter {

	private Context context;
	private List<AppEntity> list;
	private LayoutInflater inflater;

	public AppAdapter(Context context, List<AppEntity> list) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		if (list == null) {
			return null;
		}
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		if (list == null) {
			return 0;
		}
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.activity_listview_item, null);
			holder.icon = (ImageView) view.findViewById(R.id.item_icon);
			holder.name = (TextView) view.findViewById(R.id.item_name);
			holder.size = (TextView) view.findViewById(R.id.item_size);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final AppEntity ent = list.get(position);
		holder.icon.setImageDrawable(ent.getAppIcon());
		holder.name.setText(ent.getAppName());
		holder.size.setText(ent.getMemorySize() + "MB");
		return view;
	}

	static class ViewHolder {
		private ImageView icon;
		private TextView name;
		private TextView size;
	}
}
