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
    
    //all keywords should be lower case
    public static void initialize() {
        keywords.put("Clarity of Direction", new String[]{"score", "quota", "restructure", "pressure", "change", "goal", "confused", "goal setting", "sales culture", "unrealistic", "expectation"});
        keywords.put("Management Behaviors", new String[]{"manager", "fear", "favoritism", "respect", "micromanage"});
        keywords.put("Communication", new String[]{"consolidation", "communication", "leadership", "management", "opinion", "input", "disconnect"});
        keywords.put("Teamwork & Collaboration", new String[]{"colleague", "consistency", "teamwork", "collaborate", "team", "partnerships", "cooperation", "department", "5's"});
        keywords.put("Diversity & Inclusion", new String[]{"considerate", "diversity", "inclusion", "equal"});
        keywords.put("Continuous Improvement", new String[]{"exceed", "redundancy"});
        keywords.put("Wellness & Resiliency", new String[]{"stress", "disconnect", "healthy", "health", "exercise"});
        keywords.put("Growth & Development", new String[]{"advancement", "growth", "development", "career", "grow", "promote", "promoting", "promotion", "training"});
        keywords.put("Engagement", new String[]{"engage", "engaged", "engagement"});
        keywords.put("Resources & Support", new String[]{"mentor", "duplicate", "licensed", "budget", "deposit", "computer", "saturday", "apparel", "dress", "hire", "hiring", "staff", "technology", "security", "stools", "chairs", "support", "turnover"});
        keywords.put("Pride in Company", new String[]{"branch", "value", "customer", "service", "dispute", "community", "communities", "brand", "recognize", "recognition"});
        keywords.put("Reward & Recognition", new String[]{"produce", "celebrating", "valuable", "money", "credit", "pay", "benefits", "compensation", "incentive", "insurance", "health", "wellness"});
        keywords.put("Employee Empowerment", new String[]{"offers", "coaching", "decisions", "safe", "balance", "family", "work-life balance", "work life balance", "schedule", "scheduling", "hours", "call nights"});
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
