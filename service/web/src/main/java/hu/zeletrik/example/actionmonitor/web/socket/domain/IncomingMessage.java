package hu.zeletrik.example.actionmonitor.web.socket.domain;

import lombok.Data;

@Data
public class IncomingMessage {

    private String from;
    private String to;
    private String text;
}
