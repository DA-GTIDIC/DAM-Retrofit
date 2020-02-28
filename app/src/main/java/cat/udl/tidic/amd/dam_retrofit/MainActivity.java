package cat.udl.tidic.amd.dam_retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cat.udl.tidic.amd.dam_retrofit.models.UserModel;
import cat.udl.tidic.amd.dam_retrofit.network.RetrofitClientInstance;
import cat.udl.tidic.amd.dam_retrofit.services.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    UserModel u = new UserModel();
    EditText tokenEditText;
    Button checkButton;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tokenEditText = findViewById(R.id.editText_token);
        checkButton = findViewById(R.id.button_check);
        resultTextView = findViewById(R.id.textView_result);


        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = tokenEditText.getText().toString();

                Map<String, String> map = new HashMap<>();
                map.put("Authorization", token);

                UserService userService = RetrofitClientInstance.
                        getRetrofitInstance().create(UserService.class);

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
