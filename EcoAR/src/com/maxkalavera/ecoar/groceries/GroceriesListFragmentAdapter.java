package com.maxkalavera.ecoar.groceries;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
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
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
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
		ProductModel product = this.groceriesListFragment.valuesList.get(elementListPosition);
		product.setChecked(isChecked);
		this.groceriesListFragment.modifyElement(elementListPosition);		
	}
	
};

/************************************************************
 * Listener para el numero de products
 ************************************************************/

class NumberOfListener implements View.OnClickListener, DialogInterface.OnClickListener {
	private GroceriesListFragment groceriesListFragment;
	private int elementListPosition;
	private NumberPicker numberPicker;
	private Context context;
	
	public NumberOfListener(GroceriesListFragment groceriesListFragment) {
		this.groceriesListFragment = groceriesListFragment;
		this.context = groceriesListFragment.getActivity();
		this.elementListPosition = -1;
		this.numberPicker = null;
	}

	private Context getContext() {
		return this.context;
	}
	
	@Override
	public void onClick(View view) {
		this.elementListPosition = (Integer) view.getTag();
		this.setUpNumerPickerAlert();
	}
	
    public void onClick(DialogInterface dialog, int id) {
		ProductModel product = 
				this.groceriesListFragment.valuesList.get(this.elementListPosition);
		product.number_of_products = this.numberPicker.getValue();
    	this.groceriesListFragment.modifyElement(elementListPosition);
    }
	
    // reference "http://stackoverflow.com/questions/17944061/how-to-use-number-picker-with-dialog"
	public void setUpNumerPickerAlert () {
		ProductModel product = 
				this.groceriesListFragment.valuesList.get(this.elementListPosition);
		
        RelativeLayout linearLayout = new RelativeLayout(getContext());
        this.numberPicker = new NumberPicker(getContext());
        this.numberPicker.setMaxValue(99);
        this.numberPicker.setMinValue(1);
        
        this.numberPicker.setValue(product.number_of_products);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        linearLayout.setLayoutParams(params);
        linearLayout.addView(this.numberPicker, numPicerParams);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Select the number");
        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok", this)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
	}
	
};


/************************************************************
 * 
 ************************************************************/
public class GroceriesListFragmentAdapter extends ArrayAdapter<ProductModel> {
	
	private ProductNameClickListener productNameClickListener;
	private FloatingMenuListener floatingMenuListener;
	private CheckboListener checkboListener;
	private NumberOfListener numberOfListener;
		
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public GroceriesListFragmentAdapter(GroceriesListFragment groceriesListFragment, ArrayList<ProductModel> productList) {
		super(groceriesListFragment.getActivity(),
				R.layout.groceries_list_item,
				productList);
		
		// Listeners
		this.productNameClickListener = new ProductNameClickListener(this);
		this.floatingMenuListener = new FloatingMenuListener(groceriesListFragment);
		this.checkboListener = new CheckboListener(groceriesListFragment);
		this.numberOfListener = new NumberOfListener(groceriesListFragment);
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

		TextView productName = (TextView) convertView.findViewById(R.id.groceries_list_item_name);
		productName.setText(pdata.name);
		productName.setTag(Integer.valueOf(position));
		productName.setOnClickListener(this.productNameClickListener);

		CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.groceries_list_item_checkbox);
		checkbox.setChecked(pdata.isChecked());
		checkbox.setTag(Integer.valueOf(position));
		checkbox.setOnCheckedChangeListener(checkboListener);
		
		TextView numberOf = (TextView) convertView.findViewById(R.id.groceries_list_item_numberof);
		numberOf.setText(String.valueOf(pdata.number_of_products));
		numberOf.setTag(Integer.valueOf(position));
		numberOf.setOnClickListener(this.numberOfListener);
		
		Button menuButton = (Button) convertView.findViewById(R.id.groceries_list_item_menu);
		menuButton.setTag(Integer.valueOf(position));
		menuButton.setOnClickListener(this.floatingMenuListener);

		
		return convertView;
	}
	
	
 
};


