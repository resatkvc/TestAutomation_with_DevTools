package proje.com.saucedemo.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTML Report Generator - Log dosyalarını HTML raporlarına dönüştürür
 * Renkli, interaktif ve detaylı raporlar oluşturur
 * 
 * @author TestAutomation_with_DevTools
 * @version 1.0
 */
public class HTMLReportGenerator {
    
    private static final Logger logger = LoggerFactory.getLogger(HTMLReportGenerator.class);
    
    private final String logDirectory;
    private final String reportDirectory;
    
    public HTMLReportGenerator() {
        this.logDirectory = "target/logs";
        this.reportDirectory = "target/reports";
    }
    
    public HTMLReportGenerator(String logDirectory, String reportDirectory) {
        this.logDirectory = logDirectory;
        this.reportDirectory = reportDirectory;
    }
    
    /**
     * Tüm log dosyalarını HTML raporlarına dönüştür
     */
    public void generateAllReports() {
        try {
            // Rapor dizinini oluştur
            Files.createDirectories(Paths.get(reportDirectory));
            
            // Ana rapor oluştur
            generateMainReport();
            
            // DevTools raporları oluştur
            generateDevToolsNetworkReport();
            generateDevToolsConsoleReport();
            generateDevToolsPerformanceReport();
            
            logger.info("All HTML reports generated successfully in: {}", reportDirectory);
            
        } catch (Exception e) {
            logger.error("Failed to generate HTML reports: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Ana test raporu oluştur
     */
    private void generateMainReport() throws Exception {
        String logFile = logDirectory + "/test-automation.log";
        if (!Files.exists(Paths.get(logFile))) {
            logger.warn("Main log file not found: {}", logFile);
            return;
        }
        
        List<LogEntry> logEntries = parseLogFile(logFile);
        
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", "Test Automation Report");
        dataModel.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dataModel.put("logEntries", logEntries);
        dataModel.put("totalEntries", logEntries.size());
        dataModel.put("reportType", "Main Test Logs");
        
        generateHTMLReport("main-report.ftl", "test-automation-report.html", dataModel);
    }
    
    /**
     * DevTools Network raporu oluştur
     */
    private void generateDevToolsNetworkReport() throws Exception {
        String logFile = logDirectory + "/devtools-network.log";
        if (!Files.exists(Paths.get(logFile))) {
            logger.warn("DevTools network log file not found: {}", logFile);
            return;
        }
        
        List<LogEntry> logEntries = parseLogFile(logFile);
        
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", "DevTools Network Monitoring Report");
        dataModel.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dataModel.put("logEntries", logEntries);
        dataModel.put("totalEntries", logEntries.size());
        dataModel.put("reportType", "Network Monitoring");
        
        generateHTMLReport("network-report.ftl", "devtools-network-report.html", dataModel);
    }
    
    /**
     * DevTools Console raporu oluştur
     */
    private void generateDevToolsConsoleReport() throws Exception {
        String logFile = logDirectory + "/devtools-console.log";
        if (!Files.exists(Paths.get(logFile))) {
            logger.warn("DevTools console log file not found: {}", logFile);
            return;
        }
        
        List<LogEntry> logEntries = parseLogFile(logFile);
        
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", "DevTools Console Log Report");
        dataModel.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dataModel.put("logEntries", logEntries);
        dataModel.put("totalEntries", logEntries.size());
        dataModel.put("reportType", "Console Logs");
        
        generateHTMLReport("console-report.ftl", "devtools-console-report.html", dataModel);
    }
    
    /**
     * DevTools Performance raporu oluştur
     */
    private void generateDevToolsPerformanceReport() throws Exception {
        String logFile = logDirectory + "/devtools-performance.log";
        if (!Files.exists(Paths.get(logFile))) {
            logger.warn("DevTools performance log file not found: {}", logFile);
            return;
        }
        
        List<LogEntry> logEntries = parseLogFile(logFile);
        
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", "DevTools Performance Report");
        dataModel.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dataModel.put("logEntries", logEntries);
        dataModel.put("totalEntries", logEntries.size());
        dataModel.put("reportType", "Performance Metrics");
        
        generateHTMLReport("performance-report.ftl", "devtools-performance-report.html", dataModel);
    }
    
    /**
     * Log dosyasını parse et
     */
    private List<LogEntry> parseLogFile(String logFile) throws IOException {
        List<LogEntry> entries = new ArrayList<>();
        
        // Log pattern: 2025-08-13T01:27:41.928Z [CDP Connection] INFO p.c.s.utils.DevToolsHelper.Network - message
        Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z?) \\[(.*?)\\] (\\w+) (.*?) - (.+)");
        
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    LogEntry entry = new LogEntry();
                    entry.setTimestamp(matcher.group(1));
                    entry.setThread(matcher.group(2));
                    entry.setLevel(matcher.group(3));
                    entry.setLogger(matcher.group(4));
                    entry.setMessage(matcher.group(5));
                    entries.add(entry);
                } else {
                    // Eğer pattern eşleşmezse, basit bir log entry oluştur
                    LogEntry entry = new LogEntry();
                    entry.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")));
                    entry.setThread("Unknown");
                    entry.setLevel("INFO");
                    entry.setLogger("Unknown");
                    entry.setMessage(line);
                    entries.add(entry);
                }
            }
        }
        
