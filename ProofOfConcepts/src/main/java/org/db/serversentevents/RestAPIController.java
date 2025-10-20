package org.db.serversentevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
@RestController
public class RestAPIController {
    public static void main(String[] args){
        SpringApplication.run(RestAPIController.class, args);
    }

    @GetMapping(value = "/sse/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "data: Event " + sequence + " at " + LocalDateTime.now() + "\n\n");
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String getHomePage() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>SSE Demo</title>
            </head>
            <body>
                <h1>Server-Sent Events Demo</h1>
                <div id="events"></div>
                <script>
                    const eventSource = new EventSource('/sse/events');
                    eventSource.onmessage = function(event) {
                        document.getElementById('events').innerHTML += '<p>' + event.data + '</p>';
                    };
                </script>
            </body>
            </html>
            """;
    }
}
