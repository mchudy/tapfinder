package tk.tapfinderapp.view.place.beers.addbeer;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.BeerDto;
import tk.tapfinderapp.service.TapFinderApiService;

public class BeersAdapter extends ArrayAdapter<BeerDto>{
    private List<BeerDto> items;
    private Context context;
    private TapFinderApiService apiService;

    public BeersAdapter(Context context, int resource, TapFinderApiService apiService) {
        super(context, resource);
        this.context = context;
        this.apiService = apiService;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public BeerDto getItem(int position) {
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.beer_item, parent, false);
        }
        TextView beerName = ButterKnife.findById(convertView, R.id.beer_name);
        TextView brewery = ButterKnife.findById(convertView, R.id.brewery);
        BeerDto beer = items.get(position);
        beerName.setText(beer.getName());
        brewery.setText(beer.getBrewery().getName());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {
                    List<BeerDto> beers = findBeers(constraint.toString());
                    filterResults.values = beers;
                    filterResults.count = beers.size();
                }
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    items = (List<BeerDto>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
    }

    private List<BeerDto> findBeers(String query) {
        return apiService.findBeers(query)
                .toBlocking()
                .first();
    }
}
