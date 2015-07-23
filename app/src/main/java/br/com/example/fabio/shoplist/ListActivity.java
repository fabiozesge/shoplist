package br.com.example.fabio.shoplist;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.instinctcoder.sqlitedb.Product;
import com.instinctcoder.sqlitedb.ProductRepo;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends ActionBarActivity  implements android.view.View.OnClickListener {

    Button btnProductSave;
    Button btnProductCancel;
    EditText edtProductDescription;
    Spinner spsProductUnit;
    EditText edtProductQuantity;
    EditText edtProductBarnd;
    private int _Product_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        InitializeTabs();
        InitializeSpinner();
        InitializeListView();

        btnProductSave   = (Button) findViewById(R.id.btnItemSave);
        btnProductCancel = (Button) findViewById(R.id.btnItemCancel);
        edtProductDescription = (EditText)findViewById(R.id.edtDescription);
        spsProductUnit = (Spinner)findViewById(R.id.spnUnit);
        edtProductQuantity = (EditText)findViewById(R.id.edtQuantity);
        edtProductBarnd = (EditText)findViewById(R.id.edtBrand);

        _Product_Id =0;
        Intent intent = getIntent();//
        _Product_Id =intent.getIntExtra(Product.KEY_ID, 0);
        ProductRepo repo = new ProductRepo(this);
        Product product = new Product();
        product = repo.getProductById(_Product_Id);

        edtProductDescription.setText(product.description);
        edtProductQuantity.setText(String.valueOf(product.quantity));
        edtProductBarnd.setText(product.brand);
        spsProductUnit.setSelection(1);//ver como se faz

        btnProductSave.setOnClickListener(this);

    }

    public void onClick(View view) {
        if (view == findViewById(R.id.btnItemSave)){
            ProductRepo repo = new ProductRepo(this);
            Product product = new Product();
            product.quantity= Double.parseDouble(edtProductQuantity.getText().toString());
            product.description=edtProductDescription.getText().toString();
            product.brand=edtProductBarnd.getText().toString();
            product.unit=spsProductUnit.getSelectedItem().toString();
            product.product_ID=_Product_Id;
            if (_Product_Id==0){
                _Product_Id = repo.insert(product);

                Toast.makeText(this, "New Product Insert", Toast.LENGTH_SHORT).show();
            }else{
                repo.update(product);
                Toast.makeText(this,"Student Product updated",Toast.LENGTH_SHORT).show();
            }
        }else if (view== findViewById(R.id.btnItemCancel)){
            //ProductRepo repo = new ProductRepo(this);
            //repo.delete(_Product_Id);
            //Toast.makeText(this, "Product Record Deleted", Toast.LENGTH_SHORT);
            //finish();
        }else if (view== findViewById(R.id.btnItemCancel)){
            //finish();
        }


    }

    private void InitializeListView() {
        ListView productListView = (ListView)findViewById(R.id.listViewProduct);
        ProductRepo repo = new ProductRepo(this);
        ArrayList<HashMap<String, String>> productList =  repo.getProductList();
        ListAdapter adapter = new SimpleAdapter( ListActivity.this, productList, R.layout.productlist, new String[] {"description","quantity", "unit", "brand"}, new int[]
                {R.id.txtDescription, R.id.txtQuantity, R.id.txtUnit, R.id.txtBrand});
        productListView.setAdapter(adapter);
    }

    private void InitializeSpinner() {
        ArrayAdapter<String> adapterUnits = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.unitlist));
        adapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spsUnit = (Spinner)findViewById(R.id.spnUnit);
        spsUnit.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapterUnits,
                        android.R.layout.simple_spinner_dropdown_item,
                        this));
        spsUnit.setPrompt(getResources().getString(R.string.hint_unit));
    }

    private void InitializeTabs() {
        TabHost tabhost = (TabHost) findViewById(R.id.tabHost);
        tabhost.setup();
        TabHost.TabSpec ts = tabhost.newTabSpec("tab1");
        ts.setContent(R.id.tab_ShopList);
        ts.setIndicator(getResources().getString(R.string.tab_shoplist));
        tabhost.addTab(ts);

        ts = tabhost.newTabSpec("tab2");
        ts.setContent(R.id.tab_AddItem);
        ts.setIndicator(getResources().getString(R.string.tab_additem));
        tabhost.addTab(ts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_exit:
                finish();
                break;
            case R.id.action_adduser:
                Intent i = new Intent(ListActivity.this, UserActivity.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
