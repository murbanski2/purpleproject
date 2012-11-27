package edu.wctc.distjava.purpleproject.util;

import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 *
 * @author jlombardo
 */
public class Scratch {
    public static void main(String[] args) {
        DateTime startDate = DateTime.now(); // now() : since Joda Time 2.0
        System.out.println("Start: " + new Date(startDate.getMillis()));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 10);
        System.out.println("End: " + cal.getTime());
        DateTime end = new DateTime(cal.getTime());

        Period period = new Period(startDate, end, PeriodType.dayTime());

        PeriodFormatter formatter = new PeriodFormatterBuilder()
        .appendDays().appendSuffix("d ")
        .appendHours().appendSuffix("h ")
        .appendMinutes().appendSuffix("m ")
        .appendSeconds().appendSuffix("s ")
        .toFormatter();
        System.out.println(formatter.print(period));
    }
}
