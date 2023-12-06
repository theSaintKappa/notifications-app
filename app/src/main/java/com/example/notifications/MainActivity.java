package com.example.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "channel_01";
    private static ActivityResultLauncher<String> requestPermissionsLauncher;
    private NotificationManager notificationManager;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = getSystemService(NotificationManager.class);

        // Ustaw listener na wynik zapytania o pozwolenie na wyświetlanie powiadomień (opcjonalne)
        requestPermissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            refreshUI();
            if (isGranted) sendNotification();
        });

        // Utwórz kanał powiadomień
        createNotificationChannel();
        // Odśwież UI (opcjonalne)
        refreshUI();

        // Ustaw listener na przycisk
        Button button = findViewById(R.id.button_show_notification);
        button.setOnClickListener(v -> {
            // Jeśli nie ma pozwolenia na wyświetlanie powiadomień, to poproś o nie
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionsLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
                return;
            }
            // Wyślij powiadomienie
            sendNotification();
        });
    }

    private void createNotificationChannel() {
        CharSequence name = "notification_channel";
        String description = "notification_channel_description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        notificationManager.createNotificationChannel(channel);
    }

    private void refreshUI() {
        TextView textView = findViewById(R.id.text_notification_enabled);
        textView.setText(notificationManager.areNotificationsEnabled() ? "TAK" : "NIE");
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Powiadomienie")
                .setContentText("Przykładowy tekst powiadomienia")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(1, builder.build());
    }
}