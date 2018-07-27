package net.clamour.mangalcity.Home;

/**
 * Created by clamour_5 on 7/27/2018.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.clamour.mangalcity.citypost.CityTab;
import net.clamour.mangalcity.countrypost.CountryTab;
import net.clamour.mangalcity.districtpost.DistrictTab;
import net.clamour.mangalcity.statepost.StateTab;


public class SectionsPagerAdapter extends FragmentPagerAdapter {


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CityTab cityTab = new CityTab();
                return cityTab;

            case 1:
                DistrictTab districtTab = new DistrictTab();
                return districtTab;

            case 2:
                 StateTab stateTab = new StateTab();
                return stateTab;

            case 3:
                CountryTab countryTab = new CountryTab();
                return countryTab;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "City";

            case 1:
                return "District";

            case 2:
                return "State";

            case 3:
                return "Country";
            default:
                return null;
        }

    }

}