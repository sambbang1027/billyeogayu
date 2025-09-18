// 작성자 : 김민호
package app.common.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.function.Consumer;

public final class DownloadCSV {

    private static final byte[] UTF8_BOM = new byte[]{(byte)0xEF,(byte)0xBB,(byte)0xBF};

    public static void send(HttpServletResponse resp, String filename,
                            Consumer<CsvWriter> writer) throws IOException {
        setHeaders(resp, filename);
        OutputStream out = resp.getOutputStream();
        // BOM
        out.write(UTF8_BOM);

        CsvWriter csv = new CsvWriter(out, StandardCharsets.UTF_8);
        writer.accept(csv);
        csv.flush();
    }

    private static void setHeaders(HttpServletResponse resp, String filename) {
        String asciiFallback = "download.csv";
        try {
            String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                    .replace("+", "%20");
            resp.setContentType("text/csv; charset=UTF-8");
            resp.setHeader("Content-Disposition",
                    "attachment; filename=\"" + asciiFallback + "\"; filename*=UTF-8''" + encoded);
        } catch (Exception e) {
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + asciiFallback + "\"");
            resp.setContentType("text/csv; charset=UTF-8");
        }
    }

    public static final class CsvWriter {
        private final OutputStream out;
        private final Charset cs;

        CsvWriter(OutputStream out, Charset cs) {
            this.out = out;
            this.cs = cs;
        }

        public void header(String... cols) {
            row(cols);
        }

        public void row(String... cols) {
            try {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < cols.length; i++) {
                    if (i > 0) sb.append(',');
                    String cell = cols[i] == null ? "" : cols[i];
                    sb.append('"').append(cell.replace("\"", "\"\"")).append('"');
                }
                sb.append("\r\n");
                out.write(sb.toString().getBytes(cs));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void row(Collection<?> cols) {
            row(cols.stream().map(v -> v == null ? "" : String.valueOf(v)).toArray(String[]::new));
        }

        public void flush() throws IOException { out.flush(); }
    }
}