package pl.nekko.capacitor.play.managers;

import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.getcapacitor.Bridge;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.PlayGames;

public class AchievementsManager {
    private static final String TAG = "AchievementsManager";
    private static final int RC_ACHIEVEMENTS = 9003;

    private final Bridge bridge;
    private final AchievementsClient achievementsClient;
    private PluginCall savedAchievementsCall;
    private ActivityResultLauncher<Intent> achievementsLauncher;

    public AchievementsManager(Bridge bridge) {
        this.bridge = bridge;
        this.achievementsClient = PlayGames.getAchievementsClient(bridge.getActivity());
        
        // Rejestrujemy launcher dla aktywności osiągnięć
        achievementsLauncher = bridge.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleAchievementsResult
        );
        
        Log.d(TAG, "AchievementsManager initialized");
    }

    public void showAchievements(PluginCall call) {
        Log.d(TAG, "showAchievements method called");
        savedAchievementsCall = call;
        achievementsClient.getAchievementsIntent()
                .addOnSuccessListener(intent -> {
                    try {
                        call.setKeepAlive(true);
                        // Uruchamiamy aktywność za pomocą zarejestrowanego launcher'a
                        achievementsLauncher.launch(intent);
                        Log.d(TAG, "Achievements activity started");
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to start achievements activity", e);
                        call.reject("Failed to start achievements: " + e.getMessage());
                        savedAchievementsCall = null;
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to show achievements", e);
                    call.reject("Failed to show achievements: " + e.getMessage());
                    savedAchievementsCall = null;
                });
    }

    private void handleAchievementsResult(ActivityResult result) {
        Log.d(TAG, "Achievements activity result received, resultCode: " + result.getResultCode());
        if (savedAchievementsCall != null) {
            JSObject res = new JSObject();
            res.put("message", "Achievements activity closed");
            savedAchievementsCall.resolve(res);
            savedAchievementsCall = null;
        }
    }

    public void unlockAchievement(PluginCall call) {
        Log.d(TAG, "unlockAchievement method called");
        String achievementId = call.getString("achievementId");

        if (achievementId == null || achievementId.isEmpty()) {
            call.reject("Achievement ID is required");
            return;
        }

        achievementsClient.unlockImmediate(achievementId)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Achievement unlocked successfully");
                    JSObject result = new JSObject();
                    result.put("success", true);
                    call.resolve(result);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to unlock achievement", e);
                    call.reject("Failed to unlock achievement: " + e.getMessage());
                });
    }

    public void incrementAchievement(PluginCall call) {
        Log.d(TAG, "incrementAchievement method called");
        String achievementId = call.getString("achievementId");
        Integer steps = call.getInt("steps");

        if (achievementId == null || achievementId.isEmpty()) {
            call.reject("Achievement ID is required");
            return;
        }

        if (steps == null) {
            call.reject("Steps value is required");
            return;
        }

        achievementsClient.incrementImmediate(achievementId, steps)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Achievement incremented successfully");
                    JSObject result = new JSObject();
                    result.put("success", true);
                    call.resolve(result);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to increment achievement", e);
                    call.reject("Failed to increment achievement: " + e.getMessage());
                });
    }

    public void revealAchievement(PluginCall call) {
        Log.d(TAG, "revealAchievement method called");
        String achievementId = call.getString("achievementId");

        if (achievementId == null || achievementId.isEmpty()) {
            call.reject("Achievement ID is required");
            return;
        }

        achievementsClient.revealImmediate(achievementId)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Achievement revealed successfully");
                    JSObject result = new JSObject();
                    result.put("success", true);
                    call.resolve(result);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to reveal achievement", e);
                    call.reject("Failed to reveal achievement: " + e.getMessage());
                });
    }
}
