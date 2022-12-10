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
import com.example.myschool.MainActivity;
import com.example.myschool.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Controller.SessionManager;
import Controller.VolleySingleton;
import Model.User;
import Server.Urls;

public class LoginActivity extends AppCompatActivity {
    private EditText email,password;
    private Button buttonRegister,buttonlogin;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.logineditText);
        password=findViewById(R.id.logineditText2);
        buttonRegister =findViewById(R.id.button2Register);
        buttonlogin=findViewById(R.id.buttonlogin);
        progressBar=findViewById(R.id.progressBar2);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }
    private void userLogin()
    {

        final String myEmail = email.getText().toString().trim();
        final String myPassword = password.getText().toString().trim();

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
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Urls.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getJSONObject("success")!=null) {
                        Toast.makeText(getApplicationContext(), "welcome to your application", Toast.LENGTH_SHORT).show();
                        JSONObject userJson = obj.getJSONObject("success");
                        User user = new User(userJson.getString("token"));
                        SessionManager.getInstance(getApplicationContext()).userLogin(user);
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "login failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent in=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(in);
                Toast.makeText(getApplicationContext(), "login suucess", Toast.LENGTH_SHORT).show();

            }
        }
        )
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("Content-Type","application/json");

                params.put("email",myEmail);
                params.put("password",myPassword);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
