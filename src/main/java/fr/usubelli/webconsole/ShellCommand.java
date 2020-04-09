package fr.usubelli.webconsole;

import io.reactivex.Observable;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.util.StringJoiner;

public class ShellCommand {

    private final String command;
    private final String[] arguments;

    public ShellCommand(String command, String[] arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public Observable<Object> run() {

        StringJoiner joiner = new StringJoiner(" ");
        for (String arg : arguments) {
            joiner.add(arg);
        }

        return Observable.create(emitter -> {
            new Thread(() -> {
                CommandLine oCmdLine = CommandLine
                        .parse(command + " " + joiner.toString());
                DefaultExecutor oDefaultExecutor = new DefaultExecutor();
                oDefaultExecutor.setExitValue(0);
                try {
                    oDefaultExecutor.execute(oCmdLine);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }).start();
        });
    }
}
