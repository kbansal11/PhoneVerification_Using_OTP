package com.example.kartik.otp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText ed1, ed2;
    Button btn1, btn2;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1 = (EditText)findViewById(R.id.editText);
        ed2 = (EditText)findViewById(R.id.editText2);
        btn1 = (Button)findViewById(R.id.button);
        btn2 = (Button)findViewById(R.id.button2);


        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode= s;
                Toast.makeText(getApplicationContext(), "Code Sent", Toast.LENGTH_SHORT).show();
            }
        };

    }


    public void sendsms(View v){

        String number = ed1.getText().toString();

        if(Patterns.PHONE.matcher(number).matches()){
            PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, this, mCallback);
        }

        else{
            ed1.setError("Invalid Phone Number");
            ed1.requestFocus();
        }

    }


    public void signInWithPhone(PhoneAuthCredential credential){
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Yup, that's a Correct code", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void verify(View view){
        String User_code = ed2.getText().toString();


        if(User_code.isEmpty() || User_code.length()<6 || User_code.length()>6) {
            ed2.setError("Enter Correct Code");
            ed2.requestFocus();
            return;
        }

        else{
                verifyPhoneNumber(verificationCode, User_code);
            }



    }

    public void verifyPhoneNumber(String verifyCode, String User_code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyCode, User_code);
        signInWithPhone(credential);
    }
}
