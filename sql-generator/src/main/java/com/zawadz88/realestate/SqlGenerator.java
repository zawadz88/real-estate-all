package com.zawadz88.realestate;

import java.io.*;

/**
 * @author Piotr Zawadzki
 */
public class SqlGenerator {
    private static final String SQL_INSERT_PROJECT_TEMPLATE = "INSERT INTO projects (projectId, title, resourceName) values (%d, '%s', '%s');";
    private static final File OUTPUT_SQL_FILE = new File("target/projects.sql");
    private static final String PROJECTS_CSV_INPUT = "src/main/resources/projects.csv";
    private static final String CSV_SEPARATOR = ",";

    public static void main(String [] args) throws IOException{
        String line = "";
        String title;
        long id;
        String resourceName;
        if (!OUTPUT_SQL_FILE.exists()) {
            OUTPUT_SQL_FILE.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT_SQL_FILE), "UTF-8"));
        BufferedReader br = null;
        try {

            br = new BufferedReader(new InputStreamReader(new FileInputStream(PROJECTS_CSV_INPUT), "UTF-8"));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] productFields = line.split(CSV_SEPARATOR);
                if (productFields.length != 3) {
                    throw new RuntimeException("Wrong column count!");
                }
                id = Integer.parseInt(productFields[0]);
                title = productFields[1];
                resourceName = productFields[2];
                System.out.println("name: " + title + ", id: " + id + ", res: " + resourceName);
                writeSqlStatementToFile(bw, id, title, resourceName);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        bw.close();

    }

    private static void writeSqlStatementToFile(BufferedWriter writer, long id, String title, String resourceName) throws IOException {
        String updateStatement = String.format(SQL_INSERT_PROJECT_TEMPLATE, id, title.replace("'","\\'"), resourceName);
        writer.write(updateStatement);
        writer.newLine();
    }
}
