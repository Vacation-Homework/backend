package com.vacation.homework.app.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FcmService {

    public void sendCommentNotification(String token, String titleText, String homeworkSeq) {
        String title = "📘 방학숙제 알림";
        String body = "[" + shorten(titleText) + "]에 대한 선생님의 코멘트가 도착했어요!";

        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putData("homeworkSeq", homeworkSeq)


                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ FCM 메시지 전송 성공: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ FCM 메시지 전송 실패:");
            System.err.println(" - Error Code: " + e.getErrorCode());
            System.err.println(" - Messaging Error Code: " + e.getMessagingErrorCode());
            System.err.println(" - HTTP Response: " + e.getHttpResponse());
            System.err.println(" - Cause: " + e.getCause());
            e.printStackTrace(); // ✅ 전체 스택 출력
        } catch (Exception e) {
            System.err.println("❌ 예기치 않은 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String shorten(String title) {
        return title.length() <= 5 ? title : title.substring(0, 5) + "..";
    }
}
