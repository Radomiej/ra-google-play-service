package pl.nekko.capacitor.play;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.Manifest;
import android.content.Intent;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AuthenticationResult;
import com.google.android.gms.games.GamesSignInClient;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.PlayGamesSdk;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.SnapshotsClient;

import pl.nekko.capacitor.play.managers.AchievementsManager;
import pl.nekko.capacitor.play.managers.AuthManager;
import pl.nekko.capacitor.play.managers.LeaderboardsManager;
import pl.nekko.capacitor.play.managers.SavedGamesManager;

@CapacitorPlugin(
        name = "PGService",
        permissions = {
                @Permission(strings = {Manifest.permission.INTERNET}, alias = "internet")
        }
)
public class PGServicePlugin extends Plugin {

    private static final String TAG = "PGService";

    // Managery dla poszczególnych funkcjonalności
    private AuthManager authManager;
    private LeaderboardsManager leaderboardsManager;
    private AchievementsManager achievementsManager;
    private SavedGamesManager savedGamesManager;

    @Override
    public void load() {
        Log.d(TAG, "PGServicePlugin loading");

        // Inicjalizacja SDK Google Play Games
        PlayGamesSdk.initialize(bridge.getActivity());

        // Inicjalizacja managerów
        authManager = new AuthManager(getBridge());
        leaderboardsManager = new LeaderboardsManager(getBridge());
        achievementsManager = new AchievementsManager(getBridge());
        savedGamesManager = new SavedGamesManager(getBridge());

        GamesSignInClient gamesSignInClient = PlayGames.getGamesSignInClient(bridge.getActivity());

        gamesSignInClient.isAuthenticated().addOnCompleteListener(isAuthenticatedTask -> {
            boolean isAuthenticated =
                    (isAuthenticatedTask.isSuccessful() &&
                            isAuthenticatedTask.getResult().isAuthenticated());

            if (isAuthenticated) {
                Log.d(TAG, "PGServicePlugin isAuthenticated");
                // Continue with Play Games Services
            } else {
                Log.d(TAG, "PGServicePlugin not isAuthenticated");
                // Disable your integration with Play Games Services or show a
                // login button to ask  players to sign-in. Clicking it should
                // call GamesSignInClient.signIn().
            }
        });

        Log.d(TAG, "PGServicePlugin initialized with all managers");
    }

    // ========== Metody uwierzytelniania ==========

    @PluginMethod
    public void signIn(PluginCall call) {
        authManager.signIn(call);
    }

    @PluginMethod
    public void signOut(PluginCall call) {
        authManager.signOut(call);
    }

    @PluginMethod
    public void isSignedIn(PluginCall call) {
        authManager.isSignedIn(call);
    }

    @PluginMethod
    public void getPlayerInfo(PluginCall call) {
        authManager.getPlayerInfo(call);
    }

    // ========== Metody tablic wyników ==========

    @PluginMethod
    public void showLeaderboard(PluginCall call) {
        leaderboardsManager.showLeaderboard(call);
    }

    @PluginMethod
    public void showAllLeaderboards(PluginCall call) {
        leaderboardsManager.showAllLeaderboards(call);
    }

    @PluginMethod
    public void submitScore(PluginCall call) {
        leaderboardsManager.submitScore(call);
    }

    // ========== Metody osiągnięć ==========

    @PluginMethod
    public void showAchievements(PluginCall call) {
        achievementsManager.showAchievements(call);
    }

    @PluginMethod
    public void unlockAchievement(PluginCall call) {
        achievementsManager.unlockAchievement(call);
    }

    @PluginMethod
    public void incrementAchievement(PluginCall call) {
        achievementsManager.incrementAchievement(call);
    }

    @PluginMethod
    public void revealAchievement(PluginCall call) {
        achievementsManager.revealAchievement(call);
    }

    // ========== Metody zapisów gry ==========

    @PluginMethod
    public void saveGameData(PluginCall call) {
        savedGamesManager.saveGameData(call);
    }

    @PluginMethod
    public void loadGameData(PluginCall call) {
        savedGamesManager.loadGameData(call);
    }

    @PluginMethod
    public void showSavedGames(PluginCall call) {
        savedGamesManager.showSavedGames(call);
    }
}
