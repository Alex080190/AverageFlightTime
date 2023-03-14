package com.exampleAverageFlightTime;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {

        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("tickets.json");
        JSONObject tickets = (JSONObject) jsonParser.parse(reader);

        ZoneId vladivostokZone = ZoneId.of("Asia/Vladivostok");
        ZoneId tel_AvivZone = ZoneId.of("Asia/Tel_Aviv");
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd.MM.yy");

        List<Integer> flightTimeInMinutes = new ArrayList<>();

        for (Object obj :(ArrayList) tickets.get("tickets")) {

            JSONObject ticket = (JSONObject) obj;

            LocalDate departureDate = LocalDate.parse(ticket.get("departure_date").toString(), formatterDate);
            LocalDate arrivalDate = LocalDate.parse(ticket.get("arrival_date").toString(), formatterDate);

            LocalTime departureLocalTime = LocalTime.parse(ticket.get("departure_time").toString());
            LocalTime arrivalLocalTime = LocalTime.parse(ticket.get("arrival_time").toString());


            ZonedDateTime departureZonedDateTime = ZonedDateTime.of(departureDate, departureLocalTime, vladivostokZone);
            ZonedDateTime arrivalZonedDateTime = ZonedDateTime.of(arrivalDate, arrivalLocalTime, tel_AvivZone);
            int flightTime = (int) Duration.between(departureZonedDateTime, arrivalZonedDateTime).toMinutes();
            flightTimeInMinutes.add(flightTime);

        }
        int average = (int) flightTimeInMinutes.stream().mapToInt(a -> a).average().orElse(0);
        int hours = average / 60;
        int minutes = average % 60;
        System.out.println("Average flight time is " + hours + " hours and " + minutes + " minutes");

        int average90PercentTime = (int) (average * 0.9);
        int hours90 = average90PercentTime / 60;
        int minutes90 = average90PercentTime % 60;
        System.out.println("90% of the average flight time is " + hours90 + " hours and " + minutes90 + " minutes");


    }
}
