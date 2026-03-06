package com.example.ScienceCentre.Util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketNumberGenerator
{

    private static final AtomicInteger counter = new AtomicInteger(1000);

    public static String generate()
    {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "TICKET-" + timestamp + "-" + counter.getAndIncrement();
    }
}


