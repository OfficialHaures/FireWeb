package nl.inferno;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Main {
    private static final String CHANNEL_RSS_URL = "https://www.youtube.com/feeds/videos.xml?channel_id=UCd7g8clamBctLp2V62XMs-g";
    private static final String DISCORD_WEBHOOK_URL = "https://discord.com/api/webhooks/1277012483453751357/k4oZ80MSP9wnEDeqHUOPAWXnaCENAkNPzEV3_V3zt81LPvcuAXS4NOxvtLRYTTfhwieR";

    public static void main(String[] args) {
        try {
            URL feedUrl = new URL(CHANNEL_RSS_URL.replace("CHANNEL_ID", "MineSquad3"));
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            List<SyndEntry> entries = feed.getEntries();

            if (!entries.isEmpty()) {
                SyndEntry latestVideo = entries.get(0);
                String videoTitle = latestVideo.getTitle();

                sendDiscordWebhook(videoTitle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendDiscordWebhook(String content) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(DISCORD_WEBHOOK_URL);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity("{\"content\":\"" + content + "\"}"));

        httpClient.execute(request);
        httpClient.close();
    }
}

