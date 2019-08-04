package adapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aru.expapp.R;
import models.Bills;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillsViewHolder> {

    private List<Bills> billsList;
    private DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    public BillAdapter(List<Bills> billsList) {

        this.billsList = billsList;
    }

    @Override
    public BillsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill_recycler, parent, false);

        return new BillsViewHolder(itemView);
    }

    /**
     * This method is called when binding the data to the views being created in RecyclerView
     */
    @Override
    public void onBindViewHolder(BillsViewHolder holder, int position) {
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("$ #,###.##", symbols);
        holder.textViewAmount.setText(decimalFormat.format(billsList.get(position).getAmount()));

        holder.textViewDescription.setText(billsList.get(position).getDescription());
        holder.textViewCompany.setText(billsList.get(position).getCompany_name());
        holder.textViewDate.setText(billsList.get(position).getDateString());
        holder.textViewCategory.setText(billsList.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        Log.v(BillAdapter.class.getSimpleName(),""+billsList.size());
        return billsList.size();
    }

    /**
     * BillsViewHolder class that helps to populate data to the view
     */
    public class BillsViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewAmount;
        public AppCompatTextView textViewDescription;
        public AppCompatTextView textViewCompany;
        public AppCompatTextView textViewDate;
        public AppCompatTextView textViewCategory;

        public BillsViewHolder(View view) {
            super(view);
            textViewAmount = (AppCompatTextView) view.findViewById(R.id.textViewAmount);
            textViewDescription = (AppCompatTextView) view.findViewById(R.id.textViewDescription);
            textViewCompany = (AppCompatTextView) view.findViewById(R.id.textViewCompany);
            textViewDate = (AppCompatTextView) view.findViewById(R.id.textViewDate);
            textViewCategory = (AppCompatTextView) view.findViewById(R.id.textViewCategory);
        }
    }
}
