package com.mysite.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@RequiredArgsConstructor
@Controller
public class ApiController {

    @GetMapping("/fire")
    public String getApiData(@RequestParam(value = "localAreas", required = false) String localAreas, Model model) {
        if (localAreas == null || localAreas.isEmpty()) {
            // localAreas가 주어지지 않았을 때 처리할 내용
            model.addAttribute("jsonData", "No localAreas provided.");
            return "Fire"; // 또는 예외 처리에 맞는 페이지로 리턴
        }

        try {
            // API 호출을 위한 URL 구성
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1400377/forestPoint/forestPointListSidoSearch");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=%2BrjPlspLDATUPFxhEJF1VV7y793cI5CJKC9dPXTGIVLzpvYzxUTpoOUshFXdLNaHDdTQC7I8ftzGwD3qrHFliA%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("localAreas", "UTF-8") + "=" + URLEncoder.encode(localAreas, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("excludeForecast", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8"));

            // API 호출 및 결과 받아오기
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            StringBuilder sb = new StringBuilder();
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
            }

            // 받아온 JSON 데이터를 모델에 추가하여 HTML로 전달
            model.addAttribute("jsonData", sb.toString());
            return "Fire";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("jsonData", "Error occurred while fetching data from API.");
            return "Fire"; // 또는 예외 처리에 맞는 페이지로 리턴
        }
    }
}