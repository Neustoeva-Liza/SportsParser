package service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class VkService {

    private static final String TOKEN =
            "vk1.a.jCi-AKFTgAtvWad3GSNMZOvhQJBi52fVIXiunHmo3qd3jM8LTgFdeZWj4jzfCQDiomCuUos02yGTtZ_uvHuXIgLG9gBi2hnaSkiqAho4ThKtSnpKOGq54ZHWF6sGog7kuku3NE5MPco0O9QpDHutr5DxOqaHBQ1l_4DQKcSV6CcM9xeg3PPUbS-Ux3XM1c7adJJWQGU7Uzwy1Funij6zfg";

    private static final String USER_ID =
            "612073097";

    public void sendMessage(String message) {

        try {

            String urlString =
                    "https://api.vk.com/method/messages.send?"
                            + "user_id=" + USER_ID
                            + "&random_id=" + System.currentTimeMillis()
                            + "&message=" + URLEncoder.encode(message, StandardCharsets.UTF_8)
                            + "&access_token=" + TOKEN
                            + "&v=5.199";

            URL url = new URL(urlString);

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            Scanner scanner =
                    new Scanner(connection.getInputStream());

            while (scanner.hasNext()) {
                System.out.println(scanner.nextLine());
            }

            scanner.close();

            System.out.println("VK сообщение отправлено");

        } catch (Exception e) {

            System.out.println("VK ERROR");
            e.printStackTrace();
        }
    }
}
