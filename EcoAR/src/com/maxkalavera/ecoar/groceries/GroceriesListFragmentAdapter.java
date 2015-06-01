package com.maxkalavera.ecoar.groceries;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.productinfo.ProductInfo;
import com.maxkalavera.utils.database.productmodel.ProductModel;

/************************************************************
 * Listener para el menu flotante del producto
 ************************************************************/
class FloatingMenuListener 	implements View.OnClickListener,
	PopupMenu.OnMenuItemClickListener {
	
	private Context context;
	private GroceriesListFragment groceriesListFragment;
	private int elementListPosition;
	
	public FloatingMenuListener (GroceriesListFragment groceriesListFragment) {
		this.groceriesListFragment = groceriesListFragment;
		this.context = groceriesListFragment.getActivity().getApplicationContext();
		this.elementListPosition = -1;
	}
	
	private Context getContext() {
		return this.context;
	}
	
	public void onClick(View view) {  
		PopupMenu popup = new PopupMenu(getContext(), view);  
		this.elementListPosition = (Integer)view.getTag();
         
		popup.getMenuInflater().inflate(R.menu.groceries_list_floatingmenu, popup.getMenu());
		popup.setOnMenuItemClickListener(this);
		
		popup.show();
	}
	
	public boolean onMenuItemClick(MenuItem item) {  
		switch(item.getItemId()){
			case R.id.groceries_list_floatingmenu_delete:
				groceriesListFragment.removeProduct(this.elementListPosition);
     			break;
     	}
		return true;
	} 
	
};

/************************************************************
 * Listener para el nombre del producto
 ************************************************************/

class ProductNameClickListener implements View.OnClickListener {
	GroceriesListFragmentAdapter groceriesListFragmentAdapter;
	
	public ProductNameClickListener(GroceriesListFragmentAdapter groceriesListFragmentAdapter) {
		this.groceriesListFragmentAdapter = groceriesListFragmentAdapter;
	}
	
	@Override
	public void onClick(View view) {
		ProductModel pModel = groceriesListFragmentAdapter.getItem((Integer)view.getTag());
		
        Intent intent = new Intent();
        intent.setClass(groceriesListFragmentAdapter.getContext(), ProductInfo.class);
        intent.putExtra("product", pModel);
        groceriesListFragmentAdapter.getContext().startActivity(intent);
        
		
	}
	
};

/************************************************************
 * Listener para el checkbox del producto
 ************************************************************/

class CheckboListener implements CompoundButton.OnCheckedChangeListener {
	private GroceriesListFragment groceriesListFragment;

	public CheckboListener(GroceriesListFragment groceriesListFragment) {
		this.groceriesListFragment = groceriesListFragment;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int elementListPosition = (Integer) buttonView.getTag();
		this.groceriesListFragment.modifyElement(elementListPosition, 
				isChecked);		
	}
	
};

/************************************************************
 * 
 ************************************************************/
public class GroceriesListFragmentAdapter extends ArrayAdapter<ProductModel> {
	
	private ProductNameClickListener productNameClickListener;
	private FloatingMenuListener floatingMenuListener;
	private CheckboListener checkboListener;
		
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public GroceriesListFragmentAdapter(GroceriesListFragment groceriesListFragment, ArrayList<ProductModel> productList) {
		super(groceriesListFragment.getActivity(),
				R.layout.searchbar_results_item,
				productList);
		
		// Listeners
		this.productNameClickListener = new ProductNameClickListener(this);
		this.floatingMenuListener = new FloatingMenuListener(groceriesListFragment);
		this.checkboListener = new CheckboListener(groceriesListFragment);
	}
	
	/************************************************************
	 * 
	 ************************************************************/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			 LayoutInflater inflater = LayoutInflater.from(this.getContext());
			convertView = inflater.inflate(R.layout.groceries_list_item, null);
		}
		
		ProductModel pdata = this.getItem(position);
		
		TextView productName = (TextView) convertView.findViewById(R.id.groceries_item_name);
		productName.setText(pdata.name);
		productName.setTag(Integer.valueOf(position));
		productName.setOnClickListener(this.productNameClickListener);
		
		CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.groceries_item_checkbox);
		checkbox.setChecked(pdata.isChecked());
		productName.setTag(Integer.valueOf(position));
		checkbox.setOnCheckedChangeListener(checkboListener);
		
		Button menuButton = (Button) convertView.findViewById(R.id.groceries_item_menu);
		menuButton.setTag(Integer.valueOf(position));
		menuButton.setOnClickListener(this.floatingMenuListener);
		
		return convertView;
	}
	
	
 
};


