package pl.nekko.capacitor.play.managers;

import android.util.Log;

import com.getcapacitor.Bridge;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AuthenticationResult;
import com.google.android.gms.games.GamesSignInClient;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.PlayersClient;

public class AuthManager {
    private static final String TAG = "AuthManager";
    private static final int RC_SIGN_IN = 9001;

    private final Bridge bridge;
    private final GamesSignInClient gamesSignInClient;
    private final PlayersClient playersClient;
    private final SignInClient oneTapClient;
    private boolean isSigningIn = false;

    public AuthManager(Bridge bridge) {
        this.bridge = bridge;
        this.gamesSignInClient = PlayGames.getGamesSignInClient(bridge.getActivity());
        this.playersClient = PlayGames.getPlayersClient(bridge.getActivity());
        this.oneTapClient = Identity.getSignInClient(bridge.getActivity());
        
        // Sprawdzamy stan uwierzytelnienia przy inicjalizacji
        checkInitialAuthState();
        
        Log.d(TAG, "AuthManager initialized");
    }

    private void checkInitialAuthState() {
        gamesSignInClient.isAuthenticated().addOnCompleteListener(isAuthenticatedTask -> {
            boolean isAuthenticated =
                    (isAuthenticatedTask.isSuccessful() &&
                            isAuthenticatedTask.getResult().isAuthenticated());

            if (isAuthenticated) {
                Log.d(TAG, "User is already authenticated");
            } else {
                Log.d(TAG, "User is not authenticated");
            }
        });
    }

    public void signIn(PluginCall call) {
        Log.d(TAG, "signIn method called");
        gamesSignInClient.signIn()
                .addOnCompleteListener(bridge.getActivity(), task -> {
                    try {
                        // Uzyskujemy wynik logowania – może rzucić ApiException, jeśli coś poszło nie tak
                        AuthenticationResult authResult = task.getResult(ApiException.class);

                        if (authResult.isAuthenticated()) {
                            // Po udanym logowaniu pobieramy informacje o graczu
                            playersClient.getCurrentPlayer()
                                    .addOnSuccessListener(player -> {
                                        JSObject res = new JSObject();
                                        res.put("isSignedIn", true);
                                        res.put("playerId", player.getPlayerId());
                                        res.put("displayName", player.getDisplayName());
                                        if (player.getIconImageUri() != null) {
                                            res.put("imageUrl", player.getIconImageUri().toString());
                                            Log.d(TAG, "image: " + player.getIconImageUri().toString());
                                        }
                                        call.resolve(res);
                                        bridge.triggerWindowJSEvent("signInStatusChanged", res.toString());
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Failed to get player info", e);
                                        JSObject res = new JSObject();
                                        res.put("isSignedIn", false);
                                        res.put("error", "Failed to get player info: " + e.getMessage());
                                        call.resolve(res);
                                    });
                        } else {
                            JSObject res = new JSObject();
                            res.put("isSignedIn", false);
                            res.put("error", "Sign in failed.");
                            call.resolve(res);
                        }
                    } catch (ApiException e) {
                        Log.e(TAG, "Sign in failed", e);
                        JSObject res = new JSObject();
                        res.put("isSignedIn", false);
                        res.put("error", "Sign in failed: " + e.getMessage());
                        call.resolve(res);
                    }
                });
    }

    public void signOut(PluginCall call) {
        // Logujemy info, że to tylko symulacja
        Log.d(TAG, "Simulated sign-out called. No real GPGS/OneTap logout.");

        // Przygotowujemy obiekt do zwrócenia
        JSObject result = new JSObject();
        // Ustawiamy isSignedIn = false (symulacja wylogowania)
        result.put("isSignedIn", false);
        result.put("message", "Simulated signOut performed. No real tokens revoked.");

        // Zwracamy wynik do Capacitor
        call.resolve(result);

        // Powiadamiamy słuchaczy
        bridge.triggerWindowJSEvent("signInStatusChanged", result.toString());
    }

    public void isSignedIn(PluginCall call) {
        Log.d(TAG, "isSignedIn method called");
        gamesSignInClient.isAuthenticated()
                .addOnSuccessListener(authResult -> {
                    Log.d(TAG, "isAuthenticated check successful, authenticated=" + authResult.isAuthenticated());
                    JSObject result = new JSObject();
                    result.put("isSignedIn", authResult.isAuthenticated());
                    call.resolve(result);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "isAuthenticated check failed", e);
                    JSObject result = new JSObject();
                    result.put("isSignedIn", false);
                    call.resolve(result);
                });
    }

    public void getPlayerInfo(PluginCall call) {
        Log.d(TAG, "getPlayerInfo method called");
        playersClient.getCurrentPlayer()
                .addOnSuccessListener(player -> {
                    Log.d(TAG, "Player info retrieved successfully");
                    JSObject result = new JSObject();
                    result.put("playerId", player.getPlayerId());
                    result.put("displayName", player.getDisplayName());
                    if (player.getIconImageUri() != null) {
                        result.put("imageUrl", player.getIconImageUri().toString());
                        Log.d(TAG, "image: " + player.getIconImageUri().toString());
                    }
                    call.resolve(result);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get player info", e);
                    call.reject("Failed to get player info: " + e.getMessage());
                });
    }
}
