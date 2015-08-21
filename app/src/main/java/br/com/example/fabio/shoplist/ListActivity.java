package br.com.example.fabio.shoplist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import static android.widget.Toast.*;

public class ListActivity extends ActionBarActivity  implements android.view.View.OnClickListener {
    Button btnProductSave;
    Button btnProductCancel;
    EditText edtProductDescription;
    Spinner spsProductUnit;
    EditText edtProductQuantity;
    EditText edtProductBarnd;
    private int _Product_Id;
    ListAdapter productadapter;
    ListView productListView;
    ArrayList<HashMap<String, String>> productList;
    ProductRepo repo;
    TabHost tabhost;

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



        btnProductSave.setOnClickListener(this);
        btnProductCancel.setOnClickListener(this);

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
                try {
                    _Product_Id = repo.insert(product);
                }catch (Exception Ex){
                    SimpleMessage(getResources().getString(R.string.product_insert) + Ex.getMessage());
                }
                _Product_Id = 0;

                makeText(this, getResources().getString(R.string.product_insert), LENGTH_SHORT).show();
            }else{
                repo.update(product);
                makeText(this, getResources().getString(R.string.product_update), LENGTH_SHORT).show();
            }
            LoadProducts();
        }else if (view== findViewById(R.id.btnItemCancel)){
            LoadProducts();
        }
        edtProductBarnd.setText("");
        edtProductDescription.setText("");
        edtProductQuantity.setText("");

    }

    private void InitializeListView() {
        productListView = (ListView)findViewById(R.id.listViewProduct);
        registerForContextMenu(productListView);
        LoadProducts();
    }


    private void LoadProducts() {
        repo = new ProductRepo(this);
        productList =  repo.getProductList();
        productadapter = new SimpleAdapter( ListActivity.this, productList, R.layout.productlist,
                new String[] {"product_id", "description","quantity", "unit", "brand"}, new int[]
                {R.id.txtProductID, R.id.txtDescription, R.id.txtQuantity, R.id.txtUnit, R.id.txtBrand});
        productListView.setAdapter(productadapter);
        tabhost.setCurrentTab(0);

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
        tabhost = (TabHost) findViewById(R.id.tabHost);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_exit:
                ExitOrNotExit();
                break;
            case R.id.action_adduser:
                Intent i = new Intent(ListActivity.this, UserActivity.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ExitOrNotExit() {
        AlertDialog alertDialog = new AlertDialog.Builder(ListActivity.this).create();
        alertDialog.setTitle(getResources().getString(R.string.msg_title));
        alertDialog.setMessage(getResources().getString(R.string.msg_exit));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.msg_btn_positive),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        System.exit(0);
                    }

                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.msg_btn_negative),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_product, menu);
    }

    private void SimpleMessage(String AText){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ListActivity.this);
        dlgAlert.setMessage(AText);
        dlgAlert.setTitle(getResources().getString(R.string.msg_title));
        dlgAlert.setPositiveButton(getResources().getString(R.string.btn_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    private void LoadProductDetail(Integer AProduct){
        ProductRepo repo = new ProductRepo(this);
        Product product = new Product();
        product = repo.getProductById(AProduct);

        edtProductDescription.setText(product.description);
        edtProductQuantity.setText(String.valueOf(product.quantity));
        edtProductBarnd.setText(product.brand);
        spsProductUnit.setSelection(1);//ver como se faz
        tabhost.setCurrentTab(1);
    }
    private void ConfirmExclusion(String amessagetext, final Boolean adeleteall) {
        AlertDialog alertDialog = new AlertDialog.Builder(ListActivity.this).create();
        alertDialog.setTitle(getResources().getString(R.string.msg_title));
        alertDialog.setMessage(amessagetext);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.msg_btn_positive),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try {
                            if (adeleteall) {
                                repo.deleteall();
                                Toast.makeText(ListActivity.this, getResources().getString(R.string.product_delete_all), Toast.LENGTH_SHORT);
                            } else {
                                repo.delete(_Product_Id);
                                Toast.makeText(ListActivity.this, getResources().getString(R.string.product_delete), Toast.LENGTH_SHORT);
                            }
                            LoadProducts();
                            _Product_Id = 0;
                        }catch (Exception E){
                            SimpleMessage(getResources().getString(R.string.msg_delete_error)+ E.getMessage());
                        }
                    }

                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.msg_btn_negative),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int id = item.getItemId();

        HashMap<String, String> selectedproduc = (HashMap<String, String> )productadapter.getItem(info.position);
        _Product_Id = Integer.parseInt(selectedproduc.get("id"));
        switch (id) {
            case R.id.action_delete:
                ConfirmExclusion(getResources().getString(R.string.msg_delete), false);
                break;
            case R.id.action_deleteall:
                ConfirmExclusion(getResources().getString(R.string.msg_delete_all), true);
                break;
            case R.id.action_edit:
                LoadProductDetail(_Product_Id);
                break;
        }
        return super.onContextItemSelected(item);
    }

}
