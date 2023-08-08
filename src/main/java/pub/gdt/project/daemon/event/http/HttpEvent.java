package pub.gdt.project.daemon.event.http;

import com.sun.net.httpserver.HttpExchange;
import pub.gdt.project.daemon.event.Event;

public class HttpEvent extends Event<HttpExchange> {
    public HttpEvent(HttpExchange source) {
        super(source);
    }
}
