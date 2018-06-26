package net.clamour.mangalcity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import net.clamour.mangalcity.Home.PostActivity;
import net.clamour.mangalcity.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationActivity2 extends AppCompatActivity {

    String country[] = {"Select Country", "India", "France", "Germany"};
    String state[] = {"Select State", "Karnatka", "Sikkim", "Kerala", "Uttar Pradesh"};
    String district[] = {"Select District", "Mathura", "Firozabad", "Agra"};
    String block[] = {"Select Block", "Faridpuri", "Baheri"};
    @BindView(R.id.country_spinner)
    Spinner countrySpinner;
    @BindView(R.id.state_spinner)
    Spinner stateSpinner;
    @BindView(R.id.district_spinner)
    Spinner districtSpinner;
    @BindView(R.id.block_spinner)
    Spinner blockSpinner;
    @BindView(R.id.continue_button)
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);
        ButterKnife.bind(this);

//        CustomAdapterSpinner customAdapter = new CustomAdapterSpinner(getApplicationContext(), country);
//        countrySpinner.setAdapter(customAdapter);
//
//        CustomAdapterSpinner customAdapter1 = new CustomAdapterSpinner(getApplicationContext(), state);
//        stateSpinner.setAdapter(customAdapter1);
//
//        CustomAdapterSpinner customAdapter2 = new CustomAdapterSpinner(getApplicationContext(), district);
//        districtSpinner.setAdapter(customAdapter2);
//
//        CustomAdapterSpinner customAdapter3 = new CustomAdapterSpinner(getApplicationContext(), block);
//        blockSpinner.setAdapter(customAdapter3);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegistrationActivity2.this, PostActivity.class);
                startActivity(intent);
            }
        });


    }
}
