package zig.qiubo;

import java.util.ArrayList;

import zig.qiubo.utils.ContactUtil;
import zig.qiubo.utils.DBHelp;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class StarActivity extends Activity {

	private DBHelp dbHelp;
	private ContactUtil contactUtil;
	private ListView contactLv;
	private ArrayList<String> numList;
	private Adapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_star);
		dbHelp = DBHelp.getInstance(this);
//		contactUtil = ContactUtil.getInstance(this);
		numList = dbHelp.getAllStarPhone();
		
		contactLv = (ListView)findViewById(R.id.lv_main);
		adapter = new Adapter();
		contactLv.setAdapter(adapter);
	}

	
	class Adapter extends BaseAdapter{
		@Override
		public int getCount() {
			return numList == null?0:numList.size();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return null;
		}

		
		
	}

}
