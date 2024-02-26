package com.example.foobook_android.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class FieldValidation {
    public static void setupFieldValidation(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().trim().isEmpty()) {
                    editText.setError("Field is required");
                } else {
                    editText.setError(null);
                }
            }
        });
    }
}
