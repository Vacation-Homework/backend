package com.vacation.homework.app.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.key.path}")
    private String firebaseKeyPath;

    @PostConstruct
    public void init() {
        try (InputStream serviceAccount = new FileInputStream(firebaseKeyPath)) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount)
                    .createScoped(List.of(
                            "https://www.googleapis.com/auth/cloud-platform",
                            "https://www.googleapis.com/auth/firebase.messaging"));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase Admin SDK 초기화 성공");
            }
        } catch (IOException e) {
            throw new RuntimeException("❌ Firebase 초기화 실패", e);
        }
    }
}