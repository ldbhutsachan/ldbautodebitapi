package unitl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class RefGenerator {

    public static String generateReference() {
        // Format today’s date as YYYYMMDD
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // 20260615

        // Add your fixed prefix
        String prefix = "FT" + datePart;


        // Optionally add a random part for uniqueness
        String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        return prefix + randomPart;
    }

    public static void main(String[] args) {
        System.out.println(generateReference());
    }
}
