package com.example.android.contacts;

public interface FragmentActionListener {
    String KEY_SELECTED_USER="KEY_SELECTED_USER";
    void call_profile_fragment(String s);
    void move(String x);
    void refresh_main_list();
}
