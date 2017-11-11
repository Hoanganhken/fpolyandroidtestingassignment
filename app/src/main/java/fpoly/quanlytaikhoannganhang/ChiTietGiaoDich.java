package fpoly.quanlytaikhoannganhang;
import com.st.accounts.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ChiTietGiaoDich extends Activity {
	private String transId;
	private String accountId;
	private TextView textAcno;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chitietgiaodich);
		
		transId = this.getIntent().getStringExtra("transid");
		Log.d("Tài khoản", "Trans id : " + transId);
		
		textAcno = (TextView) this.findViewById(R.id.textAcno);
		TextView textTransDate = (TextView) this.findViewById(R.id.textTransDate);
		TextView textTransType = (TextView) this.findViewById(R.id.textTransType);
		TextView textTransAmount = (TextView) this.findViewById(R.id.textTransAmount);
		
		DBHelper dbhelper = new DBHelper(this);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor tran = db.rawQuery("select acno,account_id,transdate,transamount,transtype from transactions t inner join accounts a  on ( a._id = t.account_id) where t._id = ?",
				    new String[] {transId });
		
       		
		if (tran.moveToFirst()) {
			accountId  =  tran.getString(tran.getColumnIndex(Database.TRANSACTIONS_ACCOUNT_ID));
			textAcno.setText( tran.getString(tran.getColumnIndex(Database.ACCOUNTS_ACNO)));
			textTransDate.setText( tran.getString(tran.getColumnIndex(Database.TRANSACTIONS_TRANSDATE)));
			textTransType.setText( (tran.getString(tran.getColumnIndex(Database.TRANSACTIONS_TRANSTYPE))).equals("d")?"Gửi tiền":"Rút tiền");
			textTransAmount.setText( tran.getString(tran.getColumnIndex(Database.TRANSACTIONS_TRANSAMOUNT)));
		}
		else
			Log.d("Tài khoản","Không tìm thấy giao dịch!");
		
		db.close();
		dbhelper.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return Utils.inflateMenu(this,menu);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		return  Utils.handleMenuOption(this,item);
	}
	
	public void deleteTransaction(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Bạn chắc chắc muốn xóa giao dịch này?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                deleteCurrentTransaction();
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
	
         			
    public void deleteCurrentTransaction() {
    	try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			int rows = db.delete(Database.TRANSACTIONS_TABLE_NAME, "_id=?", new String[] { transId});
			dbhelper.close();
			if ( rows == 1) {
				Toast.makeText(this, "Xóa giao dịch thành công!", Toast.LENGTH_LONG).show();
				this.finish();
			}
			else
				Toast.makeText(this, "Không thể xóa giao dịch!", Toast.LENGTH_LONG).show();
		} 
    	catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
    
    public void showAccountDetails(View v) {
        Intent intent = new Intent(this,CapNhatTaiKhoan.class);
    	intent.putExtra("accountid", accountId);
    	startActivity(intent);
	}
}
