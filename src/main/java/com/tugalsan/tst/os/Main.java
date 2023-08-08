package com.tugalsan.tst.os;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.os.server.TS_OsPlatformUtils;
import com.tugalsan.api.os.server.TS_OsProcess;
import com.tugalsan.api.thread.server.async.TS_ThreadAsyncAwait;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.time.server.TS_TimeElapsed;
import static java.lang.System.out;
import java.nio.file.Path;
import java.time.Duration;
import java.util.stream.IntStream;

public class Main {

    private static final TS_Log d = TS_Log.of(Main.class);

    //cd C:\me\codes\com.tugalsan\tst\com.tugalsan.tst.os
    //java --enable-preview --add-modules jdk.incubator.concurrent -jar target/com.tugalsan.tst.os-1.0-SNAPSHOT-jar-with-dependencies.jar
    //java -jar target/com.tugalsan.tst.os-1.0-SNAPSHOT-jar-with-dependencies.jar
    public static void main(String... s) {
        d.cr("main", "TS_OSUtils.isWindows()", TS_OsPlatformUtils.isWindows());
        d.cr("main", "TS_OSUtils.getVersion()", TS_OsPlatformUtils.getVersion());
        var exe = Path.of("C:\\me\\codes\\com.tugalsan\\tut\\com.tugalsan.tut.graalvm\\helloworld.exe");
        var timer = TS_TimeElapsed.of();

        runme(Duration.ofMillis(1), exe);
        runme(Duration.ofMillis(10), exe);
        runme(Duration.ofMillis(100), exe);
        runme(Duration.ofMillis(1000), exe);

        System.out.println("Time taken: " + timer.end().toMillis() + " milliseconds");
    }

    private static void runme(Duration until, Path exe) {
        var elapsed = TS_TimeElapsed.of();
        out.println("For dur: " + until);
        TGS_CallableType1<TS_OsProcess, TS_ThreadSyncTrigger> callable = kt -> {
            return TS_OsProcess.of(exe.toString());
        };
        IntStream.range(0, 10).forEach(i -> {
            elapsed.restart();
            var called = TS_ThreadAsyncAwait.callSingle(null, until, callable);
            var durElapsed = elapsed.end();
            var result = called.resultsForSuccessfulOnes.stream().findAny().orElse(null);
            if (result != null) {
                out.println("  -result.output: " + result.output + " , elapsed: " + durElapsed);
            }
        });
    }
}