        return entries;
    }
    
    /**
     * HTML raporu oluştur
     */
    private void generateHTMLReport(String templateName, String outputFileName, Map<String, Object> dataModel) 
            throws IOException, TemplateException {
        
        // FreeMarker konfigürasyonu
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "templates");
        cfg.setDefaultEncoding("UTF-8");
        
        // Helper fonksiyonları ekle
        cfg.setSharedVariable("extractMethod", new MethodExtractor());
        cfg.setSharedVariable("extractUrl", new UrlExtractor());
        
        // Template yükle
        Template template = cfg.getTemplate(templateName);
        
        // HTML dosyası oluştur
        String outputPath = reportDirectory + "/" + outputFileName;
        try (Writer writer = new FileWriter(outputPath)) {
            template.process(dataModel, writer);
        }
        
        logger.info("HTML report generated: {}", outputPath);
    }
    
    /**
     * HTTP metodunu çıkaran helper sınıf
     */
    public static class MethodExtractor implements freemarker.template.TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) {
            if (arguments.size() > 0 && arguments.get(0) != null) {
                String message = arguments.get(0).toString();
                if (message.contains("GET")) return "GET";
                if (message.contains("POST")) return "POST";
                if (message.contains("PUT")) return "PUT";
                if (message.contains("DELETE")) return "DELETE";
                if (message.contains("PATCH")) return "PATCH";
            }
            return "UNKNOWN";
        }
    }
    
    /**
     * URL'yi çıkaran helper sınıf
     */
    public static class UrlExtractor implements freemarker.template.TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) {
            if (arguments.size() > 0 && arguments.get(0) != null) {
                String message = arguments.get(0).toString();
                // URL pattern'ini ara
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("https?://[^\\s]+");
                java.util.regex.Matcher matcher = pattern.matcher(message);
                if (matcher.find()) {
                    return matcher.group();
                }
            }
            return "No URL";
        }
    }
    
    /**
     * Log entry sınıfı
     */
    public static class LogEntry {
        private String timestamp;
        private String thread;
        private String level;
        private String logger;
        private String message;
        
        // Getters and Setters
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        
        public String getThread() { return thread; }
        public void setThread(String thread) { this.thread = thread; }
        
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        
        public String getLogger() { return logger; }
        public void setLogger(String logger) { this.logger = logger; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        /**
         * Log seviyesine göre CSS class döndür
         */
        public String getLevelClass() {
            switch (level.toUpperCase()) {
                case "ERROR": return "log-error";
                case "WARN": return "log-warn";
                case "INFO": return "log-info";
                case "DEBUG": return "log-debug";
                default: return "log-default";
            }
        }
        
        /**
         * Emoji içeren mesajları işle
         */
        public String getFormattedMessage() {
            if (message == null) return "";
            
            // Emoji'leri koru ve HTML'de güvenli hale getir
            return message
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
        }
    }
}
