package fpoly.quanlytaikhoannganhang;

import java.util.Calendar;

import com.st.accounts.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ThemGiaoDich extends Activity {
	private Spinner spinnerAccounts;
	private TextView textTransDate;
	private int day, month, year;
	private final int DATE_DIALOG = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.themgiaodich);
		spinnerAccounts = (Spinner) this.findViewById(R.id.spinnerAccounts);
		Database.populateAccounts(spinnerAccounts);
		textTransDate = (TextView) this.findViewById(R.id.textTransDate);
		 // get the current date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        updateDateDisplay();
	}
	
	private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int pYear,int pMonth, int pDay) {
                    year = pYear;
                    month = pMonth;
                    day = pDay;
                    updateDateDisplay();
                }
            };

            
	@Override 
	public void onStart() {
		super.onStart();
		
	}

	public void showDateDialog(View v) {
		  showDialog(DATE_DIALOG);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);
		
	    switch (id) {
	    case DATE_DIALOG:
	        return new DatePickerDialog(this,
	                    dateSetListener, year, month, day);
	    }
	    return null;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return Utils.inflateMenu(this,menu);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		return  Utils.handleMenuOption(this,item);
	}
	
	private void updateDateDisplay() {
            // Month is 0 based so add 1
	        textTransDate.setText( String.format("%d-%d-%d",year,month + 1,day));
	}
	 
	public void addTransaction(View v) {
		// get access to views
				String accountId = Database.getAccountId(spinnerAccounts);
				RadioButton  radioDeposit = (RadioButton) this.findViewById(R.id.radioDeposit);
				
				EditText editTransAmount = (EditText) this.findViewById(R.id.editTransAmount);

				boolean done = Database.addTransaction(this,
						accountId,
						radioDeposit.isChecked() ? "d" : "w",   // trans type 
						textTransDate.getText().toString(),
						editTransAmount.getText().toString());
				
				if ( done )
					Toast.makeText(this,"Thêm giao dịch thành công!", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(this, "Không thể thêm giao dịch!", Toast.LENGTH_LONG).show();
	} // addDeposit
	

}
