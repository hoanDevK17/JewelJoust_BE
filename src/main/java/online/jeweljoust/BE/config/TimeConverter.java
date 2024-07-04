package online.jeweljoust.BE.config;

import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class TimeConverter {

    // Phương thức chuyển đổi thời gian từ UTC sang múi giờ Việt Nam
    public String convertToVietnamTime(LocalDateTime localDateTime) {
        // Chuyển đổi sang múi giờ UTC
        ZonedDateTime utcDateTime = localDateTime.atZone(ZoneId.of("UTC"));

        // Chuyển đổi sang múi giờ Việt Nam
        ZonedDateTime vietnamDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"));

        // Định dạng kết quả
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return vietnamDateTime.format(formatter);
    }

}