package tk.tapfinderapp.view.place.addspecialoffer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.AddSpecialOfferDto;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.util.EditTextDatePicker;
import tk.tapfinderapp.util.KeyboardUtils;
import tk.tapfinderapp.view.BaseFragment;

import static android.text.format.DateFormat.getDateFormat;

public class AddSpecialOfferFragment extends BaseFragment {

    private String placeId;
    private Date startDate;
    private Date endDate;

    @Bind(R.id.title_layout)
    TextInputLayout titleLayout;

    @Bind(R.id.title)
    EditText title;

    @Bind(R.id.description_layout)
    TextInputLayout descriptionLayout;

    @Bind(R.id.description)
    EditText description;

    @Bind(R.id.start_date)
    EditText startDateView;

    @Bind(R.id.end_date)
    EditText endDateView;

    @Bind(R.id.end_date_layout)
    TextInputLayout endDateLayout;

    @Inject
    TapFinderApiService apiService;

    public static AddSpecialOfferFragment newInstance(String placeId){
        AddSpecialOfferFragment fragment = new AddSpecialOfferFragment();
        Bundle args = new Bundle();
        args.putString("placeId", placeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        placeId = args.getString("placeId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_special_offer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        activityComponent().inject(this);
        initDateListeners();
    }

    private void initDateListeners() {
        startDateView.setOnClickListener(new EditTextDatePicker(getActivity(), startDateView));
        endDateView.setOnClickListener(new EditTextDatePicker(getActivity(), endDateView));
    }

    @Override
    protected int getTitleResId() {
        return R.string.add_special_offer;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                addSpecialOffer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addSpecialOffer() {
        if(!validateInput()) return;
        AddSpecialOfferDto dto = new AddSpecialOfferDto(placeId, title.getText().toString(),
                description.getText().toString(), startDate, endDate);
        apiService.addSpecialOffer(dto, placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.code() == 201) {
                        Toast.makeText(getContext(), R.string.special_offer_added, Toast.LENGTH_SHORT).show();
                        KeyboardUtils.hideSoftKeyboard(getActivity());
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.error_occurred), Toast.LENGTH_SHORT).show();
                    }
                }, t -> Timber.wtf(t.getMessage()));
    }

    private boolean validateInput() {
        boolean valid = true;
        titleLayout.setError(null);
        descriptionLayout.setError(null);
        endDateLayout.setError(null);
        if(TextUtils.isEmpty(title.getText())) {
            titleLayout.setError(getString(R.string.error_field_required));
            valid = false;
        }
        if(TextUtils.isEmpty(description.getText())) {
            descriptionLayout.setError(getString(R.string.error_field_required));
            valid = false;
        }
        startDate = parseDateTime(startDateView.getText().toString());
        endDate = parseDateTime(endDateView.getText().toString());
        if(startDate.after(endDate)) {
            endDateLayout.setError(getString(R.string.error_end_date_before_start_date));
            valid = false;
        }
        return valid;
    }

    private Date parseDateTime(String dateText) {
        DateFormat dateFormat = getDateFormat(getContext().getApplicationContext());
        try {
            return dateFormat.parse(dateText);
        } catch (ParseException e) {
            Timber.wtf(e.getMessage());
        }
        return null;
    }
}
