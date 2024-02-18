package com.example.foobook_android;

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
                // Checks if the EditText field is empty after the user has finished typing
                if (s.toString().trim().isEmpty()) {
                    editText.setError("Field is required"); // Sets an error message on the EditText if it's empty
                } else {
                    editText.setError(null); // Clears the error message if the field is not empty
                }
            }
        });
    }
}
