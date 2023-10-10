package bitcamp.show_pet.chatting.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class PapagoTranslationService {

    @Value("${papago.client.id}")
    private String clientId;

    @Value("${papago.client.secret}")
    private String clientSecret;

    public String detectAndTranslate(String inputText, String targetLang) {
        try {
            // Step 1: 입력 텍스트의 언어 감지
            String detectedLang = detectLanguage(inputText);

            if (detectedLang == null) {
                System.out.println("언어 감지에 실패했습니다.");
                return "언어 감지에 실패했습니다.";
            }

            // 소스 언어와 타겟 언어가 동일한 경우 번역을 실행하지 않고 그대로 반환
            if (detectedLang.equalsIgnoreCase(targetLang)) {
                System.out.println("소스 언어와 타겟 언어가 동일합니다. 번역을 실행하지 않습니다.");
                return inputText;
            }

            // Step 2: 언어 감지 결과를 바탕으로 번역 수행
            String translatedText = translateText(inputText, detectedLang, targetLang);

            System.out.println("입력 텍스트: " + inputText);
            System.out.println("감지된 언어: " + detectedLang);
            System.out.println("번역 결과: " + translatedText);

            return translatedText;
        } catch (Exception e) {
            e.printStackTrace();
            return "번역에 실패했습니다.";
        }
    }

    public String detectLanguage(String inputText) {
        try {
            String query = URLEncoder.encode(inputText, "UTF-8");
            String apiURL = "https://naveropenapi.apigw.ntruss.com/langs/v1/dect";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            // POST 파라미터 설정
            String postParams = "query=" + query;

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();

                // 감지된 언어 정보를 추출
                String jsonResponse = response.toString();
                String detectedLang = extractDetectedLang(jsonResponse);

                return detectedLang;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String extractDetectedLang(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonObject.getString("langCode");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String translateText(String inputText, String sourceLang, String targetLang) {
        try {
            String query = URLEncoder.encode(inputText, "UTF-8");
            String apiURL = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            // POST 파라미터 설정
            String postParams = "source=" + sourceLang + "&target=" + targetLang + "&text=" + query;

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();

                // 번역 결과를 추출
                String jsonResponse = response.toString();
                String translatedText = extractTranslatedText(jsonResponse);

                return translatedText;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("번역에 실패했습니다.");
        return null;
    }

    private String extractTranslatedText(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonObject.getJSONObject("message").getJSONObject("result").getString("translatedText");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}