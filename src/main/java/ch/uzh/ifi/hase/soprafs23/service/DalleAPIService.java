package ch.uzh.ifi.hase.soprafs23.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

//curl https://api.openai.com/v1/images/generations \
//        -H "Content-Type: application/json" \
//        -H "Authorization: Bearer $OPENAI_API_KEY" \
//        -d '{
//        "prompt": "a white siamese cat",
//        "n": 1,
//        "size": "1024x1024"
//        }'
@Service
@Transactional
public class DalleAPIService {
    private final String apiKey = System.getenv("DALLE_API_KEY");
//    private String prompt;
    private int numImages = 1;
    private String responseFormat = "b64_json"; //url or b64_json
    private String apiUrl = "https://api.openai.com/v1/images/generations";

    //    private int size = 512; //accepted values are 256, 512, or 1024
    public enum ImageSize {
        SMALL(256, "256x256"),
        MEDIUM(512, "512x512"),
        LARGE(1024, "1024x1024");

        public final int size;
        public final String strSize;

        ImageSize(int i, String s) {
            this.size = i;
            this.strSize = s;
        }
    }

    public JSONObject getImageFromDALLE(String prompt) { //its not blob
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(apiUrl);

// Set headers
            httppost.setHeader("Content-Type", "application/json");
            httppost.setHeader("Authorization", "Bearer " + apiKey);

// Set request body
            StringEntity requestEntity = new StringEntity("{\"response_format\":\"" + responseFormat + "\", \"prompt\": \"" + prompt + "\", \"n\": numImages, \"size\": \"256x256\"}");
            httppost.setEntity(requestEntity);

// Send request and get response
            CloseableHttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

// Parse response JSON to get image URL
            JSONObject jsonResponse = new JSONObject(EntityUtils.toString(entity));
//            String imageB64 = jsonResponse.getJSONArray("data").getJSONObject(0).getString("b64_json");
//            String imageB64 = jsonResponse.getJSONObject("error").getString("code");
            jsonResponse.append("DUMMY_VAR", System.getenv("DUMMY_ENV_VAR"));
// Download image and save locally
//        HttpGet httpget = new HttpGet(imageUrl);
//        response = httpclient.execute(httpget);
//        entity = response.getEntity();
//        OutputStream outputStream = new FileOutputStream(new File("image.jpg"));
//        entity.writeTo(outputStream);
            System.out.println(apiKey);
// Clean up resources
            EntityUtils.consume(entity);
            response.close();
            httpclient.close();
            return jsonResponse;

        }
        catch (IOException e) {
            e.printStackTrace();
            return new JSONObject(e.toString());
        }
    }
}