package fpoly.quanlytaikhoannganhang;

import com.st.accounts.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CapNhatTaiKhoan extends Activity {
	private String accountId;
	private EditText editAcno, editHolders, editBankName,
			editBalance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capnhattaikhoan);
		editAcno = (EditText) this.findViewById(R.id.editAcno);
		editHolders = (EditText) this.findViewById(R.id.editHolders);
		editBankName = (EditText) this.findViewById(R.id.editBankName);
		editBalance = (EditText) this.findViewById(R.id.editBalance);
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
		accountId = this.getIntent().getStringExtra("accountid");
		Log.d("Tài khoản", "Account Id : " + accountId);
		DBHelper dbhelper = new DBHelper(this);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor account = db.query(Database.ACCOUNTS_TABLE_NAME, null,
				" _id = ?", new String[] { accountId }, null, null, null);
		//startManagingCursor(accounts);
		if (account.moveToFirst()) {
			// update view
			editAcno.setText(account.getString(account
					.getColumnIndex(Database.ACCOUNTS_ACNO)));
			editHolders.setText(account.getString(account
					.getColumnIndex(Database.ACCOUNTS_HOLDERS)));
			editBankName.setText(account.getString(account
					.getColumnIndex(Database.ACCOUNTS_BANK)));
			editBalance.setText(account.getString(account
					.getColumnIndex(Database.ACCOUNTS_BALANCE)));
		}
		account.close();
		db.close();
		dbhelper.close();

	}

	public void updateAccount(View v) {
		try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			// execute insert command
			ContentValues values = new ContentValues();
			values.put(Database.ACCOUNTS_ACNO, editAcno.getText().toString());
			values.put(Database.ACCOUNTS_HOLDERS, editHolders.getText()
					.toString());
			values.put(Database.ACCOUNTS_BANK, editBankName.getText()
					.toString());
			values.put(Database.ACCOUNTS_BALANCE, editBalance.getText()
					.toString());
			long rows = db.update(Database.ACCOUNTS_TABLE_NAME, values,
					"_id = ?", new String[] { accountId });

			db.close();
			if (rows > 0)
				Toast.makeText(this, "Cập nhật tài khoản thành công!",
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "Không thể cập nhật tài khoản!",
						Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void deleteAccount(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Bạn chắc chắn muốn xóa tài khoản này?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                deleteCurrentAccount();
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
	}
	
	
         			
    public void deleteCurrentAccount() {
    	try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			int rows = db.delete(Database.ACCOUNTS_TABLE_NAME, "_id=?", new String[] { accountId});
			dbhelper.close();
			if ( rows == 1) {
				Toast.makeText(this, "Bạn xóa tài khoản thành công!", Toast.LENGTH_LONG).show();
				this.finish();
			}
			else
				Toast.makeText(this, "Không thể xóa tài khoản!", Toast.LENGTH_LONG).show();

		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	                                                                                                                                                                                         
    	
    	
    	
    	
    	
    	
    	
    	

	}
    
    public void listAccountTransactions(View v) {
    	Intent intent = new Intent(this,DanhSachGiaoDichTaiKhoan.class);
    	intent.putExtra("accountid", accountId);
    	startActivity(intent);
	}
}
