package fpoly.quanlytaikhoannganhang;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.st.accounts.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DanhSachGiaoDich extends Activity {
	ListView listTransactions;
	String  fromDate,toDate,fromAmount,toAmount;
	String  condition = " 1 = 1 ";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.danhsachgiaodich);
		fromDate = this.getIntent().getStringExtra("fromdate");
		toDate = this.getIntent().getStringExtra("todate");
		fromAmount = this.getIntent().getStringExtra("fromamount");
		toAmount = this.getIntent().getStringExtra("toamount");
		listTransactions = (ListView) this.findViewById(R.id.listTransactions);
		
		// form condition based on input
		if ( fromDate.length() > 0)
			  condition += " and  " + Database.TRANSACTIONS_TRANSDATE + " >= '" + fromDate + "'";
		
		if ( toDate.length() > 0)
			  condition += " and  " + Database.TRANSACTIONS_TRANSDATE + " <= '" + toDate + "'";
		
		if ( fromAmount.length() > 0)
			  condition += " and  " + Database.TRANSACTIONS_TRANSAMOUNT + " >= " + fromAmount;
		
		if ( toAmount.length() > 0)
			  condition += " and  " + Database.TRANSACTIONS_TRANSAMOUNT + " <= " + toAmount;

				
		
		
		listTransactions.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View selectedView, int arg2,long arg3) {
				TextView  textTransId = (TextView) selectedView.findViewById(R.id.textTransId);
				Intent intent = new Intent(DanhSachGiaoDich.this, ChiTietGiaoDich.class);
				intent.putExtra("transid", textTransId.getText().toString());
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return Utils.inflateMenu(this,menu);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		return  Utils.handleMenuOption(this,item);
	}
	
	
	@Override 
	public void onStart() {
		super.onStart();
		try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor trans = db.rawQuery("select t._id, acno, bank, transdate,transamount,transtype from transactions t inner join accounts a  on ( a._id = t.account_id) where " + condition,null);
			if ( trans.getCount() == 0 )
				this.findViewById(R.id.textError).setVisibility(View.VISIBLE);
			else
				this.findViewById(R.id.textError).setVisibility(View.INVISIBLE);
			
			ArrayList<Map<String,String>> listTrans = new ArrayList<Map<String,String>>();
            while ( trans.moveToNext()) {
            	// get trans details for display
            	LinkedHashMap<String,String> tran = new LinkedHashMap<String,String>();
            	tran.put("transid", trans.getString(trans.getColumnIndex(Database.TRANSACTIONS_ID)));
            	tran.put("acno",  trans.getString(trans.getColumnIndex(Database.ACCOUNTS_ACNO)) + " - " +   trans.getString(trans.getColumnIndex(Database.ACCOUNTS_BANK)));
            	tran.put("transdate",trans.getString(trans.getColumnIndex(Database.TRANSACTIONS_TRANSDATE)));
            	tran.put("transtype",trans.getString(trans.getColumnIndex(Database.TRANSACTIONS_TRANSTYPE)));
            	tran.put("transamount",trans.getString(trans.getColumnIndex(Database.TRANSACTIONS_TRANSAMOUNT)));
            	
            	String transDetails = "Cash";
            	tran.put("transdetails",transDetails);
            	listTrans.add(tran);
            }
            trans.close();
            db.close();
		    dbhelper.close();
		    
		    SimpleAdapter  adapter = new SimpleAdapter(this,
		    		listTrans, 
		    		R.layout.giaodich, 
		    		new String [] {"transid", "acno", "transdate", "transdetails", "transtype", "transamount" ,"transremarks"},
		    		new  int [] {  R.id.textTransId, R.id.textAcno,  R.id.textTransDate, R.id.textTransDetails, R.id.textTransType, R.id.textTransAmount, R.id.textTransRemarks});
		    
		    listTransactions.setAdapter(adapter);
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
}

