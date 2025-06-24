package com.example.letstalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Collections;

public class CallingActivity extends AppCompatActivity {
    TextView textView;
    TextView targetId;
    TextView logout;
    TextView healthSupport;
    ZegoSendCallInvitationButton voiceCall_Btn, videoCall_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calling);

        textView = findViewById(R.id.textView);
        targetId = findViewById(R.id.targetId);
        voiceCall_Btn = findViewById(R.id.voiceCall_Btn);
        videoCall_Btn = findViewById(R.id.videoCall_Btn);
        logout = findViewById(R.id.logout);
        healthSupport = findViewById(R.id.healthSupport);

        String userId = getIntent().getStringExtra("userId");
        textView.setText("Hello " + userId);

        targetId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String targetUserId = targetId.getText().toString().trim();
                setVoiceCall(targetUserId);
                setVideoCall(targetUserId);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                prefs.edit().clear().apply();

                // Optionally redirect back to login screen
                Intent intent = new Intent(CallingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


        
    }

    void setVoiceCall(String targetUserid){
        voiceCall_Btn.setIsVideoCall(false);
        voiceCall_Btn.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
        voiceCall_Btn.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserid,targetUserid)));
    }

    void setVideoCall(String targetUserid){
        videoCall_Btn.setIsVideoCall(true);
        videoCall_Btn.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
        videoCall_Btn.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserid,targetUserid)));
    }
}