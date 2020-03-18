package cat.udl.tidic.amd.dam_retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cat.udl.tidic.amd.dam_retrofit.models.UserModel;
import cat.udl.tidic.amd.dam_retrofit.network.RetrofitClientInstance;
import cat.udl.tidic.amd.dam_retrofit.services.UserService;
import cat.udl.tidic.amd.dam_retrofit.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    UserModel u = new UserModel();
    EditText tokenEditText;
    Button checkButton;
    Button buttonRegister;
    TextView resultTextView;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tokenEditText = findViewById(R.id.editText_token);
        checkButton = findViewById(R.id.button_check);
        resultTextView = findViewById(R.id.textView_result);
        buttonRegister = findViewById(R.id.button_register);

        userService = RetrofitClientInstance.
                getRetrofitInstance().create(UserService.class);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Course API requires passwords in sha-256 in passlib format so:
                String p = "1234";
                String salt = "16";
                String encode_hash = Utils.encode(p,salt,29000);
                System.out.println("PASSWORD_ENCRYPTED " + encode_hash);


                JsonObject user_json = new JsonObject();
                user_json.addProperty("username", "jordimateo");
                user_json.addProperty("name", "jordi");
                user_json.addProperty("surname", "mateo");
                user_json.addProperty("email", "a@gmail.com");
                user_json.addProperty("phone", "972452389");
                user_json.addProperty("genere", "M");
                user_json.addProperty("password", encode_hash);

                Call<Void> call = userService.registerUser(user_json);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200){
                            Toast.makeText(MainActivity.this,"User registered", Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                Toast.makeText(MainActivity.this, Objects.requireNonNull(response.errorBody()).string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

            }
        });


        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = tokenEditText.getText().toString();

                Map<String, String> map = new HashMap<>();
                map.put("Authorization", token);

                Call<UserModel> call = userService.getUserProfile(map);
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        u = response.body();
                        try {
                            resultTextView.setText(u.toString());
                        }catch (Exception e){
                            Log.e("MainActivity", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {

                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });

            }


        });





    }
}
