package UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myschool.MainActivity2;
import com.example.myschool.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Controller.SessionManager;
import Controller.VolleySingleton;
import Model.User;
import Server.Urls;

public class RegisterActivity extends AppCompatActivity {
    private EditText fullname,email,password;
    private Button buttonRegister;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullname=findViewById(R.id.registerFullName);
        email=findViewById(R.id.registerTextEmail);
        password=findViewById(R.id.registerPasswordText);
        buttonRegister =findViewById(R.id.buttonRegister);
        progressBar=findViewById(R.id.progressBarRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }
    public void registerUser() {
        final String Myname = fullname.getText().toString().trim();
        final String myEmail = email.getText().toString().trim();
        final String myPassword = password.getText().toString().trim();
        if (TextUtils.isEmpty(Myname)) {
            fullname.setError("enter your name please");
            fullname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(myEmail)) {
            email.setError("enter your mail please");
            email.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(myPassword)){
            password.setError("enter your password please");
            password.requestFocus();
            return;
        }
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Urls.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.VISIBLE);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject userJson = obj.getJSONObject("data");
                        User user = new User(userJson.getString("token"));
                        SessionManager.getInstance(getApplicationContext()).userLogin(user);
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "register succed", Toast.LENGTH_SHORT).show();
                Intent in=new Intent(RegisterActivity.this,MainActivity2.class);
                startActivity(in);
            }
        }
        )
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("Content-Type","application/json");
                params.put("name",Myname);
                params.put("email",myEmail);
                params.put("password",myPassword);
                params.put("c_password",myPassword);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
