package edu.wctc.distjava.purpleproject.util;

import com.google.common.base.Stopwatch;
import static java.lang.String.format;
import java.util.regex.Pattern;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This test class is used for performance testing only.
 *
 * @author Marcin Grzejszczak. with modifications and addtions by Jim Lombardo
 */
public class RegexpUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegexpUtilsTest.class);
    public static final String STRING_TO_MATCH = "something";

    /**
     * To run performance tests for cached vs. non-cached regular expressions
     * you must comment out the Ignore annotation and run the test.
     */
    @Ignore
    @Test
    public void testGetPattern() {
        runTestForManualCompilationAndOneUsingCache(1);
        runTestForManualCompilationAndOneUsingCache(10);
        runTestForManualCompilationAndOneUsingCache(100);
        runTestForManualCompilationAndOneUsingCache(1000);
        runTestForManualCompilationAndOneUsingCache(10000);
        runTestForManualCompilationAndOneUsingCache(100000);
        runTestForManualCompilationAndOneUsingCache(1000000);
    }

    private static void runTestForManualCompilationAndOneUsingCache(int firstNoOfRepetitions) {
        repeatManualCompilation(firstNoOfRepetitions);
        repeatCompilationWithCache(firstNoOfRepetitions);
    }

    private static void repeatManualCompilation(int noOfRepetitions) {
        Stopwatch stopwatch = new Stopwatch().start();
        compileAndMatchPatternManually(noOfRepetitions);
        LOGGER.debug(format("Time needed to compile and check regexp expression [%d] ms, no of iterations [%d]", stopwatch.elapsedMillis(), noOfRepetitions));
    }

    private static void repeatCompilationWithCache(int noOfRepetitions) {
        Stopwatch stopwatch = new Stopwatch().start();
        compileAndMatchPatternUsingCache(noOfRepetitions);
        LOGGER.debug(format("Time needed to compile and check regexp expression using Cache [%d] ms, no of iterations [%d]", stopwatch.elapsedMillis(), noOfRepetitions));
    }

    private static void compileAndMatchPatternManually(int limit) {
        for (int i = 0; i < limit; i++) {
            Pattern.compile("something").matcher(STRING_TO_MATCH).matches();
            Pattern.compile("something1").matcher(STRING_TO_MATCH).matches();
            Pattern.compile("something2").matcher(STRING_TO_MATCH).matches();
            Pattern.compile("something3").matcher(STRING_TO_MATCH).matches();
            Pattern.compile("something4").matcher(STRING_TO_MATCH).matches();
            Pattern.compile("something5").matcher(STRING_TO_MATCH).matches();
            Pattern.compile("something6").matcher(STRING_TO_MATCH).matches();
            Pattern.compile("something7").matcher(STRING_TO_MATCH).matches();
            Pattern.compile("something8").matcher(STRING_TO_MATCH).matches();
            Pattern.compile("something9").matcher(STRING_TO_MATCH).matches();
        }
    }

    private static void compileAndMatchPatternUsingCache(int limit) {
        for (int i = 0; i < limit; i++) {
            RegexpUtils.matches(STRING_TO_MATCH, "something");
            RegexpUtils.matches(STRING_TO_MATCH, "something1");
            RegexpUtils.matches(STRING_TO_MATCH, "something2");
            RegexpUtils.matches(STRING_TO_MATCH, "something3");
            RegexpUtils.matches(STRING_TO_MATCH, "something4");
            RegexpUtils.matches(STRING_TO_MATCH, "something5");
            RegexpUtils.matches(STRING_TO_MATCH, "something6");
            RegexpUtils.matches(STRING_TO_MATCH, "something7");
            RegexpUtils.matches(STRING_TO_MATCH, "something8");
            RegexpUtils.matches(STRING_TO_MATCH, "something9");
        }
    }
}
