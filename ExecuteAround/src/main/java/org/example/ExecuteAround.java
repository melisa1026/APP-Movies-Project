package org.example;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExecuteAround {

    private static final String FILE = ExecuteAround.class.getResource("data.txt").getFile();

    public ExecuteAround() {
    }

    public static void main(String... args) throws IOException {
        String result = processFileLimited();
        System.out.println(result);
        System.out.println("---");
        String oneLine = processFile((b) -> {
            return b.readLine();
        });
        System.out.println(oneLine);
        String twoLines = processFile((b) -> {
            String var10000 = b.readLine();
            return var10000 + b.readLine();
        });
        System.out.println(twoLines);

        System.out.println("---");
        String numLines = processFile((b) -> {
            int lines = 0;
            while (b.readLine() != null)
                    lines++;
            return Integer.toString(lines);
        });
        System.out.println("Number of lines: " + numLines);

    }

    public static String processFileLimited() throws IOException {
        Throwable var0 = null;
        Object var1 = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE));

            String var10000;
            try {
                var10000 = br.readLine();
            } finally {
                if (br != null) {
                    br.close();
                }

            }

            return var10000;
        } catch (Throwable var8) {
            if (var0 == null) {
                var0 = var8;
            } else if (var0 != var8) {
                var0.addSuppressed(var8);
            }

        }
        return "";
    }

    public static String processFile(BufferedReaderProcessor p) throws IOException {
        Throwable var1 = null;
        Object var2 = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE));

            String var10000;
            try {
                var10000 = p.process(br);
            } finally {
                if (br != null) {
                    br.close();
                }

            }

            return var10000;
        } catch (Throwable var9) {
            if (var1 == null) {
                var1 = var9;
            } else if (var1 != var9) {
                var1.addSuppressed(var9);
            }

            return "";
        }
    }

    public interface BufferedReaderProcessor {
        String process(BufferedReader var1) throws IOException;
    }
}
