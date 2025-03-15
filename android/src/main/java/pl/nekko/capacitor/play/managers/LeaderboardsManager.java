package pl.nekko.capacitor.play.managers;

import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.getcapacitor.Bridge;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AuthenticationResult;
import com.google.android.gms.games.GamesSignInClient;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.PlayGames;

public class LeaderboardsManager {
    private static final String TAG = "LeaderboardsManager";
    private static final int RC_LEADERBOARD = 9002;

    private final Bridge bridge;
    private final LeaderboardsClient leaderboardsClient;
    private PluginCall savedLeaderboardCall;
    private ActivityResultLauncher<Intent> leaderboardLauncher;

    public LeaderboardsManager(Bridge bridge) {
        this.bridge = bridge;
        this.leaderboardsClient = PlayGames.getLeaderboardsClient(bridge.getActivity());
        
        // Rejestrujemy launcher dla aktywności tablic wyników
        leaderboardLauncher = bridge.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleLeaderboardResult
        );
        
        Log.d(TAG, "LeaderboardsManager initialized");
    }

    public void showLeaderboard(PluginCall call) {
        Log.d(TAG, "showLeaderboard method called");
        String leaderboardId = call.getString("leaderboardId");
        if (leaderboardId == null || leaderboardId.isEmpty()) {
            call.reject("Leaderboard ID is required");
            return;
        }
        savedLeaderboardCall = call;
        Log.d(TAG, "Showing leaderboard with ID: " + leaderboardId);
        leaderboardsClient.getLeaderboardIntent(leaderboardId)
                .addOnSuccessListener(intent -> {
                    try {
                        call.setKeepAlive(true);
                        // Launcher został już zarejestrowany, teraz tylko uruchamiamy aktywność
                        leaderboardLauncher.launch(intent);
                        Log.d(TAG, "Leaderboard activity started");
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to start leaderboard activity", e);
                        call.reject("Failed to start leaderboard: " + e.getMessage());
                        savedLeaderboardCall = null;
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to show leaderboard", e);
                    call.reject("Failed to show leaderboard: " + e.getMessage());
                    savedLeaderboardCall = null;
                });
    }

    private void handleLeaderboardResult(ActivityResult result) {
        Log.d(TAG, "Leaderboard activity result received, resultCode: " + result.getResultCode());
        if (savedLeaderboardCall != null) {
            JSObject res = new JSObject();
            res.put("message", "Leaderboard activity closed");
            savedLeaderboardCall.resolve(res);
            savedLeaderboardCall = null;
        }
    }

    public void showAllLeaderboards(PluginCall call) {
        Log.d(TAG, "showAllLeaderboards method called");
        savedLeaderboardCall = call;
        leaderboardsClient.getAllLeaderboardsIntent()
                .addOnSuccessListener(intent -> {
                    try {
                        call.setKeepAlive(true);
                        leaderboardLauncher.launch(intent);
                        Log.d(TAG, "All leaderboards activity started");
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to start all leaderboards activity", e);
                        call.reject("Failed to show all leaderboards: " + e.getMessage());
                        savedLeaderboardCall = null;
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to show all leaderboards", e);
                    call.reject("Failed to show all leaderboards: " + e.getMessage());
                    savedLeaderboardCall = null;
                });
    }

    public void submitScore(PluginCall call) {
        Log.d(TAG, "submitScore method called");
        String leaderboardId = call.getString("leaderboardId");
        Double scoreRaw = call.getDouble("score");

        Log.d(TAG, "submitScore ID: " + leaderboardId);
        Log.d(TAG, "submitScore score: " + scoreRaw);
        if (leaderboardId == null || leaderboardId.isEmpty()) {
            call.reject("Leaderboard ID is required");
            return;
        }

        if (scoreRaw == null) {
            call.reject("Score is required");
            return;
        }

        long score = Math.round(scoreRaw);

        // Pobieramy klienta autentykacji
        GamesSignInClient signInClient = PlayGames.getGamesSignInClient(bridge.getActivity());
        signInClient.isAuthenticated().addOnCompleteListener(task -> {
            boolean isAuthenticated = false;
            if (task.isSuccessful() && task.getResult() != null) {
                isAuthenticated = task.getResult().isAuthenticated();
            }
            if (!isAuthenticated) {
                // Użytkownik nie jest zalogowany – próbujemy ponownej autentykacji
                Log.d(TAG, "User not authenticated. Re-authenticating...");
                signInClient.signIn().addOnCompleteListener(authTask -> {
                    try {
                        AuthenticationResult authResult = authTask.getResult(ApiException.class);
                        if (authResult.isAuthenticated()) {
                            Log.d(TAG, "Re-authentication successful, submitting score");
                            submitScoreInternal(call, leaderboardId, score);
                        } else {
                            Log.e(TAG, "Re-authentication failed");
                            call.reject("Failed to re-authenticate user.");
                        }
                    } catch (ApiException e) {
                        Log.e(TAG, "Re-authentication failed", e);
                        call.reject("Failed to re-authenticate user: " + e.getMessage());
                    }
                });
            } else {
                // Użytkownik jest już zalogowany – wysyłamy wynik
                submitScoreInternal(call, leaderboardId, score);
            }
        });
    }

    private void submitScoreInternal(PluginCall call, String leaderboardId, long score) {
//        leaderboardsClient.submitScore(leaderboardId, score);
        leaderboardsClient.submitScoreImmediate(leaderboardId, score)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Score submitted successfully");
                    JSObject result = new JSObject();
                    result.put("success", true);
                    call.resolve(result);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to submit score", e);
                    call.reject("Failed to submit score: " + e.getMessage());
                });
    }
}
