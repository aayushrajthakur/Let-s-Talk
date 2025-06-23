package com.example.letstalk;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText userid;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        userid = findViewById(R.id.userId);
        loginBtn = findViewById(R.id.loginBtn);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String storedUserId = prefs.getString("userId", null);

        if (storedUserId != null) {
            // Session exists, auto-login
            startService(storedUserId);
            Intent intent = new Intent(MainActivity.this, CallingActivity.class);
            intent.putExtra("userId", storedUserId);
            startActivity(intent);
            finish(); // Optional: prevent returning to this activity
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = userid.getText().toString();
                if (userId.isEmpty()) {
                    return;
                }

                startService(userId);
                // save userId to SharedPreferences to save session
                SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                prefs.edit().putString("userId", userId).apply();

                Intent intent = new Intent(MainActivity.this, CallingActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        // need a activityContext.
        PermissionX.init(this).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
                        String message = "We need your consent for the following permissions in order to use the offline call function properly";
                        scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny");
                    }
                }).request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                                         @NonNull List<String> deniedList) {
                    }
                });

    }

    void startService(String userId) {
        Application application = getApplication(); // Android's application context
        long appID = 1177907768;   // yourAppID
        String appSign ="fc9caf4c58c546df196e72772df5bd4891726bca12997fc72e6612d4dcf18d77";  // yourAppSign
        String userID = userId; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName = userId;   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoUIKitPrebuiltCallService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallService.unInit();
    }
}