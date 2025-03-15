package pl.nekko.capacitor.play.managers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.getcapacitor.Bridge;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotContents;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;

import java.io.IOException;

public class SavedGamesManager {
    private static final String TAG = "SavedGamesManager";

    private final Bridge bridge;
    private final SnapshotsClient snapshotsClient;

    private PluginCall savedGamesCall;
    private ActivityResultLauncher<Intent> savedGamesLauncher;

    public SavedGamesManager(Bridge bridge) {
        this.bridge = bridge;
        this.snapshotsClient = PlayGames.getSnapshotsClient(bridge.getActivity());

        // Rejestrujemy launcher dla aktywności zapisów gry
        savedGamesLauncher = bridge.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleSavedGamesResult
        );

        Log.d(TAG, "SavedGamesManager initialized");
    }

    /**
     * Zapisuje dane gry do chmury.
     */
    public void saveGameData(PluginCall call) {
        Log.d(TAG, "saveGameData method called");
        String saveId = call.getString("saveId");
        String data = call.getString("data");
        String description = call.getString("description", "");

        if (data == null) {
            call.reject("Save data is required");
            return;
        }

        // Jeśli saveId nie jest podane, generujemy unikalny identyfikator
        if (saveId == null || saveId.isEmpty()) {
            saveId = "save_" + System.currentTimeMillis();
        }

        byte[] dataBytes = data.getBytes();
        String finalSaveId = saveId;

        snapshotsClient
                .open(saveId, /* createIfNotFound= */ true, SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED)
                .addOnSuccessListener(openResult -> {
                    // openResult jest typu SnapshotsClient.DataOrConflict<Snapshot>
                    SnapshotsClient.DataOrConflict<Snapshot> dataOrConflict = openResult;
                    Snapshot snapshot = dataOrConflict.getData();

                    // Jeśli wystąpił konflikt lub snapshot jest nullem, trzeba to obsłużyć
                    if (snapshot == null) {
                        // Tu można obsłużyć conflict, np. dataOrConflict.getConflict()
                        call.reject("Snapshot conflict or error occurred.");
                        return;
                    }

                    // Zapisujemy dane do snapshotContents
                    SnapshotContents contents = snapshot.getSnapshotContents();
                    contents.writeBytes(dataBytes);

                    // Tworzymy metadane
                    SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                            .setDescription(description)
                            .build();

                    // Commitujemy zmiany i zamykamy
                    snapshotsClient.commitAndClose(snapshot, metadataChange)
                            .addOnSuccessListener(snapshotMetadata -> {
                                Log.d(TAG, "Game saved successfully with ID: " + finalSaveId);
                                JSObject result = new JSObject();
                                result.put("success", true);
                                result.put("saveId", finalSaveId);
                                call.resolve(result);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to commit save", e);
                                JSObject result = new JSObject();
                                result.put("success", false);
                                result.put("error", "Failed to commit save: " + e.getMessage());
                                call.resolve(result);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to open snapshot", e);
                    JSObject result = new JSObject();
                    result.put("success", false);
                    result.put("error", "Failed to open snapshot: " + e.getMessage());
                    call.resolve(result);
                });
    }

    /**
     * Ładuje dane gry z chmury.
     */
    public void loadGameData(PluginCall call) {
        String saveId = call.getString("saveId");
        if (saveId == null || saveId.isEmpty()) {
            call.reject("Save ID is required");
            return;
        }

        snapshotsClient.open(saveId, false, SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED)
                .addOnSuccessListener(openResult -> {
                    SnapshotsClient.DataOrConflict<Snapshot> dataOrConflict = openResult;
                    Snapshot snapshot = dataOrConflict.getData();

                    if (snapshot == null) {
                        call.reject("Snapshot conflict or error occurred.");
                        return;
                    }

                    // Odczytujemy bajty
                    SnapshotContents contents = snapshot.getSnapshotContents();
                    byte[] dataBytes;
                    try {
                        dataBytes = contents.readFully();
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading snapshot contents", e);
                        JSObject errorResult = new JSObject();
                        errorResult.put("success", false);
                        errorResult.put("error", "Error reading snapshot contents: " + e.getMessage());
                        call.resolve(errorResult);
                        // Zamykanie snapshotu
                        snapshotsClient.discardAndClose(snapshot);
                        return;
                    }

                    // Dalej przetwarzamy dataBytes...
                    String data = new String(dataBytes);

                    // Metadane
                    SnapshotMetadata metadata = snapshot.getMetadata();
                    long lastModified = metadata.getLastModifiedTimestamp();
                    String desc = metadata.getDescription();

                    // Tworzymy wynik
                    JSObject result = new JSObject();
                    result.put("success", true);
                    result.put("saveId", saveId);
                    result.put("data", data);
                    result.put("description", desc);
                    result.put("lastModifiedTimestamp", lastModified);
                    call.resolve(result);

                    // Zamykamy snapshot bez zapisu
                    snapshotsClient.discardAndClose(snapshot);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to open snapshot", e);
                    JSObject result = new JSObject();
                    result.put("success", false);
                    result.put("error", "Failed to open snapshot: " + e.getMessage());
                    call.resolve(result);
                });
    }


    /**
     * Wyświetla UI z listą zapisanych gier.
     */
    public void showSavedGames(PluginCall call) {
        Log.d(TAG, "showSavedGames method called");
        String title = call.getString("title", "Saved Games");
        Boolean allowAddButton = call.getBoolean("allowAddButton", false);
        Boolean allowDelete = call.getBoolean("allowDelete", false);
        Integer maxSavedGames = call.getInt("maxSavedGames", 5);

        savedGamesCall = call;

        snapshotsClient
                .getSelectSnapshotIntent(title, allowAddButton, allowDelete, maxSavedGames)
                .addOnSuccessListener(intent -> {
                    try {
                        call.setKeepAlive(true);
                        savedGamesLauncher.launch(intent);
                        Log.d(TAG, "Saved games UI launched");
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to launch saved games UI", e);
                        call.reject("Failed to show saved games: " + e.getMessage());
                        savedGamesCall = null;
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get saved games intent", e);
                    call.reject("Failed to show saved games: " + e.getMessage());
                    savedGamesCall = null;
                });
    }

    /**
     * Obsługa wyniku z ekranu Saved Games UI.
     */
    private void handleSavedGamesResult(ActivityResult result) {
        Log.d(TAG, "Saved games activity result received, resultCode: " + result.getResultCode());
        if (savedGamesCall != null) {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                // Użytkownik wybrał lub utworzył zapis
                Intent data = result.getData();
                String saveId = data.getStringExtra(SnapshotsClient.EXTRA_SNAPSHOT_METADATA);

                if (saveId != null) {
                    // Ładujemy wybrany zapis
                    loadGameData(savedGamesCall);
                } else {
                    // Użytkownik utworzył nowy zapis
                    JSObject res = new JSObject();
                    res.put("success", true);
                    res.put("newSave", true);
                    savedGamesCall.resolve(res);
                }
            } else {
                // Użytkownik anulował lub wystąpił błąd
                JSObject res = new JSObject();
                res.put("success", false);
                res.put("error", "User canceled or error occurred");
                savedGamesCall.resolve(res);
            }
            savedGamesCall = null;
        }
    }
}
