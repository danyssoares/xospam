package br.net.toolbox.android.xospam.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import br.net.toolbox.android.xospam.ListSmsFragment;
import br.net.toolbox.android.xospam.NumeroDesconsiderarFragment;
import br.net.toolbox.android.xospam.SmsEnviadoFragment;
 
public class TabsPageAdapter extends FragmentPagerAdapter {
 
    public TabsPageAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Sms List fragment activity
            return new ListSmsFragment();
        case 1:
            // sms Enviado fragment activity
            return new SmsEnviadoFragment();
        case 2:
            // Número desconsiderado fragment activity
            return new NumeroDesconsiderarFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}
