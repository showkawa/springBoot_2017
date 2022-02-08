package com.kawa.script.service.tool;

import com.beust.jcommander.JCommander;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class CommandLauncher {

    public interface CmdRef {
        public String getCommandName();

        public Runnable newCommand(CommandLauncher host);
    }


    private boolean suppressSystemExit;

    public void logError(String line) {
        System.err.println(line);
    }

    public boolean start(String[] args) {
        breakCage(args);
        JCommander parser = null;
        try {

            parser = new JCommander(this);

            addCommands(parser);

            if (suppressSystemExit) {
                return true;
            } else {
                System.exit(0);
            }
        } catch (CommandAbortedError error) {
            for (String m : error.messages) {
                logError(m);
            }

            if (error.printUsage && parser != null) {
                if (parser.getParsedCommand() != null) {
                    parser.usage(parser.getParsedCommand());
                } else {
                    parser.usage();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        // abnormal termination
        if (suppressSystemExit) {
            return false;
        } else {
            System.exit(1);
            return false;
        }
    }

    protected String[] getModulesUnlockCommand() {
        return new String[]{"java.base/jdk.internal.perf=ALL-UNNAMED", "jdk.attach/sun.tools.attach=ALL-UNNAMED"};
    }

    protected List<String> getCommandPackages() {
        return Collections.singletonList(getClass().getPackage().getName() + ".cmd");
    }

    private void addCommands(JCommander parser) throws InstantiationException, IllegalAccessException {
        for (String pack : getCommandPackages()) {
            for (Class<?> c : findClasses(pack)) {
                if (CmdRef.class.isAssignableFrom(c)) {
                    CmdRef cmd = (CmdRef) c.newInstance();
                    String cmdName = cmd.getCommandName();
                    Runnable cmdTask = cmd.newCommand(this);
                    parser.addCommand(cmdName, cmdTask);
                }
            }
        }
    }

    private List<Class<?>> findClasses(String packageName) {
        List<Class<?>> result = new ArrayList<Class<?>>();
        try {
            String path = packageName.replace('.', '/');
            for (String f : findFiles(path)) {
                if (f.endsWith(".class") && f.indexOf('$') < 0) {
                    f = f.substring(0, f.length() - ".class".length());
                    f = f.replace('/', '.');
                    result.add(Class.forName(f));
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static List<String> findFiles(String path) throws IOException {
        List<String> result = new ArrayList<String>();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> en = cl.getResources(path);
        while (en.hasMoreElements()) {
            URL u = en.nextElement();
            listFiles(result, u, path);
        }
        return result;
    }

    static List<String> listFiles(List<String> results, URL packageURL, String path) throws IOException {

        if (packageURL.getProtocol().equals("jar")) {
            String jarFileName;
            JarFile jf;
            Enumeration<JarEntry> jarEntries;
            String entryName;

            // build jar file name, then loop through zipped entries
            jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
            jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
            jf = new JarFile(jarFileName);
            jarEntries = jf.entries();
            while (jarEntries.hasMoreElements()) {
                entryName = jarEntries.nextElement().getName();
                if (entryName.startsWith(path)) {
                    results.add(entryName);
                }
            }

            // loop through files in classpath
        } else {
            File dir = new File(packageURL.getFile());
            String cp = dir.getCanonicalPath();
            File root = dir;
            while (true) {
                if (cp.equals(new File(root, path).getCanonicalPath())) {
                    break;
                }
                root = root.getParentFile();
            }
            listFiles(results, root, dir);
        }
        return results;
    }

    static void listFiles(List<String> names, File root, File dir) {
        String rootPath = root.getAbsolutePath();
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    listFiles(names, root, file);
                } else {
                    String name = file.getAbsolutePath().substring(rootPath.length() + 1);
                    name = name.replace('\\', '/');
                    names.add(name);
                }
            }
        }
    }


    @SuppressWarnings("serial")
    public static class CommandAbortedError extends Error {

        public boolean printUsage;
        public String[] messages;

        public CommandAbortedError(boolean printUsage, String[] messages) {
            super();
            this.printUsage = printUsage;
            this.messages = messages;
        }

        public CommandAbortedError(boolean printUsage, String[] messages, Exception e) {
            super(e);
            this.printUsage = printUsage;
            this.messages = messages;
        }
    }

    // special hack to workaround Java module system
    private void breakCage(String... args) {
        RuntimeMXBean rtBean = ManagementFactory.getRuntimeMXBean();
        String spec = rtBean.getSpecVersion();

        // we need to unlock some modules
        StringBuilder sb = new StringBuilder();
        for (String a : rtBean.getInputArguments()) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(a);
        }

        // break cage
        List<String> command = new ArrayList<String>();
        File jhome = new File(System.getProperty("java.home"));
        File jbin = new File(jhome, "bin/java");
        command.add(jbin.getPath());
        for (String m : getModulesUnlockCommand()) {
            command.add("--add-opens");
            command.add(m);
        }
        command.add("-Dsjk.breakCage=false");
        command.add("-cp");
        command.add(rtBean.getClassPath());
        command.addAll(rtBean.getInputArguments());
        command.add(this.getClass().getName());
        command.addAll(Arrays.asList(args));

        System.err.println("Restarting java with unlocked package access");

        ProcessBuilderUtil.start(command);
    }

    public static void main(String[] args) {
        new CommandLauncher().start(args);
    }
}
