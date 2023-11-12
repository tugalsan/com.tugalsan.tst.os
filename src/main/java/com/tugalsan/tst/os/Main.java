package com.tugalsan.tst.os;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.file.server.TS_DirectoryUtils;
import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.os.server.TS_OsPlatformUtils;
import com.tugalsan.api.os.server.TS_OsProcess;
import com.tugalsan.api.thread.server.async.TS_ThreadAsyncAwait;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import static java.lang.System.out;
import java.nio.file.Path;
import java.time.Duration;
import java.util.stream.IntStream;

public class Main {

    private static final TS_Log d = TS_Log.of(Main.class);

    //cd C:\me\codes\com.tugalsan\tst\com.tugalsan.tst.os
    //java --enable-preview --add-modules jdk.incubator.vector -jar target/com.tugalsan.tst.os-1.0-SNAPSHOT-jar-with-dependencies.jar
    //java -jar target/com.tugalsan.tst.os-1.0-SNAPSHOT-jar-with-dependencies.jar
    public static void main(String... s) {
        TS_DirectoryUtils.copyDirectory(
                Path.of("\\\\10.0.0.222\\kalite_dokumanlar\\Entegre Yönetim Sistemi"),
                Path.of("C:\\Users\\me\\Desktop\\Entegre Yönetim Sistemi"),
                true, true,
                path -> TS_FileUtils.getNameLabel(path).contains(" OD")
        );

        if (true) {
            return;
        }

        d.cr("main", "TS_OSUtils.isWindows()", TS_OsPlatformUtils.isWindows());
        d.cr("main", "TS_OSUtils.getVersion()", TS_OsPlatformUtils.getVersion());

        TGS_CallableType1<TS_OsProcess, TS_ThreadSyncTrigger> callable = kt -> TS_OsProcess.of(
                "C:\\me\\codes\\com.tugalsan\\tut\\com.tugalsan.tut.graalvm\\helloworld.exe"
        );
        runme(Duration.ofMillis(1), callable);
        runme(Duration.ofMillis(10), callable);
        runme(Duration.ofMillis(100), callable);
        runme(Duration.ofMillis(1000), callable);
    }

    private static void runme(Duration until, TGS_CallableType1<TS_OsProcess, TS_ThreadSyncTrigger> callable) {
        out.println("For dur: " + until);
        IntStream.range(0, 10).forEach(i -> {
            var called = TS_ThreadAsyncAwait.callSingle(null, until, callable);
            if (called.hasError()) {
                return;
            }
            out.println("  -result.output: " + called.resultIfSuccessful.get().output + " , elapsed: " + called.elapsed);
        });
    }
}
