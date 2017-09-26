package main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

public class CommentSorter {
    private static HashMap<String, String[]> keywords = new HashMap<>();
    
    public static void main(String[] args) throws IOException {
        initialize();
        
        System.out.print("Enter the name of the file with comments you want categorized: ");
        String filename = System.console().readLine();
        
        ICsvBeanReader beanReader = null;
        ICsvBeanWriter beanWriter = null;
        
        try {
            beanReader = new CsvBeanReader(new FileReader(filename), CsvPreference.EXCEL_PREFERENCE);
            beanWriter = new CsvBeanWriter(new FileWriter("OUTPUT.csv"), CsvPreference.EXCEL_PREFERENCE);
            final String[] header = beanReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();
            
            CommentBean comment;
            beanWriter.writeHeader(header);
            
            while((comment = beanReader.read(CommentBean.class, header, processors)) != null) {
                comment.setCategory(findCategory(comment.getComment()));
                beanWriter.write(comment, header, processors);
            }
            
            
        } catch (IOException e) {
            System.out.println("ERROR: reading or writing failed make sure the files are spelled correctly and closed.");
            System.exit(0);
        } finally {
            if (beanReader != null) {
                beanReader.close();
            }
            if (beanWriter != null) {
                beanWriter.close();
            }
        }
        
        System.out.println("-----------COMMENTS SUCCESSFULLY SORTED TO OUTPUT.CSV-----------");
    }
    
    public static void initialize() {
        keywords.put("Clarity of Direction", new String[]{"restructure", "pressure", "change", "goals", "confused", "goal setting", "sales culture", "unrealistic", "expectation"});
        keywords.put("Management Behaviors", new String[]{"manager", "fear", "favoritism", "respect"});
        keywords.put("Communication", new String[]{"consolidation", "communication", "leadership", "management", "opinion", "input", "disconnect"});
        keywords.put("Teamwork And Collaboration", new String[]{"teamwork", "collaborate", "team", "partnerships", "cooperation", "department", "5's"});
        keywords.put("Diversity And Inclusion", new String[]{"diversity", "inclusion", "equal"});
        keywords.put("Continuous Improvement", new String[]{"exceed"});
        keywords.put("Wellness And Resiliency", new String[]{"stress", "disconnect", "healthy"});
        keywords.put("Growth And Development", new String[]{"growth", "development", "career", "grow", "promote", "promoting", "promotion", "training"});
        keywords.put("Engagement", new String[]{"engage", "engaged", "engagement"});
        keywords.put("Resources And Support", new String[]{"apparel", "hiring", "staffing", "technology", "security", "stools", "chairs", "support", "turnover"});
        keywords.put("Pride in Company", new String[]{"customer service", "service", "community", "communities", "brand", "recognize", "recognition"});
        keywords.put("Reward And Recognition", new String[]{"pay", "benefits", "compensation", "incentive", "insurance", "health", "wellness"});
        keywords.put("Employee Empowerment", new String[]{"safe", "balance", "family", "work-life balance", "work life balance", "schedule", "scheduling", "hours", "call nights"});
    }
    
    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional()
        };
        return processors;
    }
    
    public static String findCategory(String comment) {
        int numberOfHitsCurrent = 0;
        int numberOfHitsMax = 0;
        String currentCategory = null;
        for (String category : keywords.keySet()) {
            for (String keyword : keywords.get(category)) {
                if (comment.toLowerCase().contains(keyword)) {
                    numberOfHitsCurrent++;
                    if (numberOfHitsCurrent > numberOfHitsMax) {
                        numberOfHitsMax = numberOfHitsCurrent;
                        currentCategory = category;
                    } else if (numberOfHitsCurrent == numberOfHitsMax) {
                        currentCategory += ", " + category;
                    }
                }
            }
            numberOfHitsCurrent = 0;
        }
        return (currentCategory == null ? "None" : currentCategory); 
    }
    
}
