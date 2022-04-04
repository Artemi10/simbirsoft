package com.example.simbirsoft.service.stat;

import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.repository.stats.StatsRepository;
import com.example.simbirsoft.service.application.AppService;
import com.example.simbirsoft.transfer.stat.StatRequestDTO;
import com.example.simbirsoft.transfer.stat.StatResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@AllArgsConstructor
@Service("monthStatService")
public class MonthStatService implements StatService {
    private final AppService appService;
    private final StatsRepository statsRepository;

    @Override
    public List<StatResponseDTO> createStats(long appId, String email) {
        var to = new Timestamp(new Date().getTime());
        var calendar = new GregorianCalendar();
        calendar.setTime(to);
        calendar.add(Calendar.YEAR, -1);
        var statistics = new StatRequestDTO(email, new Timestamp(calendar.getTime().getTime()), to);
        return createStats(appId, statistics);
    }

    @Override
    public List<StatResponseDTO> createStats(long appId, StatRequestDTO statistics) {
        if (appService.isUserApp(appId, statistics.email())){
            var rawStat = statsRepository
                    .getRawApplicationStatsByMonths(appId, statistics.from(), statistics.to());
            return createMonthStatByRawStat(rawStat, statistics.from(), statistics.to());
        }
        else throw new EntityException("Приложение не найдено");
    }

    private List<StatResponseDTO> createMonthStatByRawStat(Map<String, Integer> rawStat, Timestamp from, Timestamp to){
        var stat = new ArrayList<StatResponseDTO>();
        var formatter = new SimpleDateFormat("MM.yyyy");

        var startDate = getCalendarForMonthPattern(from);
        var endDate = getCalendarForMonthPattern(to);
        while (startDate.before(endDate)) {
            var dateStr = formatter.format(startDate.getTime());
            stat.add(new StatResponseDTO(dateStr, rawStat.getOrDefault(dateStr, 0)));
            startDate.add(Calendar.MONTH, 1);
        }

        var dateStr = formatter.format(startDate.getTime());
        stat.add(new StatResponseDTO(dateStr, rawStat.getOrDefault(dateStr, 0)));
        startDate.add(Calendar.MONTH, 1);
        return stat;
    }

    private Calendar getCalendarForMonthPattern(Timestamp date) {
        var calendar = new GregorianCalendar();
        calendar.setTime(new Date(date.getTime()));
        return calendar;
    }
}
